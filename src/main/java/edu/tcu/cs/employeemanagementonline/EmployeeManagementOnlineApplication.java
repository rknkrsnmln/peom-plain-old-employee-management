package edu.tcu.cs.employeemanagementonline;

import edu.tcu.cs.employeemanagementonline.employee.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeManagementOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementOnlineApplication.class, args);
	}

	@Bean
	public IdWorker idWorker() {
		return new IdWorker(1,1);
	}
}
