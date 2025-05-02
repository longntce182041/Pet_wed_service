/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Nguyen Thanh Long - CE182041
 */
public class Customer {
    private String customer_id;
    private String user_id;
    private String customer_full_name;
    private String customer_address;
    private String customer_email;
    private String customer_phone;

    public Customer() {
    }

    public Customer(String customer_id, String user_id, String customer_full_name, String customer_address, String customer_email, String customer_phone) {
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.customer_full_name = customer_full_name;
        this.customer_address = customer_address;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_full_name() {
        return customer_full_name;
    }

    public void setCustomer_full_name(String customer_full_name) {
        this.customer_full_name = customer_full_name;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    
    
}
