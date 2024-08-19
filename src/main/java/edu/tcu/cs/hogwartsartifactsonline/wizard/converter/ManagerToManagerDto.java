package edu.tcu.cs.hogwartsartifactsonline.wizard.converter;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Manager;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.ManagerDto;
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
