<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Revenue" %>
<html>
<head>
    <title>Revenue Report</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 1000px;
            margin: auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #007bff;
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #ffffff;
        }
        th, td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
            color: #007bff;
        }
        tr:hover {
            background-color: #e9ecef;
        }
        .error-message {
            color: red;
            font-style: italic;
            margin-bottom: 15px;
        }
        .no-data {
            text-align: center;
            color: #6c757d;
            padding: 10px;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            margin-top: 20px;
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
    <div class="container my-5">
        <h2>Revenue Report</h2>

        <%-- Display error message if any --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
        <% } %>

        <table id="revenueTable" class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Year</th>
                    <th>Month</th>
                    <th>Total Revenue</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Revenue> revenues = (List<Revenue>) request.getAttribute("revenues");
                    if (revenues != null && !revenues.isEmpty()) {
                        for (Revenue revenue : revenues) {
                %>
                <tr>
                    <td><%= revenue.getOrderYear() %></td>
                    <td><%= revenue.getOrderMonth() %></td>
                    <td><%= String.format("%,.2f VNĐ", revenue.getTotalRevenue()) %></td>
                </tr>
                <%
                        }
                    } else if (request.getAttribute("errorMessage") == null) { %>
                <tr>
                    <td colspan="3" class="no-data">No data available.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#revenueTable').DataTable({
                "paging": true,
                "lengthChange": false,
                "searching": true,
                "ordering": true,
                "info": true,
                "autoWidth": true,
                "responsive": true,
                "pageLength": 10,
                "order": [[ 0, "asc" ]],
                "columns": [
                    { "data": "year" },
                    { "data": "month" },
                    { "data": "revenue", "render": function(data, type, row) {
                        return data + " VNĐ";
                    }},
                ],
                "language": {
                    "paginate": {
                        "previous": "Previous",
                        "next": "Next"
                    },
                    "search": "Search:",
                    "emptyTable": "No data available in table",
                    "info": "Showing _START_ to _END_ of _TOTAL_ entries",
                    "infoEmpty": "Showing 0 to 0 of 0 entries",
                    "infoFiltered": "(filtered from _MAX_ total entries)",
                    "lengthMenu": "Show _MENU_ entries",
                    "loadingRecords": "Loading...",
                    "processing": "Processing...",
                    "zeroRecords": "No matching records found"
                },
                 "drawCallback": function( settings ) {
                    // This function is called after the table is drawn.
                    // Check if there is data in the table.
                    if (settings.aoData.length === 0) {
                        // If there is no data, hide the table header.
                        $(this).find("thead").css("display", "none");
                    } else {
                         // Otherwise, show the header.
                         $(this).find("thead").css("display", "");
                    }
                }
            });
        });
    </script>
</body>
</html>
