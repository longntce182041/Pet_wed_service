package Model;

public class Rating {
    private int ratingId;
    private String customerId; // ID khách hàng
    private int ratingStar; // Số sao đánh giá (1-5)
    private String comment; // Nội dung đánh giá
    private int orderDetailId; // ID chi tiết đơn hàng
    private String customerName; // Thêm thuộc tính này

    // Constructor không tham số
    public Rating() {
    }

    // Constructor đầy đủ tham số
    public Rating(String customerId, int ratingStar, String comment, int orderDetailId) {
        this.customerId = customerId;
        this.ratingStar = ratingStar;
        this.comment = comment;
        this.orderDetailId = orderDetailId;
    }

    // Getters và setters
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Phương thức toString để debug
    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", customerId='" + customerId + '\'' +
                ", ratingStar=" + ratingStar +
                ", comment='" + comment + '\'' +
                ", orderDetailId=" + orderDetailId +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}