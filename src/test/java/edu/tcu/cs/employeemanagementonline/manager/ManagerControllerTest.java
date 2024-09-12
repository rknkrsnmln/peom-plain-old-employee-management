package edu.tcu.cs.employeemanagementonline.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.employeemanagementonline.employee.Employee;
import edu.tcu.cs.employeemanagementonline.manager.dto.ManagerDto;
import edu.tcu.cs.employeemanagementonline.system.StatusCode;
import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
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


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ManagerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ManagerService managerService;

    List<Manager> manager;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() throws Exception {
        Employee e1 = new Employee();
        e1.setId("1250808601744904191");
        e1.setName("Deluminator");
        e1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        e1.setImageUrl("ImageUrl");

        Employee e2 = new Employee();
        e2.setId("1250808601744904192");
        e2.setName("Invisibility Cloak");
        e2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        e2.setImageUrl("ImageUrl");

        Employee e3 = new Employee();
        e3.setId("1250808601744904193");
        e3.setName("Elder Wand");
        e3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        e3.setImageUrl("ImageUrl");

        Employee e4 = new Employee();
        e4.setId("1250808601744904194");
        e4.setName("The Marauder's Map");
        e4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        e4.setImageUrl("ImageUrl");

        Employee e5 = new Employee();
        e5.setId("1250808601744904195");
        e5.setName("The Sword Of Gryffindor");
        e5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        e5.setImageUrl("ImageUrl");

        Employee e6 = new Employee();
        e6.setId("1250808601744904196");
        e6.setName("Resurrection Stone");
        e6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        e6.setImageUrl("ImageUrl");

        this.manager = new ArrayList<>();

        Manager m1 = new Manager();
        m1.setId(1);
        m1.setName("Albus Dumbledore");
        m1.addEmployee(e1);
        m1.addEmployee(e3);
        this.manager.add(m1);

        Manager m2 = new Manager();
        m2.setId(2);
        m2.setName("Harry Potter");
        m2.addEmployee(e2);
        m2.addEmployee(e4);
        this.manager.add(m2);

        Manager m3 = new Manager();
        m3.setId(3);
        m3.setName("Neville Longbottom");
        m3.addEmployee(e5);
        this.manager.add(m3);
    }

    @Test
    void testFindAllManagersSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.findAll()).willReturn(this.manager);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/managers").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(this.manager.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value("Harry Potter"));
    }

    @Test
    void testFindManagerByIdSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.findById(1)).willReturn(this.manager.get(0));

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/managers/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find One Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Albus Dumbledore"));
    }

    @Test
    void testFindManagerByIdNotFound() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.findById(5)).willThrow(new ObjectNotFoundException("manager", 5));

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/managers/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find manager with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddManagerSuccess() throws Exception {
        ManagerDto managerDto = new ManagerDto(null, "Hermione Granger", 0);

        String json = this.objectMapper.writeValueAsString(managerDto);

        Manager savedManager = new Manager();
        savedManager.setId(4);
        savedManager.setName("Hermione Granger");

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.save(Mockito.any(Manager.class))).willReturn(savedManager);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/managers").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Hermione Granger"));
    }

    @Test
    void testUpdateManagerSuccess() throws Exception {
//        ManagerDto managerDto = new ManagerDto(null, "Manager name", 0);

        Manager updatedManager = new Manager();
        updatedManager.setId(1);
        updatedManager.setName("Updated manager name");

        String json = this.objectMapper.writeValueAsString(updatedManager);

        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.update(ArgumentMatchers.eq(1), Mockito.any(Manager.class))).willReturn(updatedManager);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/managers/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Updated manager name"));
    }

    @Test
    void testUpdateManagerErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        BDDMockito.given(this.managerService.update(ArgumentMatchers.eq(5), Mockito.any(Manager.class))).willThrow(new ObjectNotFoundException("manager", 5));

        ManagerDto managerDto = new ManagerDto(5, // This id does not exist in the database.
                "Updated wizard name",
                0);

        String json = this.objectMapper.writeValueAsString(managerDto);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put(this.baseUrl + "/managers/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find manager with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteManagerSuccess() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        Mockito.doNothing().when(this.managerService).delete(3);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/managers/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Delete Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteManagerErrorWithNonExistentId() throws Exception {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardService.
        Mockito.doThrow(new ObjectNotFoundException("manager", 5)).when(this.managerService).delete(5);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete(this.baseUrl + "/managers/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Could not find manager with Id 5 :("))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

}