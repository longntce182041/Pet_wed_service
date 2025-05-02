/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect;
import Model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StaffDAO {

    public static List<Staff> getAllStaff() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("staff_id"),
                    rs.getString("staff_full_name"),
                    rs.getString("position"),
                    rs.getString("staff_phone"),
                    rs.getString("staff_email"),
                    rs.getString("staff_img")
                );
                list.add(staff);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Staff getStaffById(String staffId) {
        String sql = "SELECT * FROM Staff WHERE staff_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Staff(
                    rs.getString("staff_id"),
                    rs.getString("staff_full_name"),
                    rs.getString("position"),
                    rs.getString("staff_phone"),
                    rs.getString("staff_email"),
                    rs.getString("staff_img")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addStaff(Staff staff) {
        String sql = "INSERT INTO Staff (staff_id, staff_full_name, position, staff_phone,  staff_email, staff_img) VALUES ( ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getStaffId());
            ps.setString(2, staff.getStaffFullName());
            ps.setString(3, staff.getPosition());
            ps.setString(4, staff.getStaffPhone());
            ps.setString(5, staff.getStaffEmail());
            ps.setString(6, staff.getStaffImg());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateStaff(Staff staff) {
        String sql = "UPDATE Staff SET staff_full_name=?, position=?, staff_phone=?, staff_email=?, staff_img=? WHERE staff_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getStaffFullName());
            ps.setString(2, staff.getPosition());
            ps.setString(3, staff.getStaffPhone());
            ps.setString(4, staff.getStaffEmail());
            ps.setString(5, staff.getStaffImg());
            ps.setString(6, staff.getStaffId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteStaff(String staffId) {
        String sql = "DELETE FROM Staff WHERE staff_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}