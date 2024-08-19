package edu.tcu.cs.hogwartsartifactsonline.artifact.converter;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Employee;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.EmployeeDto;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.ManagerToManagerDto;
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
