package Controller;

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

@WebServlet("/VerifyCodeServlet")
public class VerifyCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String inputCode = request.getParameter("otp");

        if (email == null || inputCode == null || inputCode.trim().isEmpty()) {
            response.sendRedirect("verification.jsp?email=" + email + "&error=Please enter OTP code!");
            return;
        }

        try {
            // Kết nối database
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=PetiqueSpa;encrypt=false", "sa", "MyStrongPass123");

            // Kiểm tra mã OTP
            PreparedStatement stmt = conn.prepareStatement("SELECT verification_code FROM Users WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedCode = rs.getString("verification_code");

                if (storedCode != null && storedCode.equals(inputCode)) {
                    // Xác thực thành công, cập nhật trạng thái
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE Users SET is_verified = 1, verification_code = NULL WHERE email = ?");
                    updateStmt.setString(1, email);
                    updateStmt.executeUpdate();

                    response.sendRedirect("login.jsp?message=Verify sucessfull! You can login.");
                } else {
                    response.sendRedirect("verification.jsp?email=" + email + "&error=Code OTP invalid!");
                }
            } else {
                response.sendRedirect("verification.jsp?error=Email not exist!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("verification.jsp?error=Error system, please try again.");
        }
    }
}