<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Dashboard</title>
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
    <div class="container mt-5">
        <h2 class="text-center mb-4">Welcome to the Staff Dashboard</h2>
        <div class="row">
            <div class="col-md-3">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-body text-center">
                        <i class="fas fa-money-bill-wave fa-3x mb-3"></i>
                        <h5 class="card-title">Order Management</h5>
                        <a href="#" onclick="loadContent('OrderManagement.jsp')" class="btn btn-light">Go to Order Management</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-secondary mb-3">
                    <div class="card-body text-center">
                        <i class="fas fa-users fa-3x mb-3"></i>
                        <h5 class="card-title">Pet Management</h5>
                        <a href="#" onclick="loadContent('customer_service.jsp')" class="btn btn-light">Go to Pet Management</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-success mb-3">
                    <div class="card-body text-center">
                        <i class="fas fa-chart-line fa-3x mb-3"></i>
                        <h5 class="card-title">Revenue Report</h5>
                        <a href="#" onclick="loadContent('RevenueReportServlet')" class="btn btn-light">Go To Revenue Report</a>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-danger mb-3">
                    <div class="card-body text-center">
                        <i class="fas fa-warehouse fa-3x mb-3"></i>
                        <h5 class="card-title">Quantity Product</h5>
                        <a href="#" onclick="loadContent('product.jsp')" class="btn btn-light">Go to Quantity Product </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

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

        // Không tải nội dung mặc định khi trang được tải lần đầu
        $(document).ready(function () {
            // Không gọi loadContent ở đây
        });
    </script>
</body>
</html>