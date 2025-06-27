/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author Admin
 */
public class ProductViewModel {
    private int productID;
    private String name;
    private int categoryID;
    private String categoryName;
    private double basePrice;
    private double salePrice;
    private int quantity;
    private String sellerID;
    private String sellerFullName;
    private String status;
    private int promoID;

    public ProductViewModel() {
    }

    public ProductViewModel(int productID, String name, int categoryID, String categoryName, 
            double basePrice, double salePrice, int quantity, String sellerID, 
            String sellerFullName, String status, int promoID) {
        this.productID = productID;
        this.name = name;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.sellerID = sellerID;
        this.sellerFullName = sellerFullName;
        this.status = status;
        this.promoID = promoID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerFullName() {
        return sellerFullName;
    }

    public void setSellerFullName(String sellerFullName) {
        this.sellerFullName = sellerFullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPromoID() {
        return promoID;
    }

    public void setPromoID(int promoID) {
        this.promoID = promoID;
    }
    
    
}
