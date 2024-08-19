package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    EmployeeService employeeService;

    List<Employee> employees;

    @BeforeEach
    void setUp() {
        Employee a1 = new Employee();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("imageUrl");

        Employee a2 = new Employee();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("imageUrl");

        this.employees = new ArrayList<>();
        this.employees.add(a1);
        this.employees.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",
         */
        Employee employee = new Employee();
        employee.setId("1250808601744904192");
        employee.setName("Invisibility Cloak");
        employee.setDescription("An invisibility cloak is used to make the wearer invisible.");
        employee.setImageUrl("ImageUrl");

        Manager manager = new Manager();
        manager.setId(2);
        manager.setName("Harry Potter");

        employee.setOwner(manager);

        // Given atau diberikan
        given(employeeRepository.findById("1250808601744904192")).willReturn(Optional.of(employee));

        // When
        Employee returnedEmployee = employeeService.findById("1250808601744904192");

        // Then
        assertThat(returnedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(returnedEmployee.getDescription()).isEqualTo(employee.getDescription());
        assertThat(returnedEmployee.getImageUrl()).isEqualTo(employee.getImageUrl());
        assertThat(returnedEmployee.getName()).isEqualTo(employee.getName());
        verify(employeeRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(employeeRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            employeeService.findById("1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
        verify(employeeRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        given(employeeRepository.findAll()).willReturn(this.employees);

        List<Employee> employeeServiceAll = employeeService.findAll();

        assertThat(employeeServiceAll.size()).isEqualTo(this.employees.size());
        verify(employeeRepository, times(1)).findAll();
    }


    @Test
    void testSaveSuccess() {
            // Given
            Employee newEmployee = new Employee();
            newEmployee.setName("Employee 3");
            newEmployee.setDescription("Description...");
            newEmployee.setImageUrl("ImageUrl...");

            given(idWorker.nextId()).willReturn(123456L);
            given(employeeRepository.save(newEmployee)).willReturn(newEmployee);

            // When
            Employee savedEmployee = employeeService.save(newEmployee);

            // Then
            assertThat(savedEmployee.getId()).isEqualTo("123456");
            assertThat(savedEmployee.getName()).isEqualTo(newEmployee.getName());
            assertThat(savedEmployee.getDescription()).isEqualTo(newEmployee.getDescription());
            assertThat(savedEmployee.getImageUrl()).isEqualTo(newEmployee.getImageUrl());
            verify(employeeRepository, times(1)).save(newEmployee);
        }

    @Test
    void testUpdateSuccess() {
        // Given
        Employee oldEmployee = new Employee();
        oldEmployee.setId("1250808601744904192");
        oldEmployee.setName("Invisibility Cloak");
        oldEmployee.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldEmployee.setImageUrl("ImageUrl");

        Employee update = new Employee();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(employeeRepository.findById("1250808601744904192")).willReturn(Optional.of(oldEmployee));
        //kenapa willReturn(oldEmployee), karena oldEmployee dah jadi (Employee update),
        // bisa diliat di artifactService.update
        given(employeeRepository.save(oldEmployee)).willReturn(oldEmployee);

        // When
        Employee updatedEmployee = employeeService.update("1250808601744904192", update);

        // Then
        assertThat(updatedEmployee.getId()).isEqualTo(update.getId());
        assertThat(updatedEmployee.getDescription()).isEqualTo(update.getDescription());
        verify(employeeRepository, times(1)).findById("1250808601744904192");
        verify(employeeRepository, times(1)).save(oldEmployee);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Employee update = new Employee();
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(employeeRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.update("1250808601744904192", update);
        });

        // Then
        verify(employeeRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testDeleteSuccess(){
        Employee employee = new Employee();
        employee.setId("1250808601744904192");
        employee.setName("Invisibility Cloak");
        employee.setDescription("An invisibility cloak is used to make the wearer invisible.");
        employee.setImageUrl("ImageUrl");

        given(employeeRepository.findById("1250808601744904192")).willReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById("1250808601744904192");

        employeeService.delete("1250808601744904192");

        verify(employeeRepository, times(1)).deleteById("1250808601744904192");

    }

    @Test
    void testDeleteNotFound(){
        given(employeeRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.delete("1250808601744904192");
        });

        verify(employeeRepository, times(1)).findById("1250808601744904192");
    }


}