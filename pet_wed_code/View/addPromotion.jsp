<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Add New Promotion</title>
    <link rel="stylesheet" type="text/css" href="css/addpromotionCss.css">
</head>
<body>
    <div class="container">
        <h2>Add New Promotion</h2>
        
        <form action="AddPromotionServlet" method="post">
             <label for="promotion_id">ID:</label>
            <input type="text" id="promotion_id" name="promotion_id" required> <br>
            
            <label for="promotion_name">PromotionName:</label>
            <input type="text" id="promotion_name" name="promotion_name" required> <br>

            <label for="promotion_description">PromotionDescription:</label>
            <input type="text" id="promotion_description" name="promotion_description" required> <br>

            <label for="discount_type">DiscountType:</label>
            <select id="discount_type" name="discount_type">
                <option value="percent">Percent</option>
                <option value="fixed">Fixed</option>
            </select><br>

            <label for="discount_value">DiscountValue:</label>
            <input type="number" id="discount_value" name="discount_value" required> <br>

            <label for="start_date">StartDate:</label>
            <input type="date" id="start_date" name="start_date" required> <br>

            <label for="end_date">EndDate:</label>
            <input type="date" id="end_date" name="end_date" required> <br>

            <label for="min_order_value">Min_Order_Value:</label>
            <input type="number" id="min_order_value" name="min_order_value"> <br>

            <label for="max_discount">MaxDiscount:</label>
            <input type="number" id="max_discount" name="max_discount"> <br>

            <label for="is_active">Status:</label>
            <input type="checkbox" id="is_active" name="is_active" checked> Active <br>

            <input type="submit" value="Add New Promotion" class="btn btn-add">
            <a href="PromotionServlet" class="btn btn-cancel">Cancel</a>
        </form>
    </div>
</body>
</html>
