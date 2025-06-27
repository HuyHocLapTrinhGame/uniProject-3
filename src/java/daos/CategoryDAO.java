package daos;

import dtos.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

public class CategoryDAO {
    private static final String CREATE =
        "INSERT INTO [dbo].[tblCategories] (categoryName, description) VALUES (?, ?)";
    private static final String DELETE_BY_ID =
        "DELETE FROM [dbo].[tblCategories] WHERE categoryID = ?";
    private static final String UPDATE =
        "UPDATE [dbo].[tblCategories] SET categoryName = ?, description = ? WHERE categoryID = ?";
    private static final String SEARCH_BY_ID =
        "SELECT * FROM [dbo].[tblCategories] WHERE categoryID = ?";
    private static final String SEARCH_BY_CATEGORIES =
        "SELECT * FROM [dbo].[tblCategories] WHERE categoryName LIKE ?";
    private static final String GET_ALL =
        "SELECT * FROM [dbo].[tblCategories]";

    public boolean isExit(String category) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_CATEGORIES)) {
            ps.setString(1, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int create(String name, String description) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE)) {
            ps.setString(1, name);
            ps.setString(2, description);
            return ps.executeUpdate();
        }
    }

    public int deleteByID(int id) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ID)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int update(int id, String name, String description) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);
            return ps.executeUpdate();
        }
    }

    public Category searchByID(int id) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Category> searchByCategory(String category) throws SQLException {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_CATEGORIES)) {
            ps.setString(1, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public List<Category> getAll() throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {
            List<Category> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
            rs.getInt("categoryID"),
            rs.getString("categoryName"),
            rs.getString("description")
        );
    }
}
