/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author Huy
 */
public class CustomerCareViewModel {
    int ticketID ;
    String userID ;
    String fullName;
    String subject ;
    String content ;
    String status ;
    String reply ;

    public CustomerCareViewModel() {
    }

    public CustomerCareViewModel(int ticketID, String userID, String fullName, String subject, String content, String status, String reply) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.fullName = fullName;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.reply = reply;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
    
}
