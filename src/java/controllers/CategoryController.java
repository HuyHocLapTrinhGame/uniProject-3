package controllers;

import constants.MessageKey;
import constants.Role;
import constants.Url;
import dtos.Category;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import services.CategoryService;
import utils.Message;

@WebServlet(name="CategoryController", urlPatterns={"/category"})
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();
    private final String CREATE = "create";
    private final String UPDATE = "update";
    private final String DELETE = "delete";
    private final String FIND_BY_ID = "findByID";
    private final String FIND_BY_NAME = "findByName";
    private final String GET_ALL_CATEGORY = "getAll";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_CATEGORY;
        }

        List<Category> list = null;
        String url = Url.CATEGORY_LIST_PAGE;

        switch (action) {
            case CREATE:
                url = Url.CREATE_CATEGORY_PAGE;
                break;
            case UPDATE:
                list = findByID(request, response);
                url = Url.UPDATE_CATEGORY_PAGE;
                break;
            case FIND_BY_ID:
                list = findByID(request, response);
                break;
            case FIND_BY_NAME:
                list = findByName(request, response);
                break;
            case GET_ALL_CATEGORY:
                list = getAllCategory(request, response);
                break;
            default:
                throw new AssertionError("Invalid action: " + action);
        }

        if (UPDATE.equals(action) && list != null && !list.isEmpty()) {
            request.setAttribute("category", list.get(0));
        }

        if (list == null) {
            list = new ArrayList<>();
        }

        request.setAttribute("categories", list);
        request.getRequestDispatcher(url).forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        String url = Url.CATEGORY_LIST_PAGE;

        try {
            switch (action) {
                case CREATE:
                    createCategory(request, response);
                    url = Url.CREATE_CATEGORY_PAGE;
                    break;
                case UPDATE:
                    updateCategory(request, response);
                    break;
                case DELETE:
                    deleteCategory(request, response);
                    break;
            }

            request.setAttribute("categories", categoryService.getAll());
            request.setAttribute("roleList", Role.values());
            request.getRequestDispatcher(url).forward(request, response);

        } catch (NumberFormatException | SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private List<Category> findByName(HttpServletRequest request, HttpServletResponse response) {
        List<Category> list = new ArrayList<>();
        try {
            String name = request.getParameter("keySearch");
            list = categoryService.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.CATEGORY_NOT_FOUND));
        }
        return list;
    }

    private List<Category> getAllCategory(HttpServletRequest request, HttpServletResponse response) {
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryService.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        String message;
        try {
            message = categoryService.create(categoryName, description);
        } catch (IllegalArgumentException ex) {
            message = MessageKey.CREATE_CATEGORY_FAILED;
        }
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int id = Integer.parseInt(request.getParameter("categoryID"));
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");

        Category category = categoryService.findByID(id);
        String message;

        if (category == null) {
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.CATEGORY_NOT_FOUND));
            return;
        }

        try {
            message = categoryService.update(id, categoryName, description);
        } catch (IllegalArgumentException ex) {
            message = MessageKey.UPDATE_CATEGORY_FAILED;
        }

        request.setAttribute("MSG", Message.get(request.getSession(false), message));
        request.setAttribute("categories", categoryService.getAll());
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int id = Integer.parseInt(request.getParameter("categoryID"));
        String message = categoryService.delete(id);
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private List<Category> findByID(HttpServletRequest request, HttpServletResponse response) {
        List<Category> list = new ArrayList<>();
        try {
            int categoryID = Integer.parseInt(request.getParameter("keySearch"));
            Category category = categoryService.findByID(categoryID);
            if (category != null) {
                list.add(category);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
