package daos;

import constants.Role;
import dtos.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

public class UserDAO {

    private final String GET_ALL_USERS = "SELECT * FROM tblUsers";
    private final String GET_USER_BY_ID = "SELECT * FROM tblUsers WHERE userID LIKE ?";
    private final String GET_USER_BY_NAME = "SELECT * FROM tblUsers WHERE fullName LIKE ?";
    private final String LOGIN_USER = "SELECT * FROM tblUsers WHERE userID LIKE ?";
    private final String INSERT_USER = "INSERT INTO tblUsers "
            + "(userID, fullName, roleID, password, phone)"
            + "VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_USER = "UPDATE tblUsers SET fullName = ?, roleID = ?, password = ?, phone = ? "
            + "WHERE userID LIKE ?";
    private final String UPDATE_PROFILE = "UPDATE tblUsers SET fullName = ?, password = ?, phone = ? "
            + "WHERE userID LIKE ?";
    private final String DELETE_USER = "DELETE FROM tblUsers WHERE userID LIKE ?";

    public User getUserByID(String userID, boolean isHidePassword) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(GET_USER_BY_ID)) {
            stm.setString(1, userID);
            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                String password = isHidePassword ? "*****" : rs.getString("password");
                    return mapRow(rs, password);
                }
            }
        }
        return null;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(GET_ALL_USERS);  
                ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                resultList.add(mapRow(rs, "****"));
            }
        }
        return resultList;
    }
    
    public List<User> getUsersByID(String userID) throws SQLException {
        return searchByKeyWord(GET_USER_BY_ID, userID);
    }
    
    public List<User> getUsersByName(String name) throws SQLException {
        return searchByKeyWord(GET_USER_BY_NAME, name);
    }

    private List<User> searchByKeyWord(String sql, String keyword) throws SQLException {
        List<User> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapRow(rs, "****"));
                }
            }
        }
        return resultList;
    }
    
    public User login(String userID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(LOGIN_USER)) {
            stm.setString(1, userID);
            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs, rs.getString("password"));
                }
            }
        }
        return null;
    }

    public boolean checkUserExists(String userID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(GET_USER_BY_ID)) {
            stm.setString(1, userID);
            return stm.executeQuery().next();
        }
    }

    public int insertUser(String userID, String fullName,
            Role role, String password, String phone) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(INSERT_USER)) {
            stm.setString(1, userID);
            stm.setString(2, fullName);
            stm.setInt(3, role.getValue());
            stm.setString(4, password);
            stm.setString(5, phone);
            return stm.executeUpdate();
        }
    }

    public int updateUser(String userID, String fullName,
            Role role, String password, String phone) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(UPDATE_USER)) {
            stm.setString(1, fullName);
            stm.setInt(2, role.getValue());
            stm.setString(3, password);
            stm.setString(4, phone);
            stm.setString(5, userID);
            int res = stm.executeUpdate();
            return res;
        }
    }
    
    public int updateProfile(String userID, String fullName, 
            String password, String phone) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(UPDATE_PROFILE)) {
            stm.setString(1, fullName);
            stm.setString(2, password);
            stm.setString(3, phone);
            stm.setString(4, userID);
            int res = stm.executeUpdate();
            return res;
        }
    }

    public int deleteUser(String userID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(DELETE_USER)) {
            stm.setString(1, userID);
            return stm.executeUpdate();
        }
    }
    
    // helper
    private User mapRow(ResultSet rs, String password) throws SQLException {
        return new User(
                rs.getString("userID"),
                rs.getString("fullName"),
                Role.fromValue(rs.getInt("roleID")),
                password,
                rs.getString("phone")
        );
    }
}
