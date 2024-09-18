package edu.tcu.cs.employeemanagementonline.hogwartsuser.converter;

import edu.tcu.cs.employeemanagementonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.employeemanagementonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        // Kita gak ngirim kata sandi di DTO.
        return new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles());
    }
}
