<%@ page import="java.util.*, Model.CartItem" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/Checkout.css"> <!-- Liên kết tệp CSS mới -->
<link rel="stylesheet" href="./css/toast.css">
<%@include file="popUpMessage.jsp" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
%>

<%
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null || cart.isEmpty()) {
        response.sendRedirect("Shop.jsp");
        return;
    }

    // Lấy danh sách sản phẩm được chọn
    String[] selectedProductIds = request.getParameterValues("selectedProducts");
    if (selectedProductIds == null || selectedProductIds.length == 0) {
        response.sendRedirect("Cart.jsp");
        return;
    }

    // Lọc các sản phẩm được chọn từ giỏ hàng
    List<CartItem> selectedCartItems = new ArrayList<>();
    for (CartItem item : cart) {
        for (String productId : selectedProductIds) {
            if (item.getProduct().getProductId() == Integer.parseInt(productId)) {
                selectedCartItems.add(item);
            }
        }
    }
%>

<%
    String userId = (String) session.getAttribute("customer_id");
    String userFullName = "";
    String userPhone = "";
    String userEmail = "";
    String userAddress = "";

    if (userId != null) {
        // Lấy thông tin người dùng từ session
        userFullName = (String) session.getAttribute("customer_fullname");
        userPhone = (String) session.getAttribute("customer_phone");
        userEmail = (String) session.getAttribute("customer_email");
        userAddress = (String) session.getAttribute("customer_address");
    }
%>

<div class="checkout-container">
    <h1 class="text-center my-4">Checkout</h1>
    <div class="row">
        <div class="col-md-8 order-md-1">
            <h4 class="mb-3">Billing Address</h4>
            <form action="CheckoutServlet" method="post">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="name">Full Name</label>
                        <input type="text" class="form-control" id="name" name="name" value="<%= userFullName %>" 
                               <%= (userId != null) ? "readonly" : "" %> required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="phone">Phone Number</label>
                        <input type="text" class="form-control" id="phone" name="phone" value="<%= userPhone %>" 
                               <%= (userId != null) ? "readonly" : "" %> required>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="address">Address</label>
                    <input type="text" class="form-control" id="address" name="address" value="<%= userAddress %>" 
                           <%= (userId != null) ? "readonly" : "" %> required>
                </div>
                <div class="mb-3">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" name="email" value="<%= userEmail %>" 
                           <%= (userId != null) ? "readonly" : "" %> required>
                </div>
                <div class="mb-3">
                    <label for="promoCode">Promotion Code</label>
                    <input type="text" class="form-control" id="promoCode" name="promoCode" placeholder="Enter promotion code">
                </div>
                <h4 class="mb-3">Payment</h4>
                <div class="d-block my-3">
                    <div class="custom-control custom-radio">
                        <input id="cod" name="paymentMethod" type="radio" class="custom-control-input" value="COD" required>
                        <label class="custom-control-label" for="cod">Cash on Delivery (COD)</label>
                    </div>
                    
                  
                    <div class="custom-control custom-radio">
                        <input id="vnpay" name="paymentMethod" type="radio" class="custom-control-input" value="VNPAY" required>
                        <label class="custom-control-label" for="vnpay">Pay with VNPAY</label>
                    </div>
                </div>
                <!-- Gửi danh sách sản phẩm được chọn -->
                <%
                    for (CartItem item : selectedCartItems) {
                %>
                <input type="hidden" name="selectedProducts" value="<%= item.getProduct().getProductId() %>">
                <%
                    }
                %>
                <button class="btn btn-primary btn-lg btn-block" type="submit">Place Order</button>
            </form>
        </div>
        <div class="col-md-4 order-md-2 mb-4">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
                <span class="text-muted">Your cart</span>
                <span class="badge badge-secondary badge-pill"><%= selectedCartItems.size() %></span>
            </h4>
            <ul class="list-group mb-3">
                <%
                    double total = 0;
                    for (CartItem item : selectedCartItems) {
                        double itemTotal = item.getProduct().getPrice() * item.getQuantity();
                        total += itemTotal;
                %>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0"><%= item.getProduct().getName() %></h6>
                        <small class="text-muted">Quantity: <%= item.getQuantity() %></small>
                    </div>
                    <!-- Hiển thị giá tiền theo định dạng VNĐ -->
                    <span class="text-muted"><%= currencyVN.format(itemTotal) %></span>
                </li>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <img src="<%= item.getProduct().getImageUrl() %>" alt="<%= item.getProduct().getName() %>" style="width: 100px; height: auto;">
                </li>
                <%
                    }
                %>
                <li class="list-group-item d-flex justify-content-between">
                    <span>Total (VNĐ)</span>
                    <!-- Hiển thị tổng tiền theo định dạng VNĐ -->
                    <strong><%= currencyVN.format(total) %></strong>
                </li>
                <li class="list-group-item d-flex justify-content-between">
                    <span>Shipping Fee (VNĐ)</span>
                    <strong><%= currencyVN.format(30_000) %></strong> <!-- Phí vận chuyển mặc định -->
                </li>
                <li class="list-group-item d-flex justify-content-between">
                    <span>Total (VNĐ)</span>
                    <!-- Hiển thị tổng tiền theo định dạng VNĐ -->
                    <strong id="orderTotal"><%= currencyVN.format(total + 30000) %></strong> <!-- Cộng phí vận chuyển -->
                </li>
            </ul>
        </div>
    </div>
</div>



<jsp:include page="footer.jsp" />

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script>
    $(document).ready(function () {
        let isPromoValid = true; // Biến để kiểm tra trạng thái mã khuyến mãi

        // Kiểm tra mã khuyến mãi khi người dùng nhập vào ô mã khuyến mãi
        $('#promoCode').on('blur', function () {
            const promoCode = $(this).val().trim();
            const orderValueText = $('#orderTotal').text().replace(/[^0-9]/g, ''); // Loại bỏ ký tự không phải số
            const orderValue = parseInt(orderValueText); // Chuyển đổi thành số nguyên

            console.log("Order Value being sent: ", orderValue); // Log giá trị để kiểm tra

            if (promoCode === '') {
                // Nếu ô mã khuyến mãi trống, không kiểm tra
                isPromoValid = true;
                return;
            }

            // Gửi AJAX request để kiểm tra mã khuyến mãi
            $.ajax({
                url: 'CheckPromotionServlet',
                type: 'POST',
                data: { promoCode: promoCode, orderValue: orderValue }, // Gửi cả orderValue
                success: function (response) {
                    if (response.valid) {
                        const minOrderValue = response.minOrderValue; // Lấy giá trị minOrderValue từ response

                        // Kiểm tra giá trị đơn hàng
                        if (orderValue < minOrderValue) {
                            showErrorToast(`Order value must be at least ${minOrderValue.toLocaleString('vi-VN')} VND to apply this promotion.`);
                            isPromoValid = false;
                            return;
                        }

                        // Nếu hợp lệ, hiển thị thông báo
                        isPromoValid = true;
                        showSuccessToast('Promotion code is valid.');
                    } else {
                        isPromoValid = false;
                        showErrorToast(response.message || 'Invalid or expired promotion code.');
                    }
                },
                error: function () {
                    isPromoValid = false;
                    showErrorToast('Error checking promotion code. Please try again.');
                }
            });
        });

        // Kiểm tra và gửi form khi người dùng nhấn "Place Order"
        $('form').on('submit', function (e) {
            e.preventDefault(); // Ngăn chặn hành vi mặc định của form

            const name = $('#name').val().trim();
            const phone = $('#phone').val().trim();
            const address = $('#address').val().trim();
            const email = $('#email').val().trim();

            // Regex for full name (cho phép chữ cái có dấu và dấu cách)
            const nameRegex = /^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằ̉ẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\s]+$/;

            // Regex for phone number (mã vùng Việt Nam: +84 hoặc 0, theo sau là 9-10 chữ số)
            const phoneRegex = /^(?:\+84|0)(?:[3|5|7|8|9])\d{8}$/;

            // Regex for email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            // Validate Full Name
            if (!nameRegex.test(name) || name.length < 3 || name.length > 50) {
                showErrorToast('Full Name is required, must be between 3 and 50 characters, and can only contain letters, spaces, and Vietnamese diacritics.');
                return;
            }

            // Validate Phone Number
            if (!phoneRegex.test(phone)) {
                showErrorToast('Phone Number must start with +84 or 0, followed by 9-10 digits, and match Vietnamese phone number formats.');
                return;
            }

            // Validate Address
            if (address === '' || address.length < 5 || address.length > 100) {
                showErrorToast('Address is required and must be between 5 and 100 characters.');
                return;
            }

            // Validate Email
            if (!emailRegex.test(email)) {
                showErrorToast('Invalid Email format.');
                return;
            }

            // Kiểm tra trạng thái mã khuyến mãi
            if (!isPromoValid) {
                showErrorToast('Invalid or expired promotion code. Please check your promotion code.');
                return;
            }

            // Hiển thị thông báo thành công trước khi gửi form
            showSuccessToast('Placing your order...');
            setTimeout(() => {
                this.submit(); // Gửi form sau khi hiển thị thông báo
            }, 1000); // Đợi 1.5 giây để hiển thị toast trước khi gửi form
        });

        // Hiển thị hoặc ẩn QR code khi chọn phương thức thanh toán
        $('input[name="paymentMethod"]').change(function () {
            if ($(this).val() === 'VNPAY') {
                // Gửi form đến VnpayPaymentServlet khi chọn VNPAY
                const orderInfo = "Order Payment"; // Thông tin đơn hàng
                const orderTotalText = $('#orderTotal').text().replace(/[^0-9]/g, ''); // Lấy tổng tiền
                const orderTotal = parseInt(orderTotalText); // Chuyển đổi thành số nguyên

                // Tạo form ẩn để gửi dữ liệu đến VnpayPaymentServlet
                const form = $('<form>', {
                    action: 'VnpayPaymentServlet',
                    method: 'POST'
                });

                form.append($('<input>', { type: 'hidden', name: 'orderInfo', value: orderInfo }));
                form.append($('<input>', { type: 'hidden', name: 'amount', value: orderTotal }));
                form.append($('<input>', { type: 'hidden', name: 'bankCode', value: '' })); // Bank code nếu cần

                $('body').append(form);
                form.submit();
            }
        });
    });
</script>