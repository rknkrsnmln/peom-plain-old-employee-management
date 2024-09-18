package edu.tcu.cs.employeemanagementonline.hogwartsuser;

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
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> hogwartsUsers;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.hogwartsUsers = new ArrayList<>();
        this.hogwartsUsers.add(u1);
        this.hogwartsUsers.add(u2);
        this.hogwartsUsers.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        BDDMockito.given(this.userRepository.findAll()).willReturn(this.hogwartsUsers);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<HogwartsUser> hogwartsUsers = this.userService.findAll();

        // Then. Assert expected outcomes.
        Assertions.assertThat(hogwartsUsers.size()).isEqualTo(this.hogwartsUsers.size());

        // Then. Assert expected outcomes.
        Mockito.verify(this.userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object wizardRepository.
        HogwartsUser u = new HogwartsUser();
        u.setId(1);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(u)); // Define the behavior of the mock object.

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        HogwartsUser returnedHogwartsUser = this.userService.findById(1);

        // Then. Assert expected outcomes.
        Assertions.assertThat(returnedHogwartsUser.getId()).isEqualTo(u.getId());
        Assertions.assertThat(returnedHogwartsUser.getUsername()).isEqualTo(u.getUsername());
        Assertions.assertThat(returnedHogwartsUser.getPassword()).isEqualTo(u.getPassword());
        Assertions.assertThat(returnedHogwartsUser.isEnabled()).isEqualTo(u.isEnabled());
        Assertions.assertThat(returnedHogwartsUser.getRoles()).isEqualTo(u.getRoles());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {

        BDDMockito.given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = Assertions.catchThrowable(() -> {
            HogwartsUser returnedHogwartsUser = this.userService.findById(1);
        });

        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");

        Mockito.verify(this.userRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("lily");
        newUser.setPassword("123456");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        BDDMockito.given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        HogwartsUser returnedUser = this.userService.save(newUser);

        // Then
        Assertions.assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        Assertions.assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        Assertions.assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        Assertions.assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(newUser);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        HogwartsUser oldUser = new HogwartsUser();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(false);
        oldUser.setRoles("admin user");

        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        BDDMockito.given(this.userRepository.save(oldUser)).willReturn(oldUser);

        // When
        HogwartsUser updatedUser = this.userService.update(1, update);

        // Then
        Assertions.assertThat(updatedUser.getId()).isEqualTo(1);
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.userRepository, Mockito.times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(1, update);
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        HogwartsUser user = new HogwartsUser();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        Mockito.doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        Mockito.verify(this.userRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(1);
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }

}