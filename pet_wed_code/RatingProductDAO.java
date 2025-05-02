/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect;
import Model.Rating;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingProductDAO {

    public void saveRating(Rating rating) {
        String checkCustomerSql = "SELECT COUNT(*) FROM Customer WHERE customer_id = ?";
        String insertRatingSql = "INSERT INTO Ratings (customer_id, rating_star, comment, order_detail_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkCustomerSql);
             PreparedStatement insertPs = conn.prepareStatement(insertRatingSql)) {

            // Kiểm tra xem customer_id có tồn tại không
            checkPs.setString(1, rating.getCustomerId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Customer ID does not exist.");
            }

            // Lưu đánh giá
            insertPs.setString(1, rating.getCustomerId());
            insertPs.setInt(2, rating.getRatingStar());
            insertPs.setString(3, rating.getComment());
            insertPs.setInt(4, rating.getOrderDetailId());
            insertPs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Rating> getRatingsByProductId(int productId) {
        List<Rating> ratings = new ArrayList<>();
        String sql = "SELECT r.rating_star, r.comment, c.customer_fullname " +
                     "FROM Ratings r " +
                     "JOIN Order_Details od ON r.order_detail_id = od.order_detail_id " +
                     "JOIN Customer c ON r.customer_id = c.customer_id " +
                     "WHERE od.product_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Rating rating = new Rating();
                rating.setRatingStar(rs.getInt("rating_star"));
                rating.setComment(rs.getString("comment"));
                rating.setCustomerName(rs.getString("customer_fullname")); // Lấy tên khách hàng
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ratings;
    }

    public boolean hasRated(int orderDetailId, String customerId) {
        String sql = "SELECT COUNT(*) FROM Ratings WHERE order_detail_id = ? AND customer_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderDetailId);
            ps.setString(2, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Trả về true nếu đã đánh giá
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}