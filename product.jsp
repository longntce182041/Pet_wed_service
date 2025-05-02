<%@ page import="java.util.List, DAO.ProductDAO, Model.Product" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }

        table {
            width: 80%;
            margin: 0 auto;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }

        .quantity-form {
            display: flex;
            align-items: center;
        }

        .quantity-input {
            width: 50px;
            padding: 5px;
            margin-right: 10px;
        }

        .add-button {
            padding: 5px 10px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h1>Product List</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Product Name</th>
                <th>Current Quantity</th>
                <th>Add Quantity</th>
            </tr>
        </thead>
        <tbody>
            <%
                ProductDAO productDAO = new ProductDAO();
                List<Product> productList = productDAO.getAllProducts();
                if (productList != null && !productList.isEmpty()) {
                    for (Product product : productList) {
            %>
                        <tr>
                            <td><%= product.getProductId() %></td>
                            <td><%= product.getName() %></td>
                            <td id="quantity-<%= product.getProductId() %>"><%= product.getStockQuantity() %></td>
                            <td>
                                <form class="quantity-form" id="form-<%= product.getProductId() %>">
                                    <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                                    <input type="number" class="quantity-input" name="quantityToAdd" value="0" min="0" id="quantityToAdd-<%= product.getProductId() %>">
                                    <button type="button" class="add-button" onclick="addQuantity('<%= product.getProductId() %>')">Add</button>
                                </form>
                            </td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="4">No products available.</td></tr>
            <%
                }
            %>
        </tbody>
    </table>

    <script>
        function addQuantity(productId) {
            const quantityToAddInput = document.getElementById('quantityToAdd-' + productId);
            const quantityToAdd = quantityToAddInput.value;
            const xhr = new XMLHttpRequest();
            const url = '<%=request.getContextPath()%>/AddQuantityServlet';
            const params = 'productId=' + encodeURIComponent(productId) + '&quantityToAdd=' + encodeURIComponent(quantityToAdd);

            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

            xhr.onload = function() {
                if (xhr.status === 200) {
                    const newQuantity = xhr.responseText;
                    const currentQuantityElement = document.getElementById('quantity-' + productId);
                    currentQuantityElement.textContent = newQuantity;
                } else {
                    console.error('Error adding quantity:', xhr.status, xhr.responseText);
                    alert('An error occurred while adding quantity.');
                }
            };

            xhr.onerror = function() {
                console.error('Network error while adding quantity.');
                alert('Network error while adding quantity.');
            };

            xhr.send(params);
        }
    </script>
</body>
</html>