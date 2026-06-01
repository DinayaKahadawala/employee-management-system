package com.example.ems.service.notification;

import com.example.ems.domain.notification.Notification;
import com.example.ems.domain.user.UserAccount;
import com.example.ems.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class InAppDeliveryHandler implements DeliveryHandler {

    private final NotificationRepository notificationRepository;

    public InAppDeliveryHandler(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public boolean canDeliver(Notification.DeliveryMethod method) {
        return method == Notification.DeliveryMethod.IN_APP;
    }

    @Override
    public void deliver(Notification notification, UserAccount recipient) {
        // In-app notifications are already saved to database
        // This handler is called for additional processing if needed
        System.out.println("In-app notification delivered: " + notification.getTitle() + " to " + recipient.getUsername());
    }

    @Override
    public String getHandlerName() {
        return "In-App Delivery";
    }
}
