<%-- 
    Document   : addStaff
    Created on : Mar 2, 2025, 12:32:58 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Add Staff</title>
    <link rel="stylesheet" href="./css/updateStaff.css">
</head>
<body>
    <h2>Add New Staff</h2>
    <form action="ManageStaffServlet" method="post">
        <input type="hidden" name="action" value="add">

        <label>Staff ID:</label>
        <input type="text" name="staff_id" required><br>

        <label>Full Name:</label>
        <input type="text" name="full_name" required><br>

        <label>Position:</label>
        <input type="text" name="position" required><br>

        <label>Phone:</label>
        <input type="text" name="phone" required><br>

        <label>User ID:</label>
        <input type="text" name="user_id" required><br>

        <label>Email:</label>
        <input type="email" name="email"><br>

        <label>Password:</label>
        <input type="password" name="password"><br>

        <label>Image:</label>
        <input type="text" name="img"><br>

        <input type="submit" value="Add Staff" class="btn btn-add">
        <a href="ManageStaffServlet" class="btn btn-cancel">Back</a>
    </form>
</body>
</html>
