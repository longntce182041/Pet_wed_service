/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author TruongMinhDan CE181520
 */
public class User {
    private String email;
    private String password;
    private String role;
    private String fullname;
    private String address;
    private String phone;
    private String username;
    private String userId;

    public User() {}

    public User(String email, String password, String role, String fullname, String address, String phone, String username, String userId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.userId = userId;
    }

    public User(String userId, String username, String email, String fullname, String address, String phone ,String role) {
        this.email = email;
        this.fullname = fullname;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.userId = userId;
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}