package Controller;

import ConnectDB.DBConnect;
import Controller.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            DBConnect dbConnect = new DBConnect();
            conn = dbConnect.getConnection();

            if (conn == null) {
                response.sendRedirect("register.jsp?error=connection_failed");
                return;
            }

            // Mã hóa mật khẩu (Bcrypt nên được dùng thay thế)
            String hashedPassword = Integer.toHexString(password.hashCode());

            String sql = "INSERT INTO Users (username, password, email, phone) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                // Tạo mã OTP ngẫu nhiên
                Random rand = new Random();
                int otpCode = 100000 + rand.nextInt(900000);

                // Lưu OTP vào database
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE Users SET verification_code = ? WHERE email = ?");
                updateStmt.setInt(1, otpCode);
                updateStmt.setString(2, email);
                updateStmt.executeUpdate();

                // Gửi OTP qua email
                EmailUtil.sendOTPEmail(email, String.valueOf(otpCode));

                // Điều hướng đến trang xác thực
                response.sendRedirect("verification.jsp?email=" + email);
            } else {
                response.sendRedirect("register.jsp?error=registration_failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=sql_error");
        } catch (MessagingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}