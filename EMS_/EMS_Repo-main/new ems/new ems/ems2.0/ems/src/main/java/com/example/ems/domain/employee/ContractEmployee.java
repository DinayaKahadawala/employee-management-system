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
@DiscriminatorValue("CONTRACT")
public class ContractEmployee extends EmployeeProfile {

	private Double hourlyRate;
}


