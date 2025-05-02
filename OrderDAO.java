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
                order.setShippingFee(rs.getDouble("shipping_fee"));
                order.setDiscountValue(rs.getDouble("discount_value"));
                order.setCustomerId(rs.getString("customer_id"));
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
            System.out.println("OrderDAO.getAllOrders() - Bắt đầu kết nối database...");
            conn = DBConnect.getConnection();
            if (conn == null) {
                System.err.println("OrderDAO.getAllOrders() - Lỗi: Không thể kết nối đến database!");
                return orderList; // Trả về danh sách rỗng nếu không kết nối được
            }
            System.out.println("OrderDAO.getAllOrders() - Kết nối database thành công.");
            stmt = conn.createStatement();
            System.out.println("OrderDAO.getAllOrders() - Chuẩn bị thực thi query: SELECT * FROM Orders");
            rs = stmt.executeQuery("SELECT * FROM Orders");
            System.out.println("OrderDAO.getAllOrders() - Query đã được thực thi.");

            while (rs.next()) {
                System.out.println("OrderDAO.getAllOrders() - Đọc một hàng dữ liệu...");
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
                order.setCustomerId(rs.getString("customer_id"));
                orderList.add(order);
                System.out.println("OrderDAO.getAllOrders() - Đã thêm order ID: " + order.getOrderId() + " vào danh sách.");
            }
            System.out.println("OrderDAO.getAllOrders() - Đã đọc xong tất cả các order. Số lượng: " + orderList.size());

        } catch (SQLException e) {
            System.err.println("OrderDAO.getAllOrders() - Lỗi SQLException đã xảy ra:");
            e.printStackTrace(); // In chi tiết lỗi ra console
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    System.out.println("OrderDAO.getAllOrders() - Đã đóng ResultSet.");
                }
                if (stmt != null) {
                    stmt.close();
                    System.out.println("OrderDAO.getAllOrders() - Đã đóng Statement.");
                }
                DBConnect.closeConnection(conn);
                System.out.println("OrderDAO.getAllOrders() - Đã đóng Connection.");
            } catch (SQLException e) {
                System.err.println("OrderDAO.getAllOrders() - Lỗi khi đóng tài nguyên:");
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
                order.setCustomerId(rs.getString("customer_id"));
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

    // Thêm phương thức xóa đơn hàng
    public boolean deleteOrder(int orderId) {
        Connection conn = null;
        PreparedStatement deleteOrderDetailStmt = null;
        PreparedStatement deleteOrderStmt = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }
            conn.setAutoCommit(false);

            // Xóa chi tiết đơn hàng trước
            String deleteOrderDetailSql = "DELETE FROM Order_Details WHERE order_id = ?";
            deleteOrderDetailStmt = conn.prepareStatement(deleteOrderDetailSql);
            deleteOrderDetailStmt.setInt(1, orderId);
            deleteOrderDetailStmt.executeUpdate();

            // Xóa đơn hàng chính
            String deleteOrderSql = "DELETE FROM Orders WHERE order_id = ?";
            deleteOrderStmt = conn.prepareStatement(deleteOrderSql);
            deleteOrderStmt.setInt(1, orderId);
            int rowsAffected = deleteOrderStmt.executeUpdate();

            conn.commit();
            return rowsAffected > 0; // Trả về true nếu có ít nhất một đơn hàng bị xóa
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (deleteOrderDetailStmt != null) {
                    deleteOrderDetailStmt.close();
                }
                if (deleteOrderStmt != null) {
                    deleteOrderStmt.close();
                }
                DBConnect.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Thêm phương thức lấy đơn hàng theo trạng thái
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orderList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }
            String sql = "SELECT * FROM Orders WHERE status = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
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
                order.setCustomerId(rs.getString("customer_id"));
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

    // Thêm phương thức lấy đơn hàng theo nhiều trạng thái
    public List<Order> getOrdersByStatuses(List<String> statuses) {
        List<Order> orderList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (statuses == null || statuses.isEmpty()) {
            return getAllOrders(); // Trả về tất cả đơn hàng nếu không có trạng thái nào được chỉ định
        }

        try {
            conn = DBConnect.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to connect to database");
            }

            // Tạo chuỗi điều kiện WHERE IN (?, ?, ...)
            StringBuilder inClause = new StringBuilder();
            for (int i = 0; i < statuses.size(); i++) {
                inClause.append("?");
                if (i < statuses.size() - 1) {
                    inClause.append(", ");
                }
            }

            String sql = "SELECT * FROM Orders WHERE status IN (" + inClause.toString() + ")";
            stmt = conn.prepareStatement(sql);

            // Thiết lập các tham số cho mệnh đề IN
            for (int i = 0; i < statuses.size(); i++) {
                stmt.setString(i + 1, statuses.get(i));
            }

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
                order.setCustomerId(rs.getString("customer_id"));
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
}

