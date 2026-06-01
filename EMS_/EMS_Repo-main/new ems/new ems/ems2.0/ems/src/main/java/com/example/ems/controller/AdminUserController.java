package com.example.ems.controller;

import com.example.ems.domain.user.*;
import com.example.ems.repository.RoleRepository;
import com.example.ems.repository.UserAccountRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

	private final UserAccountRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;

	public AdminUserController(UserAccountRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("users", userRepo.findAll());
		model.addAttribute("roles", roleRepo.findAll());
		return "admin/users/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("userTypes", List.of("ADMIN", "MANAGER", "EMPLOYEE"));
		model.addAttribute("roles", roleRepo.findAll());
		return "admin/users/new";
	}

	@PostMapping
	public String create(@RequestParam String username,
					   @RequestParam String password,
					   @RequestParam String userType,
					   @RequestParam(required = false) Set<Long> roleIds) {
		UserAccount u = createUserByType(userType, username, password, true);
		if (roleIds != null && !roleIds.isEmpty()) {
			Set<Role> roles = new HashSet<>(roleRepo.findAllById(roleIds));
			u.setRoles(roles);
		}
		userRepo.save(u);
		return "redirect:/admin/users";
	}

	private UserAccount createUserByType(String userType, String username, String password, boolean enabled) {
		return switch (userType.toUpperCase()) {
			case "ADMIN" -> {
				AdminUser admin = new AdminUser(username, passwordEncoder.encode(password), enabled);
				yield userRepo.save(admin);
			}
			case "MANAGER" -> {
				ManagerUser manager = new ManagerUser(username, passwordEncoder.encode(password), enabled);
				yield userRepo.save(manager);
			}
			case "EMPLOYEE" -> {
				EmployeeUser employee = new EmployeeUser(username, passwordEncoder.encode(password), enabled);
				yield userRepo.save(employee);
			}
			default -> throw new IllegalArgumentException("Invalid user type: " + userType);
		};
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		UserAccount u = userRepo.findById(id).orElseThrow();
		model.addAttribute("user", u);
		model.addAttribute("roles", roleRepo.findAll());
		return "admin/users/edit";
	}

	@PostMapping("/{id}/edit")
	public String update(@PathVariable Long id,
					  @RequestParam String username,
					  @RequestParam(required = false) String password,
					  @RequestParam(required = false, defaultValue = "false") boolean enabled,
					  @RequestParam(required = false) Set<Long> roleIds) {
		UserAccount u = userRepo.findById(id).orElseThrow();
		u.setUsername(username);
		u.setEnabled(enabled);
		if (password != null && !password.isBlank()) {
			u.setPassword(passwordEncoder.encode(password));
		}
		Set<Role> roles = roleIds == null ? new HashSet<>() : new HashSet<>(roleRepo.findAllById(roleIds));
		u.setRoles(roles);
		userRepo.save(u);
		return "redirect:/admin/users";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		userRepo.deleteById(id);
		return "redirect:/admin/users";
	}

	@PostMapping("/roles")
	public String createRole(@RequestParam String name) {
		roleRepo.findByName(name).orElseGet(() -> {
			Role r = new Role();
			r.setName(name);
			return roleRepo.save(r);
		});
		return "redirect:/admin/users";
	}

	@PostMapping("/roles/{id}/delete")
	public String deleteRole(@PathVariable Long id) {
		roleRepo.deleteById(id);
		return "redirect:/admin/users";
	}
}


