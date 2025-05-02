package Model;

public class OrderDetail {

    private int orderDetailId; // ID chi tiết đơn hàng
    private int orderId; // ID đơn hàng
    private int productId; // ID sản phẩm
    private Integer serviceBookingId; // ID dịch vụ liên quan (có thể null)
    private int quantity; // Số lượng sản phẩm
    private double price; // Giá sản phẩm
    private String orderStatus; // Thêm thuộc tính này

    // Thuộc tính bổ sung
    private String productName; // Tên sản phẩm
    private String productImage; // Hình ảnh sản phẩm

    // Getters và setters
    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getServiceBookingId() {
        return serviceBookingId;
    }

    public void setServiceBookingId(Integer serviceBookingId) {
        this.serviceBookingId = serviceBookingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    // Phương thức toString để debug
    @Override
    public String toString() {
        return "OrderDetail{"
                + "orderDetailId=" + orderDetailId
                + ", orderId=" + orderId
                + ", productId=" + productId
                + ", serviceBookingId=" + serviceBookingId
                + ", quantity=" + quantity
                + ", price=" + price
                + ", orderStatus='" + orderStatus + '\''
                + ", productName='" + productName + '\''
                + ", productImage='" + productImage + '\''
                + '}';
    }
}
