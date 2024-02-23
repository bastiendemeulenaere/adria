package be.howest.ti.adria.logic.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {

    NORMAL("normal"),
    COMPANY("company"),
    PAYED("payed");

    private final String name;

    EventType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    @JsonCreator
    public static EventType from(String eventType){
        for (EventType type : EventType.values()){
            if (type.getName().equalsIgnoreCase(eventType)){
                return type;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return name;
    }
}
