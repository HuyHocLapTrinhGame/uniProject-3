/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import constants.MessageKey;
import daos.CartDAO;
import daos.ProductDAO;
import daos.UserDAO;
import dtos.User;
import dtos.CartDetail;
import dtos.CartViewModel;
import dtos.ProductViewModel;
import java.sql.SQLException;
import java.util.List;
import responses.ServiceResponse;

/**
 *
 * @author Admin
 */
public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    
    private final ProductService productService = new ProductService();

    private final String OUT_OF_STOCK = "outOfStock";
    private final String INACTIVE = "inactive";

    public ServiceResponse<CartViewModel> getCartByID(int cartID) throws SQLException {
        CartViewModel cart = cartDAO.getCartByID(cartID);
        return cart == null
                ? ServiceResponse.failure(MessageKey.CART_NOT_FOUND)
                : ServiceResponse.success(MessageKey.SUCCESS, cart);
    }

    public ServiceResponse<CartViewModel> getCartByUserID(String userID) throws SQLException {
        CartViewModel cart = cartDAO.getCartByUserID(userID);
        return cart == null
                ? ServiceResponse.failure(MessageKey.CART_NOT_FOUND)
                : ServiceResponse.success(MessageKey.SUCCESS, cart);
    }

    public String createCart(String userID) throws SQLException {
        if (!userDAO.checkUserExists(userID)) {
            return MessageKey.USER_NOT_FOUND;
        }
        
        if (cartDAO.getCartByUserID(userID) != null) {
            return MessageKey.CART_EXISTED;
        }
        
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        if (cartDAO.insertCart(userID, currentDate) == 0) {
            return MessageKey.CREATE_CART_FAILED;
        }
        return MessageKey.CREATE_CART_SUCCESS;
    }

    public String deleteCartByID(int cartID, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        
        if (cartDAO.deleteCartByID(cartID) == 0) {
            return MessageKey.CART_NOT_FOUND;
        }
        return MessageKey.DELETE_CART_SUCCESS;
    }

    public String upsertItemToCart(int cartID, int productID, int quantity, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        
        // check quantity input
        if (quantity <= 0) {
            return MessageKey.INVALID_QUANTITY;
        }

        // check product exist
        ProductViewModel product = productDAO.getProductByID(productID);
        if (product == null) {
            return MessageKey.PRODUCT_NOT_FOUND;
        }

        if (product.getStatus().equalsIgnoreCase(INACTIVE)
                || product.getStatus().equalsIgnoreCase(OUT_OF_STOCK)) {
            return MessageKey.PRODUCT_INACTIVE_OR_OUT;
        }

        // check item is exist or not
        CartDetail existingItem = cartDAO.getItemFromCart(cartID, productID);
        int currentQuantityInCart = existingItem != null ? existingItem.getQuantity() : 0;

        // back to quantity in stock if update
        if (existingItem != null) {
            quantity += currentQuantityInCart;
            product.setQuantity(product.getQuantity() + currentQuantityInCart);
        }

        // check new stock
        if (quantity > product.getQuantity()) {
            return MessageKey.QUANTITY_EXCEEDS_AVAILABLE;
        }

        // update quantity in stock
        product.setQuantity(product.getQuantity() - quantity);
        if (product.getQuantity() == 0) {
            product.setStatus(OUT_OF_STOCK);
        }

        // update product
        if (productDAO.updateProductQuantityAndStatus(productID, product.getQuantity(), product.getStatus()) == 0) {
            return MessageKey.UPDATE_PRODUCT_FAILED;
        }

        // upsert to cart
        if (existingItem != null) {
            return cartDAO.updateItemQuantity(cartID, productID, quantity) > 0 
                    ? MessageKey.UPDATE_CART_SUCCESS : MessageKey.UPDATE_CART_FAILED;
        } else {
            return cartDAO.insertItemToCart(cartID, productID, quantity) > 0 
                    ? MessageKey.ADD_TO_CART_SUCCESS : MessageKey.ADD_TO_CART_FAILED;
        }
    }
    
    public String updateItemToCart(int cartID, int productID, int quantity, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr.getMessage();
        }
        
        // check item is exist or not
        CartDetail existingItem = cartDAO.getItemFromCart(cartID, productID);
        if (existingItem == null) {
            return MessageKey.CART_DETAIL_NOT_FOUND;
        }
        
        // check quantity input
        if (quantity <= 0) {
            return MessageKey.INVALID_QUANTITY;
        }

        // check product exist
        ProductViewModel product = productDAO.getProductByID(productID);
        if (product == null) {
            return MessageKey.PRODUCT_NOT_FOUND;
        }

        if (product.getStatus().equalsIgnoreCase(INACTIVE)
                || product.getStatus().equalsIgnoreCase(OUT_OF_STOCK)) {
            return MessageKey.PRODUCT_INACTIVE_OR_OUT;
        }
        
        // back to quantity in stock
        product.setQuantity(product.getQuantity() + existingItem.getQuantity());
        
        // check new stock
        if (quantity > product.getQuantity()) {
            return MessageKey.QUANTITY_EXCEEDS_AVAILABLE;
        }

        // update quantity in stock
        product.setQuantity(product.getQuantity() - quantity);
        if (product.getQuantity() == 0) {
            product.setStatus(OUT_OF_STOCK);
        }

        // update product
        if (productDAO.updateProductQuantityAndStatus(productID, product.getQuantity(), product.getStatus()) == 0) {
            return MessageKey.UPDATE_PRODUCT_FAILED;
        }

        return cartDAO.updateItemQuantity(cartID, productID, quantity) > 0 
                ? MessageKey.UPDATE_CART_SUCCESS : MessageKey.UPDATE_CART_FAILED;
    }
    
    public ServiceResponse deleteItemFromCart(int cartID, int productID, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr;
        }
        
        return deleteItemFromCart(cartID, productID);
    }

    public ServiceResponse deleteItemsFromCart(int cartID, List<Integer> productIDs, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr;
        }
        
        for (int i = 0; i < productIDs.size(); i++){
            sr = deleteItemFromCart(cartID, productIDs.get(i));
            if(!sr.isSuccess()){
                return ServiceResponse.failure(MessageKey.DELETE_ITEMS_FROM_CART_FAILED);
            }
        }
        
        return ServiceResponse.success(MessageKey.DELETE_ITEMS_FROM_CART_SUCCESS);
    }
    
    public ServiceResponse clearCart(int cartID, User currentUser) throws SQLException {
        ServiceResponse sr = isCreator(cartID, currentUser);
        if(!sr.isSuccess()){
            return sr;
        }
        
        List<Integer> productIDs = cartDAO.getProductIDsByCartID(cartID);
        
        if(productIDs.isEmpty()){
            return ServiceResponse.failure(MessageKey.CART_EMPTY);
        }
        
        for (int i = 0; i < productIDs.size(); i++){
            sr = deleteItemFromCart(cartID, productIDs.get(i));
            if(!sr.isSuccess()){
                return ServiceResponse.failure(MessageKey.CLEAR_CART_FAILED);
            }
        }
        
        return ServiceResponse.success(MessageKey.CLEAR_CART_SUCCESS);
    }
    
    //helper
    private ServiceResponse<CartViewModel> isCreator(int cartID, User currentUser) throws SQLException {
        // check cart exist
        CartViewModel cart = cartDAO.getCartByID(cartID);
        if(cart == null){
            return ServiceResponse.failure(MessageKey.CART_NOT_FOUND);
        }
        
        // check creator
        if(!currentUser.getUserID().equalsIgnoreCase(cart.getUserID())){
            return ServiceResponse.failure(MessageKey.UNAUTHORIZED);
        }
        
        return ServiceResponse.success(MessageKey.SUCCESS, cart);
    }
    
    private ServiceResponse deleteItemFromCart(int cartID, int productID) throws SQLException {
        CartDetail cartDetail = cartDAO.getItemFromCart(cartID, productID);
        if(cartDetail == null) {
            return ServiceResponse.failure(MessageKey.CART_DETAIL_NOT_FOUND);
        }
        
        // Update product quantity và status
        ServiceResponse updateResponse = productService.restoreProductStock(productID, cartDetail.getQuantity());
        if (!updateResponse.isSuccess()) {
            return updateResponse;
        }
        
        // delete item from cart
        if(cartDAO.deleteItemFromCart(cartID, productID) == 0){
           return ServiceResponse.failure(MessageKey.CART_DETAIL_NOT_FOUND); 
        }
        
        return ServiceResponse.success(MessageKey.DELETE_ITEMS_FROM_CART_SUCCESS);
    }
    
    public ServiceResponse deleteItemFromCartForCreateInvoice(String userID, int productID) throws SQLException {
        CartViewModel cart = cartDAO.getCartByUserID(userID);
        if (cart == null) {
            return ServiceResponse.failure(MessageKey.CART_NOT_FOUND);
        }
        
        CartDetail cartDetail = cartDAO.getItemFromCart(cart.getCartID(), productID);
        if(cartDetail == null) {
            return ServiceResponse.failure(MessageKey.CART_DETAIL_NOT_FOUND);
        }
        
        // delete item from cart
        if(cartDAO.deleteItemFromCart(cart.getCartID(), productID) == 0){
           return ServiceResponse.failure(MessageKey.CART_DETAIL_NOT_FOUND); 
        }
        
        return ServiceResponse.success(MessageKey.DELETE_ITEMS_FROM_CART_SUCCESS);
    }
}
