 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import dtos.CartViewModel;
import dtos.CartDetailViewModel;
import dtos.CartDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.DBContext;

/**
 *
 * @author Admin
 */
public class CartDAO {

    private final String GET_CART_BY_ID = 
            "SELECT c.*, u.fullName AS userFullName "
            + "FROM tblCarts c "
            + "JOIN tblUsers u ON c.userID = u.userID "
            + "WHERE c.cartID = ?";
    private final String GET_CART_BY_USER_ID = 
            "SELECT c.*, u.fullName AS userFullName "
            + "FROM tblCarts c "
            + "JOIN tblUsers u ON c.userID = u.userID "
            + "WHERE c.userID = ?";
    
    private final String INSERT_CART = "INSERT INTO tblCarts (userID, createdDate) VALUES (?, ?)";
    private final String DELETE_CART = "DELETE FROM tblCarts WHERE cartID = ?";

    private final String GET_PRODUCT_IDS_BY_CART_ID = 
            "SELECT productID FROM tblCartDetails WHERE cartID = ?";
    
    private final String GET_CART_DETAIL = 
            "SELECT * FROM tblCartDetails WHERE cartID = ? AND productID = ?";
    
    private final String GET_CART_DETAILS = 
            "SELECT cd.productID, cd.quantity, p.name AS productName, p.price, " 
            + "ROUND(p.price * (1 - ISNULL(pr.discountPercent, 0)), 2) AS salePrice, " 
            + "ROUND(cd.quantity * (p.price * (1 - ISNULL(pr.discountPercent, 0))), 2) AS subTotal " 
            + "FROM tblCartDetails cd " 
            + "JOIN tblProducts p ON cd.productID = p.productID " 
            + "LEFT JOIN tblPromotions pr ON p.promoID = pr.promoID " 
            + "WHERE cd.cartID = ?";
    
    private final String INSERT_ITEM_TO_CART = 
            "INSERT INTO tblCartDetails(cartID, productID, quantity) VALUES (?, ?, ?)";
    private final String UPDATE_ITEM_QUANTITY = 
            "UPDATE tblCartDetails SET quantity = ? WHERE cartID = ? AND productID = ?";
    private final String DELETE_ITEM_FROM_CART = 
            "DELETE FROM tblCartDetails WHERE cartID = ? AND productID = ?";
    private final String CLEAR_CART = "DELETE FROM tblCartDetails WHERE cartID = ?";
    
    //
    public CartViewModel getCartByID(int cartID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement psCart = con.prepareStatement(GET_CART_BY_ID);
             PreparedStatement psDetails = con.prepareStatement(GET_CART_DETAILS)) {

            psCart.setInt(1, cartID);
            CartViewModel cart = getCartFromPreparedStatement(psCart);

            if (cart != null) {
                psDetails.setInt(1, cartID);
                cart.setCarts(getCartDetailsFromPreparedStatement(psDetails));
            }

            return cart;
        }
    }

    public CartViewModel getCartByUserID(String userID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement psCart = con.prepareStatement(GET_CART_BY_USER_ID);
             PreparedStatement psDetails = con.prepareStatement(GET_CART_DETAILS)) {

            psCart.setString(1, userID);
            CartViewModel cart = getCartFromPreparedStatement(psCart);

            if (cart != null) {
                psDetails.setInt(1, cart.getCartID());
                cart.setCarts(getCartDetailsFromPreparedStatement(psDetails));
            }

            return cart;
        }
    }

    public int insertCart(String userID, Date createdDate) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_CART)) {

            ps.setString(1, userID);
            ps.setDate(2, new java.sql.Date(createdDate.getTime()));
            return ps.executeUpdate();
        }
    }
    
    public int deleteCartByID(int cartID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_CART)) {

            ps.setInt(1, cartID);
            return ps.executeUpdate();
        }
    }

    //
    public List<Integer> getProductIDsByCartID(int cartID) throws SQLException {
        List<Integer> productIDs = new ArrayList<>();
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_PRODUCT_IDS_BY_CART_ID)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    productIDs.add(rs.getInt("productID"));
                }
            }
        }
        return productIDs;
    }
    
    public CartDetail getItemFromCart(int cartID, int productID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_CART_DETAIL)) {
            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapCartDetail(rs);
                }
            }
        }
        return null;
    }
    
    public int insertItemToCart(int cartID, int productID, int quantity) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ITEM_TO_CART)) {

            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            ps.setInt(3, quantity);
            return ps.executeUpdate();
        }
    }

    public int updateItemQuantity(int cartID, int productID, int quantity) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ITEM_QUANTITY)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartID);
            ps.setInt(3, productID);
            return ps.executeUpdate();
        }
    }

    public int deleteItemFromCart(int cartID, int productID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ITEM_FROM_CART)) {

            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            return ps.executeUpdate();
        }
    }

    public int clearCart(int cartID) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(CLEAR_CART)) {

            ps.setInt(1, cartID);
            return ps.executeUpdate();
        }
    }
    
    //helper
    private CartViewModel getCartFromPreparedStatement(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapCartViewModel(rs);
            }
        }
        return null;
    }

    private List<CartDetailViewModel> getCartDetailsFromPreparedStatement(PreparedStatement ps) throws SQLException {
        List<CartDetailViewModel> details = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                details.add(mapCartDetailViewModel(rs));
            }
        }
        return details;
    }
    
    private CartViewModel mapCartViewModel(ResultSet rs) throws SQLException {
        CartViewModel cart = new CartViewModel();
        cart.setCartID(rs.getInt("cartID"));
        cart.setUserID(rs.getString("userID"));
        cart.setUserFullName(rs.getString("userFullName"));
        cart.setCreatedDate(rs.getDate("createdDate"));
        return cart;
    }

    private CartDetail mapCartDetail(ResultSet rs) throws SQLException {
        CartDetail detail = new CartDetail();
        detail.setProductID(rs.getInt("productID"));
        detail.setQuantity(rs.getInt("quantity"));
        return detail;
    }
    
    private CartDetailViewModel mapCartDetailViewModel(ResultSet rs) throws SQLException {
        CartDetailViewModel detail = new CartDetailViewModel();
        detail.setProductID(rs.getInt("productID"));
        detail.setProductName(rs.getString("productName"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setBasePrice(rs.getDouble("price"));
        detail.setSalePrice(rs.getDouble("salePrice"));
        detail.setSubTotal(rs.getDouble("subTotal"));
        return detail;
    }
}
