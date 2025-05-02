package Model;

import java.util.Date;

public class Promotion {
    private String promotionId;
    private String promotionName;
    private String promotionDescription;
    private String discountType;
    private double discountValue;
    private Date startDate;
    private Date endDate;
    private Double minOrderValue;
    private Double maxDiscount;
    private boolean isActive;

    // Getters and setters
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(Double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isValid(double orderValue) {
        Date now = new Date();

        // Kiểm tra nếu ngày bắt đầu hoặc ngày kết thúc không hợp lệ
        if (startDate == null || endDate == null) {
            System.out.println("Start date or end date is null.");
            return false;
        }

        // Kiểm tra nếu giá trị đơn hàng không hợp lệ
        if (orderValue <= 0) {
            System.out.println("Order value is invalid: " + orderValue);
            return false;
        }

        // Kiểm tra ngày hiện tại có nằm trong khoảng thời gian hợp lệ
        if (!now.after(startDate) || !now.before(endDate)) {
            System.out.println("Current date is not within the promotion period.");
            return false;
        }

        // Kiểm tra giá trị đơn hàng tối thiểu
        if (minOrderValue != null && orderValue < minOrderValue) {
            System.out.println("Order value does not meet the minimum order value.");
            return false;
        }

        // Nếu tất cả các điều kiện đều hợp lệ
        System.out.println("Promotion is valid.");
        return true;
    }
}