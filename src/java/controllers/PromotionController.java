package controllers;

import constants.MessageKey;
import constants.Role;
import constants.Url;
import utils.Message;
import dtos.PromotionViewModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.PromotionService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "PromotionController", urlPatterns = {"/promotion"})
public class PromotionController extends HttpServlet {

    private final PromotionService promotionService = new PromotionService();

    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_NAME = "searchByName";
    private static final String GET_ALL = "getAll";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = GET_ALL;

        List<PromotionViewModel> list = null;
        String url = Url.PROMOTION_LIST_PAGE;

        try {
            switch (action) {
                case CREATE:
                    url = Url.CREATE_PROMOTION_PAGE;
                    break;

                case UPDATE:
                    list = searchByID(request, response);
                    url = Url.UPDATE_PROMOTION_PAGE;
                    break;

                case SEARCH_BY_ID:
                    list = searchByID(request, response);
                    break;

                case SEARCH_BY_NAME:
                    list = searchByName(request, response);
                    break;

                case GET_ALL:
                default:
                    list = promotionService.getAll();
                    break;
            }

            if (UPDATE.equals(action)) {
                if (list != null && !list.isEmpty()) {
                    request.setAttribute("promotions", list.get(0));
                }
            } else {
                request.setAttribute("promotions", list);
            }

            request.setAttribute("roleList", Role.values());
            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        String url = Url.PROMOTION_LIST_PAGE;

        try {
            switch (action) {
                case CREATE:
                    createPromotion(request);
                    url = Url.CREATE_PROMOTION_PAGE;
                    break;

                case UPDATE:
                    updatePromotion(request);
                    url = Url.PROMOTION_LIST_PAGE;
                    break;

                case DELETE:
                    deletePromotion(request);
                    break;
            }

            request.setAttribute("promotions", promotionService.getAll());
            request.setAttribute("roleList", Role.values());
            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private void createPromotion(HttpServletRequest request) throws SQLException {
        String name = request.getParameter("name");
        float discount = Float.parseFloat(request.getParameter("discount"));
        String status = request.getParameter("status");
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");

        String message;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            message = promotionService.create(name, discount, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
            message = MessageKey.CREATE_PROMOTION_FAILED;
        }

        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private void updatePromotion(HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("promoID"));
        String name = request.getParameter("name");
        float discount = Float.parseFloat(request.getParameter("discount"));
        String status = request.getParameter("status");
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");

        String message;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            message = promotionService.update(id, name, discount, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
            message = MessageKey.UPDATE_PROMOTION_FAILED;
        }

        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private void deletePromotion(HttpServletRequest request) throws SQLException {
        int id = Integer.parseInt(request.getParameter("promoID"));
        String message = promotionService.delete(id);
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }

    private List<PromotionViewModel> searchByID(HttpServletRequest request, HttpServletResponse response) {
        List<PromotionViewModel> list = new ArrayList<>();
        try {
            int id = Integer.parseInt(request.getParameter("keySearch"));
            PromotionViewModel promotion = promotionService.searchByID(id);
            if (promotion != null) list.add(promotion);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.INVALID_FORMAT));
        }
        return list;
    }

    private List<PromotionViewModel> searchByName(HttpServletRequest request, HttpServletResponse response) {
        List<PromotionViewModel> list = new ArrayList<>();
        try {
            String name = request.getParameter("keySearch");
            list = promotionService.searchByName(name);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.INVALID_FORMAT));
        }
        return list;
    }
}
