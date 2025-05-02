package Controller;

import Model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            HttpSession session = request.getSession();
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                response.getWriter().write("{\"success\": false, \"message\": \"Your cart is empty.\", \"cartCount\": 0, \"totalPrice\": \"0 VNĐ\"}");
                return;
            }

            boolean removed = cart.removeIf(item -> item.getProduct().getProductId() == productId);

            if (!removed) {
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found in cart.\"}");
                return;
            }

            session.setAttribute("cart", cart);

            // Tính lại tổng tiền và số lượng sản phẩm trong giỏ hàng
            int cartCount = 0;
            double totalPrice = 0;
            for (CartItem item : cart) {
                if (item.getProduct() != null) { // Đảm bảo sản phẩm không null
                    cartCount += item.getQuantity();
                    totalPrice += item.getProduct().getPrice() * item.getQuantity();
                }
            }

            session.setAttribute("cartCount", cartCount);

            response.getWriter().write("{\"success\": true, \"message\": \"Product removed successfully.\", \"cartCount\": " + cartCount + ", \"totalPrice\": \"" + String.format("%,.0f VNĐ", totalPrice) + "\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
        }
    }
}