package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import Model.OrderDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject; // Import thư viện JSON

@WebServlet("/UpdateOrderStatusServlet")
public class UpdateOrderStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");

        response.setContentType("application/json"); // Đặt kiểu nội dung là JSON
        response.setCharacterEncoding("UTF-8");

        try {
            JSONObject jsonResponse = new JSONObject(); // Tạo đối tượng JSON để chứa phản hồi

            if ("Complete".equals(status)) {
                List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
                boolean canComplete = true;
                StringBuilder errorMessage = new StringBuilder("Insufficient quantity for the following products:\n");

                for (OrderDetail detail : orderDetails) {
                    int availableQuantity = productDAO.getProductQuantity(detail.getProductId());
                    if (availableQuantity < detail.getQuantity()) {
                        canComplete = false;
                        errorMessage.append("- Product ID ").append(detail.getProductId()).append(": ").append(availableQuantity).append(" existing, needed ").append(detail.getQuantity()).append("\n");
                    }
                    if (availableQuantity - detail.getQuantity() < 0) {
                        canComplete = false;
                        errorMessage.append("- Product ID ").append(detail.getProductId()).append(": The quantity after completion will be negative.\n");
                    }
                }

                if (canComplete) {
                    orderDAO.updateOrderStatus(orderId, status);
                    for (OrderDetail detail : orderDetails) {
                        productDAO.updateProductQuantity(detail.getProductId(), -detail.getQuantity());
                    }
                    jsonResponse.put("type", "success"); // Loại thông báo: success
                    jsonResponse.put("message", "Order completed successfully."); // Nội dung thông báo
                } else {
                    jsonResponse.put("type", "error"); // Loại thông báo: error
                    jsonResponse.put("message", "Complete Failed: " + errorMessage.toString());
                }
            } else if ("Cancel".equals(status)) {
                List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
                for (OrderDetail detail : orderDetails) {
                    productDAO.updateProductQuantity(detail.getProductId(), detail.getQuantity());
                }
                orderDAO.updateOrderStatus(orderId, status);
                jsonResponse.put("type", "success");
                jsonResponse.put("message", "Order cancelled successfully.");
            } else {
                orderDAO.updateOrderStatus(orderId, status);
                jsonResponse.put("type", "success");
                jsonResponse.put("message", "Order status updated successfully.");
            }
            response.getWriter().write(jsonResponse.toString()); // Gửi JSON response
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("type", "error");
            errorResponse.put("message", "Internal Server Error: " + e.getMessage());
            response.getWriter().write(errorResponse.toString());
        }
    }
}
