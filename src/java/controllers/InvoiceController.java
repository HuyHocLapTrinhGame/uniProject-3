/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import constants.MessageKey;
import constants.Url;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import services.InvoiceService;
import dtos.InvoiceViewModel;
import dtos.InvoiceDetailViewModel;
import dtos.Return;
import dtos.User;
import java.sql.SQLException;
import java.util.ArrayList;
import responses.ServiceResponse;
import services.ReturnService;
import utils.AuthUtils;
import utils.Message;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "InvoiceController", urlPatterns = {"/invoice"})
public class InvoiceController extends HttpServlet {

    private ReturnService returnService = new ReturnService();
    private InvoiceService invoiceService = new InvoiceService();
    private final String GET_INVOICES_BY_USER_ID_AND_STATUS = "getInvoiceByUserIDAndStatus";
    private final String GET_INVOICE_INFORMATION = "getInvoiceInformation";
    private final String CREATE = "create";
    private final String UPDATE_INVOICE_DETAIL_QUANTITY = "updateInvoiceDetailQuantity";
    private final String UPDATE_INVOICE_STATUS = "updateInvoiceStatus";
    private final String DELETE_INVOICE_DETAIL = "deleteInvoiceDetail";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.equals("")) {
            action = GET_INVOICES_BY_USER_ID_AND_STATUS;
        }

        List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
        String url = Url.INVOICE_LIST_PAGE;
        switch (action) {
            case GET_INVOICES_BY_USER_ID_AND_STATUS: {
                invoiceViewModels = getInvoiceByUserIDAndStatus(request, response);
                break;
            }

            case GET_INVOICE_INFORMATION: {
                invoiceViewModels.add(getInvoiceInformation(request, response));
                url = Url.INVOICE_DETAIL_PAGE;
                break;
            }
        }
        if (action.equalsIgnoreCase(GET_INVOICE_INFORMATION)) {
            request.setAttribute("status", invoiceViewModels.get(0).getStatus());
            request.setAttribute("invoiceViewModel", invoiceViewModels.get(0));
        } else {
            request.setAttribute("invoiceViewModels", invoiceViewModels);
        }
        request.getRequestDispatcher(url).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        String url = Url.INVOICE_LIST_PAGE;
        List<InvoiceViewModel> invoiceViewModels = new ArrayList();
        try {
            switch (action) {
                case CREATE: {
                    invoiceViewModels.add(createInvoiceAndInvoiceDetail(request, response));
                    url = Url.INVOICE_DETAIL_PAGE;
                    break;
                }
                case UPDATE_INVOICE_DETAIL_QUANTITY: {
                    invoiceViewModels.add(updateInvoiceDetailQuantity(request, response));
                    url = Url.INVOICE_DETAIL_PAGE;
                    break;
                }
                case UPDATE_INVOICE_STATUS: {
                    updateInvoiceStatus(request, response);
                    break;
                }
                case DELETE_INVOICE_DETAIL: {
                    InvoiceViewModel in = deleteInvoiceDetail(request, response);
                    if (in != null) {
                        invoiceViewModels.add(in);
                        url = Url.INVOICE_DETAIL_PAGE;
                    }
                    break;
                }
            }
            if (action.equalsIgnoreCase(UPDATE_INVOICE_STATUS) || invoiceViewModels.isEmpty()) {
                invoiceViewModels = getInvoiceByUserIDAndStatus(request, response);
                request.setAttribute("invoiceViewModels", invoiceViewModels);
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                request.setAttribute("status", invoiceViewModels.get(0).getStatus());
                request.setAttribute("invoiceViewModel", invoiceViewModels.get(0));
                request.getRequestDispatcher(url).forward(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private List<InvoiceViewModel> getInvoiceByUserIDAndStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = AuthUtils.getUserSession(request).getData();
            String status = request.getParameter("status");
            if (status == null || status.isEmpty()) {
                status = "pending";
            }
            List<InvoiceViewModel> invoiceViewModels = invoiceService.getInvoicesByUserIDAndStatus(user.getUserID(), status);
            return invoiceViewModels;
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private InvoiceViewModel getInvoiceInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = AuthUtils.getUserSession(request).getData();
            String _invoiceID = request.getParameter("invoiceID");
            
            ServiceResponse<InvoiceViewModel> sr = invoiceService.getInvoiceByID(_invoiceID, user.getUserID());
            if (!sr.getMessage().equals(MessageKey.SUCCESS)) {
                request.setAttribute("MSG", Message.get(request.getSession(false), sr.getMessage()));
            }
            
            if (sr.getData().getStatus().trim().equalsIgnoreCase("return")) {
                Return re = returnService.getReturnByInvoiceID(Integer.parseInt(_invoiceID));
                request.setAttribute("returnStatus", sr.getData().getStatus().toUpperCase() + " " + re.getStatus().toUpperCase());
                request.setAttribute("reason", re.getReason());
            }
            return sr.getData();
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private InvoiceViewModel createInvoiceAndInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        try {
            User user = AuthUtils.getUserSession(request).getData();
            String[] productID = request.getParameterValues("productID");
            String[] quantity = request.getParameterValues("quantity");
            String[] price = request.getParameterValues("price");
            ServiceResponse<InvoiceViewModel> sr = invoiceService.create(user.getUserID(), productID, quantity, price);
            request.setAttribute("MSG", Message.get(request.getSession(false), sr.getMessage()));
            return sr.getData();
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private InvoiceViewModel updateInvoiceDetailQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        try {
            String invoiceID = request.getParameter("invoiceID");
            String productID = request.getParameter("productID");
            String quantity = request.getParameter("quantity");
            String userID = AuthUtils.getUserSession(request).getData().getUserID();
            ServiceResponse<InvoiceDetailViewModel> sr = invoiceService.updateInvoiceDetail(invoiceID, productID, quantity);
            if (sr.isSuccess()) {
                ServiceResponse<InvoiceViewModel> srs = invoiceService.updateInvoiceTotalAmount(invoiceID, userID);
            }
            request.setAttribute("MSG", Message.get(request.getSession(false), sr.getMessage()));
            return invoiceService.getInvoiceByID(invoiceID, userID).getData();
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private InvoiceViewModel deleteInvoiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        try {
            String invoiceID = request.getParameter("invoiceID");
            String productID = request.getParameter("productID");
            String userID = AuthUtils.getUserSession(request).getData().getUserID();
            ServiceResponse sr = invoiceService.deleteInvoiceDetailByInvoicveIDAndProductID(invoiceID, productID);
            if (sr.isSuccess()) {
                ServiceResponse<InvoiceViewModel> srs = invoiceService.updateInvoiceTotalAmount(invoiceID, userID);
            }
            request.setAttribute("MSG", Message.get(request.getSession(false), sr.getMessage()));
            InvoiceViewModel invoiceViewModel = invoiceService.getInvoiceByID(invoiceID, userID).getData();
            if (invoiceViewModel.getTotalAmount() == 0) {
                invoiceService.deleteInvoice(invoiceID);
                return null;
            }
            return invoiceViewModel;
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private InvoiceViewModel updateInvoiceStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NumberFormatException {
        try {
            String invoiceID = request.getParameter("invoiceID");
            String status = request.getParameter("status");
            String userID = AuthUtils.getUserSession(request).getData().getUserID();
            invoiceService.updateInvoice(invoiceID, userID, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

}
