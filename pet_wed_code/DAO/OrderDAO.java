package DAO;

import Model.Order;
import Model.OrderDetail;
import ConnectDB.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int saveOrder(Order order, List<OrderDetail> orderDetails) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderDetailStmt = null;
        ResultSet rs = null;
        int orderId = 0;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }
            conn.setAutoCommit(false);

            // Lưu thông tin đơn hàng
            String orderSql = "INSERT INTO Orders (order_date, total_price, status, promotion_id, user_id, name, phone, email, address, payment_method, shipping_fee, discount_value, customer_id) VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setDouble(1, order.getTotalPrice());
            orderStmt.setString(2, order.getStatus());
            if (order.getPromotionId() != null) {
                orderStmt.setString(3, order.getPromotionId());
            } else {
                orderStmt.setNull(3, Types.VARCHAR);
            }
            orderStmt.setString(4, order.getUserId());
            orderStmt.setString(5, order.getName());
            orderStmt.setString(6, order.getPhone());
            orderStmt.setString(7, order.getEmail());
            orderStmt.setString(8, order.getAddress());
            orderStmt.setString(9, order.getPaymentMethod());
            orderStmt.setDouble(10, order.getShippingFee());
            orderStmt.setDouble(11, order.getDiscountValue());
            orderStmt.setString(12, order.getCustomerId());

            orderStmt.executeUpdate();

            // Lấy order_id vừa được tạo
            rs = orderStmt.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // Lưu thông tin chi tiết đơn hàng
            String orderDetailSql = "INSERT INTO Order_Details (order_id, product_id, service_booking_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
            orderDetailStmt = conn.prepareStatement(orderDetailSql);
            for (OrderDetail detail : orderDetails) {
                orderDetailStmt.setInt(1, orderId);
                orderDetailStmt.setInt(2, detail.getProductId());
                if (detail.getServiceBookingId() != null) {
                    orderDetailStmt.setInt(3, detail.getServiceBookingId());
                } else {
                    orderDetailStmt.setNull(3, Types.INTEGER);
                }
                orderDetailStmt.setInt(4, detail.getQuantity());
                orderDetailStmt.setDouble(5, detail.getPrice());
                orderDetailStmt.addBatch();
            }
            orderDetailStmt.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (orderStmt != null) {
                    orderStmt.close();
                }
                if (orderDetailStmt != null) {
                    orderDetailStmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderId;
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }

            String sql = "SELECT od.*, p.product_name, p.product_image_url "
                    + "FROM Order_Details od "
                    + "JOIN Products p ON od.product_id = p.product_id "
                    + "WHERE od.order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(rs.getInt("order_detail_id"));
                detail.setOrderId(rs.getInt("order_id"));
                detail.setProductId(rs.getInt("product_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getDouble("price"));
                detail.setProductName(rs.getString("product_name")); // Lấy tên sản phẩm
                detail.setProductImage(rs.getString("product_image_url")); // Lấy hình ảnh sản phẩm
                orderDetails.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderDetails;
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }
            String sql = "SELECT * FROM Orders WHERE order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date")); // Lấy giá trị order_date từ cơ sở dữ liệu
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setPromotionId(rs.getString("promotion_id"));
                order.setUserId(rs.getString("user_id")); // Lấy user_id dưới dạng chuỗi

                order.setName(rs.getString("name"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setDiscountValue(rs.getDouble("discount_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Orders");

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setPromotionId(rs.getString("promotion_id"));
                order.setUserId(rs.getString("user_id"));

                order.setName(rs.getString("name"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderList;
    }

    public void updateOrderStatus(int orderId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }

            String sql = "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setPromotionId(rs.getString("promotion_id"));
                order.setUserId(rs.getString("user_id"));
                order.setName(rs.getString("name"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setShippingFee(rs.getDouble("shipping_fee"));
                order.setDiscountValue(rs.getDouble("discount_value"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orders;
    }

    public OrderDetail getOrderDetailById(int orderDetailId) {
        String sql = "SELECT od.order_detail_id, od.quantity, od.price, p.product_name, o.status " +
                     "FROM Order_Details od " +
                     "JOIN Products p ON od.product_id = p.product_id " +
                     "JOIN Orders o ON od.order_id = o.order_id " +
                     "WHERE od.order_detail_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderDetailId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(rs.getInt("order_detail_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getDouble("price"));
                detail.setProductName(rs.getString("product_name"));
                detail.setOrderStatus(rs.getString("status")); // Lưu trạng thái đơn hàng
                return detail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy trạng thái đơn hàng
    public String getOrderStatus(int orderId) {
        String status = null;
        String query = "SELECT status FROM Orders WHERE order_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Cập nhật trạng thái đơn hàng
    public boolean cancelOrder(int orderId, String status, String reason) {
    String query = "UPDATE Orders SET status = ?, cancel_reason = ? WHERE order_id = ?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, status);
        ps.setString(2, reason);
        ps.setInt(3, orderId);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
}
