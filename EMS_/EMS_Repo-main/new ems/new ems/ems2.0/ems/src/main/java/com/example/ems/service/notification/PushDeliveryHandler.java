package com.example.ems.service.notification;

import com.example.ems.domain.notification.Notification;
import com.example.ems.domain.user.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class PushDeliveryHandler implements DeliveryHandler {

    @Override
    public boolean canDeliver(Notification.DeliveryMethod method) {
        return method == Notification.DeliveryMethod.PUSH;
    }

    @Override
    public void deliver(Notification notification, UserAccount recipient) {
        // In a real application, integrate with push notification service (FCM, APNS, etc.)
        System.out.println("Push notification sent:");
        System.out.println("To: " + recipient.getUsername());
        System.out.println("Title: " + notification.getTitle());
        System.out.println("Body: " + notification.getMessage());
        System.out.println("Priority: " + notification.getPriority().getDisplayName());
        System.out.println("Alert Type: " + notification.getAlertType().getDisplayName());
    }

    @Override
    public String getHandlerName() {
        return "Push Delivery";
    }
}
