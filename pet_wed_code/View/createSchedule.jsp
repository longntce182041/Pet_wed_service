<%-- 
    Document   : createSchedule
    Created on : Apr 20, 2025, 10:38:36 PM
    Author     : Admin
--%>

<%@ page import="java.util.*, DAO.StaffDAO, Model.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create New Schedule</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-top: 20px;
        }

        form {
            max-width: 500px;
            margin: 20px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        label, select, input {
            display: block;
            width: 100%;
            margin-bottom: 15px;
            font-size: 16px;
        }

        select, input[type="date"], input[type="time"] {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #218838;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            font-weight: bold;
            margin-bottom: 5px;
            display: block;
        }
    </style>
</head>
<body>
<h2>Create New Schedule</h2>



<%
    List<Staff> staffList = StaffDAO.getAllStaff();
%>

<form action="SetScheduleServlet" method="post">
    <div class="form-group">
        <label for="staff_id">Staff Name:</label>
        <select name="staff_id" id="staff_id" required>
            <option value="">-- Select Staff --</option>
            <% for (Staff s : staffList) { %>
                <option value="<%= s.getStaffId() %>"><%= s.getStaffFullName() %></option>
            <% } %>
        </select>
    </div>

    <div class="form-group">
        <label for="work_date">Work Date:</label>
        <input type="date" name="work_date" id="work_date" required>
    </div>

    <div class="form-group">
        <label for="start_time">Start Time:</label>
        <input type="time" name="start_time" id="start_time" required>
    </div>

    <div class="form-group">
        <label for="end_time">End Time:</label>
        <input type="time" name="end_time" id="end_time" required>
    </div>

    <input type="submit" value="Create Schedule">
    <div class="text-center my-3">
    <a href="SetScheduleServlet" class="btn btn-secondary">Back</a>
</div>

</form>
</body>
</html>