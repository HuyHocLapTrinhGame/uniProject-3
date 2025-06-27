
package controllers;

import constants.MessageKey;
import constants.Role;
import constants.Url;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import dtos.User;
import utils.Message;
import java.util.ArrayList;
import responses.ServiceResponse;
import services.UserService;
import utils.AuthUtils;

@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    private UserService userService = new UserService();

    private final String CREATE = "create";
    private final String GET_ALL_USERS = "getAllUsers";
    private final String GET_USERS_BY_ID = "getUsersByID";
    private final String GET_USERS_BY_NAME = "getUsersByName";
    private final String UPDATE = "update";
    private final String UPDATE_PROFILE = "update-profile";
    private final String DELETE = "delete";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_USERS;
        }

        List<User> users = new ArrayList<>();
        String url = Url.USER_LIST_PAGE;
        switch (action) {
            case CREATE: {
                url = Url.CREATE_USER_PAGE;
                break;
            }
            case UPDATE:
            case UPDATE_PROFILE: {
                users.add(getUserByID(request, response));
                url = Url.UPDATE_USER_PAGE;
                break;
            }
            case GET_ALL_USERS: {
                users = getAllUsers(request, response);
                break;
            }
            case GET_USERS_BY_ID: {
                users = getUsersByID(request, response);
                break;
            }
            case GET_USERS_BY_NAME: {
                users = getUsersByName(request, response);
                break;
            }
        }

        if (action.equals(UPDATE) || action.equals(UPDATE_PROFILE)) {
            request.setAttribute("user", users.get(0));
        } else {
            request.setAttribute("users", users);
        }

        request.setAttribute("roleList", Role.values());
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        String url = Url.USER_LIST_PAGE;
        try {
            switch (action) {
                case CREATE: {
                    createUser(request, response);
                    url = Url.CREATE_USER_PAGE;
                    break;
                }
                case UPDATE: {
                    updateUser(request, response);
                    url = Url.UPDATE_USER_PAGE;
                    User user = getUserByID(request, response);
                    request.setAttribute("user", user);
                    break;
                }
                case UPDATE_PROFILE: {
                    updateProfile(request, response);
                    url = Url.UPDATE_USER_PAGE;
                    User user = getUserByID(request, response);
                    request.setAttribute("user", user);
                    break;
                }
                case DELETE: {
                    deleteUser(request, response);
                    break;
                }
            }

            request.setAttribute("users", userService.getAllUsers());
            request.setAttribute("roleList", Role.values());
            request.getRequestDispatcher(url).forward(request, response);
        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private User getUserByID(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userID = request.getParameter("userID");
            User user = userService.getUserByID(userID);
            if (user == null) {
                user = new User();
                request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.USER_NOT_FOUND));
            }
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private List<User> getAllUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            return userService.getAllUsers();
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private List<User> getUsersByID(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userID = request.getParameter("keySearch");
            return userService.getUsersByID(userID);
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private List<User> getUsersByName(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("keySearch");
            return userService.getUsersByName(name);
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        String phone = request.getParameter("phone");
        String message;
        try {
            message = userService.createUser(userID, fullName,
                    Role.fromValue(roleID), password, confirmPassword, phone);
        } catch (IllegalArgumentException ex) {
            message = MessageKey.ROLE_ID_NOT_FOUND;
        }
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String oldPassword = request.getParameter("oldPassword");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        String phone = request.getParameter("phone");
        String message;
        try {
            message = userService.updateUser(userID, fullName,
                    Role.fromValue(roleID), oldPassword, password, confirmPassword, phone);
        } catch (IllegalArgumentException ex) {
            message = MessageKey.ROLE_ID_NOT_FOUND;
        }
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        ServiceResponse<User> srUser = AuthUtils.getUserSession(request);
        if(!srUser.isSuccess()){
            request.setAttribute("MSG", Message.get(request.getSession(false), srUser.getMessage()));
            return ;
        }
        User currentUser = srUser.getData();
        
        String userID = currentUser.getUserID();
        String fullName = request.getParameter("fullName");
        String oldPassword = request.getParameter("oldPassword");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String message = userService.updateProfile(userID, fullName, oldPassword, 
                password, confirmPassword, phone);
        
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String userID = request.getParameter("userID");
        String message = userService.deleteUser(userID);

        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }
}