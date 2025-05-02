<!-- File: manageStaff.jsp -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Staff" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Manage Staff</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="./css/Style_manageProduct.css">
        <style>
            .staff-img {
                width: 50px;
                height: 50px;
                object-fit: cover;
                border-radius: 50%;
            }
        </style>
    </head>
    <body>
        <!-- Top Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="admin_Dashboard.jsp"><i class="fa fa-dashboard"></i> Dashboard</a>
        </nav>

        <div class="wrapper d-flex align-items-stretch">
            <!-- Sidebar -->
            <nav id="sidebar">
                <div class="p-4 pt-5">
                    <a class="navbar-brand" href="index.jsp"><span class="flaticon-pawprint-1 mr-2"></span>Petique Spa</a>
                    <ul class="list-unstyled components mb-5">
                        <li><a href="admin_Dashboard.jsp">Dashboard</a></li>
                        <li><a href="ViewProductServlet">Manage Products</a></li>
                        <li><a href="OrderManagement.jsp">Manage Orders</a></li>
                        <li><a href="ManageCustomerServlet">Manage Users</a></li>
                        <li class="active"><a href="ManageStaffServlet">Manage Staff</a></li>
                        <li><a href="PromotionServlet">Manage Promotion</a></li>
                        <li><a href="ManageServiceServlet">Manage Service</a></li>
                        <li><a href="AppointmentScheduleServlet">Manage Appointment</a></li>
                        <li><a href="StatisticsServlet">Statistic</a></li>
                    </ul>
                </div>
            </nav>

            <!-- Page Content -->
            <div id="content" class="p-4 p-md-5">
                <h2>Staff Management</h2>
                <button class="btn btn-primary mb-3" data-toggle="modal" data-target="#addStaffModal">Add Staff</button>

                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead class="thead-dark">
                            <tr>
                                <th>Staff ID</th>
                                <th>Full Name</th>
                                <th>Position</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Image</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
                                if (staffList != null && !staffList.isEmpty()) {
                                    for (Staff staff : staffList) {
                            %>
                            <tr>
                                <td><%= staff.getStaffId() %></td>
                                <td><%= staff.getStaffFullName() %></td>
                                <td><%= staff.getPosition() %></td>
                                <td><%= staff.getStaffPhone() %></td>
                                <td><%= staff.getStaffEmail() %></td>
                                <td>
                                    <img class="staff-img" src="<%= staff.getStaffImg() != null ? staff.getStaffImg() : "./images/default.png" %>" alt="Staff Image">
                                </td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-button" 
                                            data-id="<%= staff.getStaffId() %>" 
                                            data-name="<%= staff.getStaffFullName() %>" 
                                            data-position="<%= staff.getPosition() %>" 
                                            data-phone="<%= staff.getStaffPhone() %>" 
                                            data-email="<%= staff.getStaffEmail() %>">
                                        Update
                                    </button>
                                    <a href="ManageStaffServlet?action=delete&id=<%= staff.getStaffId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this staff?');">Delete</a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="7" class="text-center">No staff found.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Add Staff Modal -->
        <div class="modal fade" id="addStaffModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form action="ManageStaffServlet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Add Staff</h5>
                            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="staffId">Staff ID</label>
                                <input type="text" class="form-control" id="staffId" name="staff_id" required>
                            </div>
                            <div class="form-group">
                                <label for="staffFullName">Full Name</label>
                                <input type="text" class="form-control" id="staffFullName" name="staff_full_name" required>
                            </div>
                            <div class="form-group">
                                <label for="position">Position</label>
                                <input type="text" class="form-control" id="position" name="position" required>
                            </div>
                            <div class="form-group">
                                <label for="staffPhone">Phone</label>
                                <input type="text" class="form-control" id="staffPhone" name="staff_phone" required>
                            </div>
                            <div class="form-group">
                                <label for="staffEmail">Email</label>
                                <input type="email" class="form-control" id="staffEmail" name="staff_email" required>
                            </div>
                            <div class="form-group">
                                <label for="staffImg">Upload Image</label>
                                <input type="file" class="form-control-file" id="staffImg" name="staff_img" accept="image/*" required>
                                <img id="addPreview" class="mt-2 staff-img d-none"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Staff</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Edit Staff Modal -->
        <div class="modal fade" id="editStaffModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form action="ManageStaffServlet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" id="editStaffId" name="staff_id">
                        <div class="modal-header">
                            <h5 class="modal-title">Update Staff</h5>
                            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="editStaffFullName">Full Name</label>
                                <input type="text" class="form-control" id="editStaffFullName" name="staff_full_name" required>
                            </div>
                            <div class="form-group">
                                <label for="editPosition">Position</label>
                                <input type="text" class="form-control" id="editPosition" name="position" required>
                            </div>
                            <div class="form-group">
                                <label for="editStaffPhone">Phone</label>
                                <input type="text" class="form-control" id="editStaffPhone" name="staff_phone" required>
                            </div>
                            <div class="form-group">
                                <label for="editStaffEmail">Email</label>
                                <input type="email" class="form-control" id="editStaffEmail" name="staff_email" required>
                            </div>
                            <div class="form-group">
                                <label for="editStaffImg">Upload New Image</label>
                                <input type="file" class="form-control-file" id="editStaffImg" name="staff_img" accept="image/*">
                                <img id="editPreview" class="mt-2 staff-img d-none"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Update Staff</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
        <script>
                                        $('.edit-button').on('click', function () {
                                            $('#editStaffId').val($(this).data('id'));
                                            $('#editStaffFullName').val($(this).data('name'));
                                            $('#editPosition').val($(this).data('position'));
                                            $('#editStaffPhone').val($(this).data('phone'));
                                            $('#editStaffEmail').val($(this).data('email'));
                                            $('#editPreview').addClass('d-none');
                                            $('#editStaffModal').modal('show');
                                        });

                                        // Add image preview
                                        $('#staffImg').change(function (e) {
                                            let reader = new FileReader();
                                            reader.onload = function (e) {
                                                $('#addPreview').attr('src', e.target.result).removeClass('d-none');
                                            }
                                            reader.readAsDataURL(this.files[0]);
                                        });

                                        $('#editStaffImg').change(function (e) {
                                            let reader = new FileReader();
                                            reader.onload = function (e) {
                                                $('#editPreview').attr('src', e.target.result).removeClass('d-none');
                                            }
                                            reader.readAsDataURL(this.files[0]);
                                        });
        </script>
    </body>
</html>
