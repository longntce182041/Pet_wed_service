<%-- 
    Document   : sendPromotionEmail
    Created on : Apr 23, 2025, 12:03:28 AM
    Author     : Admin
--%>

<%@ page import="java.util.List" %>
<%@ page import="Model.CustomerSendEmail" %>
<%@ page import="Model.Promotion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Send Promotion Email</title>
    <meta charset="UTF-8">
    <style>
        body {
            background-color: white;
            font-family: Arial, sans-serif;
            padding: 30px;
        }

        h2, h3 {
            color: #28a745;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        table, th, td {
            border: 2px solid #28a745;
        }

        th {
            background-color: #28a745;
            color: white;
            padding: 10px;
            text-align: left;
        }

        td {
            background-color: white;
            color: black;
            padding: 10px;
        }

        select {
            padding: 5px;
            width: 300px;
        }

        button {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #218838;
        }

        #selectAll {
            margin: 10px 0;
        }
    </style>

    <script>
        function toggleSelectAll(source) {
            let checkboxes = document.getElementsByName("customerIds");
            for (let i = 0; i < checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>
</head>
<body>
    <h2>Send Promotion Email</h2>
    <form action="SendPromotionEmailServlet" method="post">
        <h3>Select Customers</h3>

        <div>
            <label><input type="checkbox" id="selectAll" onclick="toggleSelectAll(this)"> Select All</label>
        </div>

        <div>
            <table>
                <tr>
                    <th>Select</th>
                    <th>Customer Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>
                </tr>
                <%
                    List<CustomerSendEmail> customerList = (List<CustomerSendEmail>) request.getAttribute("customerList");
                    if (customerList != null) {
                        for (CustomerSendEmail customer : customerList) {
                %>
                <tr>
                    <td><input type="checkbox" name="customerIds" value="<%= customer.getCustomerId() %>"></td>
                    <td><%= customer.getCustomerFullName() %></td>
                    <td><%= customer.getCustomerEmail() %></td>
                    <td><%= customer.getCustomerPhone() %></td>
                    <td><%= customer.getCustomerAddress() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
        </div>

        <h3>Select Promotion</h3>
        <div>
            <select name="promotionId" required>
                <option value="">-- Select a Promotion --</option>
                <%
                    List<Promotion> promotionList = (List<Promotion>) request.getAttribute("promotionList");
                    if (promotionList != null) {
                        for (Promotion promo : promotionList) {
                %>
                <option value="<%= promo.getPromotionId() %>"><%= promo.getPromotionName() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <br>
        <button type="submit">Send Emails</button>
    </form>
</body>
</html>
