package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.NotificationRepository;
import be.howest.ti.adria.logic.domain.Notification;

import java.util.List;

public class NotificationController {
    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotifications(String adriaId) {
        return notificationRepository.getNotifications(adriaId);
    }

    public void setToRead(int notificationId) {
        notificationRepository.setToRead(notificationId);
    }

    public int getNotificationsCount(String adriaId) {
        return notificationRepository.getNotificationsCount(adriaId);
    }
}
