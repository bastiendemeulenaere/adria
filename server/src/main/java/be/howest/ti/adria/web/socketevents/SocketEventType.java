package be.howest.ti.adria.web.socketevents;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SocketEventType {

    DISCARD("discard"),
    LEAVE("leave"),
    JOIN("join"),
    DANGERZONEINFO("dangerzone-info"),
    SUGGESTLOCATION("suggest-location"),
    CANCELEDEVENT("canceled-event"),
    DANGEREVENT("danger-event"),
    NOTIFYLOCATION("notify-location");

    private final String type;

    @JsonValue
    public String getType() {
        return type;
    }

    SocketEventType(String type) {
        this.type = type;
    }

    public static SocketEventType fromString(String type) {
        for(SocketEventType socketEventType : SocketEventType.values()){
            if (socketEventType.type.equals(type)) {
                return socketEventType;
            }
        }
        return SocketEventType.DISCARD;
    }
}
