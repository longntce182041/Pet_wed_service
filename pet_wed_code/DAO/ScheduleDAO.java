/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static ConnectDB.DBConnect.getConnection;
import Model.Schedule;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ScheduleDAO {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PetiqueSpa;encrypt=false";
        String user = "sa";
        String pass = "123";
        return DriverManager.getConnection(url, user, pass);
    }

    public static List<Schedule> getSchedulesThisWeek() {
    List<Schedule> list = new ArrayList<>();
    try (Connection conn = getConnection()) {
        String sql = "SELECT s.schedule_id, s.staff_id, st.staff_full_name AS staff_full_name, s.work_date, s.start_time, s.end_time, s.status " +
                     "FROM Work_Schedule s " +
                     "JOIN Staff st ON s.staff_id = st.staff_id " +
                     "WHERE s.work_date >= CAST(GETDATE() AS DATE) AND s.work_date <= DATEADD(DAY, 7, CAST(GETDATE() AS DATE))";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Schedule s = new Schedule();
            s.setScheduleId(rs.getInt("schedule_id"));
            s.setStaffId(rs.getString("staff_id"));
            s.setStaffFullName(rs.getString("staff_full_name"));
            s.setWorkDate(rs.getString("work_date"));
            s.setStartTime(rs.getString("start_time"));
            s.setEndTime(rs.getString("end_time"));
            s.setStatus(rs.getString("status"));
            list.add(s);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    public static boolean insertSchedule(Schedule s) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO Work_Schedule (staff_id, work_date, start_time, end_time, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getStaffId());
            ps.setString(2, s.getWorkDate());
            ps.setString(3, s.getStartTime());
            ps.setString(4, s.getEndTime());
            ps.setString(5, s.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
