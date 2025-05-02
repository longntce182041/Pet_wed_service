<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%
    String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Staff Dashboard</title>
        <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/bootstrap-datepicker.css">
    <link rel="stylesheet" href="css/jquery.timepicker.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/staffdashboard.css">
    </head>
    <body>
        <!-- New Navbar with Icon -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand fa fa-paw"href="Staffdashboard.jsp">Staff Dashboard</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="profile.jsp"><i class="fa fa-user"></i> Profile</a>
                        <% if ("staff".equalsIgnoreCase(role)) { %>
                        <a class="nav-link" href="changePassword.jsp"><i class="fa fa-key"></i> Change Password</a>
                        <% }%>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="index.jsp"><i class="fa fa-sign-out"></i> Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
        <div class="wrapper d-flex align-items-stretch">
            <nav id="sidebar" class="bg-dark">
                <div class="p-4 pt-5">
                    <ul class="list-unstyled components mb-5" id="sidebarMenu">
                        <li class="active">
                            <a href="#" onclick="loadContent('dashboard.jsp', this)"><i class="fa fa-tachometer"></i> Dashboard</a>
                        </li>
                        <li>
                            <a href="#" onclick="loadContent('OrderManagement.jsp', this)"><i class="fa fa-money"></i> Order Management</a>
                        </li>
                        <li>
                            <a href="#" onclick="loadContent('customer_service.jsp', this)"><i class="fa fa-users"></i> Customer Service Functions</a>
                        </li>
                        <li>
                            <a href="#" onclick="loadContent('RevenueReportServlet', this)"><i class="fa fa-chart-line"></i> Revenue Report</a>
                        </li>
                        <li>
                            <a href="#" onclick="loadContent('product.jsp', this)"><i class="fas fa-warehouse"></i> Quantity Product</a>
                        </li>
              
                    </ul>
                </div>
            </nav>

            <!-- Page Content  -->
            <div id="content" class="p-4 p-md-5">
                <div id="main-content" class="content-box">
                    <!-- Nội dung sẽ được tải vào đây -->
                </div>
            </div>
        </div>

        <footer class="bg-dark text-white text-center py-3">
            <p>&copy; 2025 Petique Spa. All Rights Reserved.</p>
        </footer>

        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
        <script src="js/jquery-migrate-3.0.1.min.js"></script>
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.easing.1.3.js"></script>
        <script src="js/jquery.waypoints.min.js"></script>
        <script src="js/jquery.stellar.min.js"></script>
        <script src="js/jquery.animateNumber.min.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        <script src="js/jquery.timepicker.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/scrollax.min.js"></script>
        <script src="js/main.js"></script>
        <script>
            function loadContent(page, element) {
                $.ajax({
                    url: page,
                    method: 'GET',
                    success: function (data) {
                        $('#main-content').html(data);
                        // Loại bỏ lớp 'active' khỏi tất cả các mục
                        $('#sidebarMenu li').removeClass('active');
                        // Thêm lớp 'active' vào mục hiện tại
                        $(element).closest('li').addClass('active');
                    },
                    error: function () {
                        $('#main-content').html('<p>Error loading content.</p>');
                    }
                });
            }

            // Tải nội dung Dashboard khi trang được tải lần đầu
            $(document).ready(function () {
                loadContent('dashboard.jsp', $('#sidebarMenu li.active a'));
            });
        </script>
    </body>
</html>s