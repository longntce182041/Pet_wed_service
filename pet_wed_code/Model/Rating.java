/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */

public class Rating {
    private int ratingId;
    private String customerFullName;
    private String productName;
    private int servicebookingId;
    private int ratingStar;
    private String comment;

    public Rating(int ratingId, String customerFullName, String productName, int servicebookingId, int ratingStar, String comment) {
        this.ratingId = ratingId;
        this.customerFullName = customerFullName;
        this.productName = productName;
        this.servicebookingId = servicebookingId;
        this.ratingStar = ratingStar;
        this.comment = comment;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getServicebookingId() {
        return servicebookingId;
    }

    public void setServicebookingId(int servicebookingId) {
        this.servicebookingId = servicebookingId;
    }

    public int getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    } 
}
