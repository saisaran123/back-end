package com.kunto.util;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class Mail {
    private static Mail instance;  // Singleton instance
    private final String senderEmail;
    private final String password;
    private final Session session;
    private final Random random = new Random();
    private String otp;

    // Corrected Constructor Name
    private Mail(String senderEmail, String password) {
        this.senderEmail = senderEmail;
        this.password = password;

        // Configure SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a single session instance
        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });
    }

    // Singleton method to get the instance (Ensuring only one instance)
    public static Mail getInstance(String senderEmail, String password) {
        if (instance == null) {
            instance = new Mail(senderEmail, password);
        }
        return instance;
    }

    // Generate a random OTP of given length
    public String generateOTP(int length) {
        String chars = "0123456789";
        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otpBuilder.append(chars.charAt(random.nextInt(chars.length())));
        }
        return otpBuilder.toString();
    }

    public String getOtp() {
		return otp;
	}
    // Prepare and send an email
    public boolean sendOTP(String recipient) {
        otp = generateOTP(6);  // Generate a 6-digit OTP

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("🚨 Your OTP is Escaping! Catch It Fast!");
            message.setText(
                    "Hey Superhuman,\n\n" +
                            "Your One-Time Password is: " + otp + "\n" +
                            "But hurry! This OTP is like my ex—it won’t wait for you forever. In 10 minutes, it will vanish into thin air.\n\n" +
                            "Stay Secure,\n\n" +
                            "[ZS Private Limited]"
            );

            Transport.send(message);
            System.out.println("OTP Sent Successfully to " + recipient);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
