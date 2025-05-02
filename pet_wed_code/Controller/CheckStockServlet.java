package Controller;

import DAO.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.json.JSONObject;

@WebServlet("/CheckStockServlet")
public class CheckStockServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        ProductDAO productDAO = new ProductDAO();
        int stockQuantity = productDAO.getProductStockQuantity(productId);

        JSONObject jsonResponse = new JSONObject();
        if (stockQuantity >= 0) {
            jsonResponse.put("success", true);
            jsonResponse.put("stockQuantity", stockQuantity);
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Failed to retrieve stock quantity.");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}