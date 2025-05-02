package Controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    private static final String senderEmail = "petquespact@gmail.com"; // Email
    private static final String senderPassword = "ewdl hqap xbjd cdqt"; // Mật khẩu ứng dụng

    public static void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new MessagingException("Lỗi khi gửi email.");
        }
    }

    public static void sendOTPEmail(String recipientEmail, String otpCode) throws MessagingException {
        String subject = "Mã OTP xác thực";
        String content = "Mã OTP của bạn là: " + otpCode + "\nMã này có hiệu lực trong 1 phút.";
        sendEmail(recipientEmail, subject, content);
    }

    public static void sendOrderConfirmationEmail(String recipientEmail, String subject, String content) throws MessagingException {
        sendEmail(recipientEmail, subject, content);
    }
}