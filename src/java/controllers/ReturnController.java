/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import constants.MessageKey;
import constants.Url;
import dtos.Delivery;
import dtos.InvoiceViewModel;
import dtos.Return;
import dtos.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import services.DeliveryService;
import services.InvoiceService;
import services.ReturnService;
import utils.AuthUtils;
import utils.Message;

/**
 *
 * @author ngogi
 */
@WebServlet(name = "ReturnController", urlPatterns = {"/return"})
public class ReturnController extends HttpServlet {

    
    private ReturnService returnService = new ReturnService();
    private DeliveryService deliveryService = new DeliveryService();
    private InvoiceService invoiceService = new InvoiceService();
    
    private final String UPDATE = "update";
    private final String CREATE = "create";

    private final String GET_ALL_RETURN = "getAllReturn";
    private final String GET_RETURN_BY_ID = "getReturnByID";
    private final String GET_RETURN_BY_STATUS = "getReturnByStatus";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_RETURN;
        }

        List<Return> returns = null;
        String url = Url.RETURN_LIST_PAGE;
        switch (action) {
            case GET_ALL_RETURN: {
                returns = getAllReturn(request, response);
                break;
            }
            case GET_RETURN_BY_ID: {
                returns = getReturnID(request, response);
                break;
            }
            
            case GET_RETURN_BY_STATUS: {
                returns = getReturnByStatus(request, response);
                break;
            }
            
        }

        request.setAttribute("returns", returns);
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_RETURN;
        }
        String url = Url.RETURN_LIST_PAGE;

        try {
            switch (action) {
                case UPDATE: {
                    updateReturn(request, response);
                    url = Url.RETURN_LIST_PAGE;
                    break;
                }
                
                case CREATE: {
                    createReturn(request, response);
                    url = Url.INVOICE_LIST_PAGE;
                    request.setAttribute("status", "return");
                    List<InvoiceViewModel> invoiceViewModels = getInvoiceByUserIDAndStatus(request, response);
                    request.setAttribute("invoiceViewModels", invoiceViewModels);
                    break;
                }

            }

            request.setAttribute("returns", returnService.getAllReturn());
            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
            request.getRequestDispatcher(Url.ERROR_PAGE).forward(request, response);
        }
    }

    private List<Return> getAllReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            return returnService.getAllReturn();
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }

    private List<Return> getReturnID(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        List<Return> resultList = new ArrayList<>();
        try {
            int returnID = Integer.parseInt(request.getParameter("returnID"));
            resultList.add(returnService.getReturnID(returnID));
            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        return null;
    }
    
     private List<Return> getReturnByStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
         List<Return> resultList = new ArrayList<>();
         try {
//             int returnID = Integer.parseInt(request.getParameter("returnID"));
             String status = request.getParameter("status");
             return returnService.getReturnStatus(status);
         } catch (Exception ex) {
             ex.printStackTrace();
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
         }
         
        return null;
     }
    
    public void updateReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, SQLException {
        int returnID = Integer.parseInt(request.getParameter("returnID"));
        String status = request.getParameter("status");
        String message = returnService.updateReturn(returnID, status);
          request.setAttribute("MSG", Message.get(request.getSession(false), message));
    }
    
    public void createReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, SQLException {
        
        
        String invoiceID = request.getParameter("invoiceID");
        
        String reason = request.getParameter("reason");
        String status = "pending";
        Delivery delivery = deliveryService.getDeliveryByInvoiceID(Integer.parseInt(invoiceID));
        if(delivery.getStatus().equalsIgnoreCase("delivering")) return;
        
        String message = returnService.createReturn(Integer.parseInt(invoiceID), reason, status);
        deliveryService.deleteDelivery(Integer.parseInt(invoiceID));
        
        String userID = AuthUtils.getUserSession(request).getData().getUserID();
        try {
            invoiceService.updateInvoice(invoiceID, userID, "return");
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
