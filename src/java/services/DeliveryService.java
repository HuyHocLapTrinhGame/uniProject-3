/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import constants.MessageKey;
import daos.DeliveryDAO;
import dtos.Delivery;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ngogi
 */
public class DeliveryService {
    private DeliveryDAO  deliveryDAO = new DeliveryDAO();
    
    public String createDelivery(Integer invoiceID,
            String address, LocalDate deliveryDate, String status) throws SQLException {
        if ( invoiceID == null
            || isNullOrEmptyString(address)
            || deliveryDate == null
            || isNullOrEmptyString(status)) {
            return MessageKey.ALL_FIELDS_REQUIRED;
        }

        LocalDate newDate = deliveryDate.plusDays(3);
        
        if(deliveryDAO.insertDelivery(invoiceID, address, newDate, status) == 0){
            return MessageKey.CREATE_DELIVERY_FAILED;
        }
        return MessageKey.CREATE_DELIVERY_SUCCESS;
        
    }   
    
    public String updateDelivery(Integer deliveryID, String status) throws  SQLException {
        if(!deliveryDAO.checkDelivertyExists(deliveryID)){
            return MessageKey.DELIVERY_NOT_FOUND;
        }
        
        Delivery delivery = new Delivery();
        
        if(isNullOrEmptyString(status)){
            status = delivery.getStatus();
        }
       
        if (deliveryDAO.updateDelivery(deliveryID, status) == 0){
            return MessageKey.UPDATE_DELIVERY_FAILED;
        }
        
        return MessageKey.UPDATE_DELIVERY_SUCCESS;
    }
    
    public String deleteDelivery(int invoiceID) throws SQLException {
        if(deliveryDAO.deleteDelivery(invoiceID) == 0){
            return MessageKey.DELIVERY_NOT_FOUND;
        }
        return  MessageKey.DELETE_DELIVERY_SUCCESS;
    }
    
    public List<Delivery> getAllDelivery() throws SQLException {
        return deliveryDAO.getAllDelivery();
    }
    
    public List<Delivery> getDeliveryByStatus(String status) throws SQLException {
        return deliveryDAO.getDeliveryStatus(status);
    }
    
    public Delivery getDeliveryByInvoiceID(int invoiceID) throws SQLException {
        return deliveryDAO.getDeliveryByInvoiceID(invoiceID);
    }
    
//    public List<Delivery> getDeleveryDate(LocalDate deliveryDate) throws SQLException {
//        return deliveryDAO.getDeliveryByDate(deliveryDate);
//    }
        
    private boolean isNullOrEmptyString(String str){
        return str == null || str.isEmpty();
    }

    
}