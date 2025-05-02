<%@ page import="java.util.*, Model.Order" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="header.jsp" />

<div class="container mt-5">
    <!-- Breadcrumb Navigation -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="Shop.jsp">Shop</a></li>
            <li class="breadcrumb-item active" aria-current="page">Order History</li>
        </ol>
    </nav>

    <h1 class="text-center my-4">Order History</h1>

    <%
        List<Order> orders = (List<Order>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <p class="text-center">You have no orders yet.</p>
    <%
        } else {
    %>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Order Date</th>
                    <th>Total Price (VNĐ)</th>
                    <th class="status-column">Status</th>
                    <th>Details</th>
                    <th class="actions-column">Actions</th> <!-- Thêm cột Actions -->
                </tr>
            </thead>
            <tbody>
                <%
                    for (Order order : orders) {
                %>
                <tr>
                    <td><%= order.getOrderId() %></td>
                    <td><%= order.getOrderDate() %></td>
                    <td><%= String.format("%,.0f", order.getTotalPrice()) %></td>
                    <td class="status-column"><%= order.getStatus() %></td>
                    <td><a href="OrderDetailsServlet?orderId=<%= order.getOrderId() %>" class="btn btn-primary btn-sm">View</a></td>
                    <td class="actions-column">
                        <% if ("Pending".equalsIgnoreCase(order.getStatus())) { %>
                            <button class="btn btn-danger btn-sm cancel-order-btn" data-order-id="<%= order.getOrderId() %>">Cancel</button>
                        <% } else { %>
                            <button class="btn btn-secondary btn-sm" disabled>Cannot Cancel</button>
                        <% } %>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        }
    %>
</div>

<!-- Modal nhập lý do hủy -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" aria-labelledby="cancelOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cancelOrderModalLabel">Cancel Order</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="cancelOrderForm">
                    <input type="hidden" id="modalOrderId" name="orderId">
                    <div class="mb-3">
                        <label for="cancelReason" class="form-label">Reason for cancellation</label>
                        <textarea class="form-control" id="cancelReason" name="reason" rows="3" required></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                
                <button type="button" class="btn btn-danger" id="confirmCancelOrder">Confirm Cancel</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script>
    $(document).ready(function () {
        let selectedOrderId = null;

        // Khi nhấn nút "Cancel", mở modal
        $('.cancel-order-btn').click(function () {

            console.log("Cancel button clicked!"); // Log để kiểm tra sự kiện click
            selectedOrderId = $(this).data('order-id'); // Lấy orderId từ nút
            $('#modalOrderId').val(selectedOrderId); // Gán orderId vào modal
            $('#cancelOrderModal').modal('show'); // Hiển thị modal
        });

        // Khi nhấn nút "Confirm Cancel", gửi yêu cầu AJAX
        $('#confirmCancelOrder').click(function () {
            const orderId = $('#modalOrderId').val();
            const reason = $('#cancelReason').val();

            if (!reason.trim()) {
                alert("Please provide a reason for cancellation.");
                return;
            }

            cancelOrder(orderId, reason);
        });
    });

    function cancelOrder(orderId, reason) {
        $.ajax({
            url: 'CancelOrderServlet',
            type: 'POST',
            data: { orderId: orderId, reason: reason },
            success: function (response) {
                if (response.success) {
                    // Tải lại trang sau khi hủy đơn hàng thành công
                    alert("Order canceled successfully!");
                    location.reload(); // Tải lại trang
                } else {
                    alert(response.message); // Hiển thị thông báo lỗi nếu cần
                }
            },
            error: function () {
                alert("Failed to cancel the order. Please try again."); // Hiển thị thông báo lỗi nếu cần
            }
        });
    }
</script>

