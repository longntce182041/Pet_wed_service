package Controller;

import ConnectDB.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

@WebServlet("/AddPromotionServlet")
public class AddPromotionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "INSERT INTO Promotion (promotion_id, promotion_name, promotion_description, discount_type, discount_value, start_date, end_date, min_order_value, max_discount, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                 
                stmt.setString(1, request.getParameter("promotion_id"));
                stmt.setString(2, request.getParameter("promotion_name"));
                stmt.setString(3, request.getParameter("promotion_description"));
                stmt.setString(4, request.getParameter("discount_type"));

                String discountValue = request.getParameter("discount_value");
                stmt.setDouble(5, (discountValue != null && !discountValue.isEmpty()) ? Double.parseDouble(discountValue) : 0.0);

                String startDate = request.getParameter("start_date");
                String endDate = request.getParameter("end_date");
                if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
                    throw new ServletException("Start date and end date are required.");
                }
                stmt.setDate(6, Date.valueOf(startDate));
                stmt.setDate(7, Date.valueOf(endDate));

                String minOrderValue = request.getParameter("min_order_value");
                stmt.setDouble(8, (minOrderValue != null && !minOrderValue.isEmpty()) ? Double.parseDouble(minOrderValue) : 0.0);

                String maxDiscount = request.getParameter("max_discount");
                stmt.setObject(9, (maxDiscount != null && !maxDiscount.isEmpty()) ? Double.parseDouble(maxDiscount) : null);

                stmt.setBoolean(10, request.getParameter("is_active") != null);

                stmt.executeUpdate();
            }
        } catch (Exception e) {
        }
        response.sendRedirect("PromotionServlet");
    }
}
