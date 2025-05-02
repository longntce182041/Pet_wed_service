<%-- 
    Document   : statisticsReports
    Created on : Mar 5, 2025, 1:28:15 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Rating" %>
<html>
<head>
    <title>Statistics and Reports</title>
        <link rel="stylesheet" type="text/css" href="css/promotionCss.css">
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/animate.css">
        <link rel="stylesheet" href="css/owl.carousel.min.css">
        <link rel="stylesheet" href="css/owl.theme.default.min.css">
        <link rel="stylesheet" href="css/magnific-popup.css">
        <link rel="stylesheet" href="css/bootstrap-datepicker.css">
        <link rel="stylesheet" href="css/jquery.timepicker.css">
        <link rel="stylesheet" href="css/flaticon.css">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/Style_admin_dashboard.css">
        <link rel="stylesheet" href="css/Style_manageProduct.css">>
    </head>
    <body>
        <!-- New Navbar with Icon and Search Box -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="#"><i class="fa fa-dashboard"></i> Dashboard</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <form class="form-inline my-2 my-lg-0">
                    <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                </form>
            </div>
        </nav>
        <div class="wrapper d-flex align-items-stretch">
            <nav id="sidebar">
                <div class="p-4 pt-5">
                    <a class="navbar-brand" href="index.jsp"><span class="flaticon-pawprint-1 mr-2"></span>Petique Spa</a>
                    <ul class="list-unstyled components mb-5">
                        <li>
                            <a href="admin_Dashboard.jsp">Dashboard</a>
                        </li>
                        <li>
                            <a href="ViewProductServlet">Manage Products</a>
                        </li>
                        <li>
                            <a href="ManageCategories.jsp">Manage Categories</a>
                        </li>
                        <li>
                            <a href="ManageOrders.jsp">Manage Orders</a>
                        </li>
                        <li>
                            <a href="ManageUsers.jsp">Manage Users</a>
                        </li>
                        <li>
                            <a href="ManageStaffServlet">Manage Staff</a>
                        </li>
                        <li>
                            <a href="PromotionServlet">Manage Promotion</a>
                        </li>
                        <li>
                            <a href="AppointmentScheduleServlet">Manage Appointment</a>
                        </li>
                        <li class="active">
                            <a href="StatisticsServlet">Statistics</a>
                        </li>
                    </ul>
                </div>
            </nav>
            <div id="content" class="p-4 p-md-5">
                <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                    <a class="navbar-brand" href="#">Admin Dashboard</a>
                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav ml-auto">
                            <li class="nav-item">
                                <a class="nav-link" href="profile.jsp"><i class="fa fa-user"></i> Profile</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="logout.jsp"><i class="fa fa-sign-out"></i> Logout</a>
                            </li>
                        </ul>
                    </div>
                </nav>
        <div class="container mt-4">
        <h2 class="mb-4 text-dark">Customer Ratings Summary</h2>

        <table class="table table-bordered table-striped table-hover table-green">
            <thead class="thead-success">
                <tr>
                    <th>ID</th>
                    <th>Customer Name</th>
                    <th>Product Name</th>
                    <th>Rating (Stars)</th>
                    <th>Comment</th>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        List<Rating> ratings = (List<Rating>) request.getAttribute("ratings");
                        if (ratings != null && !ratings.isEmpty()) {
                            for (Rating rating : ratings) {
                %>
                <tr>
                    <td><%= rating.getRatingId() %></td>
                    <td><%= (rating.getCustomerFullName() != null) ? rating.getCustomerFullName() : "Unknown" %></td>
                    <td><%= (rating.getProductName() != null) ? rating.getProductName() : "Unknown" %></td>
                    <td><%= rating.getRatingStar() %> ⭐</td>
                    <td><%= (rating.getComment() != null && !rating.getComment().isEmpty()) ? rating.getComment() : "No comment" %></td>
                </tr>
                <%
                            }
                        } else {
                %>
                <tr>
                    <td colspan="5" class="text-center text-muted">No ratings available.</td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                %>
                <tr>
                    <td colspan="5" class="text-center text-danger">Error loading data. Please try again later.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
