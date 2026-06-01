package com.example.ems.controller;

import com.example.ems.domain.notification.Notification;
import com.example.ems.domain.user.UserAccount;
import com.example.ems.repository.NotificationRepository;
import com.example.ems.repository.UserAccountRepository;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepo;
    private final UserAccountRepository userRepo;

	public NotificationController(NotificationRepository notificationRepo, UserAccountRepository userRepo) {
		this.notificationRepo = notificationRepo;
		this.userRepo = userRepo;
	}

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "false") boolean unread,
                       @RequestParam(required = false) Long recipientId,
                       Authentication auth,
                       Model model) {
        String username = auth.getName();
        UserAccount currentUser = userRepo.findByUsername(username).orElseThrow();

        List<Notification> notifications;
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR"));

        if (isAdmin || isHr) {
            // Admin/HR can view all notifications or filter by a recipient
            if (recipientId != null) {
                UserAccount target = userRepo.findById(recipientId).orElseThrow();
                notifications = unread
                    ? notificationRepo.findByRecipientAndReadFalse(target)
                    : notificationRepo.findByRecipientOrderByCreatedAtDesc(target);
            } else {
                notifications = unread
                    ? notificationRepo.findByReadFalseOrderByCreatedAtDesc()
                    : notificationRepo.findAllByOrderByCreatedAtDesc();
            }
        } else {
            // Non-admins see only their own
            notifications = unread
                ? notificationRepo.findByRecipientAndReadFalse(currentUser)
                : notificationRepo.findByRecipientOrderByCreatedAtDesc(currentUser);
        }

        long unreadCount = (isAdmin || isHr)
            ? notificationRepo.countByReadFalse()
            : notificationRepo.findByRecipientAndReadFalse(currentUser).size();

        model.addAttribute("notifications", notifications);
        model.addAttribute("unread", unread);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("unreadNotificationCount", unreadCount);
        model.addAttribute("isAdminOrHr", isAdmin || isHr);
        model.addAttribute("filterRecipientId", recipientId);
        return "notification/list";
    }

	@PostMapping("/mark-read")
	public String markRead(@RequestParam Long id, Authentication auth) {
		String username = auth.getName();
		UserAccount currentUser = userRepo.findByUsername(username).orElseThrow();
		
		var n = notificationRepo.findById(id).orElseThrow();
		// Ensure user can only mark their own notifications as read
		if (n.getRecipient().equals(currentUser)) {
			n.setRead(true);
			notificationRepo.save(n);
		}
		return "redirect:/notifications";
	}

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, Authentication auth) {
        String username = auth.getName();
        UserAccount currentUser = userRepo.findByUsername(username).orElseThrow();
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));

        var n = notificationRepo.findById(id).orElseThrow();
        // Admin/HR can delete any notification; others only their own
        if (isAdminOrHr || n.getRecipient().equals(currentUser)) {
            notificationRepo.deleteById(id);
        }
        return "redirect:/notifications";
    }

    @PostMapping("/delete-all")
    public String deleteAll(Authentication auth) {
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));
        if (!isAdminOrHr) {
            return "redirect:/notifications?error=forbidden";
        }
        notificationRepo.deleteAll();
        return "redirect:/notifications";
    }

    @GetMapping("/new")
    public String createForm(Model model, Authentication auth) {
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));
        if (!isAdminOrHr) {
            return "redirect:/notifications?error=forbidden";
        }
        model.addAttribute("users", userRepo.findAll());
        return "notification/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam Long recipientId,
                       @RequestParam String message,
                       @RequestParam(required = false, defaultValue = "false") boolean read,
                       Authentication auth) {
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));
        if (!isAdminOrHr) {
            return "redirect:/notifications?error=forbidden";
        }
        Notification n = new Notification();
        n.setRecipient(userRepo.findById(recipientId).orElseThrow());
        n.setMessage(message);
        n.setRead(read);
        notificationRepo.save(n);
        return "redirect:/notifications";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication auth) {
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));
        if (!isAdminOrHr) {
            return "redirect:/notifications?error=forbidden";
        }
        Notification n = notificationRepo.findById(id).orElseThrow();
        model.addAttribute("notification", n);
        model.addAttribute("users", userRepo.findAll());
        return "notification/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                       @RequestParam Long recipientId,
                       @RequestParam String message,
                       @RequestParam(required = false, defaultValue = "false") boolean read,
                       Authentication auth) {
        boolean isAdminOrHr = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_HR"));
        if (!isAdminOrHr) {
            return "redirect:/notifications?error=forbidden";
        }
        Notification n = notificationRepo.findById(id).orElseThrow();
        n.setRecipient(userRepo.findById(recipientId).orElseThrow());
        n.setMessage(message);
        n.setRead(read);
        notificationRepo.save(n);
        return "redirect:/notifications";
    }

}


