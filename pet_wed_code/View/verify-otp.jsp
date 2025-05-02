<%-- 
    Document   : verify-otp
    Created on : Mar 1, 2025, 7:40:00 PM
    Author     : TruongMinhDan CE181520
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify OTP</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h3 class="text-center">Verify OTP</h3>
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <form action="VerifyOTPServlet" method="post">
                            <input type="hidden" name="email" value="<%= request.getParameter("email") %>">
                            <div class="mb-3">
                                <label for="otp" class="form-label">Enter OTP</label>
                                <input type="text" class="form-control" id="otp" name="otp" placeholder="Enter 6-digit OTP" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Verify OTP</button>
                        </form>
                        <a href="forgot-password.jsp" class="btn btn-secondary w-100 mt-3">Back</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
