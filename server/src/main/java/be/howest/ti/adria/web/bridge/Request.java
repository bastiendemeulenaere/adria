package be.howest.ti.adria.web.bridge;

import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.CoordinateRange;
import be.howest.ti.adria.web.dto.EventCreateDto;
import be.howest.ti.adria.web.exceptions.MalformedRequestException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {
    private static final Logger LOGGER = Logger.getLogger(Request.class.getName());
    public static final String SPEC_CATEGORY_ID = "categoryId";
    public static final String SPEC_EVENT_ID = "eventId";
    private static final String SPEC_ADRIA_ID = "adriaId";
    private static final String SPEC_SECTOR_ID = "sectorId";
    private static final String SPEC_NOTIFICATION_ID = "notificationId";
    private static final String MSG_UNABLE_TO_DECIPHER = "Unable to decipher the data in the body";
    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public int getCategoryId() {
        return params.pathParameter(SPEC_CATEGORY_ID).getInteger();
    }

    public int getEventId() {
        return params.pathParameter(SPEC_EVENT_ID).getInteger();
    }
    public int getSectorId() {
        return params.pathParameter(SPEC_SECTOR_ID).getInteger();
    }

    public int getNotificationId() {
        return params.pathParameter(SPEC_NOTIFICATION_ID).getInteger();
    }

    public EventCreateDto getEvent(){
        EventCreateDto event = null;
        try {
            if (params.body().isJsonObject()) {
                String name = params.body().getJsonObject().getString("name");
                String description = params.body().getJsonObject().getString("description");
                int amountOfPeople = params.body().getJsonObject().getInteger("amountOfPeople");
                int categoryId = params.body().getJsonObject().getInteger(SPEC_CATEGORY_ID);
                int sectorId = params.body().getJsonObject().getInteger(SPEC_SECTOR_ID);
                long startDateTime = params.body().getJsonObject().getLong("startDateTime");
                int hours = params.body().getJsonObject().getInteger("hours");

                event = new EventCreateDto(name, description, amountOfPeople, categoryId, sectorId, startDateTime, hours);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, MSG_UNABLE_TO_DECIPHER, ex);
            throw new MalformedRequestException(MSG_UNABLE_TO_DECIPHER);
        }
        return event;
    }
    public String getAdriaId(){
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_ADRIA_ID);
            return params.body().get().toString();
        }catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, "Unable to get the AdriaId", ex);
            throw new MalformedRequestException("Unable to get the AdriaId.");
        }
    }

    public List<Integer> getUserInterests() {
        List<Integer> interestIds = new ArrayList<>();
        try{
            if(params.body().isJsonArray()){
                List<?> interests = params.body().getJsonArray().getList();
                for(Object interestId : interests){
                    if (interestId instanceof Integer integer) {
                        interestIds.add(integer);
                    } else {
                        throw new IllegalArgumentException("Expected an Integer object!");
                    }

                }
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, MSG_UNABLE_TO_DECIPHER, ex);
            throw new MalformedRequestException(MSG_UNABLE_TO_DECIPHER);
        }
        return interestIds;
    }

    public String getAboutMe() {
        return params.body().getJsonObject().getString("aboutMe");
    }

    public Coordinate getCoordinate() {
        Coordinate coordinate = null;
        try {
            if (params.body().isJsonObject()) {
                int x = params.body().getJsonObject().getInteger("x");
                int y = params.body().getJsonObject().getInteger("y");

                coordinate = new Coordinate(x,y);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, MSG_UNABLE_TO_DECIPHER, ex);
            throw new MalformedRequestException(MSG_UNABLE_TO_DECIPHER);
        }
        return coordinate;
    }


    public CoordinateRange getCoordinateRange() {
        CoordinateRange coordinateRange = null;
        try {
            if (params.body().isJsonObject()) {
                JsonObject startObj = params.body().getJsonObject().getJsonObject("start");
                JsonObject endObj = params.body().getJsonObject().getJsonObject("end");
                Coordinate start = new Coordinate(startObj.getInteger("x"), startObj.getInteger("y"));
                Coordinate end = new Coordinate(endObj.getInteger("x"), endObj.getInteger("y"));

                coordinateRange = new CoordinateRange(start,end);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, MSG_UNABLE_TO_DECIPHER, ex);
            throw new MalformedRequestException(MSG_UNABLE_TO_DECIPHER);
        }
        return coordinateRange;
    }
}
