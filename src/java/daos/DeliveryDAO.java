/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import dtos.Delivery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

/**
 *
 * @author ngogi
 */
public class DeliveryDAO {

    private final String GET_ALL_DELIVERY = "SELECT * FROM tblDeliveries";
    
    private final String GET_DELIVERY_BY_ID = "SELECT * FROM tblDeliveries WHERE deliveryID LIKE ?";
    private final String GET_DELIVERY_BY_STATUS = "SELECT * FROM tblDeliveries WHERE status LIKE ?";
    private final String GET_DELIVERY_BY_INVOICEID = "SELECT * FROM tblDeliveries WHERE invoiceID LIKE ?";
    private final String INSERT_DELIVERY = "INSERT INTO tblDeliveries"
            + "(invoiceID, address, deliveryDate, status)"
            + " VALUES (?, ?, ?, ?)";
    private final String UPDATE_DELIVERY = "UPDATE tblDeliveries SET  status = ?  WHERE deliveryID = ?";
    private final String DELETE_DELIVERY = "DELETE  from tblDeliveries where invoiceID = ?";
    
    public List<Delivery> getAllDelivery() throws SQLException {
        List<Delivery> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();
                PreparedStatement stm = conn.prepareStatement(GET_ALL_DELIVERY);
                ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                resultList.add(mapRow(rs));
            }
        }
        return resultList;
    }
    
    

    public Delivery getDeliveryByID(int deliveryID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(GET_DELIVERY_BY_ID)) {
            stm.setInt(1, deliveryID);
            try ( ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Delivery> getDeliveryStatus(String status) throws SQLException {
        return searchByKeyWord(GET_DELIVERY_BY_STATUS, status);
    }

    public Delivery getDeliveryByInvoiceID(int invoiceID) throws SQLException{
        try( Connection conn = DBContext.getConnection();
                PreparedStatement stm = conn.prepareStatement(GET_DELIVERY_BY_INVOICEID)) {
            stm.setInt(1, invoiceID);
            try( ResultSet rs = stm.executeQuery()) {
                if(rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return  null;
    }
    
    
    private List<Delivery> searchByKeyWord(String sql, String keyword) throws SQLException {
        List<Delivery> resultList = new ArrayList<>();
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1,keyword);
            try ( ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultList.add(mapRow(rs));
                }
            }
        }
        return resultList;
    }

    public int insertDelivery(int invoiceID,
            String address, LocalDate deliveryDate, String status) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(INSERT_DELIVERY)) {
            stm.setInt(1, invoiceID);
            stm.setString(2, address);

            if (deliveryDate != null) {
                stm.setDate(3, java.sql.Date.valueOf(deliveryDate));
            } else {
                stm.setNull(3, Types.DATE);
            }

            stm.setString(4, status);
            return stm.executeUpdate();
        }
    }

    public int updateDelivery(int deliveryID, String status) throws SQLException {  
        try ( Connection conn = DBContext.getConnection(); 
                PreparedStatement stm = conn.prepareStatement(UPDATE_DELIVERY)) {
            stm.setString(1, status);
            stm.setInt(2, deliveryID);
             
            return stm.executeUpdate();
        }
        
    }
    
     public int deleteDelivery(int invoiceID) throws SQLException{
         try ( Connection conn = DBContext.getConnection(); 
                PreparedStatement stm = conn.prepareStatement(DELETE_DELIVERY)) {
            
            stm.setInt(1, invoiceID);
            
            return stm.executeUpdate();
        }
     }

    public boolean checkDelivertyExists(Integer deliveryID) throws SQLException {
        try ( Connection conn = DBContext.getConnection();  PreparedStatement stm = conn.prepareStatement(GET_DELIVERY_BY_ID)) {
            stm.setInt(1, deliveryID);
            return stm.executeQuery().next();
        }
    }

    private Delivery mapRow(ResultSet rs) throws SQLException {
        LocalDate deliveryDate = rs.getDate("deliveryDate") != null
                ? rs.getDate("deliveryDate").toLocalDate() : null;

        return new Delivery(
                rs.getInt("deliveryID"),
                rs.getInt("invoiceID"),
                rs.getString("address"),
                deliveryDate,
                rs.getString("status")
        );
    }
}
