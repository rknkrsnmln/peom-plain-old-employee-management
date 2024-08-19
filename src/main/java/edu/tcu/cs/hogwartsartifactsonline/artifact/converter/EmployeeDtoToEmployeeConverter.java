package edu.tcu.cs.hogwartsartifactsonline.artifact.converter;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Employee;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.EmployeeDto;
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
