/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author TruongMinhDan CE181520
 */
public class ServiceBooking {

    private int serviceBookingId;
    private String serviceId;
    private String userId;
    private String petId;
    private String status;
    private Date serviceBookingDate;
    private Time serviceBookingTime;
    private Date createAt;
    private String note;
    private int orderDetailId;
    private BigDecimal servicePrice;
    private boolean rated;

    // Additional display-only fields (joined from other tables)
    private String serviceName;
//    private double servicePrice;
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    private String petName;
    private String petSpecies;
    private String petGender;
    private int petAge;

    public ServiceBooking() {
    }

    public ServiceBooking(int serviceBookingId, String serviceId, String userId, String petId, String status, Date serviceBookingDate, Time serviceBookingTime, Date createAt, String note) {
        this.serviceBookingId = serviceBookingId;
        this.serviceId = serviceId;
        this.userId = userId;
        this.petId = petId;
        this.status = status;
        this.serviceBookingDate = serviceBookingDate;
        this.serviceBookingTime = serviceBookingTime;
        this.createAt = createAt;
        this.note = note;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
    
    
    
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    // --- Auto-generated getters and setters ---
    public void setServicePrice(BigDecimal servicePrice) {    
        this.servicePrice = servicePrice;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

//    public double getServicePrice() {
//        return servicePrice;
//    }
//
//    public void setServicePrice(double servicePrice) {
//        this.servicePrice = servicePrice;
//    }

    public int getServiceBookingId() {
        return serviceBookingId;
    }

    public void setServiceBookingId(int serviceBookingId) {
        this.serviceBookingId = serviceBookingId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getServiceBookingDate() {
        return serviceBookingDate;
    }

    public void setServiceBookingDate(Date serviceBookingDate) {
        this.serviceBookingDate = serviceBookingDate;
    }

    public Time getServiceBookingTime() {
        return serviceBookingTime;
    }

    public void setServiceBookingTime(Time serviceBookingTime) {
        this.serviceBookingTime = serviceBookingTime;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // --- Display fields ---
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
}
