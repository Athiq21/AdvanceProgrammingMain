package services;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Timestamp;
import java.util.Properties;

public class EmailService {

    public void sendOtpEmail(String toEmail, String otp, Timestamp otpExpiration) throws MessagingException {
        String smtpHost = "smtp-relay.brevo.com";
        String smtpPort = "587";
        String smtpUser = "7a1d10001@smtp-brevo.com";
        String smtpPassword = "CEpqPmD3RbT7vakw";
        String senderEmail = "jadeyaa22@gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        String subject = "Mega City Cab Verification";
        String body =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>OTP Verification</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            width: 100%;\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background-color: #ffffff;\n" +
                        "            padding: 20px;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            text-align: center;\n" +
                        "            padding: 10px 0;\n" +
                        "            background-color: #FF8C00;\n" +
                        "            color: #ffffff;\n" +
                        "            border-radius: 8px 8px 0 0;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            padding: 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .otp {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: bold;\n" +
                        "            color: #333333;\n" +
                        "        }\n" +
                        "        .expiration {\n" +
                        "            margin-top: 10px;\n" +
                        "            font-size: 14px;\n" +
                        "            color: #888888;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            padding: 10px 0;\n" +
                        "            font-size: 12px;\n" +
                        "            color: #888888;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>OTP Verification</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <p>Your One-Time Password (OTP) for Mega City Cab is:</p>\n" +
                        "            <p class=\"otp\">" + otp + "</p>\n" +
                        "            <p class=\"expiration\">This OTP is valid until " + otpExpiration + ".</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>If you did not request this OTP, please ignore this email.</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail)); // Set the sender email address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Set the recipient
        message.setSubject(subject); // Set the subject
        message.setContent(body, "text/html"); // Set the email content as HTML

        // Send the email
        Transport.send(message);
    }


    public void resendOtpEmail(String toEmail, String newOtp, Timestamp newOtpExpiration) throws MessagingException {
        String smtpHost = "smtp-relay.brevo.com";
        String smtpPort = "587";
        String smtpUser = "7a1d10001@smtp-brevo.com";
        String smtpPassword = "CEpqPmD3RbT7vakw";
        String senderEmail = "jadeyaa22@gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        String subject = "Resend OTP";
        String body =
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>OTP Resent</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            width: 100%;\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background-color: #ffffff;\n" +
                        "            padding: 20px;\n" +
                        "            border-radius: 8px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "        }\n" +
                        "        .header {\n" +
                        "            text-align: center;\n" +
                        "            padding: 10px 0;\n" +
                        "            background-color: #FF8C00;\n" +
                        "            color: #ffffff;\n" +
                        "            border-radius: 8px 8px 0 0;\n" +
                        "        }\n" +
                        "        .content {\n" +
                        "            padding: 20px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        .otp {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: bold;\n" +
                        "            color: #333333;\n" +
                        "        }\n" +
                        "        .expiration {\n" +
                        "            margin-top: 10px;\n" +
                        "            font-size: 14px;\n" +
                        "            color: #888888;\n" +
                        "        }\n" +
                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            padding: 10px 0;\n" +
                        "            font-size: 12px;\n" +
                        "            color: #888888;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Your OTP Has Been Resent</h1>\n" +
                        "        </div>\n" +
                        "        <div class=\"content\">\n" +
                        "            <p>Your new One-Time Password (OTP) for Mega City Cab is:</p>\n" +
                        "            <p class=\"otp\">" + newOtp + "</p>\n" +
                        "            <p class=\"expiration\">This OTP is valid until " + newOtpExpiration + ".</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>If you did not request this OTP, please ignore this email.</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail)); // Set the sender email address
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Set the recipient
        message.setSubject(subject); // Set the subject
        message.setContent(body, "text/html"); // Set the email content as HTML

        // Send the email
        Transport.send(message);
    }

}
