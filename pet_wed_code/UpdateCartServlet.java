package Controller;

import DAO.ProductDAO;
import Model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Cart is empty.\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String quantityParam = request.getParameter("quantity");

            // Kiểm tra nếu quantity là null hoặc không hợp lệ
            if (quantityParam == null || quantityParam.isEmpty()) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid quantity.\"}");
                return;
            }

            // Sử dụng long để tránh lỗi NumberFormatException
            long newQuantity = Long.parseLong(quantityParam);

            if (newQuantity < 1) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Quantity must be at least 1.\"}");
                return;
            }

            ProductDAO productDAO = new ProductDAO();
            int availableQuantity = productDAO.getProductById(productId).getStockQuantity();

            // Tìm sản phẩm trong giỏ hàng
            CartItem cartItem = null;
            for (CartItem item : cart) {
                if (item.getProduct().getProductId() == productId) {
                    cartItem = item;
                    break;
                }
            }

            if (cartItem == null) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found in cart.\"}");
                return;
            }

            // Kiểm tra số lượng tối đa
            if (newQuantity > availableQuantity) {
                newQuantity = availableQuantity; // Đặt lại số lượng tối đa
            }

            // Cập nhật số lượng trong giỏ hàng
            cartItem.setQuantity((int) newQuantity);
            session.setAttribute("cart", cart); // Cập nhật lại giỏ hàng trong session

            // Tính toán tổng tiền mới
            double totalCartValue = cart.stream()
                .filter(item -> item.getProduct() != null) // Đảm bảo sản phẩm không null
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

            // Trả về phản hồi JSON
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"updatedQuantity\": " + newQuantity + ", \"totalCartValue\": \"" + String.format("%,.0f VNĐ", totalCartValue) + "\"}");

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log lỗi
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid input.\"}");
        }
    }
}