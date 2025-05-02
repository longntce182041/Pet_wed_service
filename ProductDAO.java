package DAO;

import Model.Product;
import ConnectDB.DBConnect;
import java.sql.*;
import java.util.*;

public class ProductDAO {

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT * FROM Products WHERE active = 1";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock_quantity"),
                        rs.getString("product_image_url"),
                        rs.getString("product_category_id"),
                        rs.getString("product_description_detail"),
                        rs.getBoolean("active")
                ));
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
        return list;
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int productId) {
        Product product = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT * FROM Products WHERE product_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock_quantity"),
                        rs.getString("product_image_url"),
                        rs.getString("product_category_id"),
                        rs.getString("product_description_detail"),
                        rs.getBoolean("active"));
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
        return product;
    }

    // Kiểm tra sự tồn tại của sản phẩm
    public boolean productExists(int productId) {
        String sql = "SELECT COUNT(*) FROM Products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
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
        return false;
    }

    // Lấy tất cả danh mục
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT DISTINCT product_category_name FROM Product_Categories";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("product_category_name"));
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
        return categories;
    }

    public List<Product> getProductsByCategory(String categoryName) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT p.* FROM Products p "
                    + "JOIN Product_Categories c ON p.product_category_id = c.product_category_id "
                    + "WHERE c.product_category_name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, categoryName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock_quantity"),
                        rs.getString("product_image_url"),
                        rs.getString("product_category_id"),
                        rs.getString("product_description_detail"),
                        rs.getBoolean("active")
                ));
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
        return list;
    }

    public List<Product> searchProducts(String query) {
        List<Product> productList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT * FROM Products WHERE product_name LIKE ? OR product_description LIKE ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock_quantity"),
                        rs.getString("product_image_url"),
                        rs.getString("product_category_id"),
                        rs.getString("product_description_detail"),
                        rs.getBoolean("active")
                );
                productList.add(product);
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
        return productList;
    }

    public void updateProductQuantity(int productId, int quantityChange) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "UPDATE Products SET product_stock_quantity = product_stock_quantity + ? WHERE product_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, productId);
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

    // Lấy danh sách các sản phẩm tương tự
    public List<Product> getSimilarProducts(int productId) {
        List<Product> similarProducts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "SELECT * FROM Products WHERE product_category_id = (SELECT product_category_id FROM Products WHERE product_id = ?) AND product_id != ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("product_price"),
                        rs.getInt("product_stock_quantity"),
                        rs.getString("product_image_url"),
                        rs.getString("product_category_id"),
                        rs.getString("product_description_detail"),
                        rs.getBoolean("active")
                );
                similarProducts.add(product);
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
        return similarProducts;
    }

    // Phương thức lấy số lượng sản phẩm hiện có
    public int getProductQuantity(int productId) {
        String sql = "SELECT product_stock_quantity FROM Products WHERE product_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_stock_quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void increaseProductQuantity(int productId, int quantityToAdd) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnect.getConnection();
            String sql = "UPDATE Products SET product_stock_quantity = product_stock_quantity + ? WHERE product_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantityToAdd);
            stmt.setInt(2, productId);
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
    }
