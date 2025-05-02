  package DAO;

import ConnectDB.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.User;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Kiểm tra xem email có tồn tại trong bảng Users không
    public boolean isEmailRegistered(String email) {
        String query = "SELECT COUNT(*) FROM Users WHERE user_email = ?";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật OTP vào bảng Users
    public boolean updateOTP(String email, String otp) {
        String query = "UPDATE Users SET verification_code = ? WHERE user_email = ?";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, otp);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra OTP có hợp lệ không
    public boolean verifyOTP(String email, String otp) {
        String query = "SELECT COUNT(*) FROM Users WHERE user_email = ? AND verification_code = ?";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, otp);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật mật khẩu mới cho user
    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE Users SET password = ? WHERE user_email = ?";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái xác thực và đặt lại mã xác minh
    public boolean resetVerifiedCode(String email) {
        String query = "UPDATE Users SET is_verified = 1, verification_code = NULL WHERE user_email = ?";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // in ra tất cả các user có role là customer
    public static void viewAllCustomer() {
        String query = "SELECT * FROM Users";
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa user theo user_id
    public boolean deleteUser(String userId) {
        try ( Connection conn = DBConnect.getConnection()) {
            String sql = "DELETE FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin user
    public boolean updateUser(User user) {
        try ( Connection conn = DBConnect.getConnection()) {
            String sql = "UPDATE Users SET username = ?, user_email = ?, user_fullname = ?, user_address = ?, user_phone = ?, [role] = ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFullname());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getRole());
            stmt.setString(7, user.getUserId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getAllCustomer() {
        List<User> customerList = new ArrayList<>();
        try ( Connection conn = DBConnect.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT * FROM Users")) {
            while (rs.next()) {
                User customer = new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("user_email"),
                        rs.getString("user_fullname"),
                        rs.getString("user_address"),
                        rs.getString("user_phone"),
                        rs.getString("role"));
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static User getCustomerById(String customerId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("user_email"),
                        null,
                        rs.getString("role"),
                        rs.getString("user_fullname"),
                        rs.getString("user_address"),
                        rs.getString("user_phone"),
                        rs.getString("username"),
                        rs.getString("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        User user = null;
        try ( Connection conn = DBConnect.getConnection();  PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("user_email"),
                        rs.getString("user_fullname"),
                        rs.getString("user_address"),
                        rs.getString("user_phone"),
                        rs.getString("role"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getUserIdByUsername(String username) {
        String userId = null;
        String query = "SELECT user_id FROM Users WHERE username = ?";

        try ( Connection conn = DBConnect.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getString("user_id");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public boolean checkOldPassword(String userId, String oldPassword) {
        String query = "SELECT password FROM Users WHERE user_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                return dbPassword.equals(hashPassword(oldPassword)); // Hash for comparison
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePasswordbyUsername(String userId, String hashedNewPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnect.getConnection();
            String sql = "UPDATE Users SET password = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hashedNewPassword);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                DBConnect.closeConnection(conn);
            }
        }
        return false;
    }

    public static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }
}
