package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ConnectDB.DBConnect;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check if username or password is empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Tên đăng nhập và mật khẩu không được để trống!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Hash the password
        String hashedPassword = Integer.toHexString(password.hashCode());
        System.out.println("🔍 [DEBUG] Username: " + username);
        System.out.println("🔍 [DEBUG] Mật khẩu mã hóa: " + hashedPassword);

        // Check login information
        String sql = "SELECT u.user_id, u.role, u.username, c.customer_id, c.customer_address, c.customer_email, c.customer_fullname, c.customer_phone "
                + "FROM Users u "
                + "LEFT JOIN Customer c ON u.user_id = c.user_id "
                + "WHERE u.username COLLATE SQL_Latin1_General_CP1_CS_AS = ? AND u.password = ?";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                request.setAttribute("errorMessage", "Không thể kết nối đến database!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Login successful -> Save session
                String userId = rs.getString("user_id");
                String role = rs.getString("role");
                String customerId = rs.getString("customer_id");
                String customerAddress = rs.getString("customer_address");
                String customerEmail = rs.getString("customer_email");
                String customerFullname = rs.getString("customer_fullname");
                String customerPhone = rs.getString("customer_phone");

                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                session.setAttribute("role", role);
                session.setAttribute("username", username);

                // Save customer-specific details in the session
                if ("customer".equalsIgnoreCase(role)) {
                    session.setAttribute("customer_id", customerId);
                    session.setAttribute("customer_address", customerAddress);
                    session.setAttribute("customer_email", customerEmail);
                    session.setAttribute("customer_fullname", customerFullname);
                    session.setAttribute("customer_phone", customerPhone);
                }

                session.setMaxInactiveInterval(60 * 60); // 1 hour (in seconds)

                System.out.println("✅ [DEBUG] Đăng nhập thành công! Role: " + role);
                System.out.println("✅ [DEBUG] Session created successfully for user_id: " + userId);

                if ("admin".equalsIgnoreCase(role)) {
                    response.sendRedirect("admin_Dashboard.jsp"); // Redirect to admin dashboard
                } else if ("staff".equalsIgnoreCase(role)) {
                    response.sendRedirect("Staffdashboard.jsp"); // Redirect to staff dashboard
                } else {
                    response.sendRedirect("homepage.jsp"); // Redirect to customer home page
                }
                return; // Exit method
            } else {
                // Incorrect username or password
                request.setAttribute("errorMessage", "Sai tài khoản hoặc mật khẩu!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống! Vui lòng thử lại sau.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
