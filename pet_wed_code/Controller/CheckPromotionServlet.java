/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controller;

import DAO.PromotionDAO;
import Model.Promotion;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet kiểm tra mã khuyến mãi
 */
@WebServlet("/CheckPromotionServlet")
public class CheckPromotionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Lấy tham số từ client
            String promoCode = request.getParameter("promoCode");
            String orderValueParam = request.getParameter("orderValue");

            // Kiểm tra nếu promoCode hoặc orderValue không hợp lệ
            if (promoCode == null || promoCode.trim().isEmpty()) {
                out.write("{\"valid\": false, \"message\": \"Promotion code is required.\"}");
                return;
            }

            double orderValue;
            try {
                orderValue = Double.parseDouble(orderValueParam);
                if (orderValue <= 0) {
                    out.write("{\"valid\": false, \"message\": \"Order value must be greater than 0.\"}");
                    return;
                }
            } catch (NumberFormatException e) {
                out.write("{\"valid\": false, \"message\": \"Invalid order value.\"}");
                return;
            }

            // Sử dụng DAO để lấy thông tin mã khuyến mãi
            PromotionDAO promotionDAO = new PromotionDAO();
            Promotion promotion = promotionDAO.getPromotionByCode(promoCode);

            // Kiểm tra mã khuyến mãi
            if (promotion != null) {
                // Log giá trị để kiểm tra
                System.out.println("Order Value: " + orderValue);
                System.out.println("Minimum Order Value: " + promotion.getMinOrderValue());

                // Kiểm tra giá trị tối thiểu của đơn hàng
                if (promotion.getMinOrderValue() != null && orderValue < promotion.getMinOrderValue()) {
                    out.write("{\"valid\": false, \"message\": \"Order value does not meet the minimum required for this promotion. Minimum order value is " + promotion.getMinOrderValue() + ".\"}");
                    return;
                }

                // Kiểm tra nếu mã khuyến mãi đang hoạt động
                if (promotion.isActive()) {
                    // Kiểm tra các điều kiện khác
                    if (promotion.isValid(orderValue)) {
                        out.write("{\"valid\": true, \"discountType\": \"" + promotion.getDiscountType() + "\", \"discountValue\": " + promotion.getDiscountValue() + ", \"minOrderValue\": " + promotion.getMinOrderValue() + ", \"maxDiscount\": " + promotion.getMaxDiscount() + "}");
                    } else {
                        out.write("{\"valid\": false, \"message\": \"Promotion code is expired, not yet active, or does not meet the minimum order value.\"}");
                    }
                } else {
                    out.write("{\"valid\": false, \"message\": \"Promotion code is not active.\"}");
                }
            } else {
                out.write("{\"valid\": false, \"message\": \"Invalid promotion code.\"}");
            }
        } catch (Exception e) {
            // Ghi log lỗi thay vì in stack trace
            e.printStackTrace();
            out.write("{\"valid\": false, \"message\": \"An error occurred while checking the promotion code.\"}");
        }
    }
}