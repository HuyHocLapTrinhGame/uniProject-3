/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CartViewModel {
    private int cartID;
    private String userID;
    private String userFullName;
    private Date createdDate;
    private List<CartDetailViewModel> carts;

    public CartViewModel() {
    }

    public CartViewModel(int cartID, String userID, String userFullName, Date createdDate, List<CartDetailViewModel> carts) {
        this.cartID = cartID;
        this.userID = userID;
        this.userFullName = userFullName;
        this.createdDate = createdDate;
        this.carts = carts;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<CartDetailViewModel> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDetailViewModel> carts) {
        this.carts = carts;
    }
}
