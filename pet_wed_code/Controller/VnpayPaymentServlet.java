package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/VnpayPaymentServlet")
public class VnpayPaymentServlet extends HttpServlet {

    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_TMNCODE = "AL0QUF81"; // Mã TmnCode của bạn
    private static final String VNP_HASHSECRET = "8RJFQ4F0G4MFSYGVYYA27XOABX8RJEW4"; // Chuỗi bí mật
    private static final String VNP_RETURNURL = "http://localhost:8080/PetiqueSpa/VnpayReturnServlet"; // URL trả về

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin từ request
        String orderInfo = request.getParameter("orderInfo");
        String amount = request.getParameter("amount");
        String bankCode = request.getParameter("bankCode");

        // Kiểm tra các tham số bắt buộc
        if (orderInfo == null || orderInfo.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order information is required.");
            return;
        }

        if (amount == null || amount.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount is required for payment.");
            return;
        }

        try {
            // Kiểm tra giá trị amount
            int amountValue = Integer.parseInt(amount);
            if (amountValue <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount must be greater than zero.");
                return;
            }

            // Tạo mã giao dịch duy nhất
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());

            // Chuẩn bị các tham số gửi đến VNPAY
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", VNP_TMNCODE);
            vnp_Params.put("vnp_Amount", String.valueOf(amountValue * 100)); // Nhân với 100 để chuyển sang đơn vị VND
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNP_RETURNURL);

            if (bankCode != null && !bankCode.trim().isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            // Thêm thời gian tạo giao dịch
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            vnp_Params.put("vnp_CreateDate", formatter.format(date));

            // Sắp xếp các tham số theo thứ tự bảng chữ cái
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(fieldValue).append('&');
                    query.append(URLEncoder.encode(fieldName, "UTF-8")).append('=')
                         .append(URLEncoder.encode(fieldValue, "UTF-8")).append('&');
                }
            }

            // Xóa ký tự '&' cuối cùng
            hashData.deleteCharAt(hashData.length() - 1);
            query.deleteCharAt(query.length() - 1);

            // Tạo chữ ký bảo mật
            String secureHash = VnpayUtils.hmacSHA256(VNP_HASHSECRET, hashData.toString());
            query.append("&vnp_SecureHash=").append(URLEncoder.encode(secureHash, "UTF-8"));

            // Tạo URL thanh toán
            String paymentUrl = VNP_URL + "?" + query.toString();
            System.out.println("VNPAY Parameters: " + vnp_Params);
            System.out.println("Payment URL: " + paymentUrl);
            response.sendRedirect(paymentUrl); // Chuyển hướng người dùng đến URL thanh toán

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount format. Please enter a valid number.");
        }
    }
}