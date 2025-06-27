package daos;

import dtos.CustomerCare;
import dtos.CustomerCareViewModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

public class CustomerCareDAO {
    private final String CREATE = 
        "INSERT INTO [dbo].[tblCustomerCares] " +
        "([userID], [subject], [content], [status], [reply]) VALUES (?, ?, ?, ?, ?)";
    private final String DELETE_BY_ID = 
        "DELETE FROM [dbo].[tblCustomerCares] WHERE ticketID = ?";
    private final String UPDATE = 
        "UPDATE [dbo].[tblCustomerCares] " +
        "SET [userID] = ?, [subject] = ?, [content] = ?, [status] = ?, [reply] = ? " +
        "WHERE ticketID = ?";
    private final String CHECK_EXIST_BY_ID = 
        "SELECT * FROM [dbo].[tblCustomerCares] WHERE ticketID = ?";
    private final String SEARCH_BY_ID = 
        "SELECT * FROM [dbo].[tblCustomerCares] WHERE ticketID = ? AND userID = ?";
    private final String SEARCH_BY_SUBJECT = 
        "SELECT * FROM [dbo].[tblCustomerCares] WHERE subject LIKE ? AND userID = ?";
    private final String GET_ALL = 
        "SELECT * FROM [dbo].[tblCustomerCares] WHERE userID = ?";
    private final String CHECK_EXIST = 
        "SELECT 1 FROM [dbo].[tblCustomerCares] WHERE userID = ? AND subject = ?";
    private final String GET_ALL_VIEW_MODEL =
        "SELECT c.ticketID, c.userID, u.fullName, c.subject, c.content, c.status, c.reply " +
        "FROM [dbo].[tblCustomerCares] c " +
        "JOIN [dbo].[tblUsers] u ON c.userID = u.userID";

    public int create(String userID, String subject, String content, String status, String reply) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE)) {
            ps.setString(1, userID);
            ps.setString(2, subject);
            ps.setString(3, content);
            ps.setString(4, status);
            ps.setString(5, reply);
            return ps.executeUpdate();
        }
    }

    public int deleteByID(int ticketID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ID)) {
            ps.setInt(1, ticketID);
            return ps.executeUpdate();
        }
    }

    public int update(int id, String userID, String subject, String content, String status, String reply) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, userID);
            ps.setString(2, subject);
            ps.setString(3, content);
            ps.setString(4, status);
            ps.setString(5, reply);
            ps.setInt(6, id);
            return ps.executeUpdate();
        }
    }

    public boolean checkExistByID(int ticketID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EXIST_BY_ID)) {
            ps.setInt(1, ticketID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
    
    public CustomerCare searchByID(int ticketID, String userID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_ID)) {
            ps.setInt(1, ticketID);
            ps.setString(2, userID);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        }
    }

    public List<CustomerCare> searchBySubject(String subject, String userID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_SUBJECT)) {
            List<CustomerCare> list = new ArrayList<>();
            ps.setString(1,"%" + subject + "%");
            ps.setString(2, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<CustomerCare> getAll(String userID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            List<CustomerCare> list = new ArrayList<>();
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public boolean isExist(String userID, String subject) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EXIST)) {
            ps.setString(1, userID);
            ps.setString(2, subject);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public List<CustomerCareViewModel> getAllViewModels() throws SQLException {
        List<CustomerCareViewModel> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL_VIEW_MODEL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowVM(rs));
            }
        }
        return list;
    }

    private CustomerCare mapRow(ResultSet rs) throws SQLException {
        return new CustomerCare(
            rs.getInt("ticketID"),
            rs.getString("userID"),
            rs.getString("subject"),
            rs.getString("content"),
            rs.getString("status"),
            rs.getString("reply")
        );
    }

    private CustomerCareViewModel mapRowVM(ResultSet rs) throws SQLException {
        return new CustomerCareViewModel(
            rs.getInt("ticketID"),
            rs.getString("userID"),
            rs.getString("fullName"),
            rs.getString("subject"),
            rs.getString("content"),
            rs.getString("status"),
            rs.getString("reply")
        );
    }
}
