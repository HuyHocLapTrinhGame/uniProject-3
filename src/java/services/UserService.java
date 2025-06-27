/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import constants.MessageKey;
import constants.Regex;
import constants.Role;
import daos.UserDAO;
import dtos.User;
import java.sql.SQLException;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import responses.ServiceResponse;

/**
 *
 * @author Admin
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();
    
    public ServiceResponse<User> register(String userID, String fullName,
            String password, String confirmPassword, String phone) throws SQLException{
        if(isNullOrEmptyString(userID) 
                || isNullOrEmptyString(fullName)
                || isNullOrEmptyString(password)
                || isNullOrEmptyString(confirmPassword)
                || isNullOrEmptyString(phone)){
            return ServiceResponse.failure(MessageKey.ALL_FIELDS_REQUIRED);
        }
        
        if(!checkPhone(phone)){
            return ServiceResponse.failure(MessageKey.INVALID_PHONE_FORMAT);
        }
        
        if(userDAO.checkUserExists(userID)){
            return ServiceResponse.failure(MessageKey.USER_ID_EXISTED);
        }
        
        if(!checkConfirmPassword(password, confirmPassword)){
            return ServiceResponse.failure(MessageKey.PASSWORD_NOT_MATCH);
        }
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if(userDAO.insertUser(userID, fullName, Role.BUYER, hashedPassword, phone) == 0){
            return ServiceResponse.failure(MessageKey.REGISTER_USER_FAILED);
        }
        
        return ServiceResponse.success(MessageKey.REGISTER_USER_SUCCESS);
    }
    
    public User login(String userID, String password)
            throws SQLException{
        if(isNullOrEmptyString(userID) || isNullOrEmptyString(password)){
            return null;
        }
        
        User user = userDAO.login(userID); 
        if(user == null) return null;
        
        String storedHashedPassword = user.getPassword();
        
        return BCrypt.checkpw(password, storedHashedPassword) 
                ? user : null;
    }
    
    public User getUserByID(String userID) throws SQLException{
        return userDAO.getUserByID(userID, true);
    }
    
    public List<User> getAllUsers() throws SQLException{
        return userDAO.getAllUsers();
    }
    
    public List<User> getUsersByID(String userID) throws SQLException {
        return userDAO.getUsersByID(userID);
    }
    
    public List<User> getUsersByName(String name) throws SQLException {
        return userDAO.getUsersByName(name);
    }
    
    public String createUser(String userID, String fullName, Role role,
            String password, String confirmPassword, String phone) throws SQLException{
        if(isNullOrEmptyString(userID) 
                || isNullOrEmptyString(fullName)
                || role == null
                || isNullOrEmptyString(password)
                || isNullOrEmptyString(confirmPassword)
                || isNullOrEmptyString(phone)){
            return MessageKey.ALL_FIELDS_REQUIRED;
        }
        
        if(!checkPhone(phone)){
            return MessageKey.INVALID_PHONE_FORMAT;
        }
        
        if(userDAO.checkUserExists(userID)){
            return MessageKey.USER_ID_EXISTED;
        }
        
        if(!checkConfirmPassword(password, confirmPassword)){
            return MessageKey.PASSWORD_NOT_MATCH;
        }
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        if(userDAO.insertUser(userID, fullName, role, hashedPassword, phone) == 0){
            return MessageKey.CREATE_USER_FAILED;
        }
        
        return MessageKey.CREATE_USER_SUCCESS;
    }
    
    public String updateUser(String userID, String fullName, Role role,
            String oldPassword, String password, String confirmPassword, String phone) 
            throws SQLException{
        if(!userDAO.checkUserExists(userID)){
            return MessageKey.USER_NOT_FOUND;
        }
        
        User user = userDAO.getUserByID(userID, false);
        
        if(!isNullOrEmptyString(oldPassword) 
            && (isNullOrEmptyString(password) 
                || isNullOrEmptyString(confirmPassword))){
            return MessageKey.PASSWORD_AND_CONFIRM_REQUIRED;
        }
        
        if(isNullOrEmptyString(oldPassword)
            && (!isNullOrEmptyString(password)
                || !isNullOrEmptyString(confirmPassword))){
            return MessageKey.OLD_PASSWORD_REQUIRED;
        }
        
        if(!isNullOrEmptyString(password)
            && !isNullOrEmptyString(confirmPassword)
            && !checkConfirmPassword(password, confirmPassword)){
            return MessageKey.PASSWORD_NOT_MATCH;
        }
        
        if(isNullOrEmptyString(fullName)){
            fullName = user.getFullName();
        }
        
        if(role == null) {
            role = user.getRole();
        }
        
        if(isNullOrEmptyString(password)){
            password = user.getPassword();
        } else {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
        
        if(isNullOrEmptyString(phone)){
            phone = user.getPhone();
        } else if (!checkPhone(phone)){
            return MessageKey.INVALID_PHONE_FORMAT;
        }
        
        if(userDAO.updateUser(userID, fullName, role, password, phone) == 0){
            return MessageKey.UPDATE_USER_FAILED;
        }
        
        return MessageKey.UPDATE_USER_SUCCESS;
    }
    
    public String updateProfile(String userID, String fullName,
            String oldPassword, String password, String confirmPassword, String phone) 
            throws SQLException{
        if(!userDAO.checkUserExists(userID)){
            return MessageKey.USER_NOT_FOUND;
        }
        
        User user = userDAO.getUserByID(userID, false);
        
        if(!isNullOrEmptyString(oldPassword) 
            && (isNullOrEmptyString(password) 
                || isNullOrEmptyString(confirmPassword))){
            return MessageKey.PASSWORD_AND_CONFIRM_REQUIRED;
        }
        
        if(isNullOrEmptyString(oldPassword)
            && (!isNullOrEmptyString(password)
                || !isNullOrEmptyString(confirmPassword))){
            return MessageKey.OLD_PASSWORD_REQUIRED;
        }
        
        if(!isNullOrEmptyString(password)
            && !isNullOrEmptyString(confirmPassword)
            && !checkConfirmPassword(password, confirmPassword)){
            return MessageKey.PASSWORD_NOT_MATCH;
        }
        
        if(isNullOrEmptyString(fullName)){
            fullName = user.getFullName();
        }
        
        if(isNullOrEmptyString(password)){
            password = user.getPassword();
        } else {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
        
        if(isNullOrEmptyString(phone)){
            phone = user.getPhone();
        } else if (!checkPhone(phone)){
            return MessageKey.INVALID_PHONE_FORMAT;
        }
        
        if(userDAO.updateProfile(userID, fullName, password, phone) == 0){
            return MessageKey.UPDATE_USER_FAILED;
        }
        
        return MessageKey.UPDATE_USER_SUCCESS;
    }
    
    public String deleteUser(String userID) throws SQLException{
        if(userDAO.deleteUser(userID) == 0){
            return MessageKey.USER_NOT_FOUND;
        }
        
        return MessageKey.DELETE_USER_SUCCESS;
    }
    
    private boolean isNullOrEmptyString(String str){
        return str == null || str.isEmpty();
    }
    
    private boolean checkPhone(String phone){
        return phone.matches(Regex.USER_PHONE);
    }
    
    private boolean checkConfirmPassword(String pwd, String confirmPwd) {
        return pwd.equals(confirmPwd);
    }
}
