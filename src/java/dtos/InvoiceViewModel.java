/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class InvoiceViewModel {
    int invoiceID;
    String userID;
    String userName;
    float totalAmount;
    String status;
    LocalDate createdDate;
    List<InvoiceDetailViewModel> invoiceDetailList;

    public InvoiceViewModel(int invoiceID, String userID, String userName, float totalAmount, String status, LocalDate createdDate, List<InvoiceDetailViewModel> invoiceDetailList) {
        this.invoiceID = invoiceID;
        this.userID = userID;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdDate = createdDate;
        this.invoiceDetailList = invoiceDetailList;
    }

    public InvoiceViewModel() {
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createDate) {
        this.createdDate = createDate;
    }

    public List<InvoiceDetailViewModel> getInvoiceDetailList() {
        return invoiceDetailList;
    }

    public void setInvoiceDetailList(List<InvoiceDetailViewModel> invoiceDetailList) {
        this.invoiceDetailList = invoiceDetailList;
    }
    
}
