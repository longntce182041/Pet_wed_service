/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class AppointmentSchedule {
    private String customerFullName;
    private String petName;
    private String servicebookingDate;
    private String servicebookingTime;
    private String status;
    private int servicebookingId;

    public AppointmentSchedule(int servicebookingId, String customerFullName, String petName, String servicebookingDate, String servicebookingTime, String status) {
        this.servicebookingId = servicebookingId;
        this.customerFullName = customerFullName;
        this.petName = petName;
        this.servicebookingDate = servicebookingDate;
        this.servicebookingTime = servicebookingTime;
        this.status = status;
    }

    public int getServicebookingId() {
        return servicebookingId;
    }

    public void setServicebookingId(int servicebookingId) {
        this.servicebookingId = servicebookingId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getServicebookingDate() {
        return servicebookingDate;
    }

    public void setServicebookingDate(String servicebookingDate) {
        this.servicebookingDate = servicebookingDate;
    }

    public String getServicebookingTime() {
        return servicebookingTime;
    }

    public void setServicebookingTime(String servicebookingTime) {
        this.servicebookingTime = servicebookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}