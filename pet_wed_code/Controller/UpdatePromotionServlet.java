/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import ConnectDB.DBConnect;
import Model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Admin
 */
@WebServlet("/UpdatePromotionServlet")
public class UpdatePromotionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String promotionId = request.getParameter("id");
    System.out.println("Received Promotion ID: " + promotionId); // Log kiểm tra

    Connection conn = null;
    try {
        DBConnect dbConnect = new DBConnect();
        conn = DBConnect.getConnection();
        String sql = "SELECT * FROM Promotion WHERE promotion_id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, promotionId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Promotion promo = new Promotion(
                rs.getString("promotion_id"),
                rs.getString("promotion_name"),
                rs.getString("promotion_description"),
                rs.getString("discount_type"),
                rs.getDouble("discount_value"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getDouble("min_order_value"),
                rs.getObject("max_discount") != null ? rs.getDouble("max_discount") : null,
                rs.getBoolean("is_active")
            );
            request.setAttribute("promotion", promo);
        } else {
            System.out.println("No promotion found with ID: " + promotionId);
        }

        conn.close();
    } catch (SQLException e) {
    }

    request.getRequestDispatcher("updatePromotion.jsp").forward(request, response);
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Connection conn = null;
    try {
        DBConnect dbConnect = new DBConnect();
        conn = DBConnect.getConnection();

        String promotionId = request.getParameter("id");
        if (promotionId == null || promotionId.isEmpty()) {
            throw new IllegalArgumentException("Promotion ID is required.");
        }

        String sql = "UPDATE Promotion SET promotion_name=?, promotion_description=?, discount_type=?, discount_value=?, start_date=?, end_date=?, min_order_value=?, max_discount=?, is_active=? WHERE promotion_id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, request.getParameter("promotion_name"));
        stmt.setString(2, request.getParameter("promotion_description"));
        stmt.setString(3, request.getParameter("discount_type"));

        String discountValueStr = request.getParameter("discount_value");
        stmt.setDouble(4, discountValueStr == null || discountValueStr.isEmpty() ? 0 : Double.parseDouble(discountValueStr));

        stmt.setDate(5, Date.valueOf(request.getParameter("start_date")));
        stmt.setDate(6, Date.valueOf(request.getParameter("end_date")));
        stmt.setDouble(7, Double.parseDouble(request.getParameter("min_order_value")));

        String maxDiscountStr = request.getParameter("max_discount");
        stmt.setObject(8, (maxDiscountStr == null || maxDiscountStr.isEmpty()) ? null : Double.parseDouble(maxDiscountStr));
        stmt.setBoolean(9, request.getParameter("isActive") != null);
        stmt.setString(10, promotionId);

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated == 0) {
            throw new Exception("No promotion found with ID: " + promotionId);
        }

        conn.close();
    } catch (Exception e) {
        throw new ServletException("Error updating promotion: " + e.getMessage(), e);
    }

    response.sendRedirect("PromotionServlet");
}
}
