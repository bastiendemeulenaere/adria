package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Notification;

import java.util.List;

public abstract class NotificationRepository extends Repository{
    protected NotificationRepository(H2Connection h2Connection) {
        super(h2Connection);
    }
    public abstract List<Notification> getNotifications(String adriaId);
    public abstract void setToRead(int notificationId);

    public abstract int getNotificationsCount(String adriaId);

    public abstract void generateTestData(String testUserId);
}
