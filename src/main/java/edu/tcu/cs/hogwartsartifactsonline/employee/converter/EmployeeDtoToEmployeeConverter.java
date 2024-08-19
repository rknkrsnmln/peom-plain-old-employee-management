package edu.tcu.cs.hogwartsartifactsonline.employee.converter;

import edu.tcu.cs.hogwartsartifactsonline.employee.Employee;
import edu.tcu.cs.hogwartsartifactsonline.employee.dto.EmployeeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDtoToEmployeeConverter implements Converter<EmployeeDto, Employee> {

    @Override
    public Employee convert(EmployeeDto source) {
        Employee employee = new Employee();
        employee.setId(source.id());
        employee.setName(source.name());
        employee.setDescription(source.description());
        employee.setImageUrl(source.imageUrl());
        return employee;
    }
}
