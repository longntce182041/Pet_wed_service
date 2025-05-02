/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect; // Đảm bảo package này tồn tại và chứa lớp DBConnect của bạn
import Model.Revenue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class RevenueDAO {

    // Phương thức để lấy danh sách doanh thu theo năm và tháng từ bảng Orders
    public List<Revenue> getMonthlyRevenueReport() {
        List<Revenue> revenues = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }

            String sql = "SELECT " +
                         "YEAR(order_date) AS order_year, " +
                         "MONTH(order_date) AS order_month, " +
                         "SUM(total_price) AS total_revenue " +
                         "FROM Orders " +
                         "WHERE status = 'Complete' " + // ĐÃ SỬA THÀNH 'Complete'
                         "GROUP BY YEAR(order_date), MONTH(order_date) " +
                         "ORDER BY YEAR(order_date), MONTH(order_date)";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Revenue revenue = new Revenue();
                revenue.setOrderYear(rs.getInt("order_year"));
                revenue.setOrderMonth(rs.getInt("order_month"));
                revenue.setTotalRevenue(rs.getDouble("total_revenue"));
                revenues.add(revenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi kết nối hoặc truy vấn tại đây (ví dụ: ghi log)
        } finally {
            // Đóng các tài nguyên JDBC
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close(); // Đóng connection ở đây
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return revenues;
    }

    // Ví dụ: Lấy tổng doanh thu theo năm
    public List<Revenue> getYearlyRevenueReport() {
        List<Revenue> yearlyRevenues = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }

            String sql = "SELECT " +
                         "YEAR(order_date) AS order_year, " +
                         "SUM(total_price) AS total_revenue " +
                         "FROM Orders " +
                         "WHERE status = 'Complete' " + // ĐÃ SỬA THÀNH 'Complete'
                         "GROUP BY YEAR(order_date) " +
                         "ORDER BY YEAR(order_date)";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Revenue revenue = new Revenue();
                revenue.setOrderYear(rs.getInt("order_year"));
                revenue.setTotalRevenue(rs.getDouble("total_revenue"));
                yearlyRevenues.add(revenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close(); // Đóng connection ở đây
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return yearlyRevenues;
    }
}