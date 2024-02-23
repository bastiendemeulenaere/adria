package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.controller.*;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.*;
import be.howest.ti.adria.logic.domain.adria.*;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.util.AuthHelper;
import be.howest.ti.adria.web.dto.EventCreateDto;
import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.NotificationDto;
import be.howest.ti.adria.web.dto.adria.SectorDto;
import be.howest.ti.adria.web.dto.adria.WorldSectorDto;
import be.howest.ti.adria.web.exceptions.MalformedRequestException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * In the AdriaOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the `controller` is turned to Json or another Web data type in the `Response` class.
 */
public class OpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(OpenApiBridge.class.getName());
    private CategoryController categoryController = null;
    private EventController eventController = null;
    private AuthHelper authHelper;
    private UserController userController;
    private InterestController interestController;

    private NotificationController notificationController;
    private World world;
    private RandomUserDataGenerator randomUserDataGenerator;
    private AdriaController adriaController;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(this::globalHandler);

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getCategoryById");
        routerBuilder.operation("getCategoryById").handler(this::getCategoryById);

        LOGGER.log(Level.INFO, "Installing handler for: getCategories");
        routerBuilder.operation("getCategories").handler(this::getCategories);

        LOGGER.log(Level.INFO, "Installing handler for: login");
        routerBuilder.operation("login").handler(this::login);

        LOGGER.log(Level.INFO, "Installing handler for: getEvents");
        routerBuilder.operation("getEvents").handler(this::getEvents);

        LOGGER.log(Level.INFO, "Installing handler for: getEventById");
        routerBuilder.operation("getEventById").handler(this::getEventById);

        LOGGER.log(Level.INFO, "Installing handler for: createEvent");
        routerBuilder.operation("createEvent").handler(this::createEvent);

        LOGGER.log(Level.INFO, "Installing handler for: joinEvent");
        routerBuilder.operation("joinEvent").handler(this::joinEvent);

        LOGGER.log(Level.INFO, "Installing handler for: leaveEvent");
        routerBuilder.operation("leaveEvent").handler(this::leaveEvent);

        LOGGER.log(Level.INFO, "Installing handler for: cancelEvent");
        routerBuilder.operation("cancelEvent").handler(this::cancelEvent);

        LOGGER.log(Level.INFO, "Installing handler for: editEvent");
        routerBuilder.operation("editEvent").handler(this::editEvent);

        LOGGER.log(Level.INFO, "Installing handler for: getOngoingEvents");
        routerBuilder.operation("getOngoingEvents").handler(this::getOngoingEvents);

        LOGGER.log(Level.INFO, "Installing handler for: getUpcomingEvents");
        routerBuilder.operation("getUpcomingEvents").handler(this::getUpcomingEvents);

        LOGGER.log(Level.INFO, "Installing handler for: getCreatedEventsById");
        routerBuilder.operation("getCreatedEventsById").handler(this::getCreatedEventsById);

        LOGGER.log(Level.INFO, "Installing handler for: getUserInterests");
        routerBuilder.operation("getUserInterests").handler(this::getUserInterests);


        LOGGER.log(Level.INFO, "Installing handler for: isAttendingEvent");
        routerBuilder.operation("isAttendingEvent").handler(this::isAttendingEvent);

        LOGGER.log(Level.INFO, "Installing handler for: getUser");
        routerBuilder.operation("getUser").handler(this::getUser);

        LOGGER.log(Level.INFO, "Installing handler for: addUserInterests");
        routerBuilder.operation("addUserInterests").handler(this::addUserInterests);

        LOGGER.log(Level.INFO, "Installing handler for: getSectors");
        routerBuilder.operation("getSectors").handler(this::getSectors);

        LOGGER.log(Level.INFO, "Installing handler for: getSector");
        routerBuilder.operation("getSector").handler(this::getSector);

        LOGGER.log(Level.INFO, "Installing handler for: getAdria");
        routerBuilder.operation("getAdria").handler(this::getAdria);

        LOGGER.log(Level.INFO, "Installing handler for: setEventLocation");
        routerBuilder.operation("setEventLocation").handler(this::setEventLocation);

        LOGGER.log(Level.INFO, "Installing handler for: editAboutMe");
        routerBuilder.operation("editAboutMe").handler(this::editAboutMe);

        LOGGER.log(Level.INFO, "Installing handler for: getNotifications");
        routerBuilder.operation("getNotifications").handler(this::getNotifications);

        LOGGER.log(Level.INFO, "Installing handler for: addDangerZone");
        routerBuilder.operation("addDangerZone").handler(this::addDangerZone);

        LOGGER.log(Level.INFO, "Installing handler for: setToRead");
        routerBuilder.operation("setToRead").handler(this::setToRead);

        LOGGER.log(Level.INFO, "Installing handler for: getNotificationsCount");
        routerBuilder.operation("getNotificationsCount").handler(this::getNotificationsCount);

        LOGGER.log(Level.INFO, "Installing handler for: getEventsPublic");
        routerBuilder.operation("getEventsPublic").handler(this::getEventsPublic);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");

        return routerBuilder.createRouter();
    }
    private CorsHandler globalHandler(RoutingContext ctx){

        // every time check if the user exists otherwise login
        // this is only neccesary because the database gets reset on every reboot.
        // if this wasn't the case this should be removed

        io.vertx.ext.auth.User user = ctx.user();
        if (user == null){
            return createCorsHandler(ctx);
        }
        String adriaId = ctx.user().get("sub");
        if (adriaId!=null && !adriaId.isBlank()){
            loginWithoutResponse(adriaId);
        }
        return createCorsHandler(ctx);
    }
    private CorsHandler createCorsHandler(RoutingContext ctx) {
        ctx.next();
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }


    private void getEventsPublic(RoutingContext ctx) {
        List<Event> events = eventController.getEvents(false);

        Response.sendEvents(ctx, events.stream().map(Event::toDto).toList());
    }

    private void addDangerZone(RoutingContext ctx) {
        Sector sector = Sector.from(Request.from(ctx).getSectorId());
        DangerousArea dangerousArea = adriaController.addDangerzone(sector);

        Response.sendDangerousArea(ctx, dangerousArea.toDto());
    }

    private void setEventLocation(RoutingContext ctx) {
        int eventId = Request.from(ctx).getEventId();
        Coordinate coordinate = Request.from(ctx).getCoordinate();
        Event event = eventController.addCoordinateToEvent(eventId, coordinate);

        adriaController.sendNotifyLocation(event, coordinate);

        Response.sendEvent(ctx, event.toDto());
    }

    private void getAdria(RoutingContext ctx) {
        Response.sendWorld(ctx, world.toDto());
    }


    private void getSectors(RoutingContext ctx) {
        List<SectorDto> sectors = this.world.getSectors().stream().map(Sector::toDto).toList();

        Response.sendSectors(ctx, sectors);
    }
    private void getSector(RoutingContext ctx) {
        int sectorId = Request.from(ctx).getSectorId();
        Sector sector = Sector.from(sectorId);
        if (sector == null){
            Response.sendFailure(ctx, 404, "This Sector wasn't found!");
            return;
        }
        WorldSectorDto sectorDto = this.world.getSector(Sector.from(sectorId)).toDto();
        Response.sendSector(ctx, sectorDto);

    }


    private void getUser(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        User user = userController.getUserById(adriaId);
        Response.sendUser(ctx, user.toDto());
    }

    private void isAttendingEvent(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        int eventId = Request.from(ctx).getEventId();

        boolean hasUserJoinedEvent = eventController.hasUserJoinedEvent(adriaId, eventId);

        Response.sendHasUserJoinedEvent(ctx, hasUserJoinedEvent, eventId);

    }
    private void getUserInterests(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        List<Category> interests = interestController.getUserInterests(adriaId);
        Response.sendInterests(ctx, interests);
    }

    public void setAuth(JWTAuth auth) {
        this.authHelper = new AuthHelper(auth);
    }

    private void populateDev() {
        EventCreateDto eventCreateDto = new EventCreateDto(
                "Dynamic Event", "Just for testing",
                5,
                1,
                1,
                Instant.now().plusSeconds(0).toEpochMilli(),
                1
        );
        EventCreateDto eventCreateDto2 = new EventCreateDto(
                "Dynamic Event no lication", "Just for testing without location",
                5,
                1,
                1,
                Instant.now().plusSeconds(60).toEpochMilli(),
                1
        );
        String user2 = "user2";
        String user3 = "user3";
        Event event = eventController.createEvent(eventCreateDto, user2);
        eventController.addCoordinateToEvent(event.getId(), new Coordinate(12, 25));
        eventController.joinEvent(user3, event.getId());
        eventController.joinEvent(user2, event.getId());
        Event event2 = eventController.createEvent(eventCreateDto2, user2);
        eventController.joinEvent(user2, event2.getId());
        eventController.joinEvent(user3, event2.getId());
    }

    public void addControllers(ControllerFactory controllerFactory){
        this.eventController = controllerFactory.createEventController();
        this.categoryController = controllerFactory.createCategoryController();
        this.userController = controllerFactory.createUserController();
        this.interestController = controllerFactory.createInterestController();
        this.notificationController = controllerFactory.createNotificationController();

        populateDev();
    }
    public void login(RoutingContext ctx)  {
        String id = Request.from(ctx).getAdriaId();
        String jwt = loginWithoutResponse(id);
        Response.sendJwt(ctx, jwt);
    }
    public String loginWithoutResponse(String adriaId)  {

        boolean userExists = userController.doesUserExist(adriaId);

        if (!userExists){
            register(adriaId);
        }

        return authHelper.generateJwt(adriaId);
    }
    public void register(String adriaId) {
        UserCreateObject userCreateObject = randomUserDataGenerator.generateRandomUser(adriaId);
        userController.createUser(userCreateObject);
    }

    private void addUserInterests(RoutingContext ctx) {
        String id = ctx.user().get("sub");
        List<Integer> interestIds = Request.from(ctx).getUserInterests();
        interestController.setUserInterests(interestIds, id);

        List<Category> interests = interestController.getUserInterests(id);
        Response.sendInterests(ctx, interests);
    }

    public void getCategoryById(RoutingContext ctx) {
        Category category = categoryController.getCategoryById(Request.from(ctx).getCategoryId());

        Response.sendCategory(ctx, category);
    }

    public void getCategories(RoutingContext ctx) {
        List<Category> categories = Repositories.getInstance().getCategoryRepository().getCategories();

        Response.sendCategories(ctx, categories);
    }

    public void getEvents(RoutingContext ctx) {
        List<Event> events = eventController.getEvents();
        List<EventDto> eventDtos = events.stream().map(Event::toDto).toList();
        Response.sendEvents(ctx, eventDtos);
    }

    public void getEventsByUserId(RoutingContext ctx) {
        String id = ctx.user().get("sub");
        List<Event> events = eventController.getEventsByUserId(id);
        List<EventDto> eventDtos = events.stream().map(Event::toDto).toList();
        Response.sendEvents(ctx, eventDtos);
    }

    public void getCreatedEventsById(RoutingContext ctx) {
        String id = ctx.user().get("sub");
        List<EventDto> eventDtos = eventController.getCreatedEventsById(id).stream().map(Event::toDto).toList();
        Response.sendEvents(ctx, eventDtos);
    }

    public void getEventById(RoutingContext ctx) {
        Event event = eventController.getEventById(Request.from(ctx).getEventId());
        Response.sendEvent(ctx, event.toDto());
    }

    public void getOngoingEvents(RoutingContext ctx) {
        String id = ctx.user().get("sub");
        List<EventDto> ongoingEvents = eventController.getOngoingEvents(id).stream().map(Event::toDto).toList();
        Response.sendOngoingEvents(ctx, ongoingEvents);
    }

    public void getUpcomingEvents(RoutingContext ctx) {
        String id = ctx.user().get("sub");
        List<EventDto> upcomingEvents = eventController.getUpComingEvents(id).stream().map(Event::toDto).toList();
        Response.sendUpComingEvents(ctx, upcomingEvents);
    }

    public void createEvent(RoutingContext ctx) {
        EventCreateDto eventDto = Request.from(ctx).getEvent();

        if (eventDto == null){
            Response.sendFailure(ctx, 400, "Event not found in request.");
        }
        String adriaId = ctx.user().get("sub");
        Event event = eventController.createEvent(eventDto, adriaId);
        event = eventController.joinEvent(adriaId, event.getId());
        Response.sendEventCreated(ctx, event.toDto());
    }


    public void joinEvent(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        int eventId = Request.from(ctx).getEventId();

        Event event = eventController.joinEvent(adriaId, eventId);

        Response.sendEventJoined(ctx, event.toDto());
    }

    public void leaveEvent(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        int eventId = Request.from(ctx).getEventId();

        Event event = eventController.leaveEvent(adriaId, eventId);

        Response.sendEventLeaved(ctx, event.toDto());
    }

    public void cancelEvent(RoutingContext ctx) {
        int eventId = Request.from(ctx).getEventId();

        Event event = eventController.cancelEvent(eventId);

        adriaController.sendCancelNotification(event);

        Response.sendEventCanceled(ctx, event.toDto());
    }

    public void editEvent(RoutingContext ctx) {
        EventCreateDto eventDto = Request.from(ctx).getEvent();
        int eventId = Request.from(ctx).getEventId();

        if (eventDto == null){
            Response.sendFailure(ctx, 400, "Event not found in request.");
        }

        Event event = eventController.editEvent(eventId ,eventDto);
        Response.sendEventEdited(ctx, event.toDto());
    }

    public void editAboutMe(RoutingContext ctx) {
        String aboutMe = Request.from(ctx).getAboutMe();
        String adriaId = ctx.user().get("sub");

        String res = userController.editAboutMe(adriaId, aboutMe);
        Response.sendAboutMeEdited(ctx, res);
    }

    public void getNotifications(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");

        List<NotificationDto> notifications = notificationController.getNotifications(adriaId).stream().map(Notification::toDto).toList();

        Response.sendNotifications(ctx, notifications);
    }

    public void setToRead(RoutingContext ctx) {
        int notificationId = Request.from(ctx).getNotificationId();
        notificationController.setToRead(notificationId);

        Response.sendReadUpdated(ctx, 200);
    }

    public void getNotificationsCount(RoutingContext ctx) {
        String adriaId = ctx.user().get("sub");
        int count = notificationController.getNotificationsCount(adriaId);

        Response.sendNotificationsCount(ctx, count);
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }


    public void setAdria(World world) {
        this.world = world;
    }
    public void setRandomUserDataGenerator(RandomUserDataGenerator randomUserDataGenerator){
        this.randomUserDataGenerator = randomUserDataGenerator;
    }

    public void setAdriaController(AdriaController adriaController) {
        this.adriaController = adriaController;
    }
}
