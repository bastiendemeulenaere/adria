package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.adria.Coordinate;
import be.howest.ti.adria.logic.domain.adria.Sector;
import be.howest.ti.adria.logic.domain.event.Event;
import be.howest.ti.adria.logic.domain.event.EventType;
import be.howest.ti.adria.logic.exceptions.RepositoryException;
import be.howest.ti.adria.web.dto.EventCreateDto;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventRepositoryImpl extends EventRepository{
    private static final String SQL_ALL_EVENTS = "select * from events;";
    private static final String SQL_ALL_EVENTS_NOT_PAST = "SELECT * FROM events WHERE startDateTime >= NOW();";
    private static final String SQL_EVENTS_FROM_SECTOR_NOT_PAST = "SELECT * FROM events WHERE sector = ? AND startDateTime >= NOW();";
    private static final String SQL_EVENTS_FROM_SECTOR_WITH_PAST = "SELECT * FROM events WHERE sector = ?;";
    private static final String SQL_INSERT_EVENT = "INSERT INTO `events` (`name`, `eventType`, `description`, `amountOfPeople`, `categoryId`, `organiserId`, `sector`, `startDateTime`, `hours`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_SELECT_EVENT_BY_ID = "SELECT * FROM events WHERE id = ?";
    private static final String SQL_EVENTS_FROM_USER_USERID = "SELECT events.* FROM events JOIN user_events ON events.id=user_events.eventId WHERE user_events.userId=?";
    private static final String SQL_EVENTS_FROM_ORGANISERID = "SELECT * FROM events WHERE organiserId=?";
    private static final String SQL_JOIN_EVENT = "INSERT INTO user_events (userId, eventId) VALUES (?, ?)";
    private static final String SQL_LEAVE_EVENT = "DELETE FROM user_events WHERE userId = ? AND eventId = ?";
    private static final String SQL_CANCEL_EVENT = "UPDATE events SET canceled = true WHERE id = ?";
    private static final String SQL_EDIT_EVENT = "UPDATE events SET name=?, eventType=?, description=?, amountOfPeople=?, categoryId=?, sector=?, startDateTime=?, hours=? WHERE id=?";
    private static final String SQL_SET_EVENT_LOCATION = "UPDATE events SET locX=?, locY=? WHERE id=?";
    private static final Logger LOGGER = Logger.getLogger(EventRepositoryImpl.class.getName());
    private static final String SQL_CHECK_USER_EVENT = "SELECT COUNT(*) AS count FROM user_events WHERE userId = ? AND eventId = ?";
    private static final String SQL_GET_ATTENDEES = "SELECT userId FROM user_events WHERE eventId = ?";
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    public EventRepositoryImpl(H2Connection h2Connection) {
        super(h2Connection);
        categoryRepository = Repositories.getInstance().getCategoryRepository();
        userRepository = Repositories.getInstance().getUserRepository();
    }
    @Override
    public Event createEvent(EventCreateDto event, String organiserId, EventType eventType) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_EVENT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, eventType.getName());
            stmt.setString(3, event.getDescription());
            stmt.setInt(4, event.getAmountOfPeople());
            stmt.setInt(5, event.getCategoryId());
            stmt.setString(6, organiserId);
            stmt.setInt(7, event.getSectorId());
            stmt.setTimestamp(8, new Timestamp(event.getStartDateTime()));
            stmt.setInt(9, event.getHours());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Event(
                            generatedKeys.getInt(1),
                            event.getName(),
                            eventType,
                            event.getDescription(),
                            event.getAmountOfPeople(),
                            categoryRepository.getCategory(event.getCategoryId()),
                            userRepository.getUserById(organiserId),
                            getAttendees(generatedKeys.getInt(1)),
                            Sector.from(event.getSectorId()),
                            Instant.ofEpochMilli(event.getStartDateTime()),
                            event.getHours()
                    );
                } else {
                    throw new SQLException("Creating event failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create event.", ex);
            throw new RepositoryException("Could not create event.");
        }
    }
    public Event getEventById(int id) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_EVENT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractEvent(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve user.", ex);
            throw new RepositoryException("Could not find event with id: " + id);
        }
    }

    @Override
    public List<Event> getEventsByUserId(String id){
        List<Event> events = new ArrayList<>();

        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_EVENTS_FROM_USER_USERID)
        ) {
            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEvent(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get ongoing events.", ex);
            throw new RepositoryException("Could not get ongoing events.");
        }

        return events;
    }

    private Event extractEvent(ResultSet rs) throws SQLException {

        Coordinate location = null;
        int id = rs.getInt("id");
        int locX = rs.getInt("locX");
        int locY = rs.getInt("locY");
        if (!(locX < 0 || locY < 0)){
            location = new Coordinate(locX, locY);
        }

        return new Event(
                id,
                rs.getString("name"),
                EventType.from(rs.getString("eventType")),
                rs.getString("description"),
                rs.getInt("amountOfPeople"),
                categoryRepository.getCategory(rs.getInt("categoryId")),
                userRepository.getUserById(rs.getString("organiserId")),
                Sector.from(rs.getInt("sector")),
                getAttendees(id),
                rs.getTimestamp("startDateTime").toInstant(),
                rs.getInt("hours"),
                location,
                rs.getBoolean("canceled")
        );
    }

    public List<User> getAttendees(int eventId) {
        List<User> res = new ArrayList<>();
        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_GET_ATTENDEES);
        ) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    res.add(userRepository.getUserById(rs.getString("userId")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve attendee count for event.", ex);
            throw new RepositoryException("Could not retrieve attendee count for event.");
        }

        return res;
    }

    public List<Event> getOngoingEvents(String id) {
        List<Event> events = getEventsByUserId(id);

        Instant now = Instant.now();

        events = events.stream()
                .filter(event -> isEventOngoing(event, now))
                .filter(event -> !event.isCancelled())
                .toList();

        return events;
    }


    private boolean isEventOngoing(Event event, Instant now) {
        Instant eventStart = event.getStartDateTime();
        Instant eventEnd = eventStart.plus(event.getHours(), ChronoUnit.HOURS);

        return now.isAfter(eventStart) && now.isBefore(eventEnd);
    }

    public List<Event> getUpComingEvents(String id) {
        List<Event> events = getEventsByUserId(id);

        Instant now = Instant.now();

        events = events.stream()
                .filter(e -> e.getStartDateTime().isAfter(now))
                .filter(event -> !event.isCancelled())
                .toList();

        return events;
    }

    @Override
    public List<Event> getCreatedEventsById(String id) {
        List<Event> res = new ArrayList<>();
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_EVENTS_FROM_ORGANISERID)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    res.add(extractEvent(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve events.", ex);
            throw new RepositoryException("Could not find event");
        }
        return res.stream().filter(event -> !event.isCancelled()).toList();
    }

    @Override
    public List<Event> getEvents(boolean includePast) {
        List<Event> res = new ArrayList<>();
        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(includePast ? SQL_ALL_EVENTS : SQL_ALL_EVENTS_NOT_PAST)
        ) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    res.add(extractEvent(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get event.", ex);
            throw new RepositoryException("Could not get event.");
        }
        return res.stream().filter(event -> !event.isCancelled()).toList();
    }

    @Override
    public Event joinEvent(String userId, int eventId) {
        Event event = null;
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_JOIN_EVENT)) {

            stmt.setString(1, userId);
            stmt.setInt(2, eventId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Joining event failed, no rows affected.");
            }

            event = getEventById(eventId);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to join event.", ex);
            throw new RepositoryException("Could not join event.");
        }

        return event;
    }

    @Override
    public Event leaveEvent(String adriaId, int eventId) {
        Event event = null;
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_LEAVE_EVENT)) {

            stmt.setString(1, adriaId);
            stmt.setInt(2, eventId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Joining event failed, no rows affected.");
            }

            event = getEventById(eventId);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to leave event.", ex);
            throw new RepositoryException("Could not leave event.");
        }

        return event;
    }
    @Override
    public List<Event> getEventsFromSector(int sectorNumber) {
        return getEventsFromSector(sectorNumber, false);
    }
    @Override
    public List<Event> getEventsFromSector(int sectorNumber, boolean includePast) {
        List<Event> events = new ArrayList<>();
        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        includePast ? SQL_EVENTS_FROM_SECTOR_WITH_PAST: SQL_EVENTS_FROM_SECTOR_NOT_PAST)
        ) {
            stmt.setInt(1, sectorNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(extractEvent(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve events from sector.", ex);
            throw new RepositoryException("Could not retrieve events from sector.");
        }

        return events;
    }
    @Override
    public Event cancelEvent(int eventId) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_CANCEL_EVENT)) {

            stmt.setInt(1, eventId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Canceling event failed, no rows affected.");
            }

            return getEventById(eventId);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to cancel event.", ex);
            throw new RepositoryException("Could not cancel event.");
        }
    }

    public Event editEvent(EventCreateDto eventDto, int eventId, EventType eventType) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_EDIT_EVENT)) {

            stmt.setString(1, eventDto.getName());
            stmt.setString(2, eventType.getName());
            stmt.setString(3, eventDto.getDescription());
            stmt.setInt(4, eventDto.getAmountOfPeople());
            stmt.setInt(5, eventDto.getCategoryId());
            stmt.setInt(6, eventDto.getSectorId());
            stmt.setTimestamp(7, Timestamp.from(Instant.ofEpochMilli(eventDto.getStartDateTime())));
            stmt.setInt(8, eventDto.getHours());
            stmt.setInt(9, eventId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Editing event failed, no rows affected.");
            }

            return getEventById(eventId);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to edit event.", ex);
            throw new RepositoryException("Could not edit event.");
        }
    }
    @Override
    public Event setEventLocation(int eventId, Coordinate coordinate) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SET_EVENT_LOCATION)) {
            stmt.setInt(1, coordinate.x());
            stmt.setInt(2, coordinate.y());
            stmt.setInt(3, eventId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Setting event coordinate failed, no rows affected.");
            }

            return getEventById (eventId);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to edit event.", ex);
            throw new RepositoryException("Could not edit event.");
        }
    }
    @Override
    public boolean hasUserJoinedEvent(String userId, int eventId) {
        boolean hasJoined = false;
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_USER_EVENT)) {

            stmt.setString(1, userId);
            stmt.setInt(2, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    hasJoined = count > 0;
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to check if user joined event.", ex);
            throw new RepositoryException("Failed to check if user joined event.");

        }

        return hasJoined;
    }
}
