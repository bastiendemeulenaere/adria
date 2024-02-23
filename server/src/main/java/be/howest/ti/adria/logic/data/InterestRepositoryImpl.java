package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Category;
import be.howest.ti.adria.logic.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterestRepositoryImpl extends InterestRepository{
    private static final Logger LOGGER = Logger.getLogger(InterestRepositoryImpl.class.getName());
    private static final String SQL_SELECT_USER_INTERESTS =
            """
            SELECT categories.* FROM user_interests 
            INNER JOIN categories ON user_interests.categoryId = categories.id 
            WHERE user_interests.userId = ?;
            """;
    private static final String SQL_INSERT_USER_INTERESTS =
            "INSERT INTO user_interests (userid, categoryid) VALUES(?, ?)";
    private static final String SQL_DELETE_USER_INTERESTS = "DELETE FROM user_interests WHERE userid = ?";

    public InterestRepositoryImpl(H2Connection h2Connection) {
        super(h2Connection);
    }

    @Override
    public List<Category> getUserInterests(String userId){
        List<Category> interests = new ArrayList<>();
        try (Connection conn = h2Connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_INTERESTS)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    interests.add(new Category(
                            rs.getInt("id"),
                            rs.getString("category")
                    ));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve user.", ex);
            throw new RepositoryException("Could not find user with id: " + userId);
        }
        return interests;
    }
    @Override
    public void setUserInterests(List<Integer> interestIds, String adriaId) {
        if (interestIds.isEmpty()) {
            return;
        }
        try (Connection conn = h2Connection.getConnection()) {
            deleteAllUserInterests(conn, adriaId);
            for (Integer interestId : interestIds) {
                insertUserInterest(conn, adriaId, interestId);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Could not insert interests.", ex);
            throw new RepositoryException("Could not insert interests into: " + adriaId);
        }
    }

    private void deleteAllUserInterests(Connection conn, String adriaId) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_USER_INTERESTS)) {
            stmt.setString(1, adriaId);
            stmt.executeUpdate();
        }
    }

    private void insertUserInterest(Connection conn, String adriaId, int interestId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_USER_INTERESTS)) {
            stmt.setString(1, adriaId);
            stmt.setInt(2, interestId);
            stmt.executeUpdate();
        }
    }
}
