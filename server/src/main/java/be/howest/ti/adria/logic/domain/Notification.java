package be.howest.ti.adria.logic.domain;

import be.howest.ti.adria.web.dto.NotificationDto;

import java.time.Instant;

public class Notification {

    private final int id;
    private final int eventId;
    private final String title;
    private final Instant startTime;
    private final String description;
    private boolean read;

    public Notification(int id, int eventId, String title, Instant timeAgo, String description, boolean read) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.startTime = timeAgo;
        this.description = description;
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }

    public boolean isRead() {
        return read;
    }

    public static NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getEventId(),
                notification.getTitle(),
                notification.getStartTime(),
                notification.getDescription(),
                notification.isRead()
        );
    }
}
