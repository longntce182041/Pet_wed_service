/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import ConnectDB.DBConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 *
 * @author Admin
 */
@WebServlet("/DeletePromotionServlet")
public class DeletePromotionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        try {
            DBConnect dbConnect = new DBConnect();
            conn = dbConnect.getConnection();

            String promotionId = request.getParameter("id");
            if (promotionId == null || promotionId.isEmpty()) {
                throw new IllegalArgumentException("Promotion ID is required.");
            }

            String sql = "DELETE FROM Promotion WHERE promotion_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, promotionId);

            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);

            if (rowsDeleted == 0) {
                throw new Exception("No promotion found with ID: " + promotionId);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error deleting promotion: " + e.getMessage(), e);
        }

        response.sendRedirect("PromotionServlet");
    }
}
