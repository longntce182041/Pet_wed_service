<%-- 
    Document   : appointmentSchedule
    Created on : Apr 21, 2025, 9:00:52 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.AppointmentSchedule" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>Appointment Schedule</title>
    <link rel="stylesheet" type="text/css" href="css/promotionCss.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/bootstrap-datepicker.css">
    <link rel="stylesheet" href="css/jquery.timepicker.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/Style_admin_dashboard.css">
    <link rel="stylesheet" href="css/Style_manageProduct.css">
</head>
<body>
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
                    <li><a href="admin_Dashboard.jsp">Dashboard</a></li>
                    <li><a href="ViewProductServlet">Manage Products</a></li>
                    <li><a href="ManageCategories.jsp">Manage Categories</a></li>
                    <li><a href="ManageOrders.jsp">Manage Orders</a></li>
                    <li><a href="ManageUsers.jsp">Manage Users</a></li>
                    <li><a href="ManageStaffServlet">Manage Staff</a></li>
                    <li><a href="PromotionServlet">Manage Promotion</a></li>
                    <li class="active"><a href="AppointmentScheduleServlet">Manage Appointment</a></li>
                    <li><a href="StatisticsServlet">Statistics</a></li>
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
                        <li class="nav-item"><a class="nav-link" href="profile.jsp"><i class="fa fa-user"></i> Profile</a></li>
                        <li class="nav-item"><a class="nav-link" href="logout.jsp"><i class="fa fa-sign-out"></i> Logout</a></li>
                    </ul>
                </div>
            </nav>
            <h2 class="md-4">Appointment Schedule</h2>
            <table class="table table-bordered table-striped">
                <tr>
                    <th>Booking ID</th>
                    <th>Customer Name</th>
                    <th>Pet Name</th>
                    <th>Booking Date</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="s" items="${scheduleList}">
                    <tr>
                        <td>${s.servicebookingId}</td>
                        <td>${s.customerFullName}</td>
                        <td>${s.petName}</td>
                        <td>${s.servicebookingDate}</td>
                        <td>${s.servicebookingTime}</td>
                        <td>${s.status}</td>
                        <td>
                            <form action="AppointmentScheduleServlet" method="post" style="display:inline;">
                                <input type="hidden" name="servicebookingId" value="${s.servicebookingId}" />
                                <input type="hidden" name="action" value="confirm" />
                                <c:if test="${s.status eq 'Pending'}">
                                    <input type="submit" value="Confirm" />
                                </c:if>
                            </form>

                            <form action="AppointmentScheduleServlet" method="post" style="display:inline;">
                                <input type="hidden" name="servicebookingId" value="${s.servicebookingId}" />
                                <input type="hidden" name="action" value="cancel" />
                                <c:if test="${s.status eq 'Pending'}">
                                    <input type="submit" value="Cancel" />
                                </c:if>
                            </form>

                            <form action="SetScheduleServlet" method="get" style="display:inline;">
                                <input type="hidden" name="servicebookingId" value="${s.servicebookingId}" />
                                <c:if test="${s.status eq 'Confirmed'}">
                                    <input type="submit" value="Set Schedule" />
                                </c:if>
                            </form>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</body>
</html>
