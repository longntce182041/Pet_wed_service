<%@ page import="java.util.*, Model.Order, Model.OrderDetail" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="DAO.RatingProductDAO" %>
<%@ page import="Model.OrderDetail" %>
<%@ page import="java.util.List" %>
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/shop.css"> <!-- Liên kết tệp CSS mới -->
<style>
    /* Cải tiến giao diện phần rating */
    .rating {
        display: flex;
        flex-direction: row-reverse;
        justify-content: flex-start;
        gap: 5px; /* Khoảng cách giữa các sao */
    }
    .rating input {
        display: none;
    }
    .rating label {
        font-size: 2rem; /* Tăng kích thước sao */
        color: #ddd;
        cursor: pointer;
        transition: color 0.2s ease-in-out;
    }
    .rating input:checked ~ label {
        color: #ffc107; /* Màu vàng cho sao được chọn */
    }
    .rating label:hover,
    .rating label:hover ~ label {
        color: #ffc107; /* Hiệu ứng hover */
    }
    .rating-container {
        display: flex;
        flex-direction: column;
        gap: 10px; /* Khoảng cách giữa rating và textarea */
    }
    .rating-container textarea {
        resize: none; /* Không cho phép thay đổi kích thước textarea */
        height: 80px; /* Chiều cao cố định */
    }
    .rating-container button {
        align-self: flex-start; /* Căn nút về bên trái */
    }
</style>

<%
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    Order order = (Order) request.getAttribute("order");
    List<OrderDetail> orderDetails = (List<OrderDetail>) request.getAttribute("orderDetails");
%>

<div class="container mt-5">
    <!-- Breadcrumb Navigation -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="Shop.jsp">Shop</a></li>
            <li class="breadcrumb-item"><a href="OrderHistoryServlet">Order History</a></li>
            <li class="breadcrumb-item active" aria-current="page">Order Details</li>
        </ol>
    </nav>

    <!-- Header -->
    <div class="text-center mb-4">
        <h1 class="display-5">Order Details</h1>
        <p class="text-muted">Below are the details of your order.</p>
    </div>

    <!-- Order Summary -->
    <div class="row">
        <div class="col-md-8">
            <h4 class="mb-3">Products</h4>
            <table class="table table-bordered">
                <thead class="table-light">
                    <tr>
                        <th>Image</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price (VNĐ)</th>
                        
                    </tr>
                </thead>
                <tbody>
                    <%
                        double total = 0;
                        for (OrderDetail detail : orderDetails) {
                            double itemTotal = detail.getPrice() * detail.getQuantity();
                            total += itemTotal;
                    %>
                    <tr>
                        <td>
                            <img src="<%= detail.getProductImage() %>" alt="<%= detail.getProductName() %>" class="img-thumbnail" style="width: 80px; height: 80px; object-fit: cover;">
                        </td>
                        <td><%= detail.getProductName() %></td>
                        <td><%= detail.getQuantity() %></td>
                        <td><%= currencyVN.format(detail.getPrice()) %></td>
                        
                    </tr>
                    <%
                        }
                    %>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="3" class="text-end">Subtotal (VNĐ):</th>
                        <th>
                            <%
                                double subtotal = 0;
                                for (OrderDetail detail : orderDetails) {
                                    subtotal += detail.getPrice() * detail.getQuantity();
                                }
                                out.print(currencyVN.format(subtotal));
                            %>
                        </th>
                    </tr>
                    <tr>
                        <th colspan="3" class="text-end">Shipping Fee (VNĐ):</th>
                        <th><%= currencyVN.format(30_000) %></th> <!-- Phí vận chuyển mặc định -->
                    </tr>
                    <tr>
                        <th colspan="3" class="text-end">Promotion Code:</th>
                        <th><%= order.getPromotionId() != null ? order.getPromotionId() : "None" %></th>
                    </tr>
                    <tr>
                        <th colspan="3" class="text-end">Discount (VNĐ):</th>
                        <th><%= currencyVN.format(order.getDiscountValue()) %></th>
                    </tr>
                    <tr class="table-success">
                        <th colspan="3" class="text-end">Total (VNĐ):</th>
                        <th><%= currencyVN.format(order.getTotalPrice()) %></th>
                    </tr>
                </tfoot>
            </table>

            <!-- Rating Table -->
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Rating</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        RatingProductDAO ratingDAO = new RatingProductDAO();
                        String customerId = (String) session.getAttribute("customer_id");
                    %>
                    <% for (OrderDetail detail : orderDetails) { %>
                    <tr>
                        <td>
                            <%= detail.getProductName() %> <!-- Hiển thị tên sản phẩm -->
                        </td>
                        <td>
                            <%= detail.getQuantity() %>
                        </td>
                        <td>
                            <%= currencyVN.format(detail.getPrice()) %>
                        </td>
                        <td>
                            <% 
                                // Kiểm tra trạng thái đơn hàng
                                if ("Complete".equalsIgnoreCase(order.getStatus())) {
                                    // Kiểm tra nếu người dùng đã đánh giá sản phẩm trong đơn hàng này
                                    boolean hasRated = ratingDAO.hasRated(detail.getOrderDetailId(), customerId);
                                    if (hasRated) {
                            %>
                                        <p class="text-success">Already Rated</p>
                                    <% } else { %>
                                        <!-- Form đánh giá -->
                                        <form class="ratingForm">
                                            <input type="hidden" name="customerId" value="<%= customerId %>">
                                            <input type="hidden" name="orderDetailId" value="<%= detail.getOrderDetailId() %>">
                                            <div class="rating-container">
                                                <div class="rating">
                                                    <input type="radio" id="star5-<%= detail.getOrderDetailId() %>" name="rating" value="5" required>
                                                    <label for="star5-<%= detail.getOrderDetailId() %>">★</label>
                                                    <input type="radio" id="star4-<%= detail.getOrderDetailId() %>" name="rating" value="4">
                                                    <label for="star4-<%= detail.getOrderDetailId() %>">★</label>
                                                    <input type="radio" id="star3-<%= detail.getOrderDetailId() %>" name="rating" value="3">
                                                    <label for="star3-<%= detail.getOrderDetailId() %>">★</label>
                                                    <input type="radio" id="star2-<%= detail.getOrderDetailId() %>" name="rating" value="2">
                                                    <label for="star2-<%= detail.getOrderDetailId() %>">★</label>
                                                    <input type="radio" id="star1-<%= detail.getOrderDetailId() %>" name="rating" value="1">
                                                    <label for="star1-<%= detail.getOrderDetailId() %>">★</label>
                                                </div>
                                                <textarea id="comment" name="comment" required placeholder="Leave a comment..."></textarea>
                                                <button type="submit" class="btn btn-primary">Submit</button>
                                            </div>
                                        </form>
                                    <% } %>
                                <% } else { %>
                                    <p class="text-danger">Rating is only available for complete orders.</p>
                                <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Customer Details -->
        <div class="col-md-4">
            <h4 class="mb-3">Customer Details</h4>
            <ul class="list-group">
                <li class="list-group-item">
                    <strong>Name:</strong> <span><%= order.getName() %></span>
                </li>
                <li class="list-group-item">
                    <strong>Address:</strong> <span><%= order.getAddress() %></span>
                </li>
                <li class="list-group-item">
                    <strong>Phone:</strong> <span><%= order.getPhone() %></span>
                </li>
                <li class="list-group-item">
                    <strong>Email:</strong> <span><%= order.getEmail() %></span>
                </li>
                <li class="list-group-item">
                    <strong>Payment Method:</strong> <span><%= order.getPaymentMethod() %></span>
                </li>
            </ul>
        </div>
    </div>

    <!-- Back to Order History -->
    <div class="text-center mt-4">
        <a href="OrderHistoryServlet" class="btn btn-primary btn-lg">Back to Order History</a>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        // Gắn sự kiện submit cho tất cả các form có class "ratingForm"
        $('.ratingForm').on('submit', function (e) {
            e.preventDefault(); // Ngăn chặn hành vi mặc định của form

            const form = $(this); // Lấy form hiện tại
            const formData = form.serialize(); // Lấy dữ liệu từ form

            $.ajax({
                url: 'RatingProductServlet',
                type: 'POST',
                data: formData,
                success: function (response) {
                    if (response.status === 'success') {
                        // Cập nhật giao diện: Thay form bằng thông báo "Already Rated"
                        form.replaceWith('<p class="text-success">Already Rated</p>');
                    } else {
                        // Cập nhật giao diện: Thay form bằng thông báo lỗi
                        form.replaceWith('<p class="text-danger">Failed to submit rating</p>');
                    }
                },
                error: function () {
                    // Cập nhật giao diện: Thay form bằng thông báo lỗi hệ thống
                    form.replaceWith('<p class="text-danger">An error occurred while submitting the rating.</p>');
                }
            });
        });
    });
</script>

<jsp:include page="footer.jsp" />