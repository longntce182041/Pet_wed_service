package DAO;

import ConnectDB.DBConnect;
import java.sql.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import Model.ServiceBooking;

/**
 *
 * @author TruongMinhDan CE181520
 */
public class ServiceBookingDAO {

    public static String insertBooking(String serviceId, String userId, String petId, Date date, Time time, String note) {
        String generatedId = null;
        String sql = "INSERT INTO Service_Booking (service_id, user_id, pet_id, service_booking_date, service_booking_time, status, note, create_at) VALUES (?, ?, ?, ?, ?, 'Pending', ?, GETDATE())";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, serviceId);
            pstmt.setString(2, userId);
            pstmt.setString(3, petId);
            pstmt.setDate(4, date);
            pstmt.setTime(5, time);
            pstmt.setString(6, note);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public static List<ServiceBooking> getBookingHistory(String userId) {
        List<ServiceBooking> bookings = new ArrayList<>();
        String sql = "SELECT sb.service_booking_id, sb.service_id, sb.status, sb.service_booking_date, sb.service_booking_time,\n"
                    + "       sb.create_at, sb.note, sb.user_id, sb.pet_id,\n"
                    + "       od.order_detail_id, od.price,\n"
                    + "       s.service_name,\n"
                    + "       u.user_fullname, u.user_phone, u.user_email, u.user_address,\n"
                    + "       p.pet_name, p.pet_type, p.pet_gender, p.age\n"
                    + "FROM Service_Booking sb\n"
                    + "LEFT JOIN Order_Details od ON sb.service_booking_id = od.service_booking_id\n"
                    + "LEFT JOIN Services s ON sb.service_id = s.service_id\n"
                    + "LEFT JOIN Users u ON sb.user_id = u.user_id\n"
                    + "LEFT JOIN Pets p ON sb.pet_id = p.pet_id\n"
                    + "WHERE sb.user_id = ?\n"
                    + "ORDER BY sb.service_booking_date DESC, sb.service_booking_time DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ServiceBooking booking = new ServiceBooking();
                booking.setServiceBookingId(rs.getInt("service_booking_id"));
                booking.setServiceId(rs.getString("service_id"));
                booking.setServiceBookingDate(rs.getDate("service_booking_date"));
                booking.setServiceBookingTime(rs.getTime("service_booking_time"));
                booking.setStatus(rs.getString("status"));
                booking.setNote(rs.getString("note"));
                booking.setCreateAt(rs.getDate("create_at"));
                booking.setOrderDetailId(rs.getInt("order_detail_id"));
                booking.setServicePrice(rs.getBigDecimal("price"));
                booking.setServiceName(rs.getString("service_name"));
                booking.setUserName(rs.getString("user_fullname"));
                booking.setPhoneNumber(rs.getString("user_phone"));
                booking.setEmail(rs.getString("user_email"));
                booking.setAddress(rs.getString("user_address"));
                booking.setPetName(rs.getString("pet_name"));
                booking.setPetSpecies(rs.getString("pet_type"));
                booking.setPetGender(rs.getString("pet_gender"));
                booking.setPetAge(rs.getInt("age"));

                if ("Completed".equalsIgnoreCase(booking.getStatus()) && booking.getOrderDetailId() != 0) {
                    booking.setRated(RatingDAO.hasRated(booking.getOrderDetailId()));
                } else {
                    booking.setRated(false); // Không cần check DB nữa, chắc chắn chưa rate
                }

                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static void cancelBooking(int bookingId) {
        String sql = "UPDATE Service_Booking SET status = 'Cancelled' WHERE service_booking_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Phương thức mới để lấy tất cả các dịch vụ
public List<ServiceBooking> getAllServices() {
    List<ServiceBooking> bookings = new ArrayList<>();
    String sql = "SELECT service_booking_id, status, service_booking_date, service_booking_time,\n"
            + "       create_at, note, pet_id, schedule_id, customer_id, price_id\n"
            + "FROM Service_Booking\n"
            + "ORDER BY service_booking_date DESC, service_booking_time DESC";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            ServiceBooking booking = new ServiceBooking();
            booking.setServiceBookingId(rs.getInt("service_booking_id"));
            booking.setStatus(rs.getString("status"));
            booking.setServiceBookingDate(rs.getDate("service_booking_date"));
            booking.setServiceBookingTime(rs.getTime("service_booking_time"));
            booking.setCreateAt(rs.getDate("create_at"));
            booking.setNote(rs.getString("note"));
            // Không lấy serviceName nữa
            bookings.add(booking);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bookings;
}
    // Lấy dịch vụ theo trạng thái
    public List<ServiceBooking> getServicesByStatus(String status) {
        List<ServiceBooking> services = new ArrayList<>();
        String sql = "SELECT service_booking_id,  status, service_booking_date, service_booking_time, note, s.service_name FROM Service_Booking sb JOIN Services s ON sb.service_id = s.service_id WHERE status = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ServiceBooking service = new ServiceBooking();
                 service.setServiceBookingId(rs.getInt("service_booking_id"));
                service.setStatus(rs.getString("status"));
                service.setServiceBookingDate(rs.getDate("service_booking_date"));
                service.setServiceBookingTime(rs.getTime("service_booking_time"));
                service.setNote(rs.getString("note"));
                service.setServiceName(rs.getString("service_name"));
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    
     public ServiceBooking getServiceBookingById(int serviceBookingId) {
        ServiceBooking service = null;
        String sql = "SELECT service_booking_id,  status, service_booking_date, service_booking_time, note, s.service_name FROM Service_Booking sb JOIN Services s ON sb.service_id = s.service_id WHERE service_booking_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceBookingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                service = new ServiceBooking();
                service.setServiceBookingId(rs.getInt("service_booking_id"));
                service.setStatus(rs.getString("status"));
                service.setServiceBookingDate(rs.getDate("service_booking_date"));
                service.setServiceBookingTime(rs.getTime("service_booking_time"));
                service.setNote(rs.getString("note"));
                service.setServiceName(rs.getString("service_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return service;
    }


    public boolean updateServiceBookingStatus(int serviceBookingId, String status) {
        String sql = "UPDATE Service_Booking SET status = ? WHERE service_booking_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, serviceBookingId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

