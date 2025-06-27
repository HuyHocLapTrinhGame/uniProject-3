/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import dtos.ProductViewModel;
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
public class ProductDAO {

    private final String CHECK_PRODUCT_BY_ID = "SELECT * FROM tblProducts WHERE productID = ?";
    private final String CHECK_PRODUCT_STATUS = "SELECT * FROM tblProducts "
            + "WHERE productID = ? AND status = ?";

    private final String GET_ALL_PRODUCTS = "SELECT p.*, c.categoryName, u.fullName AS sellerFullName, " 
            + "ROUND(p.price * (1 - ISNULL(pr.discountPercent, 0)), 2) AS salePrice " 
            + "FROM tblProducts p " 
            + "LEFT JOIN "
            + "(SELECT * FROM tblPromotions WHERE status = 'active') AS pr "
            + "ON p.promoID = pr.promoID " 
            + "JOIN tblCategories c ON p.categoryID = c.categoryID " 
            + "JOIN tblUsers u ON p.sellerID = u.userID ";
    
    private final String GET_PRODUCT_BY_ID
            = GET_ALL_PRODUCTS + "WHERE p.productID = ?";

    private final String GET_PRODUCTS_BY_NAME
            = GET_ALL_PRODUCTS + "WHERE p.name LIKE ?";

    private final String GET_PRODUCTS_BY_CATEGORY_NAME
            = GET_ALL_PRODUCTS + "WHERE c.categoryName LIKE ?";

    private final String GET_PRODUCTS_BY_PROMO_ID
            = GET_ALL_PRODUCTS + "WHERE pr.promoID = ?";
    
    private final String GET_PRODUCTS_BY_MIN_QUANTITY
            = GET_ALL_PRODUCTS + "WHERE p.quantity >= ?";

    private final String GET_PRODUCTS_BY_SELLER_ID
            = GET_ALL_PRODUCTS + "WHERE p.sellerID = ?";

    private final String GET_PRODUCTS_BY_STATUS
            = GET_ALL_PRODUCTS + "WHERE p.status = ?";

    private final String INSERT_PRODUCT
            = "INSERT INTO tblProducts (name, categoryID, price, quantity, sellerID, status, promoID) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String UPDATE_PRODUCT_BY_ID
            = "UPDATE tblProducts SET name = ?, categoryID = ?, price = ?, quantity = ?, status = ?, promoID = ? "
            + "WHERE productID = ?";

    private final String UPDATE_PRODUCT_STATUS_BY_ID
            = "UPDATE tblProducts SET status = ? WHERE productID = ?";

    private final String UPDATE_PRODUCT_QUANTITY_AND_STATUS_BY_ID
            = "UPDATE tblProducts SET quantity = ?, status = ? "
            + "WHERE productID = ?";

    private final String DELETE_PRODUCT_BY_ID = "DELETE FROM tblProducts WHERE productID = ?";

    private final String CREATE_HISTORY = 
        "INSERT INTO [dbo].[tblPriceHistory] " +
        "([productID], [changeDate], [oldPrice], [newPrice], [note]) " +
        "VALUES (?, ?, ?, ?, ?)";

    
    //
    public boolean checkProductExists(int productID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(CHECK_PRODUCT_BY_ID)) {
            stm.setInt(1, productID);
            return stm.executeQuery().next();
        }
    }

    public boolean checkProductStatus(int productID, String status) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(CHECK_PRODUCT_STATUS)) {
            stm.setInt(1, productID);
            stm.setString(2, status);
            return stm.executeQuery().next();
        }
    }

    public List<ProductViewModel> getAllProducts() throws SQLException {
        List<ProductViewModel> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(GET_ALL_PRODUCTS);  ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                resultList.add(mapRow(rs));
            }
        }
        return resultList;
    }

    public ProductViewModel getProductByID(int productID) throws SQLException {
        List<ProductViewModel> products = getProductsByInteger(GET_PRODUCT_BY_ID, productID);
        return products.isEmpty() ? null : products.get(0);
    }

    public List<ProductViewModel> getProductsByName(String name) throws SQLException {
        return getProductsByString(GET_PRODUCTS_BY_NAME, "%" + name + "%");
    }

    public List<ProductViewModel> getProductsByCategoryName(String cName) throws SQLException {
        return getProductsByString(GET_PRODUCTS_BY_CATEGORY_NAME, "%" + cName + "%");
    }

    public List<ProductViewModel> getProductsByPromoID(int promoID) throws SQLException {
        return getProductsByInteger(GET_PRODUCTS_BY_PROMO_ID, promoID);
    }
    
    public List<ProductViewModel> getProductsByMinQuantity(int quantity) throws SQLException {
        return getProductsByInteger(GET_PRODUCTS_BY_MIN_QUANTITY, quantity);
    }

    public List<ProductViewModel> getProductsBySellerID(String sellerID) throws SQLException {
        return getProductsByString(GET_PRODUCTS_BY_SELLER_ID, sellerID);
    }

    public List<ProductViewModel> getProductsByStatus(String status) throws SQLException {
        return getProductsByString(GET_PRODUCTS_BY_STATUS, status);
    }

    public int insertProduct(String name, int categoryID, double price,
            int quantity, String sellerID, String status, Integer promoID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  
                PreparedStatement stm = conn.prepareStatement(INSERT_PRODUCT)) {
            stm.setString(1, name);
            stm.setInt(2, categoryID);
            stm.setDouble(3, price);
            stm.setInt(4, quantity);
            stm.setString(5, sellerID);
            stm.setString(6, status);
            if (promoID != null) {
                stm.setInt(7, promoID);
            } else {
                stm.setNull(7, java.sql.Types.INTEGER);
            }
            return stm.executeUpdate();
        }
    }

    public int updateProduct(int productID, String name, int categoryID, double price,
            int quantity, String status, Integer promoID) throws SQLException {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stm = conn.prepareStatement(UPDATE_PRODUCT_BY_ID)) {
            stm.setString(1, name);
            stm.setInt(2, categoryID);
            stm.setDouble(3, price);
            stm.setInt(4, quantity);
            stm.setString(5, status);

            if (promoID != null) {
                stm.setInt(6, promoID);
            } else {
                stm.setNull(6, java.sql.Types.INTEGER);
            }

            stm.setInt(7, productID);

            return stm.executeUpdate();
        }
    }


    public int updateProductStatus(int productID, String status) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(UPDATE_PRODUCT_STATUS_BY_ID)) {
            stm.setString(1, status);
            stm.setInt(2, productID);
            return stm.executeUpdate();
        }
    }

    public int updateProductQuantityAndStatus(int productID, int quantity, String status) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(UPDATE_PRODUCT_QUANTITY_AND_STATUS_BY_ID)) {
            stm.setInt(1, quantity);
            stm.setString(2, status);
            stm.setInt(3, productID);
            return stm.executeUpdate();
        }
    }

    public int deleteProduct(int productID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(DELETE_PRODUCT_BY_ID)) {
            stm.setInt(1, productID);
            return stm.executeUpdate();
        }
    }

    // helper
    private List<ProductViewModel> getProductsByString(String sql, String key) throws SQLException {
        List<ProductViewModel> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, key);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapRow(rs));
                }
            }
        }
        return resultList;
    }

    private List<ProductViewModel> getProductsByInteger(String sql, int number) throws SQLException {
        List<ProductViewModel> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, number);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapRow(rs));
                }
            }
        }
        return resultList;
    }

    private ProductViewModel mapRow(ResultSet rs) throws SQLException {
        return new ProductViewModel(
                rs.getInt("productID"),
                rs.getString("name"),
                rs.getInt("categoryID"),
                rs.getString("categoryName"),
                rs.getDouble("price"),
                rs.getDouble("salePrice"),
                rs.getInt("quantity"),
                rs.getString("sellerID"),
                rs.getString("sellerFullName"),
                rs.getString("status"),
                rs.getInt("promoID")
        );
    }
    
    public int createHistory(int productID, Date changeDate, double oldPrice, double newPrice, String note) throws SQLException {
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(CREATE_HISTORY)) {
        ps.setInt(1, productID);
        ps.setTimestamp(2, new java.sql.Timestamp(changeDate.getTime())); 
        ps.setDouble(3, oldPrice);
        ps.setDouble(4, newPrice);
        ps.setString(5, note);

        return ps.executeUpdate();
    }
}
}
