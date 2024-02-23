package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationControllerTest extends BaseControllerTest {
    private NotificationController notificationController;
    private static final String TEST_USER_ID = "test";

    @BeforeEach
    void setup() {
        this.notificationController = new NotificationController(Repositories.getInstance().getNotificationRepository());

        // Create a test notification
        Repositories.getInstance().getNotificationRepository().generateTestData(TEST_USER_ID);
    }

    @Test
    void getNotifications() {
        // Test for getNotifications method
        // Arrange
        // Assuming there are notifications for the test user (created in setup)

        // Act
        List<Notification> notifications = notificationController.getNotifications(TEST_USER_ID);

        // Assert
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
    }

    @Test
    void setToRead() {
        // Test for setToRead method
        // Arrange
        // Assuming there is an unread notification for the test user (created in setup)
        List<Notification> notificationsBefore = notificationController.getNotifications(TEST_USER_ID);
        int notificationId = notificationsBefore.get(0).getId();

        // Act
        notificationController.setToRead(notificationId);

        // Assert
        List<Notification> notificationsAfter = notificationController.getNotifications(TEST_USER_ID);
        Notification updatedNotification = notificationsAfter.stream()
                .filter(notification -> notification.getId() == notificationId)
                .findFirst()
                .orElse(null);
        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.isRead());
    }

    @Test
    void getNotificationsCount() {
        // Test for getNotificationsCount method
        // Arrange
        // Assuming there are unread notifications for the test user (created in setup)

        // Act
        int unreadCount = notificationController.getNotificationsCount(TEST_USER_ID);

        // Assert
        assertTrue(unreadCount > 0);
    }
}
