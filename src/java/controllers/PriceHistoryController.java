package controllers;

import constants.Url;
import dtos.PriceHistoryTracking;
import services.PriceHistoryService;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PriceHistoryController", urlPatterns = {"/priceHistory"})
public class PriceHistoryController extends HttpServlet {

    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_FIND_BY_ID = "findByID";
    private static final String ACTION_GET_ALL = "getAll";

    private final PriceHistoryService historyService = new PriceHistoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = ACTION_GET_ALL;
        }

        List<PriceHistoryTracking> historyList = null;
        String message = null;
        String url = Url.HISTORY_LIST_PAGE;

        try {
            switch (action) {
                case ACTION_FIND_BY_ID:
                    int productID = Integer.parseInt(request.getParameter("productID"));
                    historyList = historyService.getHistoryByProductID(productID);
                    request.setAttribute("productID", productID);
                    break;

                case ACTION_DELETE:
                    message = historyService.deleteOldHistoriesByID();
                    historyList = historyService.getAllHistory();
                    break;

                case ACTION_GET_ALL:
                default:
                    historyList = historyService.getAllHistory();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "An error occurred while processing the request.";
        }

        request.setAttribute("MSG", message);
        request.setAttribute("historyList", historyList);
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String message = null;

        try {
            if (ACTION_UPDATE.equals(action)) {
                int historyID = Integer.parseInt(request.getParameter("historyID"));
                String note = request.getParameter("note");
                message = historyService.updateNote(historyID, note);
                request.getSession().setAttribute("MSG", message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("MSG", "Error while updating note.");
        }

        // Redirect to GET to prevent form resubmission
        response.sendRedirect(request.getContextPath() + "/main/priceHistory");
    }
}
