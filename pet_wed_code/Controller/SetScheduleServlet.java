/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ScheduleDAO;
import Model.Schedule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 *
 * @author Admin
 */
@WebServlet("/SetScheduleServlet")
public class SetScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Schedule> scheduleList = ScheduleDAO.getSchedulesThisWeek();
            System.out.println("Number of schedules this week: " + scheduleList.size());
            request.setAttribute("scheduleList", scheduleList);

            String success = request.getParameter("success");
            if (success != null) {
                request.setAttribute("success", success);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("setschedule.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving schedules", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String staffId = request.getParameter("staff_id");
            String workDate = request.getParameter("work_date");
            String startTime = request.getParameter("start_time");
            String endTime = request.getParameter("end_time");

            Schedule schedule = new Schedule();
            schedule.setStaffId(staffId);
            schedule.setWorkDate(workDate);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setStatus("Available");

            boolean success = ScheduleDAO.insertSchedule(schedule);
            if (success) {
                response.sendRedirect("SetScheduleServlet?success=true");
            } else {
                response.sendRedirect("SetScheduleServlet?success=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error creating schedule", e);
        }
    }
}