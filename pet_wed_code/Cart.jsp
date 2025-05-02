<%@ page import="java.util.*, Model.CartItem" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/shop.css"> <!-- Liên kết tệp CSS -->
<link rel="stylesheet" href="./css/toast.css">
<%@include file="popUpMessage.jsp" %>

<%
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    if (cart == null) {
        cart = new ArrayList<>();
    }

    // Tính tổng tiền của toàn bộ giỏ hàng
    double totalCartValue = 0;
    for (CartItem item : cart) {
        totalCartValue += item.getProduct().getPrice() * item.getQuantity();
    }
%>

<div class="container mt-5">
    <h1 class="text-center my-4">Shopping Cart</h1>
    <div id="cart-alert" class="alert alert-danger text-center" style="display: none;">Your cart is empty!</div>
    <form id="cart-form" action="Checkout.jsp" method="post">
        <!-- Thêm nút "Chọn tất cả sản phẩm" ở đây -->
        <div class="d-flex justify-content-between mb-3">
            <button type="button" id="select-all-button" class="btn btn-primary">Select All Products</button>
        </div>
        <table class="table table-hover" id="cart-table">
            <thead class="thead-light">
                <tr>
                    <th>Select</th>
                    <th>Product</th>
                    <th>Image</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (CartItem item : cart) {
                        double itemTotal = item.getProduct().getPrice() * item.getQuantity();
                %>
                <tr class="product-row"
                    data-product-id="<%= item.getProduct().getProductId() %>" 
                    data-product-price="<%= item.getProduct().getPrice() %>" 
                    data-product-quantity="<%= item.getQuantity() %>" 
                    data-product-stock="<%= item.getProduct().getStockQuantity() %>">
                    <td class="text-center">
                        <input type="checkbox" name="selectedProducts" value="<%= item.getProduct().getProductId() %>" class="product-checkbox">
                    </td>
                    <td><%= item.getProduct().getName() %></td>
                    <td>
                        <img src="<%= item.getProduct().getImageUrl() %>" alt="<%= item.getProduct().getName() %>" class="img-thumbnail" style="width: 100px; height: auto;">
                    </td>
                    <td><%= currencyVN.format(item.getProduct().getPrice()) %></td>
                    <td>
                        <input type="number" class="form-control product-quantity" 
                               value="<%= item.getQuantity() %>" 
                               min="1" 
                               max="<%= item.getProduct().getStockQuantity() %>" 
                               style="width: 60px; text-align: center;" 
                               data-product-id="<%= item.getProduct().getProductId() %>">
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger btn-remove" data-product-id="<%= item.getProduct().getProductId() %>">
                            Remove
                        </button>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <div class="text-right mt-3">
            <h4>Total: <span id="cart-total"><%= currencyVN.format(totalCartValue) %></span></h4>
            <button id="checkout-button" class="btn btn-success"><i class="fas fa-credit-card"></i> Proceed to Checkout</button>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp" />

<script>
    $(document).ready(function () {
        // Hàm định dạng tiền tệ VNĐ
        function formatCurrency(value) {
            return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
        }

        

        // Hàm cập nhật tổng tiền
        function updateTotal() {
            let total = 0;

            // Duyệt qua từng sản phẩm trong giỏ hàng
            $('.product-row').each(function () {
                const isChecked = $(this).find('.product-checkbox').is(':checked'); // Kiểm tra checkbox
                const price = parseFloat($(this).data('product-price')); // Lấy giá sản phẩm
                const quantity = parseInt($(this).find('.product-quantity').val()); // Lấy số lượng sản phẩm

                // Kiểm tra giá trị hợp lệ
                if (!isNaN(price) && !isNaN(quantity) && isChecked) {
                    total += price * quantity; // Cộng dồn vào tổng tiền
                }
            });

            // Hiển thị tổng tiền
            $('#cart-total').text(total === 0 ? '0 VNĐ' : formatCurrency(total));
        }

        // Hàm cập nhật số lượng sản phẩm trong giỏ hàng
        function updateCartCount() {
            const cartCount = $('.product-row').length; // Đếm số lượng sản phẩm trong giỏ hàng
            $('#cart-count').text(cartCount); // Cập nhật số lượng sản phẩm
        }

        // Tính tổng tiền mặc định khi tải trang
        updateTotal();
        updateCartCount();

        // Tính lại tổng tiền khi số lượng thay đổi
        $('.product-quantity').on('change', function () {
            const productId = $(this).data('product-id');
            const newQuantity = parseInt($(this).val());

            if (newQuantity < 1) {
                showErrorToast('Quantity must be at least 1.');
                $(this).val(1); // Đặt lại giá trị tối thiểu là 1
                return;
            }

            // Gửi yêu cầu AJAX để kiểm tra số lượng tối đa
            $.ajax({
                url: 'UpdateCartServlet',
                type: 'POST',
                data: { productId: productId, quantity: newQuantity },
                success: function (response) {
                    if (response.success) {
                        // Cập nhật số lượng hiển thị
                        $(`.product-quantity[data-product-id="${productId}"]`).val(response.updatedQuantity);

                        // Cập nhật tổng tiền
                        updateTotal();

                        // Hiển thị thông báo
                        if (response.updatedQuantity < newQuantity) {
                            showErrorToast(`Maximum quantity available ${response.updatedQuantity}.`);
                        } 
                    } else {
                        showErrorToast(response.message);
                    }
                },
                error: function () {
                    showErrorToast('Failed to connect to the server.');
                }
            });
        });

        // Tính lại tổng tiền khi chọn/bỏ chọn sản phẩm
        $('.product-checkbox').on('change', function () {
            updateTotal();
        });

        // Xóa một sản phẩm
        $('.btn-remove').on('click', function () {
            const productId = $(this).data('product-id');
            const row = $(this).closest('.product-row');

            $.ajax({
                url: 'RemoveFromCartServlet',
                type: 'POST',
                data: { productId: productId },
                success: function (response) {
                    if (response.success) {
                        row.remove(); // Xóa sản phẩm khỏi giao diện
                        updateTotal(); // Cập nhật tổng tiền
                        updateCartCount(); // Cập nhật số lượng sản phẩm
                        showSuccessToast('Product removed successfully.');
                    } else {
                        showErrorToast(response.message);
                    }
                },
                error: function () {
                    showErrorToast('Failed to connect to the server.');
                }
            });
        });

        // Xử lý nút "Chọn tất cả sản phẩm"
        $('#select-all-button').on('click', function () {
            const cartRows = $('.product-row'); // Lấy tất cả các hàng sản phẩm trong giỏ hàng

            if (cartRows.length === 0) {
                showErrorToast('Your cart is empty! There are no products to select.');
                return; // Dừng thực hiện nếu giỏ hàng trống
            }

            $('.product-checkbox').prop('checked', true); // Chọn tất cả sản phẩm
            updateTotal(); // Cập nhật tổng tiền
            showSuccessToast('All products have been selected.');
        });

        // Xử lý khi nhấn "Proceed to Checkout"
        $('#checkout-button').on('click', function (e) {
            e.preventDefault(); // Ngăn chặn hành vi mặc định của nút

            const cartRows = $('.product-row'); // Lấy tất cả các hàng sản phẩm trong giỏ hàng
            const selectedProducts = $('input[name="selectedProducts"]:checked'); // Lấy các sản phẩm được chọn

            if (cartRows.length === 0) {
                showErrorToast('Your cart is empty! Please add products before proceeding to checkout.');
                return;
            }

            if (selectedProducts.length === 0) {
                showErrorToast('Please select at least one product before proceeding to checkout.');
                return;
            }

            let outOfStock = false;
            let pendingRequests = 0;

            selectedProducts.each(function () {
                const productId = $(this).val(); // Lấy ID sản phẩm từ checkbox
                const row = $(this).closest('.product-row'); // Lấy hàng sản phẩm tương ứng

                pendingRequests++;
                $.ajax({
                    url: 'CheckStockServlet',
                    type: 'POST',
                    data: { productId: productId },
                    success: function (response) {
                        if (response.success) {
                            const stockQuantity = response.stockQuantity;
                            if (stockQuantity === 0) {
                                outOfStock = true;
                                const productName = row.find('td:nth-child(2)').text().trim();
                                showErrorToast(`Product ${productName} is out of stock! Please remove it from your cart.`);
                            }
                        } else {
                            showErrorToast('Failed to check stock for some products.');
                        }
                    },
                    error: function () {
                        showErrorToast('Failed to connect to the server.');
                    },
                    complete: function () {
                        pendingRequests--;
                        console.log('Pending requests:', pendingRequests);
                        if (pendingRequests === 0) {
                            if (outOfStock) {
                                return; // Ngăn không cho tiếp tục nếu có sản phẩm hết hàng
                            }
                            $('#cart-form').submit(); // Gửi form nếu tất cả sản phẩm hợp lệ
                        }
                    }
                });
            });
        });
    });
</script>