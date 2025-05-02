<%@ page import="java.util.List" %>
<%@ page import="Model.Promotion" %>
<%@ page import="Model.CustomerSendEmail" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Manage Promotion</title>
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
        <!-- Sidebar and Navbar -->
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
                        <li class="active"><a href="PromotionServlet">Manage Promotion</a></li>
                        <li><a href="AppointmentScheduleServlet">Manage Appointment</a></li>
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
                <%
                    // Lấy danh sách khuyến mãi từ request
                    List<Promotion> promotionList = (List<Promotion>) request.getAttribute("promotionList");
                %>
                <div class="container">
                    <h2 class="md-4">List Promotion</h2>
                    <a href="addPromotion.jsp" class="btn btn-primary">Add New Promotion</a>
                    <a href="SendPromotionEmailServlet" class="btn btn-primary">Send Email to Customer</a>
                    <div class="table-responsive" style="overflow-x: auto;">
                        <table class="table table-bordered table-striped" style="min-width: 1200px; width: 100%; table-layout: auto;">
                            <thead class="thead-dark">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Type</th>
                                    <th>Value</th>
                                    <th>StartDate</th>
                                    <th>EndDate</th>
                                    
                                    
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    if (promotionList != null && !promotionList.isEmpty()) {
                                        for (Promotion promo : promotionList) {
                                %>
                                <tr>
                                    <td><%= promo.getPromotionId() %></td>
                                    <td><%= promo.getPromotionName() %></td>
                                    <td><%= promo.getPromotionDescription() %></td>
                                    <td><%= promo.getDiscountType() %></td>
                                    <td><%= promo.getDiscountValue() %>%</td>
                                    <td><%= promo.getStartDate() %></td>
                                    <td><%= promo.getEndDate() %></td>
                                    
                                    
                                    <td><%= promo.isIsActive() ? "Active" : "Not Active" %></td>
                                    <td>
                                        <a href="UpdatePromotionServlet?id=<%= promo.getPromotionId() %>" class="btn btn-danger">Update</a>
                                        <a href="DeletePromotionServlet?id=<%= promo.getPromotionId() %>" class="btn btn-danger" onclick="return confirm('Confirm delete?');">Delete</a>
                                    </td>
                                </tr>
                                <%
                                        }
                                    } else {
                                %>
                                <tr>
                                    <td colspan="11">No promotion data!</td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
