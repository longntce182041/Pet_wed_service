<%@ page import="java.util.*, Model.Order" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width-width, initial-scale=1.0">
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
    <style>
        .button-container {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
        }

        .save-button, .cancel-button, .delete-button {
            margin-right: 5px;
            margin-bottom: 5px;
        }

        .save-button, .cancel-button {
            width: 80px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="text-center my-4">Order Management</h1>
        <div class="row">
            <div class="col-md-12">
                <div class="d-flex justify-content-between mb-3">
                    <div>
                        <form id="filterForm" class="form-inline">
                            <label class="mr-2">Filter By Status:</label>
                            <select class="form-control form-control-sm mr-2" id="filterStatus" name="filterStatus">
                                <option value="">All Status</option>
                                <%
                                    List<String> statusOptions = Arrays.asList("Pending", "Pending Pickup", "Shipping", "Complete", "Cancel");
                                    for (String status : statusOptions) {
                                %>
                                        <option value="<%= status %>"><%= status.equals("Pending Pickup") ? "Pending Pickup" : status %></option>
                                <% } %>
                            </select>
                            <button type="submit" class="btn btn-primary btn-sm">Filter</button>
                        </form>
                    </div>
                </div>
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
                    </tbody>
                </table>
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
        function updateOrderStatus(orderId, specificStatus) {
            var status;
            if (specificStatus) {
                status = specificStatus;
            } else {
                status = $('#statusSelect' + orderId).val();
            }

            $.ajax({
                url: 'UpdateOrderStatusServlet',
                method: 'POST',
                data: {
                    orderId: orderId,
                    status: status
                },
                dataType: 'json',
                success: function(response) {
                    if (response.type === 'success') {
                        // Cập nhật trực tiếpDOM
                        $('#orderStatus' + orderId).text(status);
                        var isCompleteOrCancel = (status === 'Complete' || status === 'Cancel');
                        $('#statusSelect' + orderId).prop('disabled', isCompleteOrCancel);
                        $('.save-button[onclick="updateOrderStatus(' + orderId + ')"]').toggle(!isCompleteOrCancel);
                        $('.cancel-button[onclick="updateOrderStatus(' + orderId + ', \'Cancel\')"]').prop('disabled', status === 'Complete' || status === 'Cancel').toggle(!isCompleteOrCancel);
                    } else if (response.type === 'error') {
                        alert(response.message); // Hiển thị thông báo lỗi
                    }
                    $('#orderStatus' + orderId).data('previous-status', status);
                },
                error: function() {
                    alert('Error updating order status.');
                }
            });
        }

        function deleteOrder(orderId) {
            if (confirm('Are you sure you want to delete this order?')) {
                $.ajax({
                    url: 'DeleteOrderServlet',
                    method: 'POST',
                    data: {
                        orderId: orderId
                    },
                    success: function(response) {
                        if (response.includes('delete success')) {
                            $('#orderRow' + orderId).remove();
                        } else {
                            alert(response);
                        }
                    },
                    error: function() {
                        alert('Failed to delete order.');
                    }
                });
            }
        }

        $(document).ready(function() {
            $.ajax({
                url: 'FilterOrderByStatusServlet',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    updateOrderTable(data);
                },
                error: function(xhr, status, error) {
                    console.error('Error:', status, error);
                    alert('An error occurred while loading the orders.');
                }
            });

            $('#filterForm').on('submit', function(e) {
                e.preventDefault();
                var status = $('#filterStatus').val();
                $.ajax({
                    url: 'FilterOrderByStatusServlet',
                    type: 'GET',
                    data: {
                        status: status
                    },
                    dataType: 'json',
                    success: function(data) {
                        updateOrderTable(data);
                    },
                    error: function(xhr, status, error) {
                        console.error('Error', status, error);
                        alert('Error when filtering orders.');
                    }
                });
            });
        });

        function updateOrderTable(orders) {
            var orderTableBody = $('#orderTableBody');
            orderTableBody.empty();
            if (orders.length === 0) {
                orderTableBody.append('<tr><td colspan="8" class="text-center">No orders exist.</td></tr>');
                return;
            }

            var html = '';
            for (var i = 0; i < orders.length; i++) {
                var order = orders[i];
                html += '<tr id="orderRow' + order.orderId + '">';
                html += '<td>' + order.orderId + '</td>';
                html += '<td>' + new Date(order.orderDate).toLocaleString() + '</td>';
                html += '<td>' + order.totalPrice + '</td>';
                html += '<td>' + order.name + '</td>';
                html += '<td>' + order.phone + '</td>';
                html += '<td>' + order.email + '</td>';
                html += '<td id="orderStatus' + order.orderId + '">' + order.status + '</td>';
                html += '<td>';
                html += '<div class="button-container">'; // Thêm div bao ngoài
                html += '<select class="form-control form-control-sm mb-2 status-select" id="statusSelect' + order.orderId + '" ' + (order.status === 'Complete' || order.status === 'Cancel' ? 'disabled' : '') + '>';
                var statusOptions = ["Pending Pickup", "Shipping", "Complete", "Cancel"];
                statusOptions.forEach(function(option) {
                    html += '<option value="' + option + '" ' + (order.status === option ? 'selected' : '') + '>' + (option === 'Pending Pickup' ? 'Pending Pickup' : option) + '</option>';
                });
                html += '</select>';
                html += '<button class="btn btn-primary btn-sm save-button" onclick="updateOrderStatus(' + order.orderId + ')"' + (order.status === 'Complete' || order.status === 'Cancel' ? ' style="display:none;"' : '') + '>Save</button>';
                html += '<button class="btn btn-danger btn-sm cancel-button" onclick="updateOrderStatus(' + order.orderId + ', \'Cancel\')"' + (order.status === 'Complete' ? ' disabled' : (order.status === 'Cancel' ? ' disabled style="display:none;"' : '')) + '>Cancel</button>';
                html += '<button class="btn btn-danger btn-sm delete-button" onclick="deleteOrder(' + order.orderId + ')">Delete</button>';
                html += '</div>'; // Đóng div
                html += '</td>';
                html += '</tr>';
            }
            orderTableBody.html(html);

            $('.status-select').each(function() {
                var orderId = this.id.replace('statusSelect', '');
                $('#orderStatus' + orderId).data('previous-status', $(this).val());
            });
        }

        function updateActionButtons(orderId, status) {
            var selectElement = $('#statusSelect' + orderId);
            var saveButton = $('.save-button[onclick="updateOrderStatus(' + orderId + ')"]');
            var cancelButton = $('.cancel-button[onclick="updateOrderStatus(' + orderId + ', \'Cancel\')"]');
            var isCompleteOrCancel = (status === 'Complete' || status === 'Cancel');

            selectElement.prop('disabled', isCompleteOrCancel);
            saveButton.toggle(!isCompleteOrCancel);
            cancelButton.prop('disabled', status === 'Complete' || status === 'Cancel').toggle(!isCompleteOrCancel);
        }

        $(document).ready(function() {
            $('.status-select').each(function() {
                var orderId = this.id.replace('statusSelect', '');
                var currentStatus = $('#orderStatus' + orderId).text();
                $('#orderStatus' + orderId).data('previous-status', $(this).val());
                updateActionButtons(orderId, currentStatus);
            });
        });
    </script>
</body>
</html>
