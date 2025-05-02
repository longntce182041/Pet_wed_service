/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import ConnectDB.DBConnect;
import Model.Rating;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@WebServlet("/StatisticsServlet")
public class StatisticsServlet extends HttpServlet {
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Rating> ratings = new ArrayList<>();
    Connection conn = null;
    try {
        conn = DBConnect.getConnection();
        String sql = "SELECT r.rating_id, c.customer_fullname, " +
                     "p.product_name, od.service_booking_id, " +
                     "r.rating_star, r.comment " +
                     "FROM Ratings r " +
                     "LEFT JOIN Order_Details od ON r.order_detail_id = od.order_detail_id " +
                     "LEFT JOIN Products p ON od.product_id = p.product_id " +
                     "LEFT JOIN Orders o ON od.order_id = o.order_id " +
                     "LEFT JOIN Customer c ON o.customer_id = c.customer_id";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int ratingId = rs.getInt("rating_id");
            String customerFullName = rs.getString("customer_fullname");
            String productName = rs.getString("product_name");
            int serviceBookingId = rs.getInt("service_booking_id");
            int ratingStar = rs.getInt("rating_star");
            String comment = rs.getString("comment");

            ratings.add(new Rating(ratingId, customerFullName, productName, serviceBookingId, ratingStar, comment));
        }
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    request.setAttribute("ratings", ratings);
    RequestDispatcher dispatcher = request.getRequestDispatcher("statisticsReports.jsp");
    dispatcher.forward(request, response);
}
}
