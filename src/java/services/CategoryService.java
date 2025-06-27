package services;

import constants.MessageKey;
import daos.CategoryDAO;
import dtos.Category;
import java.sql.SQLException;
import java.util.List;
import static utils.ServiceUtils.isNullOrEmptyString;

public class CategoryService {
    CategoryDAO categoryDAO = new CategoryDAO();
    
    //create
    public String create(String name,String description) throws SQLException{
        if(categoryDAO.isExit(name)){
            return MessageKey.CATEGORY_EXISTED;
        }
        if(isNullOrEmptyString(name) || isNullOrEmptyString(description)){
            return MessageKey.INVALID_CATEGORY_FORMAT;
        }
        if(categoryDAO.create(name, description)==1){
            return MessageKey.CREATE_CATEGORY_SUCCESS;
        }
        else{
            return MessageKey.CREATE_CATEGORY_FAILED;
        }
    }
    
    //update
    public String update(int id, String name, String description) throws SQLException {
        if (isNullOrEmptyString(name) || isNullOrEmptyString(description)) {
            return MessageKey.INVALID_CATEGORY_FORMAT;
        }

        Category existing = categoryDAO.searchByID(id);
        if (existing == null) {
            return MessageKey.CATEGORY_NOT_FOUND;
        }

        if (categoryDAO.update(id, name, description) == 1) {
            return MessageKey.UPDATE_CATEGORY_SUCCESS;
        } else {
            return MessageKey.UPDATE_CATEGORY_FAILED;
        }
    }
    
    //delete
    public String delete(int id) throws SQLException{
        if(categoryDAO.deleteByID(id)==1){
            return MessageKey.DELETE_CATEGORY_SUCCESS;
        }
        return MessageKey.UPDATE_CATEGORY_FAILED;
    }
    
    //find by id
    public Category findByID(int id) throws SQLException{
        return categoryDAO.searchByID(id);
    }
    
    //find by name
    public List<Category> findByName(String name) throws SQLException{
        if(isNullOrEmptyString(name)){
            return null;
        }
        return categoryDAO.searchByCategory(name);
    }
    
    //get all
    public List<Category> getAll() throws SQLException{
        return categoryDAO.getAll();
    }
}
