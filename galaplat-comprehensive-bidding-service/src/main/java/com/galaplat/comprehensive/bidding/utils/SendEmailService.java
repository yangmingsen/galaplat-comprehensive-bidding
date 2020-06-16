package com.galaplat.comprehensive.reservation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class SendEmailService {
	Logger logger = LoggerFactory.getLogger(SendEmailService.class);
	@Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;
	
	public void sendemail(String to, String title, String content) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            String[] toAddresses = to.split(";");
            helper.setTo(toAddresses);
            helper.setSubject(title);
            helper.setText(content, true);
            sender.send(message);
        } catch (Exception e) {
        	logger.error("发送html邮件时发生异常！", e);
        }
    }

}
