/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.AppointmentScheduleDAO;
import Model.AppointmentSchedule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AppointmentScheduleServlet")
public class AppointmentScheduleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<AppointmentSchedule> list = AppointmentScheduleDAO.getAllSchedules();
            request.setAttribute("scheduleList", list);
            request.getRequestDispatcher("appointmentSchedule.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while fetching the appointment schedules.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("ServicebookingId");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int servicebookingId = Integer.parseInt(idParam);

                if ("confirm".equals(action)) {
                    AppointmentScheduleDAO.updateStatus(servicebookingId, "Confirmed");
                } else if ("cancel".equals(action)) {
                    AppointmentScheduleDAO.updateStatus(servicebookingId, "Cancelled");
                } else {
                    System.out.println("[DEBUG] Invalid action: " + action);
                }
            } catch (NumberFormatException e) {
                System.out.println("[DEBUG] Invalid ServicebookingId format: " + idParam);
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("[DEBUG] Error while updating status for ServicebookingId: " + idParam);
                e.printStackTrace();
            }
        } else {
            System.out.println("[DEBUG] ServicebookingId is null or empty. Action = " + action);
        }

        // Redirect back to the servlet to refresh the schedule list
        response.sendRedirect("AppointmentScheduleServlet");
    }
}