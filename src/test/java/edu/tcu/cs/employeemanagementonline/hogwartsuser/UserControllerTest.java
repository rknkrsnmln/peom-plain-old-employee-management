package edu.tcu.cs.employeemanagementonline.hogwartsuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.employeemanagementonline.hogwartsuser.dto.UserDto;
import edu.tcu.cs.employeemanagementonline.system.StatusCode;
import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    List<HogwartsUser> users;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        this.users.add(u1);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        this.users.add(u2);

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");
        this.users.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].username").value("john"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].username").value("eric"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.findById(2)).willReturn(this.users.get(1));

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("eric"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.findById(5)).willThrow(new ObjectNotFoundException("user", 5));

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserSuccess() throws Exception {
        HogwartsUser user = new HogwartsUser();
        user.setId(4);
        user.setUsername("lily");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user"); // The delimiter is space.

        String json = this.objectMapper.writeValueAsString(user);

        user.setId(4);

        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.save(Mockito.any(HogwartsUser.class))).willReturn(user);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("lily"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("admin user"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto userDto = new UserDto(3, "tom123", false, "user");

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(3);
        updatedUser.setUsername("tom123"); // Username is changed. It was tom.
        updatedUser.setEnabled(false);
        updatedUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.update(ArgumentMatchers.eq(3), Mockito.any(HogwartsUser.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("tom123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.enabled").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        BDDMockito.given(this.userService.update(ArgumentMatchers.eq(5), Mockito.any(HogwartsUser.class))).willThrow(new ObjectNotFoundException("user", 5));

        UserDto userDto = new UserDto(5, "tom123", false, "user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        Mockito.doNothing().when(this.userService).delete(2);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/users/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"));
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userService.
        Mockito.doThrow(new ObjectNotFoundException("user", 5)).when(this.userService).delete(5);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find user with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

}