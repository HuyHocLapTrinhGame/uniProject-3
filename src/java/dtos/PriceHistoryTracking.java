package dtos;

import java.util.Date;

public class PriceHistoryTracking {
    private int historyID;
    private int productID;
    private Date changeDate;
    private double oldPrice;
    private double newPrice;
    private String note; // ✅ Thêm thuộc tính ghi chú

    public PriceHistoryTracking() {
    }

    public PriceHistoryTracking(int historyID, int productID, Date changeDate, double oldPrice, double newPrice, String note) {
        this.historyID = historyID;
        this.productID = productID;
        this.changeDate = changeDate;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.note = note;
    }

    // Getter & Setter

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
