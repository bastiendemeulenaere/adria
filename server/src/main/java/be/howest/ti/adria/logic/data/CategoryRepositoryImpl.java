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

public class CategoryRepositoryImpl extends CategoryRepository {
    private static final Logger LOGGER = Logger.getLogger(CategoryRepositoryImpl.class.getName());
    private static final String SQL_CATEGORY_BY_ID = "select id, category from categories where id = ?;";
    private static final String SQL_ALL_CATEGORIES = "select * from categories;";
    public CategoryRepositoryImpl(H2Connection repository) {
        super(repository);
    }
    @Override
    public Category getCategory(int id) {
        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CATEGORY_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("category"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get category.", ex);
            throw new RepositoryException("Could not get category.");
        }
    }
    @Override
    public List<Category> getCategories() {
        List<Category> res = new ArrayList<>();
        try (
                Connection conn = h2Connection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_CATEGORIES)
        ) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    res.add(new Category(rs.getInt("id"), rs.getString("category")));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get category.", ex);
            throw new RepositoryException("Could not get category.");
        }
        return res;
    }
}
