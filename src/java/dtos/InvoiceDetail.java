/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author ADMIN
 */
public class InvoiceDetail {
    int invoiceID;
    int productID;
    int quanlity;
    float price;

    public InvoiceDetail(int invoiceID, int productID, int quanlity, float price) {
        this.invoiceID = invoiceID;
        this.productID = productID;
        this.quanlity = quanlity;
        this.price = price;
    }

    public InvoiceDetail() {
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(int quanlity) {
        this.quanlity = quanlity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
}
