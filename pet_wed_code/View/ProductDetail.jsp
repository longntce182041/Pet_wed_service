<%@ page import="java.sql.*, java.util.*, DAO.ProductDAO, Model.Product" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="DAO.RatingProductDAO, Model.Rating" %>
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/shop.css"> <!-- Liên kết tệp CSS mới -->
<link rel="stylesheet" href="./css/toast.css">
<%@include file="popUpMessage.jsp" %>

<%
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    int productId = Integer.parseInt(request.getParameter("productId"));
    ProductDAO productDAO = new ProductDAO();
    Product product = productDAO.getProductById(productId);
    List<Product> similarProducts = productDAO.getSimilarProducts(productId);

    RatingProductDAO ratingDAO = new RatingProductDAO();
    List<Rating> ratings = ratingDAO.getRatingsByProductId(productId);
%>

<div class="container mt-5">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="index.jsp">Home</a></li>
            <li class="breadcrumb-item"><a href="Shop.jsp">Shop</a></li>
            <li class="breadcrumb-item active" aria-current="page"><%= product.getName()%></li>
        </ol>
    </nav>

    <div class="row">
        <div class="col-md-6">
            <img src="<%= product.getImageUrl() %>" class="img-fluid rounded shadow-sm" alt="<%= product.getName() %>">
        </div>
        <div class="col-md-6">
            <h2 class="my-3"><%= product.getName() %></h2>
            <h4 class="text-success"><%= currencyVN.format(product.getPrice()) %></h4>
            <p><%= product.getDescription() %></p>
            <p><%= product.getDescriptionDetail() %></p>
            <p>Stock: <%= product.getStockQuantity() %></p>
            <% if (product.getStockQuantity() == 0) { %>
            <span class="badge badge-danger">Sold Out</span>
            <% } else { %>
            <div class="form-group d-flex align-items-center">
                <label for="quantity" class="mr-2">Quantity:</label>
                <input 
                    type="number" 
                    class="form-control quantity-input" 
                    id="quantity" 
                    name="quantity" 
                    value="1" 
                    min="1" 
                    max="<%= product.getStockQuantity() %>" 
                    step="1"
                    >
                <button type="button" class="btn btn-success add-to-cart ml-3" data-product-id="<%= product.getProductId() %>">
                    <i class="fas fa-shopping-cart"></i> Add to Cart
                </button>
            </div>
            <% } %>
        </div>
    </div>
    <div class="container mt-5">
        <h2 class="text-center my-5">Customer Feedback</h2>
        <div class="row">
            <% if (ratings.isEmpty()) { %>
                <p class="text-center">No feedback available for this product.</p>
            <% } else { %>
                <% for (Rating rating : ratings) { %>
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <div class="avatar bg-primary text-white rounded-circle d-flex justify-content-center align-items-center" style="width: 50px; height: 50px;">
                                        <%= rating.getCustomerName().substring(0, 1).toUpperCase() %>
                                    </div>
                                    <h5 class="card-title ml-3 mb-0"> <%= rating.getCustomerName() %></h5>
                                </div>
                                <p class="card-text">Rating: 
                                    <% for (int i = 0; i < rating.getRatingStar(); i++) { %>
                                        <i class="fas fa-star text-warning"></i>
                                    <% } %>
                                    <% for (int i = rating.getRatingStar(); i < 5; i++) { %>
                                        <i class="far fa-star text-warning"></i>
                                    <% } %>
                                </p>
                                <p class="card-text">Comment: <%= rating.getComment() %></p>
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
    </div>
    <h2 class="text-center my-5">Similar Products</h2>
    <div class="row">
        <% int count = 0; %>
        <% for (Product similarProduct : similarProducts) { %>
        <% if (count < 4) { %>
        <div class="col-md-3 d-flex align-items-stretch">
            <div class="card mb-4 shadow-sm product-card" data-product-id="<%= similarProduct.getProductId() %>">
                <img src="<%= similarProduct.getImageUrl() %>" class="card-img-top" alt="<%= similarProduct.getName() %>">
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title"><%= similarProduct.getName() %></h5>
                    <p class="card-text text-truncate"><%= similarProduct.getDescription() %></p>
                    <p class="card-text"><strong>Price: <%= currencyVN.format(similarProduct.getPrice()) %></strong></p>
                    <% if (similarProduct.getStockQuantity() == 0) { %>
                    <span class="badge badge-danger">Sold Out</span>
                    <% } else { %>
                    <a href="ProductDetail.jsp?productId=<%= similarProduct.getProductId() %>" class="btn btn-primary mt-auto">View Details</a>
                    <% } %>
                </div>
            </div>
        </div>
        <% count++; %>
        <% } %>
        <% } %>
    </div>
</div>



<jsp:include page="footer.jsp" />

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function () {
        $('.add-to-cart').click(function () {
            var productId = $(this).data('product-id');
            var quantity = $('#quantity').val();
            var maxQuantity = $('#quantity').attr('max');

            if (quantity < 1 || !$.isNumeric(quantity) || parseInt(quantity) > parseInt(maxQuantity)) {
                showErrorToast('Please enter a valid quantity between 1 and ' + maxQuantity + '.');
                return;
            }

            $.ajax({
                url: 'AddToCartServlet',
                type: 'POST',
                data: {productId: productId, quantity: quantity},
                success: function (response) {
                    if (response.success) {
                        // Cập nhật số lượng sản phẩm trong giỏ hàng trên thanh navbar
                        $('#cart-count').text(response.cartCount);

                        // Đặt lại giá trị của ô nhập số lượng về 1
                        $('#quantity').val(0);

                        // Hiển thị thông báo thành công
                        showSuccessToast(response.message);
                    } else {
                        // Hiển thị thông báo lỗi
                        showErrorToast(response.message);
                    }
                },
                error: function (xhr, status, error) {
                    // Hiển thị thông báo lỗi nếu xảy ra lỗi kết nối
                    showErrorToast('Failed to add product to cart. Please try again.');
                }
            });
        });

        $('.product-card').click(function () {
            var productId = $(this).data('product-id');
            window.location.href = 'ProductDetail.jsp?productId=' + productId;
        });
    });
</script>