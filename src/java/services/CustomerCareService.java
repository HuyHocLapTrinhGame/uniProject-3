package services;

import constants.MessageKey;
import daos.CustomerCareDAO;
import daos.UserDAO;
import dtos.CustomerCare;
import dtos.CustomerCareViewModel;
import java.sql.SQLException;
import java.util.List;
import static utils.ServiceUtils.isNullOrEmptyString;

public class CustomerCareService {

    CustomerCareDAO customerCareDAO = new CustomerCareDAO();
    UserDAO userDAO = new UserDAO();

    //create
    public String create(String userID, String subject, String content) throws SQLException {
        if (!userDAO.checkUserExists(userID)) {
            return MessageKey.USER_NOT_FOUND;
        }
        if (isNullOrEmptyString(subject) || isNullOrEmptyString(content)) {
            return MessageKey.INVALID_CUSTOMERCARE_FORMAT;
        }
        if (customerCareDAO.create(userID, subject, content, "waiting", "pending") != 0) {
            return MessageKey.CREATE_CUSTOMERCARE_SUCCESS;
        }
        return MessageKey.CREATE_CUSTOMERCARE_FAILED;
    }

    //delete
    public String deleteByID(int ticketID) throws SQLException {
        if (customerCareDAO.deleteByID(ticketID) != 0) {
            return MessageKey.DELETE_CUSTOMERCARE_SUCCESS;
        }
        return MessageKey.DELETE_CUSTOMERCARE_FAILED;
    }

    //update
    public String update(int id, String userID, String subject, String content, String status, String reply) throws SQLException {
        if (!userDAO.checkUserExists(userID)) {
            return MessageKey.USER_NOT_FOUND;
        }
        if (isNullOrEmptyString(subject) || isNullOrEmptyString(content)
                || isNullOrEmptyString(status) || isNullOrEmptyString(reply)) {
            return MessageKey.INVALID_CUSTOMERCARE_FORMAT;
        }
        if (customerCareDAO.update(id, userID, subject, content, status, reply) == 1) {
            return MessageKey.CREATE_CUSTOMERCARE_SUCCESS;
        }
        return MessageKey.UPDATE_CUSTOMERCARE_FAILED;
    }

    // check exist by id
    public boolean checkExistByID(int ticketID) throws SQLException {
        return customerCareDAO.checkExistByID(ticketID);
    }
    
    //search by id
    public CustomerCare searchByID(int ticketID, String userID) throws SQLException {
        return customerCareDAO.searchByID(ticketID, userID);
    }

    //search by subject
    public List<CustomerCare> searchBySubject(String subject, String userID) throws SQLException {
        return customerCareDAO.searchBySubject(subject, userID);
    }

    //get all
    public List<CustomerCare> getAll(String userID) throws SQLException {
        return customerCareDAO.getAll(userID);
    }

    public List<CustomerCareViewModel> getAllViewModels() throws SQLException {
        return customerCareDAO.getAllViewModels();
    }
}
