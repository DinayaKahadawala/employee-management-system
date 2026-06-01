package com.example.ems.service.notification;

import com.example.ems.domain.notification.Notification;
import com.example.ems.domain.user.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class EmailDeliveryHandler implements DeliveryHandler {

    @Override
    public boolean canDeliver(Notification.DeliveryMethod method) {
        return method == Notification.DeliveryMethod.EMAIL;
    }

    @Override
    public void deliver(Notification notification, UserAccount recipient) {
        // In a real application, integrate with email service (SendGrid, AWS SES, etc.)
        System.out.println("Email notification sent:");
        System.out.println("To: " + recipient.getUsername() + "@company.com");
        System.out.println("Subject: " + notification.getTitle());
        System.out.println("Body: " + notification.getMessage());
        System.out.println("Priority: " + notification.getPriority().getDisplayName());
        System.out.println("Alert Type: " + notification.getAlertType().getDisplayName());
    }

    @Override
    public String getHandlerName() {
        return "Email Delivery";
    }
}
