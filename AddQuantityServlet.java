package Controller;

import DAO.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddQuantityServlet")
public class AddQuantityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdParam = request.getParameter("productId");
        String quantityToAddParam = request.getParameter("quantityToAdd");

        System.out.println("AddQuantityServlet - doPost called");
        System.out.println("productIdParam: " + productIdParam);
        System.out.println("quantityToAddParam: " + quantityToAddParam);

        if (productIdParam == null || productIdParam.isEmpty()) {
            System.err.println("Lỗi: Thiếu tham số productId");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Lỗi: Thiếu tham số productId.");
            return;
        }

        if (quantityToAddParam == null || quantityToAddParam.isEmpty()) {
            System.err.println("Lỗi: Thiếu tham số quantityToAdd");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Lỗi: Thiếu tham số quantityToAdd.");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantityToAdd = Integer.parseInt(quantityToAddParam);

            // Gọi phương thức increaseProductQuantity để cập nhật số lượng
            productDAO.increaseProductQuantity(productId, quantityToAdd);

            // Lấy số lượng mới sau khi cập nhật để trả về
            int newQuantity = productDAO.getProductQuantity(productId);

            // Gửi phản hồi thành công (chỉ trả về số lượng mới)
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.valueOf(newQuantity));

        } catch (NumberFormatException e) {
            System.err.println("Lỗi: Số lượng không hợp lệ");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Lỗi: Số lượng không hợp lệ.");
        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Lỗi: Không thể cập nhật số lượng.");
        }
    }
}