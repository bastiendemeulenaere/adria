package be.howest.ti.adria.web.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.time.Instant;

public class NotificationDto {
    private int id;
    private final int eventId;
    private final String title;
    private final long startTime;
    private final String description;
    private final boolean read;

    private NotificationDto(int eventId, String title, Instant startTime, String description, boolean read) {
        this.eventId = eventId;
        this.title = title;
        this.startTime = startTime.toEpochMilli();
        this.description = description;
        this.read = read;
    }

    public NotificationDto(int id, int eventId, String title, Instant startTime, String description, boolean read) {
        this(eventId, title, startTime, description, read);
        this.id = id;
    }

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    @JsonGetter("title")
    public String getTitle() {
        return title;
    }

    @JsonGetter("startTime")
    public long getStartTime() {
        return startTime;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }
    @JsonGetter("eventId")
    public int getEventId() {
        return eventId;
    }
    @JsonGetter("read")
    public boolean isRead() {
        return read;
    }
}
