package edu.tcu.cs.employeemanagementonline.employee.dto;

import edu.tcu.cs.employeemanagementonline.manager.dto.ManagerDto;
import jakarta.validation.constraints.NotEmpty;

public record EmployeeDto(String id,
                          @NotEmpty(message = "name is required")
                          String name,
                          @NotEmpty(message = "description is required")
                          String description,
                          @NotEmpty(message = "imageUrl is required")
                          String imageUrl,
                          ManagerDto owner) {
}
