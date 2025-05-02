/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.DBConnect;
import Model.ViewSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ViewScheduleDAO {
    public static List<ViewSchedule> getScheduleByStaffId(int staffId) {
        List<ViewSchedule> list = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.staff_id, st.staff_full_name AS staff_full_name, s.work_date, s.start_time, s.end_time " +
             "FROM Work_Schedule s " +
             "JOIN Staff st ON s.staff_id = st.staff_id " +
             "WHERE s.staff_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ViewSchedule vs = new ViewSchedule();
                vs.setScheduleId(rs.getInt("schedule_id"));
                vs.setStaffId(rs.getInt("staff_id"));
                vs.setStaffFullName(rs.getString("staff_full_name"));
                vs.setWorkDate(rs.getString("work_date"));
                vs.setStartTime(rs.getString("start_time"));
                vs.setEndTime(rs.getString("end_time"));
                list.add(vs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

