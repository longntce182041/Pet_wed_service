package Controller;

import DAO.OrderDAO;
import Model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

@WebServlet("/FilterOrderByStatusServlet")
public class FilterOrderByStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số status. Có thể là một giá trị đơn hoặc một chuỗi các giá trị phân tách bằng dấu phẩy.
        String statusParam = request.getParameter("status");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Order> orders;
            if (statusParam == null || statusParam.isEmpty()) {
                orders = orderDAO.getAllOrders(); // Lấy tất cả nếu không có trạng thái nào được chỉ định
            } else {
                // Chuyển đổi chuỗi trạng thái thành List
                List<String> statuses = Arrays.asList(statusParam.split(","));
                orders = orderDAO.getOrdersByStatuses(statuses);
            }

            JSONArray jsonArray = new JSONArray();
            for (Order order : orders) {
                JSONObject jsonOrder = new JSONObject();
                jsonOrder.put("orderId", order.getOrderId());
                jsonOrder.put("orderDate", order.getOrderDate().toString());
                jsonOrder.put("totalPrice", order.getTotalPrice());
                jsonOrder.put("name", order.getName());
                jsonOrder.put("phone", order.getPhone());
                jsonOrder.put("email", order.getEmail());
                jsonOrder.put("status", order.getStatus());
               
                
                // Thêm các thuộc tính khác của đơn hàng nếu cần
                jsonArray.put(jsonOrder);
            }
            response.getWriter().write(jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new JSONObject().put("error", "Internal Server Error").toString());
        }
    }
}
