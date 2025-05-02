/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect;
import Model.AppointmentSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AppointmentScheduleDAO {

    public static List<AppointmentSchedule> getAllSchedules() {
        List<AppointmentSchedule> list = new ArrayList<>();
        String sql = "SELECT sb.service_booking_id, sb.customer_id, c.customer_fullname, sb.service_booking_date, " +
             "sb.service_booking_time, sb.status, sb.pet_id, p.pet_name " +
             "FROM Service_Booking sb " +
             "JOIN Customer c ON sb.customer_id = c.customer_id " +
             "JOIN Pets p ON sb.pet_id = p.pet_id";



        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AppointmentSchedule a = new AppointmentSchedule(
                        rs.getInt("service_booking_id"),
                        rs.getString("customer_fullname"),
                        rs.getString("pet_name"),
                        rs.getString("service_booking_date"),
                        rs.getString("service_booking_time"),
                        rs.getString("status")
                );
                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStatus(int bookingId, String status) {
        String sql = "UPDATE service_booking SET status=? WHERE service_booking_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
