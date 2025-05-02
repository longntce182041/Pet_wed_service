package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/VnpayIpnServlet")
public class VnpayIpnServlet extends HttpServlet {

    private static final String VNP_HASHSECRET = "8RJFQ4F0G4MFSYGVYYA27XOABX8RJEW4";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> fields = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            fields.put(entry.getKey(), entry.getValue()[0]);
        }

        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        String hashData = VnpayUtils.createHashData(fields);
        String calculatedHash = VnpayUtils.hmacSHA256(VNP_HASHSECRET, hashData);

        System.out.println("Received fields: " + fields);
        System.out.println("Received vnp_SecureHash: " + vnp_SecureHash);
        System.out.println("Calculated hash: " + calculatedHash);

        if (calculatedHash.equals(vnp_SecureHash)) {
            // Xử lý trạng thái giao dịch
            String transactionStatus = fields.get("vnp_TransactionStatus");
            String transactionId = fields.get("vnp_TxnRef");

            if ("00".equals(transactionStatus)) {
                // Giao dịch thành công
                System.out.println("Transaction successful. Transaction ID: " + transactionId);
                updateTransactionStatus(transactionId, "SUCCESS");
                response.setStatus(HttpServletResponse.SC_OK); // HTTP 200
                response.getWriter().write("success");
            } else {
                // Giao dịch thất bại
                System.out.println("Transaction failed. Status: " + transactionStatus);
                updateTransactionStatus(transactionId, "FAILED");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // HTTP 400
                response.getWriter().write("fail");
            }
        } else {
            // Chữ ký không hợp lệ
            System.out.println("Invalid signature.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // HTTP 403
            response.getWriter().write("invalid signature");
        }
    }

    private void updateTransactionStatus(String transactionId, String status) {
        // Kết nối đến cơ sở dữ liệu và cập nhật trạng thái giao dịch
        System.out.println("Updating transaction " + transactionId + " to status " + status);
        // Thực hiện logic cập nhật cơ sở dữ liệu tại đây
    }
}