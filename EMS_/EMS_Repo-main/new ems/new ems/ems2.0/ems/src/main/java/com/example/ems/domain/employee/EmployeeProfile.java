package com.example.ems.domain.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employment_type")
public abstract class EmployeeProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "first_name")
	private String firstName;

	@NotBlank
	@Column(name = "last_name")
	private String lastName;

	@Email
	@NotBlank
	@Column(unique = true)
	private String email;

	@NotBlank
	private String phone;

	@NotBlank
	private String department;

	@NotBlank
	@Column(name = "job_title")
	private String jobTitle;

	@NotNull
	private Boolean active = true;

	// Optional profile photo filename stored under uploads/
	@Column(name = "photo_filename")
	private String photoFilename;
}


