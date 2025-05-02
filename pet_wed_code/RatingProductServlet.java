/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import DAO.RatingProductDAO;
import DAO.OrderDAO;
import Model.Rating;
import Model.OrderDetail;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Tran Phan Trung Kien - CE180170
 */
@WebServlet("/RatingProductServlet")
public class RatingProductServlet extends HttpServlet {

    private RatingProductDAO ratingDAO = new RatingProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String customerId = request.getParameter("customerId");
        String orderDetailIdStr = request.getParameter("orderDetailId");
        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        // Kiểm tra đầu vào
        if (customerId == null || customerId.trim().isEmpty() ||
            orderDetailIdStr == null || orderDetailIdStr.trim().isEmpty() ||
            ratingStr == null || ratingStr.trim().isEmpty() ||
            comment == null || comment.trim().isEmpty()) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid input\"}");
            return;
        }

        try {
            int orderDetailId = Integer.parseInt(orderDetailIdStr);
            int ratingStar = Integer.parseInt(ratingStr);

            // Kiểm tra giá trị rating hợp lệ
            if (ratingStar < 1 || ratingStar > 5) {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Rating must be between 1 and 5\"}");
                return;
            }

            // Kiểm tra trạng thái đơn hàng
            OrderDetail detail = orderDAO.getOrderDetailById(orderDetailId);
            if (detail == null || !"Complete".equalsIgnoreCase(detail.getOrderStatus())) {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Order not confirmed\"}");
                return;
            }

            // Kiểm tra nếu người dùng đã đánh giá sản phẩm trong đơn hàng này
            if (ratingDAO.hasRated(orderDetailId, customerId)) {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Already rated\"}");
                return;
            }

            // Lưu đánh giá
            Rating rating = new Rating();
            rating.setCustomerId(customerId);
            rating.setOrderDetailId(orderDetailId);
            rating.setRatingStar(ratingStar);
            rating.setComment(comment);
            ratingDAO.saveRating(rating);

            response.getWriter().write("{\"status\":\"success\", \"message\":\"Rating submitted successfully\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid input\"}");
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi chi tiết
            response.getWriter().write("{\"status\":\"error\", \"message\":\"An unexpected error occurred\"}");
        }
    }
}