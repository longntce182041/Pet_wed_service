/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;


import DAO.CustomerSendEmailDAO;
import DAO.PromotionDAO;
import Model.CustomerSendEmail;
import Model.EmailUtil;
import Model.Promotion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/SendPromotionEmailServlet")
public class SendPromotionEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy danh sách khách hàng và chương trình khuyến mãi
            List<CustomerSendEmail> customerList = CustomerSendEmailDAO.getAllCustomers();
            List<Promotion> promotionList = PromotionDAO.getAllPromotions();

            request.setAttribute("customerList", customerList);
            request.setAttribute("promotionList", promotionList);

            request.getRequestDispatcher("sendPromotionEmail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading email page.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] customerIds = request.getParameterValues("customerIds");
            String promotionId = request.getParameter("promotionId");

            if (customerIds != null && promotionId != null) {
                Promotion promotion = PromotionDAO.getPromotionById(promotionId);


                for (String customerId : customerIds) {
                    CustomerSendEmail customer = CustomerSendEmailDAO.getCustomerById(customerId);

                    String emailContent = "Dear " + customer.getCustomerFullName() + ",\n\n" +
                            "We are excited to share our latest promotion with you:\n\n" +
                            "Promotion: " + promotion.getPromotionName() + "\n" +
                            "Description: " + promotion.getPromotionDescription() + "\n" +
                            "Discount: " + promotion.getDiscountValue() + "%\n" +
                            "Valid from: " + promotion.getStartDate() + " to " + promotion.getEndDate() + "\n\n" +
                            "Don't miss out on this great offer!\n\n" +
                            "Best regards,\nYour Company";

                    // Gửi email
                    EmailUtil.sendEmail(customer.getCustomerEmail(), "Exclusive Promotion for You!", emailContent);
                }
            }

            response.sendRedirect("PromotionServlet");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error sending emails.");
        }
    }
}
