package edu.tcu.cs.employeemanagementonline.manager.converter;

import edu.tcu.cs.employeemanagementonline.manager.Manager;
import edu.tcu.cs.employeemanagementonline.manager.dto.ManagerDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ManagerDtoToManager implements Converter<ManagerDto, Manager> {
    @Override
    public Manager convert(ManagerDto source) {
        Manager manager = new Manager();
        manager.setId(source.id());
        manager.setName(source.name());
        return manager;
    }
}
