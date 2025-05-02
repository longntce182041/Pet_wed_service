/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;



import Model.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;


@WebServlet("/SendOTPServlet")
public class SendOTPServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("register.jsp?error=Email không hợp lệ");
            return;
        }

        // Tạo mã OTP ngẫu nhiên
        Random rand = new Random();
        int otpCode = 100000 + rand.nextInt(900000);

        try {
            // Kết nối SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PetiqueSpa;encrypt=false", "sa", "MyStrongPass123");

            // Kiểm tra email có tồn tại không
            PreparedStatement checkStmt = conn.prepareStatement("SELECT email FROM Users WHERE email = ?");
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                response.sendRedirect("register.jsp?error=Email chưa được đăng ký!");
                return;
            }

            // Lưu OTP vào database
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE Users SET verification_code = ? WHERE email = ?");
            updateStmt.setInt(1, otpCode);
            updateStmt.setString(2, email);
            updateStmt.executeUpdate();

            // Gửi OTP qua email
            EmailUtil.sendEmail(email, String.valueOf(otpCode));

            // Điều hướng đến trang xác thực
            response.sendRedirect("verification.jsp?email=" + email);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Lỗi hệ thống, thử lại sau.");
        }
    }
}
