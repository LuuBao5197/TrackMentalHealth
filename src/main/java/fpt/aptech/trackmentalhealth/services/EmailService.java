package fpt.aptech.trackmentalhealth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            String normalizedEmail = toEmail.trim().toLowerCase(); // Chuẩn hóa email
            System.out.println("Sending OTP to: " + normalizedEmail + " | OTP: " + otp); // log
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(normalizedEmail);
            message.setSubject("OTP to recover password");
            message.setText("Your OTP: " + otp + "\nEffective in 5 minutes.");
            javaMailSender.send(message);
            System.out.println("OTP email sent successfully");
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }


}