<%-- 
    Document   : ViewSchedule
    Created on : May 2, 2025, 6:44:16 PM
    Author     : Admin
--%>

<%@ page import="java.util.*, Model.ViewSchedule" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Work Schedule</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-4">
    <h2 class="text-center mb-4">My Work Schedule</h2>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
        <tr>
            <th>No</th>
            <th>Name</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<ViewSchedule> scheduleList = (List<ViewSchedule>) request.getAttribute("scheduleList");
            if (scheduleList != null && !scheduleList.isEmpty()) {
                for (ViewSchedule vs : scheduleList) {
        %>
            <tr>
                <td><%= index++ %></td>
                <td><%= vs.getStaffFullName() %></td>
                <td><%= vs.getWorkDate() %></td>
                <td><%= vs.getStartTime() %></td>
                <td><%= vs.getEndTime() %></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="4" class="text-center">No schedule found.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>

