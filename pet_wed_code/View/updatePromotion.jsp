<%-- 
    Document   : updatePromotion
    Created on : Mar 1, 2025, 6:31:15 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Model.Promotion" %>
<%
    Promotion promo = (Promotion) request.getAttribute("promotionList");
    if (promo == null) {
        response.sendRedirect("PromotionServlet");
        return;
    }
%>
<html>
<head>
    <title>Update Promotion</title>
    <link rel="stylesheet" type="text/css" href="css/addpromotionCss.css">
</head>
<body>
    <div class="container">
        <h2>Update Promotion</h2>

        <form action="UpdatePromotionServlet" method="post">
            <input type="hidden" name="id" value="<%= promo.getPromotionId() %>">
            
            <label for="name">PromotionName:</label>
            <input type="text" id="name" name="name" value="<%= promo.getPromotionName() %>" required> <br>

            <label for="description">Status:</label>
            <input type="text" id="description" name="description" value="<%= promo.getPromotionDescription() %>" required> <br>

            <label for="discountType">DiscountType:</label>
            <select id="discountType" name="discountType">
                <option value="percent" <%= promo.getDiscountType().equals("percent") ? "selected" : "" %>>Percent</option>
                <option value="fixed" <%= promo.getDiscountType().equals("fixed") ? "selected" : "" %>>Fixed</option>
            </select><br>

            <label for="discountValue">DiscountValue:</label>
            <input type="number" id="discountValue" name="discountValue" value="<%= promo.getDiscountValue() %>" required> <br>

            <label for="startDate">StartDate:</label>
            <input type="date" id="startDate" name="startDate" value="<%= promo.getStartDate() %>" required> <br>

            <label for="endDate">EndDate:</label>
            <input type="date" id="endDate" name="endDate" value="<%= promo.getEndDate() %>" required> <br>

            <label for="minOrderValue">MinOrderValue:</label>
            <input type="number" id="minOrderValue" name="minOrderValue" value="<%= promo.getMinOrderValue() %>"> <br>

            <label for="maxDiscount">MaxDiscount:</label>
            <input type="number" id="maxDiscount" name="maxDiscount" value="<%= promo.getMaxDiscount() != null ? promo.getMaxDiscount() : "" %>"> <br>

           <label for="isActive">Status:</label>
           <input type="checkbox" id="isActive" name="isActive" <%= promo.isIsActive() ? "checked" : "" %>> Active <br>

            <input type="submit" value="Cập Nhật" class="btn btn-update">
            <a href="PromotionServlet" class="btn btn-cancel">Cancel</a>
        </form>
    </div>
</body>
</html>
