package edu.tcu.cs.employeemanagementonline.manager;

import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    ManagerRepository managerRepository;

    @InjectMocks
    ManagerService managerService;

    List<Manager> manager;

    @BeforeEach
    void setUp() {
        Manager m1 = new Manager();
        m1.setId(1);
        m1.setName("Albus Dumbledore");

        Manager m2 = new Manager();
        m2.setId(2);
        m2.setName("Harry Potter");

        Manager m3 = new Manager();
        m2.setId(3);
        m2.setName("Neville Longbottom");

        this.manager = new ArrayList<>();
        this.manager.add(m1);
        this.manager.add(m2);
        this.manager.add(m3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        BDDMockito.given(this.managerRepository.findAll()).willReturn(this.manager);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<Manager> actualManager = this.managerService.findAll();

        // Then. Assert expected outcomes.
        Assertions.assertThat(actualManager.size()).isEqualTo(this.manager.size());

        // Then. Assert expected outcomes.
        Mockito.verify(this.managerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        Manager m = new Manager();
        m.setId(1);
        m.setName("Albus Dumbledore");

        BDDMockito.given(this.managerRepository.findById(1)).willReturn(Optional.of(m)); // Define the behavior of the mock object.

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        Manager returnManager = this.managerService.findById(1);

        // Then. Assert expected outcomes.
        Assertions.assertThat(returnManager.getId()).isEqualTo(m.getId());
        Assertions.assertThat(returnManager.getName()).isEqualTo(m.getName());
        Mockito.verify(this.managerRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {

        BDDMockito.given(this.managerRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = Assertions.catchThrowable(() -> {
            Manager returnedManager = this.managerService.findById(1);
        });

        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find manager with Id 1 :(");

        Mockito.verify(this.managerRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        Manager newManager = new Manager();
        newManager.setName("Hermione Granger");

        BDDMockito.given(this.managerRepository.save(newManager)).willReturn(newManager);

        // When
        Manager returnedManager = this.managerService.save(newManager);

        // Then
        Assertions.assertThat(returnedManager.getName()).isEqualTo(newManager.getName());
        Mockito.verify(this.managerRepository, Mockito.times(1)).save(newManager);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Manager oldManager = new Manager();
        oldManager.setId(1);
        oldManager.setName("Albus Dumbledore");

        Manager update = new Manager();
        update.setName("Albus Dumbledore - update");

        BDDMockito.given(this.managerRepository.findById(1)).willReturn(Optional.of(oldManager));
        BDDMockito.given(this.managerRepository.save(oldManager)).willReturn(oldManager); // willReturn oldManager karena udah berubah.
        // Bisa diliat di implementasi managerService.update nya.

        // Whens
        Manager updatedManager = this.managerService.update(1, update);

        // Then
        Assertions.assertThat(updatedManager.getId()).isEqualTo(1);
        Assertions.assertThat(updatedManager.getName()).isEqualTo(update.getName());
        Mockito.verify(this.managerRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.managerRepository, Mockito.times(1)).save(oldManager);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Manager update = new Manager();
        update.setName("Albus Dumbledore - update");

        BDDMockito.given(this.managerRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = Assertions.catchThrowable(() -> {
            Manager returnedManager = this.managerService.findById(1);
        });

        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find manager with Id 1 :(");

        Mockito.verify(this.managerRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Manager manager = new Manager();
        manager.setId(1);
        manager.setName("Albus Dumbledore");

        BDDMockito.given(this.managerRepository.findById(1)).willReturn(Optional.of(manager));
        Mockito.doNothing().when(this.managerRepository).deleteById(1);

        // When
        this.managerService.delete(1);

        // Then
        Mockito.verify(this.managerRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
// Given
        BDDMockito.given(this.managerRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.managerService.delete(1);
        });

        // Then
        Mockito.verify(this.managerRepository, Mockito.times(1)).findById(1);
    }
}