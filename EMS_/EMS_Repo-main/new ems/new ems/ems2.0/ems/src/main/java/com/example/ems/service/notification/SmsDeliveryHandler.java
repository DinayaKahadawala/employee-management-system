package com.example.ems.service.notification;

import com.example.ems.domain.notification.Notification;
import com.example.ems.domain.user.UserAccount;
import org.springframework.stereotype.Service;

@Service
public class SmsDeliveryHandler implements DeliveryHandler {

    @Override
    public boolean canDeliver(Notification.DeliveryMethod method) {
        return method == Notification.DeliveryMethod.SMS;
    }

    @Override
    public void deliver(Notification notification, UserAccount recipient) {
        // In a real application, integrate with SMS service (Twilio, AWS SNS, etc.)
        System.out.println("SMS notification sent:");
        System.out.println("To: +1234567890"); // Would use recipient's phone number
        System.out.println("Message: " + notification.getTitle() + " - " + notification.getMessage());
        System.out.println("Priority: " + notification.getPriority().getDisplayName());
    }

    @Override
    public String getHandlerName() {
        return "SMS Delivery";
    }
}
