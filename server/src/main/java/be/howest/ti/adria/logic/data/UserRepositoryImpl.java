package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserCreateObject;
import be.howest.ti.adria.logic.exceptions.RepositoryException;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepositoryImpl extends UserRepository{
    private static final Logger LOGGER = Logger.getLogger(UserRepositoryImpl.class.getName());
    private static final String SQL_SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SQL_INSERT_USER = "INSERT INTO `users` (id, first_name, last_name, about_me) VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_ABOUT_ME = "UPDATE users SET about_me = ? WHERE id = ?";
    public UserRepositoryImpl(H2Connection repository) {
        super(repository);
    }
    @Override
    public boolean userExists(String userId){
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_BY_ID)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Could not check if user exists.", ex);
            throw new RepositoryException("Could not check if user exists with id: " + userId);
        }
    }

    @Override
    public User getUserById(String userId) {
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_BY_ID)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("about_me")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve user.", ex);
            throw new RepositoryException("Could not find user with id: " + userId);
        }
    }

    @Override
    public User createUser(UserCreateObject user) {
        User createdUser = null;
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_USER)) {

            stmt.setString(1, user.adriaId());
            stmt.setString(2, user.firstName());
            stmt.setString(3, user.lastName());
            stmt.setString(4, user.aboutMe());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            createdUser = new User(
                    user.adriaId(),
                    user.firstName(),
                    user.lastName(),
                    user.aboutMe()
            );

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create user.", ex);
            throw new RepositoryException("Could not create user.");
        }
        return createdUser;
    }

    public String editAboutMe(String adriaId, String aboutMe) {


        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ABOUT_ME)) {

            stmt.setString(1, aboutMe);
            stmt.setString(2, adriaId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Editing about_me failed, no rows affected.");
            }

            return aboutMe;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to edit about_me.", ex);
            throw new RepositoryException("Could not edit about_me for user with id: " + adriaId);
        }
    }

}
