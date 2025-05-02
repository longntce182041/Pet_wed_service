package Controller;

import DAO.ProductDAO;
import Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/SearchProductsServlet")
public class SearchProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = productDAO.searchProducts(query);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        for (Product product : productList) {
            out.println("<div class='col-md-4 d-flex align-items-stretch'>");
            out.println("<div class='card mb-4 shadow-sm product-card' data-product-id='" + product.getProductId() + "'>");
            out.println("<img src='" + product.getImageUrl() + "' class='card-img-top' alt='" + product.getName() + "'>");
            out.println("<div class='card-body d-flex flex-column'>");
            out.println("<h5 class='card-title'>" + product.getName() + "</h5>");
            out.println("<p class='card-text'>" + product.getDescription() + "</p>");
            out.println("<p class='card-text'><strong>Price: " + String.format("%,.0f VNĐ", product.getPrice()) + "</strong></p>");
            if (product.getStockQuantity() == 0) {
                out.println("<span class='badge badge-danger'>Sold Out</span>");
            } else {
                out.println("<a href='javascript:void(0);' class='btn btn-outline-success add-to-cart mt-auto' data-product-id='" + product.getProductId() + "'>");
                out.println("<i class='fa fa-shopping-cart'></i> Add to Cart</a>");
            }
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
        }
    }
}
