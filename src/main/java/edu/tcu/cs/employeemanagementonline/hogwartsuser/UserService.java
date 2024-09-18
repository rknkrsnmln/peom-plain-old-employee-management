package edu.tcu.cs.employeemanagementonline.hogwartsuser;

import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogwartsUser findById(int userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public HogwartsUser save(HogwartsUser newUser) {
        return this.userRepository.save(newUser);
    }

    public HogwartsUser update(int userId, HogwartsUser newHogwartsUser) {
        return this.userRepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setUsername(newHogwartsUser.getUsername() != null ? newHogwartsUser.getUsername() : oldUser.getUsername());
                    oldUser.setEnabled(newHogwartsUser.isEnabled() != oldUser.isEnabled() ? newHogwartsUser.isEnabled() : oldUser.isEnabled());
                    oldUser.setRoles(newHogwartsUser.getRoles() != null ? newHogwartsUser.getRoles() : oldUser.getRoles());
                    return this.userRepository.save(oldUser);
                })
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public void delete(int userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }
}
