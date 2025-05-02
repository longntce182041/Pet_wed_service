<!-- filepath: /Users/tranphantrungkien/Ky5/SWP391/PetiqueSpa/web/OrderManagement.jsp -->
<%@ page import="java.util.*, DAO.OrderDAO, Model.Order" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/bootstrap-datepicker.css">
    <link rel="stylesheet" href="css/jquery.timepicker.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="./css/toast.css">
    <%@include file="popUpMessage.jsp" %>
</head>
<body>
    <div class="container">
        <h1 class="text-center my-4">Order Management</h1>
        <div class="text-right mb-3">
            <button id="viewRevenueReportBtn" class="btn btn-primary">View Revenue Report</button>
            <button id="backToOrdersBtn" class="btn btn-secondary" style="display: none;">Back to Orders</button>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div id="orderTableContainer">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Total Amount</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="orderTableBody">
                            <%
                                Locale localeVN = new Locale("vi", "VN");
                                NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
                                OrderDAO orderDAO = new OrderDAO();
                                List<Order> orderList = orderDAO.getAllOrders();
                                for (Order order : orderList) {
                            %>
                            <tr id="orderRow<%= order.getOrderId() %>">
                                <td><%= order.getOrderId()%></td>
                                <td><%= order.getOrderDate()%></td>
                                <td><%= currencyVN.format(order.getTotalPrice()) %></td> <!-- Hiển thị tiền tệ VNĐ -->
                                <td><%= order.getName() %></td>
                                <td><%= order.getPhone() %></td>
                                <td><%= order.getEmail() %></td>
                                <td id="orderStatus<%= order.getOrderId() %>"><%= order.getStatus()%></td>
                                <td>
                                    <button class="btn btn-success mt-2" onclick="updateOrderStatus(<%= order.getOrderId() %>, 'Complete')">Complete</button>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>

                <div id="revenueReportContainer" style="display: none;">
                    <!-- Báo cáo doanh thu sẽ được load vào đây -->
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
        function updateOrderStatus(orderId, status) {
            $.ajax({
                url: 'UpdateOrderStatusServlet',
                method: 'POST',
                data: {
                    orderId: orderId,
                    status: status
                },
                success: function(response) {
                    if (response.success) {
                        // Cập nhật trạng thái đơn hàng trên giao diện
                        $('#orderStatus' + orderId).text(status);

                        // Hiển thị thông báo thành công
                        showSuccessToast(response.message);
                    } else {
                        // Hiển thị thông báo lỗi
                        showErrorToast(response.message);
                    }
                },
                error: function() {
                    // Hiển thị thông báo lỗi nếu xảy ra lỗi kết nối
                    showErrorToast('Failed to connect to the server. Please try again.');
                }
            });
        }

        $(document).ready(function () {
            // Xử lý sự kiện khi nhấn nút "View Revenue Report"
            $('#viewRevenueReportBtn').click(function () {
                $.ajax({
                    url: 'RevenueReportServlet', // Load nội dung của RevenueReport.jsp
                    method: 'GET',
                    success: function (data) {
                        $('#orderTableContainer').hide(); // Ẩn bảng quản lý đơn hàng
                        $('#revenueReportContainer').html(data).show(); // Hiển thị báo cáo doanh thu
                        $('#viewRevenueReportBtn').hide(); // Ẩn nút "View Revenue Report"
                        $('#backToOrdersBtn').show(); // Hiển thị nút "Back to Orders"
                    },
                    error: function () {
                        alert('Failed to load revenue report.');
                    }
                });
            });

            // Xử lý sự kiện khi nhấn nút "Back to Orders"
            $('#backToOrdersBtn').click(function () {
                $('#revenueReportContainer').hide(); // Ẩn báo cáo doanh thu
                $('#orderTableContainer').show(); // Hiển thị bảng quản lý đơn hàng
                $('#backToOrdersBtn').hide(); // Ẩn nút "Back to Orders"
                $('#viewRevenueReportBtn').show(); // Hiển thị nút "View Revenue Report"
            });
        });
    </script>
</body>
</html>