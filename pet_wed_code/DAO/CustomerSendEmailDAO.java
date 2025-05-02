package DAO;

import ConnectDB.DBConnect;
import Model.CustomerSendEmail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerSendEmailDAO {

    public static List<CustomerSendEmail> getAllCustomers() throws Exception {
        List<CustomerSendEmail> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CustomerSendEmail customer = new CustomerSendEmail();
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setCustomerFullName(rs.getString("customer_fullname"));
                customer.setCustomerPhone(rs.getString("customer_phone"));
                customer.setCustomerEmail(rs.getString("customer_email"));
                customer.setCustomerAddress(rs.getString("customer_address"));
                customers.add(customer);
            }
        }

        return customers;
    }

    public static CustomerSendEmail getCustomerById(String customerId) throws Exception {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CustomerSendEmail customer = new CustomerSendEmail();
                    customer.setCustomerId(rs.getString("customer_id"));
                    customer.setCustomerFullName(rs.getString("customer_fullname"));
                    customer.setCustomerPhone(rs.getString("customer_phone"));
                    customer.setCustomerEmail(rs.getString("customer_email"));
                    customer.setCustomerAddress(rs.getString("customer_address"));
                    return customer;
                }
            }
        }

        return null;
    }
}
