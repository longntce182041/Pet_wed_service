/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.StaffDAO;
import Model.Staff;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Admin
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50   // 50MB
)
@WebServlet("/ManageStaffServlet")
public class ManageStaffServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "view"; // Default action
        }

        try {
            if (action.equals("view")) {
                // View all staff
                List<Staff> staffList = StaffDAO.getAllStaff();
                request.setAttribute("staffList", staffList);
                request.getRequestDispatcher("manageStaff.jsp").forward(request, response);

            } else if (action.equals("delete")) {
                // Delete a staff member
                String staffId = request.getParameter("id");
                if (staffId != null && !staffId.trim().isEmpty()) {
                    boolean isDeleted = StaffDAO.deleteStaff(staffId);
                    if (isDeleted) {
                        request.getSession().setAttribute("success", "Staff deleted successfully!");
                    } else {
                        request.getSession().setAttribute("error", "Failed to delete staff.");
                    }
                } else {
                    request.getSession().setAttribute("error", "Invalid staff ID.");
                }
                response.sendRedirect("ManageStaffServlet?action=view");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error processing request: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Add a new staff member
                Staff staff = extractStaffFromRequest(request, true);
                boolean isAdded = StaffDAO.addStaff(staff);
                if (isAdded) {
                    request.getSession().setAttribute("success", "Staff added successfully!");
                } else {
                    request.getSession().setAttribute("error", "Failed to add staff.");
                }
            } else if ("update".equals(action)) {
                // Update an existing staff member
                Staff staff = extractStaffFromRequest(request, false);
                boolean isUpdated = StaffDAO.updateStaff(staff);
                if (isUpdated) {
                    request.getSession().setAttribute("success", "Staff updated successfully!");
                } else {
                    request.getSession().setAttribute("error", "Failed to update staff.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "An error occurred while processing the request.");
        }

        response.sendRedirect("ManageStaffServlet?action=view");
    }

    /**
     * Extracts staff details from the HTTP request and validates the input.
     *
     * @param request The HTTP request
     * @param isNew Indicates whether the staff is new or existing
     * @return A Staff object populated with the request data
     * @throws IllegalArgumentException If any required field is missing or invalid
     * @throws IOException If an I/O error occurs during file handling
     * @throws ServletException If a servlet error occurs
     */
    private Staff extractStaffFromRequest(HttpServletRequest request, boolean isNew) throws IllegalArgumentException, IOException, ServletException {
        String staffId = request.getParameter("staff_id");
        String fullName = request.getParameter("staff_full_name");
        String position = request.getParameter("position");
        String phone = request.getParameter("staff_phone");
        String email = request.getParameter("staff_email");

        // Handle file upload
        Part filePart = request.getPart("staff_img");
        String fileName = null;
        String imageUrl = null;

        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

            // Create the upload directory if it doesn't exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Save the file
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // Save the relative path to the database
            imageUrl = UPLOAD_DIR + "/" + fileName;
        } else if (!isNew) {
            // Retain the existing image for updates
            Staff existingStaff = StaffDAO.getStaffById(staffId);
            if (existingStaff != null) {
                imageUrl = existingStaff.getStaffImg();
            }
        }

        // Validate required fields
        if (staffId == null || staffId.isEmpty() || fullName == null || fullName.isEmpty() ||
            position == null || position.isEmpty() || phone == null || phone.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        return new Staff(staffId, fullName, position, phone, email, imageUrl);
    }
}