<%-- 
    Document   : setschedule
    Created on : Apr 20, 2025, 10:12:08 PM
    Author     : Admin
--%>

<%@ page import="java.util.List" %>
<%@ page import="Model.Schedule" %>
<%@ page import="DAO.ScheduleDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Staff Weekly Schedule</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .create-btn {
            margin-bottom: 10px;
            background-color: #4CAF50;
            color: white;
            padding: 8px 12px;
            border: none;
            cursor: pointer;
        }
        .create-btn:hover {
            background-color: #45a049;
        }
        table {
            border-collapse: collapse;
            width: 90%;
        }
        th, td {
            border: 1px solid #000;
            padding: 8px;
        }
        th {
            background-color: #d4edda;
        }
    </style>
</head>
<body>
<h2>Staff Weekly Schedule</h2>

<%
    String success = (String) request.getAttribute("success");
    if ("true".equals(success)) {
%>
    <p style="color: green;">Create a successful work schedule!</p>
<%
    } else if ("false".equals(success)) {
%>
    <p style="color: red;">Failed to create work schedule!</p>
<%
    }

    List<Schedule> schedules = (List<Schedule>) request.getAttribute("scheduleList");
%>


<form action="createSchedule.jsp" method="get" style="margin-bottom: 20px;">
    <input type="submit" value="Create Schedule" class="create-btn">
</form>
<table>
    <tr>
        <th>Schedule ID</th>
        <th>Staff Name</th>
        <th>Work Date</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Status</th>
    </tr>
    <%
        if (schedules != null && !schedules.isEmpty()) {
            for (Schedule s : schedules) {
    %>
    <tr>
        <td><%= s.getScheduleId() %></td>
        <td><%= s.getStaffFullName() %></td>
        <td><%= s.getWorkDate() %></td>
        <td><%= s.getStartTime() %></td>
        <td><%= s.getEndTime() %></td>
        <td><%= s.getStatus() %></td>
    </tr>
    <%
            }
        } else {
    %>
    <tr>
        <td colspan="6" style="text-align: center;">No schedules found</td>
    </tr>
    <%
        }
    %>
</table>
<div class="text-center my-3">
    <a href="AppointmentScheduleServlet" class="btn btn-secondary">Back</a>
</div>
</body>
</html>