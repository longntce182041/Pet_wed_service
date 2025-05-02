<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Service Bookings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateBookingStatus(bookingId, newStatus) {
            fetch('UpdateServiceBookingStatusServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'serviceBookingId=' + encodeURIComponent(bookingId) + '&status=' + encodeURIComponent(newStatus)
            })
            .then(response => response.json())
            .then(data => {
                if (data.type === 'success') {
                    alert(data.message);
                    // Reload the page to reflect the updated status
                    window.location.reload();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error updating booking status:', error);
                alert('Failed to update booking status.');
            });
        }
    </script>
    <style>
        .table-container {
            margin-top: 20px;
        }
        .action-buttons button {
            margin-right: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>All Service Bookings</h1>

        <div class="table-container">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Status</th>
                        <th>Booking Date</th>
                        <th>Booking Time</th>
                        <th>Note</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ServiceBookingDAO serviceBookingDAO = new ServiceBookingDAO();
                        List<ServiceBooking> allBookings = serviceBookingDAO.getAllServices();
                        if (allBookings != null && !allBookings.isEmpty()) {
                            for (ServiceBooking booking : allBookings) {
                    %>
                        <tr>
                            <td><%= booking.getServiceBookingId() %></td>
                            <td><%= booking.getStatus() %></td>
                            <td><%= booking.getServiceBookingDate() %></td>
                            <td><%= booking.getServiceBookingTime() %></td>
                            <td><%= booking.getNote() %></td>
                            <td class="action-buttons">
                                <c:if test="${booking.status != 'Cancelled' && booking.status != 'Completed'}">
                                    <button onclick="updateBookingStatus('<%= booking.getServiceBookingId() %>', 'Cancelled')" class="btn btn-warning btn-sm">Cancel</button>
                                    <button onclick="updateBookingStatus('<%= booking.getServiceBookingId() %>', 'Completed')" class="btn btn-success btn-sm">Complete</button>
                                </c:if>
                                <c:if test="${booking.status == 'Cancelled'}">
                                    <span class="badge bg-danger">Cancelled</span>
                                </c:if>
                                <c:if test="${booking.status == 'Completed'}">
                                    <span class="badge bg-success">Completed</span>
                                </c:if>
                                <c:if test="${booking.status != 'Cancelled' && booking.status != 'Completed' && booking.status != 'Pending'}">
                                    <button onclick="updateBookingStatus('<%= booking.getServiceBookingId() %>', 'Pending')" class="btn btn-info btn-sm">Set Pending</button>
                                </c:if>
                                <c:if test="${booking.status == 'Pending'}">
                                    <span class="badge bg-info">Pending</span>
                                </c:if>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr><td colspan="6"><strong>No service bookings available.</strong></td></tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>