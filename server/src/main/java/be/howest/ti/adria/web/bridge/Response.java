package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.domain.Category;
import be.howest.ti.adria.web.dto.EventDto;
import be.howest.ti.adria.web.dto.SimpleUserDto;
import be.howest.ti.adria.web.dto.NotificationDto;
import be.howest.ti.adria.web.dto.adria.DangerousAreaDto;
import be.howest.ti.adria.web.dto.adria.SectorDto;
import be.howest.ti.adria.web.dto.adria.WorldDto;
import be.howest.ti.adria.web.dto.adria.WorldSectorDto;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }
    public static void sendCategory(RoutingContext ctx, Category category) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(category));
    }

    public static void sendCategories(RoutingContext ctx, List<Category> categories) {
        JsonArray categoryArray = new JsonArray(categories);
        sendOkJsonArrayResponse(ctx, categoryArray);
    }
    public static void sendEvents(RoutingContext ctx, List<EventDto> events) {
        JsonArray eventsArray = new JsonArray(events);
        sendOkJsonArrayResponse(ctx, eventsArray);
    }

    public static void sendEvent(RoutingContext ctx, EventDto event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    public static void sendInterests(RoutingContext ctx, List<Category> interests) {
        JsonArray interestsArray = new JsonArray(interests);
        sendOkJsonArrayResponse(ctx, interestsArray);
    }

    public static void sendEventCreated(RoutingContext ctx, EventDto event) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(event));
    }

    public static void sendEventJoined(RoutingContext ctx, EventDto event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }
    private static void sendOkJsonArrayResponse(RoutingContext ctx, JsonArray response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendEmptyResponse(RoutingContext ctx, int statusCode) {
        ctx.response()
                .setStatusCode(statusCode)
                .end();
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendJwt(RoutingContext ctx, String jwt) {
        JsonObject responseJson = new JsonObject().put("jwt", jwt);
        sendOkJsonResponse(ctx, responseJson);
    }

    public static void sendUser(RoutingContext ctx, SimpleUserDto user) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(user));
    }

    public static void sendSectors(RoutingContext ctx, List<SectorDto> sectors) {
        sendOkJsonArrayResponse(ctx, new JsonArray(sectors));
    }
    public static void sendSector(RoutingContext ctx, WorldSectorDto sectorDto) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(sectorDto));
    }

    public static void sendWorld(RoutingContext ctx, WorldDto world) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(world));
    }

    public static void sendOngoingEvents(RoutingContext ctx, List<EventDto> ongoingEvents) {
        JsonArray eventsArray = new JsonArray(ongoingEvents);
        sendOkJsonArrayResponse(ctx, eventsArray);
    }

    public static void sendUpComingEvents(RoutingContext ctx, List<EventDto> upcomingEvents) {
        JsonArray eventsArray = new JsonArray(upcomingEvents);
        sendOkJsonArrayResponse(ctx, eventsArray);
    }
    public static void sendHasUserJoinedEvent(RoutingContext ctx, boolean hasUserJoinedEvent, int eventId) {
        JsonObject responseJson = new JsonObject().put("attending", hasUserJoinedEvent);
        responseJson.put("eventId", eventId);
        sendOkJsonResponse(ctx, responseJson);
    }
    public static void sendEventLeaved(RoutingContext ctx, EventDto event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    public static void sendEventCanceled(RoutingContext ctx, EventDto event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    public static void sendEventEdited(RoutingContext ctx, EventDto event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    public static void sendAboutMeEdited(RoutingContext ctx, String res) {
        sendOkJsonResponse(ctx, new JsonObject()
                    .put("aboutMe", res));
    }

    public static void sendNotifications(RoutingContext ctx, List<NotificationDto> notifications) {
        JsonArray notificationsArray = new JsonArray(notifications);
        sendOkJsonArrayResponse(ctx, notificationsArray);
    }

    public static void sendDangerousArea(RoutingContext ctx, DangerousAreaDto dangerousAreaDto) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(dangerousAreaDto));
    }

    public static void sendReadUpdated(RoutingContext ctx, int i) {
        sendEmptyResponse(ctx, i);
    }

    public static void sendNotificationsCount(RoutingContext ctx, int count) {
        JsonObject responseJson = new JsonObject().put("count", count);
        sendOkJsonResponse(ctx, responseJson);
    }
}
