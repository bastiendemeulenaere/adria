package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.controller.AdriaController;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.World;
import be.howest.ti.adria.logic.exceptions.RepositoryException;
import be.howest.ti.adria.logic.listeners.WeatherListener;
import be.howest.ti.adria.web.SocketConnections;
import be.howest.ti.adria.web.dto.DangerZoneInfoDto;
import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.SuggestedLocationDto;
import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;
import be.howest.ti.adria.web.listeners.NotificationListener;
import be.howest.ti.adria.web.socketevents.*;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.

 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java

 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
public class RtcBridge implements WeatherListener, NotificationListener {
    private static final String EB_EVENT_TO_USERS = "events.to.users";
    private static final String EB_NOTIFY_TO_USER = "events.to.user.";
    private static final String EB_NOTIFY_TO_USER_JOIN = "events.to.user.join";
    private static final String EB_USER_TO_SERVER = "events.to.server";
    private SockJSHandler sockJSHandler;
    private EventBus eb;
    private AdriaController adriaController;
    private final SocketConnections socketConnections = new SocketConnections();
    private static final Logger LOGGER = Logger.getLogger(RtcBridge.class.getName());

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex("events\\..+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);

        sockJSHandler.bridge(options, this::handleNewSocketConnection);
    }

    private void handleNewSocketConnection(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED) {
            SockJSSocket socket = event.socket();
            String joinId = socket.writeHandlerID();
            socketConnections.put(joinId, null);
            //Send uuid to client
            JsonObject uuidJson = new JsonObject().put("joinId", joinId);
            event.socket().write(new JsonObject().put("address", EB_NOTIFY_TO_USER_JOIN).put("body", uuidJson).encode());
        } else if (event.type() == BridgeEventType.SOCKET_CLOSED) {
            SockJSSocket socket = event.socket();
            String joinId = socket.writeHandlerID();
            String adriaId = socketConnections.getFromJoinId(joinId);
            adriaController.removeListenersOf(adriaId);
            socketConnections.remove(joinId);
        }
        event.complete(true);
    }

    @Override
    public void onWeatherChange(World world){
        if (eb == null) return;
        eb.publish(EB_EVENT_TO_USERS,JsonObject.mapFrom(world.toDto()));
    }

    public void setAdriaController(AdriaController adriaController){
        if (this.adriaController != null) {
            LOGGER.log(Level.WARNING, "AdriaController is already set!");
            throw new RepositoryException("AdriaController is already set!");
        }
        this.adriaController = adriaController;
        adriaController.subscribeToWeather(this);
    }
    public SockJSHandler getSockJSHandler(Vertx vertx) {
        SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions();
        sockJSHandlerOptions.setRegisterWriteHandler(true);
        sockJSHandler = SockJSHandler.create(vertx, sockJSHandlerOptions);
        eb = vertx.eventBus();
        createSockJSHandler();
        registerConsumers();
        return sockJSHandler;
    }
    private void registerConsumers() {
        eb.consumer(EB_USER_TO_SERVER, this::handleIncomingMessage);
    }
    private void handleIncomingMessage(Message<JsonObject> msg) {
        IncomingSocketEvent incoming = SocketEventFactory.getInstance().createIncomingEvent(msg.body());

        if (incoming.getEventType().equals(SocketEventType.JOIN)){
            SocketJoinEvent socketJoinEvent = (SocketJoinEvent) incoming;
            if (!socketJoinEvent.getAdriaId().isBlank()){
                socketConnections.put(socketJoinEvent.getJoinId(), socketJoinEvent.getAdriaId());
            }
        }

        adriaController.handleSocket(incoming);
    }

    private String getAdrianSocketAddress(String adriaId){
        return EB_NOTIFY_TO_USER + adriaId;
    }
    @Override
    public void sendDangerZoneNotification(String adriaId, DangerZoneInfoDto info) {
        eb.publish(getAdrianSocketAddress(adriaId), JsonObject.mapFrom(new SocketDangerEvent(info)));
    }

    @Override
    public void sendSuggestLocationNotification(String adriaId, SuggestedLocationDto suggestedLocationDto, EventDto eventDto) {
        eb.publish(getAdrianSocketAddress(adriaId), JsonObject.mapFrom(new SocketSuggestedLocationEvent(suggestedLocationDto, eventDto)));
    }

    @Override
    public void sendLocationNotification(String adriaId, Coordinate coordinate, EventDto eventDto) {
        eb.publish(getAdrianSocketAddress(adriaId), JsonObject.mapFrom(new SocketNotifyLocationEvent(coordinate, eventDto)));
    }

    @Override
    public void sendCanceledNotification(String adriaId, EventDto eventDto) {
        eb.publish(getAdrianSocketAddress(adriaId), JsonObject.mapFrom(new SocketNotifyCanceledEvent(eventDto)));
    }

    @Override
    public void sendDangerNotification(String adriaId, EventDto eventDto, DangerousAreaDto dangerousAreaDto) {
        eb.publish(getAdrianSocketAddress(adriaId), JsonObject.mapFrom(new SocketNotifyDangerEvent(eventDto, dangerousAreaDto)));
    }
}
