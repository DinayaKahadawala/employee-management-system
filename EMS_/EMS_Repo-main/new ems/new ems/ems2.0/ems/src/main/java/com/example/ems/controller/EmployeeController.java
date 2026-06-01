package com.example.ems.controller;

import com.example.ems.domain.employee.ContractEmployee;
import com.example.ems.domain.employee.EmployeeDocument;
import com.example.ems.domain.employee.EmployeeProfile;
import com.example.ems.domain.employee.FullTimeEmployee;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "active", required = false) Boolean active,
                         Model model) {
        model.addAttribute("employees", employeeService.listAll(q, active));
		model.addAttribute("q", q == null ? "" : q);
		model.addAttribute("active", active);
		return "employee/list";
	}

	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("fullTimeEmployee", new FullTimeEmployee());
		model.addAttribute("contractEmployee", new ContractEmployee());
		return "employee/new";
	}

	@PostMapping("/new/fulltime")
	public String createFullTime(@Valid @ModelAttribute("fullTimeEmployee") FullTimeEmployee e, BindingResult br) {
		if (br.hasErrors()) return "employee/new";
		employeeService.saveFullTime(e);
		return "redirect:/employee";
	}

	@PostMapping("/new/contract")
	public String createContract(@Valid @ModelAttribute("contractEmployee") ContractEmployee e, BindingResult br) {
		if (br.hasErrors()) return "employee/new";
		employeeService.saveContract(e);
		return "redirect:/employee";
	}

	@GetMapping("/{id}")
	public String view(@PathVariable Long id, Model model) {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		model.addAttribute("employee", e);
		model.addAttribute("documents", employeeService.listDocuments(e));
		return "employee/view";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		model.addAttribute("employee", e);
		return "employee/edit";
	}

	@PostMapping("/{id}/edit")
	public String update(@PathVariable Long id,
						@RequestParam String firstName,
						@RequestParam String lastName,
						@RequestParam String email,
						@RequestParam String phone,
						@RequestParam String department,
						@RequestParam String jobTitle,
						@RequestParam(required = false, defaultValue = "false") boolean active,
						@RequestParam(required = false) MultipartFile photo) throws IOException {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		e.setFirstName(firstName);
		e.setLastName(lastName);
		e.setEmail(email);
		e.setPhone(phone);
		e.setDepartment(department);
		e.setJobTitle(jobTitle);
		e.setActive(active);
		if (photo != null && !photo.isEmpty()) {
			String filename = employeeService.storeProfilePhoto(photo);
			e.setPhotoFilename(filename);
		}
		employeeService.save(e);
		return "redirect:/employee";
	}

	@PostMapping("/{id}/deactivate")
	public String deactivate(@PathVariable Long id) {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		e.setActive(false);
		employeeService.save(e);
		return "redirect:/employee/" + id;
	}

	@PostMapping("/{id}/activate")
	public String activate(@PathVariable Long id) {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		e.setActive(true);
		employeeService.save(e);
		return "redirect:/employee/" + id;
	}

	@PostMapping("/{id}/documents")
	public String uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		if (!file.isEmpty()) {
			employeeService.storeDocument(e, file);
		}
		return "redirect:/employee/" + id;
	}

	@PostMapping("/{id}/documents/{docId}/delete")
	public String deleteDocument(@PathVariable Long id, @PathVariable Long docId) throws IOException {
		EmployeeProfile e = employeeService.findById(id).orElseThrow();
		for (EmployeeDocument d : employeeService.listDocuments(e)) {
			if (d.getId().equals(docId)) {
				employeeService.deleteDocument(d);
				break;
			}
		}
		return "redirect:/employee/" + id;
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		employeeService.delete(id);
		return "redirect:/employee";
	}
}


