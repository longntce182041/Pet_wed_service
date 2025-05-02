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

@WebServlet("/UpdateOrderStatusServlet")
public class UpdateOrderStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");

            // Cập nhật trạng thái đơn hàng
            orderDAO.updateOrderStatus(orderId, status);

            // Nếu trạng thái là "Confirmed", trừ số lượng sản phẩm trong bảng Products
            if ("Complete".equals(status)) {
                List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
                for (OrderDetail detail : orderDetails) {
                    productDAO.updateProductQuantity(detail.getProductId(), -detail.getQuantity());
                }
            }

            // Trả về phản hồi JSON thành công
            response.getWriter().write("{\"success\": true, \"message\": \"Order status updated to " + status + " successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về phản hồi JSON lỗi
            response.getWriter().write("{\"success\": false, \"message\": \"Failed to update order status.\"}");
        }
    }
}