package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Notification;
import be.howest.ti.adria.logic.exceptions.RepositoryException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationRepositoryImpl extends NotificationRepository{
    public NotificationRepositoryImpl(H2Connection h2Connection) {
        super(h2Connection);
    }
    private static final Logger LOGGER = Logger.getLogger(NotificationRepositoryImpl.class.getName());
    private static final String SQL_GET_NOTIFICATIONS = "SELECT id, eventId, title, startTime, description, `read` FROM notifications WHERE userId = ?";
    private static final String SQL_SET_TO_READ = "UPDATE notifications SET `read` = true  WHERE id = ?";
    private static final String SQL_NOTIFICATION_GET_COUNT = "SELECT COUNT(*) FROM notifications WHERE userId = ? AND `read` = false";
    private static final String SQL_INSERT_NOTIFICATION = "INSERT INTO `notifications` (userId, eventId, title, startTime, description, `read`) VALUES (?, ?, ?, ?, ?, ?)";
    @Override
    public List<Notification> getNotifications(String adriaId) {
        List<Notification> notifications = new ArrayList<>();

        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_GET_NOTIFICATIONS)) {
            stmt.setString(1, adriaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    Instant startTime = rs.getTimestamp("startTime").toInstant();
                    int eventId = rs.getInt("eventId");
                    String description = rs.getString("description");
                    int id = rs.getInt(1);
                    boolean read = rs.getBoolean("read");
                    Notification notification = new Notification(id, eventId, title, startTime, description, read);
                    notifications.add(notification);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get notifications.", ex);
            throw new RepositoryException("Could not get notifications.");
        }

        return notifications;
    }
    @Override
    public void setToRead(int notificationId) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SET_TO_READ)) {
            stmt.setInt(1, notificationId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to set to read");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to set to read.", ex);
            throw new RepositoryException("Failed to set to read.");
        }
    }
    @Override
    public int getNotificationsCount(String adriaId) {
        String errorMessage = "Failed to get notification count.";
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_NOTIFICATION_GET_COUNT)) {
            stmt.setString(1, adriaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException(errorMessage);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, errorMessage, ex);
            throw new RepositoryException(errorMessage);
        }
    }

    public void generateTestData(String userId) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_NOTIFICATION)) {

            // Creating a test notification
            stmt.setString(1, userId);
            stmt.setInt(2, 1); // Assuming eventId
            stmt.setString(3, "Test Notification");
            stmt.setTimestamp(4, java.sql.Timestamp.from(Instant.now()));
            stmt.setString(5, "This is a test notification description");
            stmt.setBoolean(6, false); // Unread

            stmt.executeUpdate();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to generate test data for notifications.", ex);
            throw new RepositoryException("Failed to generate test data for notifications.");
        }
    }

}
