package Controller;

import DAO.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteOrderServlet")
public class DeleteOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        boolean deleted = orderDAO.deleteOrder(orderId);

        if (deleted) {
            response.getWriter().write("Xóa đơn hàng thành công");
        } else {
            response.getWriter().write("Xóa đơn hàng thất bại");
        }
    }
}