/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import constants.MessageKey;
import constants.Url;
import daos.ProductDAO;
import dtos.Product;
import dtos.User;
import dtos.ProductViewModel;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import responses.ServiceResponse;
import services.ProductService;
import utils.AuthUtils;
import utils.Message;

/**
 *
 * @author Admin
 */
@WebServlet(name="ProductController", urlPatterns={"/product"})
public class ProductController extends HttpServlet {
    private ProductService productService = new ProductService();
    
    private final String GET_ALL_PRODUCTS = "getAllProducts";
    private final String GET_PRODUCT_BY_ID = "getProductByID";
    private final String GET_PRODUCTS_BY_NAME = "getProductsByName";
    private final String GET_PRODUCTS_BY_CATEGORY_NAME = "getProductsByCategoryName";
    private final String GET_PRODUCTS_BY_MIN_QUANTITY = "getProductsByMinQuantity";
    private final String GET_PRODUCTS_BY_SELLER_ID = "getProductsBySellerID";
    private final String GET_PRODUCTS_BY_STATUS = "getProductsByStatus";
    
    private final String CREATE = "create";
    private final String UPDATE = "update";
    private final String UPDATE_QUANTITY = "updateQuantity";
    private final String DELETE = "delete";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = GET_ALL_PRODUCTS;
        }
        
        List<ProductViewModel> products = new ArrayList<>();
        String url = Url.PRODUCT_LIST_PAGE;
        
        try {
            switch (action) {
                case CREATE:{
                    url = Url.CREATE_PRODUCT_PAGE;
                    break;
                }
                case UPDATE:{
                    products.add(getProductByID(request));
                    url = Url.UPDATE_PRODUCT_PAGE;
                    break;
                }
                case GET_ALL_PRODUCTS:{
                    products = getAllProducts(request);
                    break;
                }
                case GET_PRODUCT_BY_ID:{
                    products.add(getProductByID(request));
                    break;
                }
                case GET_PRODUCTS_BY_NAME:{
                    products = getProductsByName(request);
                    break;
                }
                case GET_PRODUCTS_BY_CATEGORY_NAME:{
                    products = getProductsByCategoryName(request);
                    break;
                }
                case GET_PRODUCTS_BY_MIN_QUANTITY:{
                    products = getProductsByMinQuantity(request);
                    break;
                }
                case GET_PRODUCTS_BY_SELLER_ID:{
                    products = getProductsBySellerID(request);
                    break;
                }
                case GET_PRODUCTS_BY_STATUS:{
                    products = getProductsByStatus(request);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            url = Url.ERROR_PAGE;
            request.setAttribute("MSG", Message.get(request.getSession(false), MessageKey.SYSTEM_ERROR));
        }
        
        if (action.equals(UPDATE)) {
            request.setAttribute("product", products.get(0));
        } else {
            request.setAttribute("products", products);
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
        
        String url = Url.PRODUCT_LIST_PAGE;
        String message = MessageKey.SYSTEM_ERROR;
        try{
            switch(action){
                case CREATE:{
                    message = createProduct(request);
                    url = Url.CREATE_PRODUCT_PAGE;
                    break;
                }
                case UPDATE:{
                    message = updateProduct(request);
                    request.setAttribute("product", getProductByID(request));
                    url = Url.UPDATE_PRODUCT_PAGE;
                    break;
                }
                case UPDATE_QUANTITY:{
                    message = updateProductQuantityAndStatus(request);
                    request.setAttribute("product", getProductByID(request));
                    url = Url.PRODUCT_LIST_PAGE;
                    break;
                }
                case DELETE:{
                    message = deleteProduct(request);
                    request.setAttribute("products", getAllProducts(request));
                    break;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            url = Url.ERROR_PAGE;
        }
        request.setAttribute("MSG", Message.get(request.getSession(false), message));
        request.getRequestDispatcher(url).forward(request, response);
    }
    
    private ProductViewModel getProductByID(HttpServletRequest request) 
            throws ServletException, IOException, NumberFormatException, SQLException{
        int productID = Integer.parseInt(request.getParameter("productID"));
        
        ServiceResponse<ProductViewModel> sr = productService.getProductByID(productID);
        
        return handleServiceResponse(request, sr);
    }

    private List<ProductViewModel> getAllProducts(HttpServletRequest request)
            throws ServletException, IOException, SQLException{
        ServiceResponse<List<ProductViewModel>> sr = productService.getAllProducts();
        return handleServiceResponse(request, sr);
    }
    
    private List<ProductViewModel> getProductsByName(HttpServletRequest request)
            throws ServletException, IOException, SQLException{
        String keySearch = request.getParameter("keySearch");
        ServiceResponse<List<ProductViewModel>> sr = productService.getProductsByName(keySearch);
        return handleServiceResponse(request, sr);
    }
    
    private List<ProductViewModel> getProductsByCategoryName(HttpServletRequest request)
            throws ServletException, IOException, SQLException{
        String keySearch = request.getParameter("keySearch");
        ServiceResponse<List<ProductViewModel>> sr = productService.getProductsByCategoryName(keySearch);
        return handleServiceResponse(request, sr);
    }
    
    private List<ProductViewModel> getProductsByMinQuantity(HttpServletRequest request)
            throws ServletException, IOException, SQLException, NumberFormatException{
        int keySearch = Integer.parseInt(request.getParameter("keySearch"));
        ServiceResponse<List<ProductViewModel>> sr = productService.getProductsByMinQuantity(keySearch);
        return handleServiceResponse(request, sr);
    }
    
    private List<ProductViewModel> getProductsBySellerID(HttpServletRequest request)
            throws ServletException, IOException, SQLException{
        String keySearch = request.getParameter("keySearch");
        ServiceResponse<List<ProductViewModel>> sr = productService.getProductsBySellerID(keySearch);
        return handleServiceResponse(request, sr);
    }
    
    private List<ProductViewModel> getProductsByStatus(HttpServletRequest request)
            throws ServletException, IOException, SQLException{
        String keySearch = request.getParameter("keySearch");
        ServiceResponse<List<ProductViewModel>> sr = productService.getProductsByStatus(keySearch);
        return handleServiceResponse(request, sr);
    }
    
    private String createProduct(HttpServletRequest request)
            throws ServletException, IOException, SQLException, NumberFormatException{
        // get currentUser
        ServiceResponse<User> sr = AuthUtils.getUserSession(request);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        User currentUser = sr.getData();
        
        String name = request.getParameter("name");
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String sellerID = currentUser.getUserID();
        String status = request.getParameter("status");
        
        Integer promoID = null;
        String _promoID = request.getParameter("promoID");
        if(_promoID != null && !_promoID.isEmpty()){
            promoID = Integer.parseInt(_promoID);
        }
        
        return productService.createProduct(name, categoryID, price, quantity, sellerID, status, promoID);
    }
    
    private String updateProduct(HttpServletRequest request)
            throws ServletException, IOException, SQLException, NumberFormatException{
        // get currentUser
        ServiceResponse<User> sr = AuthUtils.getUserSession(request);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        User currentUser = sr.getData();
        
        int productID = Integer.parseInt(request.getParameter("productID"));
        String name = request.getParameter("name");
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String status = request.getParameter("status");
        
        Integer promoID = null;
        String _promoID = request.getParameter("promoID");
        if(_promoID != null && !_promoID.isEmpty()){
            promoID = Integer.parseInt(_promoID);
        }
        
        ServiceResponse<ProductViewModel> currentProduct = productService.getProductByID(productID);
        double oldPrice = currentProduct.getData().getBasePrice();

        if (oldPrice != price) {
            Timestamp changeDate = new Timestamp(System.currentTimeMillis());
            String note = oldPrice > price ? "increasing price" : "decreasing price";
            productService.createHistory(productID, changeDate, oldPrice, price, note);
        }
        return productService.updateProduct(productID, name, categoryID, price, quantity, status, promoID, currentUser);
    }
    
    private String updateProductQuantityAndStatus(HttpServletRequest request)
            throws ServletException, IOException, SQLException, NumberFormatException{
        // get currentUser
        ServiceResponse<User> sr = AuthUtils.getUserSession(request);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        User currentUser = sr.getData();
        
        int productID = Integer.parseInt(request.getParameter("productID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        return productService.updateProductQuantityAndStatus(productID, quantity, currentUser);
    }
    
    private String deleteProduct(HttpServletRequest request)
            throws ServletException, IOException, SQLException, NumberFormatException{
        // get currentUser
        ServiceResponse<User> sr = AuthUtils.getUserSession(request);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        User currentUser = sr.getData();
        
        int productID = Integer.parseInt(request.getParameter("productID"));
        
        return productService.deleteProductByID(productID, currentUser);
    }
    
    
    //helper
    private <T> T handleServiceResponse(HttpServletRequest request, ServiceResponse<T> rs){
        if(!rs.isSuccess()){
            request.setAttribute("MSG", Message.get(request.getSession(false), rs.getMessage()));
        }
        return rs.getData();
    }
}
