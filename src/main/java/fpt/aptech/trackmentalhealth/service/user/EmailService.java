package fpt.aptech.trackmentalhealth.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            String normalizedEmail = toEmail.trim().toLowerCase();
            System.out.println("Sending OTP to: " + normalizedEmail + " | OTP: " + otp);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(normalizedEmail);
            helper.setSubject("🔑 Your OTP Code");

            // HTML content có logo, màu sắc chữa lành (xanh dương + vàng nhạt)
            String htmlContent = """
                        <div style="font-family: Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden;">
                            <!-- Header -->
                            <div style="background: linear-gradient(90deg, #1976D2, #FFC107);
                                        padding: 20px; text-align: center; color: white;">
                                <img src="cid:logoTMH" alt="Logo"
                                     style="width: 100%%; max-width: 500px; height: auto; display: block; margin: 0 auto;"/>
                            </div>
                            <!-- Body -->
                            <div style="padding: 25px;">
                                <p style="font-size: 16px; color: #333;">Hello,</p>
                                <p style="font-size: 15px; color: #555;">Here is your One-Time Password (OTP). Please use it to continue your process securely:</p>
                    
                                <div style="text-align: center; margin: 30px 0;">
                                    <span style="font-size: 32px; font-weight: bold; color: #1976D2; background-color: #FFF3CD; padding: 12px 25px; border-radius: 8px; display: inline-block;">
                                        %s
                                    </span>
                                </div>
                    
                                <p style="color: #555;">This OTP will expire in <strong>5 minutes</strong>.</p>
                                <p style="color: #555;">If you did not request this code, please ignore this email.</p>
                                <p style="margin-top: 20px; color: #1976D2; font-style: italic;">💙 Take a deep breath. Healing starts with every small step.</p>
                            </div>
                    
                            <!-- Footer -->
                            <div style="background-color: #f5f5f5; text-align: center; padding: 15px; font-size: 12px; color: #777;">
                                <p>© 2025 TrackMentalHealth. All rights reserved.</p>
                            </div>
                        </div>
                    """.formatted(otp);


            helper.setText(htmlContent, true);

            // Thêm logo từ thư mục uploads
            FileSystemResource logo = new FileSystemResource(new File("uploads/LogoTMH.png"));
            if (logo.exists()) {
                helper.addInline("logoTMH", logo);
            } else {
                System.out.println("⚠️ Logo file not found at uploads/LogoTMH.png");
            }

            javaMailSender.send(mimeMessage);

            System.out.println("✅ OTP email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
