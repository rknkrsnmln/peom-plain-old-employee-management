package edu.tcu.cs.hogwartsartifactsonline.employee.converter;

import edu.tcu.cs.hogwartsartifactsonline.employee.Employee;
import edu.tcu.cs.hogwartsartifactsonline.employee.dto.EmployeeDto;
import edu.tcu.cs.hogwartsartifactsonline.manager.converter.ManagerToManagerDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToEmployeeDtoConverter implements Converter<Employee, EmployeeDto> {

    private final ManagerToManagerDto managerToManagerDto;

    public EmployeeToEmployeeDtoConverter(ManagerToManagerDto managerToManagerDto) {
        this.managerToManagerDto = managerToManagerDto;
    }

    @Override
    public EmployeeDto convert(Employee source) {
        return new EmployeeDto(source.getId(),
                                source.getName(),
                                source.getDescription(),
                                source.getImageUrl(),
                                source.getOwner() != null
                                        ? this.managerToManagerDto.convert(source.getOwner())
                                        : null);
    }
}
