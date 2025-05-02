<%@page import="Model.Order"%>
<%@ page import="java.util.*, DAO.ProductDAO, Model.Product, Model.OrderDetail, DAO.OrderDAO" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/OrderConfirmation.css">

<%
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    // Lấy danh sách sản phẩm được chọn từ request
    String[] selectedProductIds = request.getParameterValues("selectedProducts");
    if (selectedProductIds == null || selectedProductIds.length == 0) {
        response.sendRedirect("Cart.jsp");
        return;
    }

    Integer orderId = (Integer) request.getAttribute("orderId");
    if (orderId == null) {
        response.sendRedirect("Shop.jsp");
        return;
    }

    OrderDAO orderDAO = new OrderDAO();
    Order order = orderDAO.getOrderById(orderId);
    if (order == null) {
        response.sendRedirect("Shop.jsp");
        return;
    }

    List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
    ProductDAO productDAO = new ProductDAO();

    // Lọc các sản phẩm được chọn
    List<OrderDetail> selectedOrderDetails = new ArrayList<>();
    for (OrderDetail detail : orderDetails) {
        for (String productId : selectedProductIds) {
            if (detail.getProductId() == Integer.parseInt(productId)) {
                selectedOrderDetails.add(detail);
            }
        }
    }
%>

<div class="containerOrder">
    <div class="confirmation-header">
        <h1>Order Confirmation</h1>
        <p>Thank you for your order! Your order has been placed successfully.</p>
    </div>

    <div class="order-details">
        <h4>Order Details</h4>
        <ul class="list-group">
            <li class="list-group-item">
                <strong>Order ID:</strong> <span><%= order.getOrderId() %></span>
            </li>
            <li class="list-group-item">
                <strong>Order Date:</strong> <span><%= order.getOrderDate() %></span>
            </li>
            <%
                double total = 0;
                for (OrderDetail detail : selectedOrderDetails) {
                    Product product = productDAO.getProductById(detail.getProductId());
                    double itemTotal = detail.getPrice() * detail.getQuantity();
                    total += itemTotal;
            %>
            <li class="list-group-item">
                <div class="product-info">
                    <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" class="product-image">
                    <div>
                        <h6><%= product.getName() %></h6>
                        <small>Quantity: <%= detail.getQuantity() %></small>
                    </div>
                </div>
                <span class="price"><%= currencyVN.format(itemTotal) %></span>
            </li>
            <%
                }
            %>
            <li class="list-group-item total">
                <strong>Subtotal (VNĐ):</strong> 
                <span>
                    <%
                        double subtotal = 0;
                        for (OrderDetail detail : selectedOrderDetails) {
                            subtotal += detail.getPrice() * detail.getQuantity();
                        }
                        out.print(currencyVN.format(subtotal));
                    %>
                </span>
            </li>
            <li class="list-group-item">
                <strong>Shipping Fee (VNĐ):</strong> <span><%= currencyVN.format(30_000) %></span> <!-- Phí vận chuyển mặc định -->
            </li>
            <li class="list-group-item">
                <strong>Promotion Code:</strong> <span><%= order.getPromotionId() != null ? order.getPromotionId() : "None" %></span>
            </li>
            <li class="list-group-item">
                <strong>Discount Value (VNĐ):</strong> 
                <span><%= currencyVN.format(order.getDiscountValue()) %></span>
            </li>
            <li class="list-group-item total">
                <strong>Total (VNĐ):</strong> <span><%= currencyVN.format(order.getTotalPrice()) %></span>
            </li>
        </ul>
    </div>

    <div class="customer-details">
        <h4>Customer Details</h4>
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
            <li class="list-group-item">
                <strong>User ID:</strong> <span><%= order.getUserId() %></span>
            </li>
        </ul>
    </div>

    <div class="continue-shopping">
        <a href="Shop.jsp" class="btn btn-primary">Continue Shopping</a>
    </div>
</div>

<jsp:include page="footer.jsp" />