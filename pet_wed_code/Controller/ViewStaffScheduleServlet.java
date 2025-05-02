/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ViewScheduleDAO;
import Model.ViewSchedule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 *
 * @author Admin
 */
@WebServlet(name = "ViewStaffScheduleServlet", urlPatterns = {"/ViewStaffScheduleServlet"})
public class ViewStaffScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("staff_id") == null) {
            response.sendRedirect("Staffdashboard.jsp");
            return;
        }

        int staffId = (int) session.getAttribute("staff_id");

        List<ViewSchedule> scheduleList = ViewScheduleDAO.getScheduleByStaffId(staffId);
        request.setAttribute("scheduleList", scheduleList);
        request.getRequestDispatcher("viewStaffSchedule.jsp").forward(request, response);
    }
}
