package edu.tcu.cs.employeemanagementonline.manager.converter;

import edu.tcu.cs.employeemanagementonline.manager.Manager;
import edu.tcu.cs.employeemanagementonline.manager.dto.ManagerDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ManagerToManagerDto implements Converter<Manager, ManagerDto> {

    @Override
    public ManagerDto convert(Manager source) {

        return new ManagerDto(source.getId(),
                                source.getName(),
                                source.getNumberOfArtifacts());
    }
}
