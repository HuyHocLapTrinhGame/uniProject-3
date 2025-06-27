/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import constants.MessageKey;
import constants.Url;
import dtos.Delivery;
import dtos.InvoiceViewModel;
import dtos.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import services.DeliveryService;
import services.InvoiceService;
import utils.AuthUtils;
import utils.Message;

/**
 *
 * @author ngogi
 */
@WebServlet(name = "DeliveryController", urlPatterns = {"/delivery"})
public class DeliveryController extends HttpServlet {

    private DeliveryService deliveryService = new DeliveryService();
    private InvoiceService invoiceService = new InvoiceService();

    private final String CREATE = "create";
    private final String UPDATE = "update";

    private final String GET_ALL_DELIVERY = "getAllDelivery";
    private final String GET_DELIVERY_BY_STATUS = "getDeliveryByStatus";
    private final String GET_INVOICE_BY_STATUS = "getInvoiceByStatus";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_DELIVERY;
        }

        List<Delivery> deliverys = null;
        String url = Url.DELIVERY_LIST_PAGE;
        switch (action) {
            case GET_ALL_DELIVERY: {
                deliverys = getAllDelivery(request, response);
                break;
            }
            case GET_DELIVERY_BY_STATUS: {
                deliverys = getDeliveryByStatus(request, response);
                break;
            }
        }

        request.setAttribute("delivery", deliverys);
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_DELIVERY;
        }
        String url = Url.DELIVERY_LIST_PAGE;

        try {
            switch (action) {

                case CREATE: {
                    createDelivery(request, response);
                    url = Url.INVOICE_LIST_PAGE;
                    request.setAttribute("status", "paid");
                    List<InvoiceViewModel> invoiceViewModels = getInvoiceByUserIDAndStatus(request, response);
                    request.setAttribute("invoiceViewModels", invoiceViewModels);
                    break;
                }
                case UPDATE: {
                    updateDelivery(request, response);
                    url = Url.DELIVERY_LIST_PAGE;
                    request.setAttribute("status", "delivered");
                    break;
                }
            }

            request.setAttribute("delivery", deliveryService.getAllDelivery());
            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private List<Delivery> getAllDelivery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            return deliveryService.getAllDelivery();
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private List<Delivery> getDeliveryByStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            String status = request.getParameter("status");
            return deliveryService.getDeliveryByStatus(status);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    public void createDelivery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, SQLException {

        int invoiceID = Integer.parseInt(request.getParameter("invoiceID"));
        String invoiceID_ = request.getParameter("invoiceID");
        String address = request.getParameter("address");
        LocalDate deliveryDate = LocalDate.now();
        String status = "pending";

        String message = deliveryService.createDelivery(invoiceID, address, deliveryDate, status);
        String userID = AuthUtils.getUserSession(request).getData().getUserID();
        try {
            invoiceService.updateInvoice(invoiceID_, userID, "paid");
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }

    }

    public void updateDelivery(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("deliveryID"));
        String invoiceID = request.getParameter("invoiceID");
        String status = request.getParameter("status");

        String message = deliveryService.updateDelivery(id, status);
        request.setAttribute("MSG", Message.get(request.getSession(false), message));

        try {
            invoiceService.updateInvoiceStatus(Integer.parseInt(invoiceID), status);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
    }

    private List<InvoiceViewModel> getInvoiceByUserIDAndStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = AuthUtils.getUserSession(request).getData();
            String status = request.getParameter("status");
            List<InvoiceViewModel> invoiceViewModels = invoiceService.getInvoicesByUserIDAndStatus(user.getUserID(), status);
            return invoiceViewModels;
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

}
