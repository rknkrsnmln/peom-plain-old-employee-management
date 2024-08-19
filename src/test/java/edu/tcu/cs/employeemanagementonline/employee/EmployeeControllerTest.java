package edu.tcu.cs.employeemanagementonline.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.employeemanagementonline.employee.dto.EmployeeDto;
import edu.tcu.cs.employeemanagementonline.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;


    @Autowired
    ObjectMapper objectMapper;

    List<Employee> employees;

    @BeforeEach
    void setUp() {
        this.employees = new ArrayList<>();

        Employee a1 = new Employee();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        this.employees.add(a1);

        Employee a2 = new Employee();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.employees.add(a2);

        Employee a3 = new Employee();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        this.employees.add(a3);

        Employee a4 = new Employee();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        this.employees.add(a4);

        Employee a5 = new Employee();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");
        this.employees.add(a5);

        Employee a6 = new Employee();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");
        this.employees.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void tesFindArtifactByIdSuccess() throws Exception {

        given(this.employeeService.findById("1250808601744904191"))
                .willReturn(this.employees.get(0));

        this.mockMvc.perform(get("/api/v1/employees/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void tesFindArtifactByIdNotFound() throws Exception {
        // Given
        given(this.employeeService.findById("1250808601744904191")).willThrow(new EmployeeNotFoundException("1250808601744904191"));

        // When and then
        this.mockMvc.perform(get("/api/v1/employees/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // Given
        given(this.employeeService.findAll()).willReturn(this.employees);

        // When and then
        this.mockMvc.perform(get("/api/v1/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", hasSize(this.employees.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(null,
                "Remembrall",
                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                "ImageUrl",
                null);

        String json = this.objectMapper.writeValueAsString(employeeDto);

        Employee savedEmployee = new Employee();
        savedEmployee.setId("1250808601744904197");
        savedEmployee.setName("Remembrall");
        savedEmployee.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedEmployee.setImageUrl("ImageUrl");

        given(this.employeeService.save(any(Employee.class))).willReturn(savedEmployee);

        this.mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedEmployee.getName()))
                .andExpect(jsonPath("$.data.description").value(savedEmployee.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedEmployee.getImageUrl()));

    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // Given
        EmployeeDto employeeDto = new EmployeeDto("1250808601744904192",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(employeeDto);

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId("1250808601744904192");
        updatedEmployee.setName("Invisibility Cloak");
        updatedEmployee.setDescription("A new description.");
        updatedEmployee.setImageUrl("ImageUrl");

        given(this.employeeService.update(eq("1250808601744904192"), any(Employee.class))).willReturn(updatedEmployee);

        // When and then
        this.mockMvc.perform(put("/api/v1/employees/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedEmployee.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedEmployee.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        // Given
        EmployeeDto employeeDto = new EmployeeDto("1250808601744904192",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(employeeDto);

        given(this.employeeService.update(eq("1250808601744904192"), any(Employee.class))).willThrow(new EmployeeNotFoundException("1250808601744904192"));

        // When and then
        this.mockMvc.perform(put("/api/v1/employees/1250808601744904192").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904192 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.employeeService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(delete("/api/v1/employees/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        // Given
        doThrow(new EmployeeNotFoundException("1250808601744904191")).when(this.employeeService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(delete("/api/v1/employees/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}