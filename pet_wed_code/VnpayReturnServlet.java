package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/VnpayReturnServlet")
public class VnpayReturnServlet extends HttpServlet {

    private static final String VNP_HASHSECRET = "8RJFQ4F0G4MFSYGVYYA27XOABX8RJEW4";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            // Thành công
            response.getWriter().println("Payment successful!");
        } else {
            // Thất bại
            response.getWriter().println("Invalid payment signature!");
        }
    }
}
