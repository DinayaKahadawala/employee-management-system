package com.example.ems.domain.employee;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("FULL_TIME")
public class FullTimeEmployee extends EmployeeProfile {

	private Double annualSalary;
}


