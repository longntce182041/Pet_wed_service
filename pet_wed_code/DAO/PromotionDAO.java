/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect;
import Model.Promotion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {

    public static List<Promotion> getAllPromotions() throws Exception {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM Promotion";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Promotion promo = new Promotion();
                promo.setPromotionId(rs.getString("promotion_id"));
                promo.setPromotionName(rs.getString("promotion_name"));
                promo.setPromotionDescription(rs.getString("promotion_description"));
                promo.setDiscountType(rs.getString("discount_type"));
                promo.setDiscountValue(rs.getDouble("discount_value"));
                promo.setStartDate(rs.getDate("start_date"));
                promo.setEndDate(rs.getDate("end_date"));
                promo.setMinOrderValue(rs.getDouble("min_order_value"));
                promo.setMaxDiscount(rs.getDouble("max_discount"));
                promo.setIsActive(rs.getBoolean("is_active"));
                promotions.add(promo);
            }
        }

        return promotions;
    }

    public static Promotion getPromotionById(String promotionId) throws Exception {
        String sql = "SELECT * FROM Promotion WHERE promotion_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, promotionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Promotion promo = new Promotion();
                    promo.setPromotionId(rs.getString("promotion_id"));
                    promo.setPromotionName(rs.getString("promotion_name"));
                    promo.setPromotionDescription(rs.getString("promotion_description"));
                    promo.setDiscountType(rs.getString("discount_type"));
                    promo.setDiscountValue(rs.getDouble("discount_value"));
                    promo.setStartDate(rs.getDate("start_date"));
                    promo.setEndDate(rs.getDate("end_date"));
                    promo.setMinOrderValue(rs.getDouble("min_order_value"));
                    promo.setMaxDiscount(rs.getDouble("max_discount"));
                    promo.setIsActive(rs.getBoolean("is_active"));
                    return promo;
                }
            }
        }

        return null;
    }
}