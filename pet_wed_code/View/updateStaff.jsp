<%-- 
    Document   : updateStaff
    Created on : Mar 19, 2025
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="Model.Staff" %>

<html>
<head>
    <title>Update Staff</title>
    <link rel="stylesheet" href="./css/updateStaff.css">
</head>

<body>
    <h2>Update Staff</h2>

    <%
        Staff staff = (Staff) request.getAttribute("staff");

        if (staff != null) {
    %>

    <form action="ManageStaffServlet" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="staff_id" value="<%= staff.getStaffId() %>">

        <label>Full Name:</label>
        <input type="text" name="full_name" value="<%= staff.getStaffFullName() %>" required>

        <label>Position:</label>
        <input type="text" name="position" value="<%= staff.getPosition() %>" required>

        <label>Phone:</label>
        <input type="text" name="phone" value="<%= staff.getStaffPhone() %>" required>

        <label>User ID:</label>
        <input type="text" name="user_id" value="<%= staff.getUserId() %>" required>

        <label>Email:</label>
        <input type="email" name="email" value="<%= staff.getStaffEmail() %>">

        <label>Password:</label>
        <input type="password" name="password" value="<%= staff.getPassword() %>">

        <label>Image URL:</label>
        <input type="text" name="img" id="imgInput" value="<%= staff.getStaffImg() %>" oninput="previewImage()">

        <!-- Hiển thị ảnh preview -->
        <img id="preview" src="<%= staff.getStaffImg() %>" alt="Image Preview">

        <input type="submit" value="Update Staff" class="btn-submit">
    </form>

    <a href="ManageStaffServlet" class="back-link">Back</a>

    <%
        } else {
    %>
        <p>No staff information available.</p>
    <%
        }
    %>

    <script>
        function previewImage() {
            var imgUrl = document.getElementById("imgInput").value;
            document.getElementById("preview").src = imgUrl;
        }
    </script>
</body>
</html>
