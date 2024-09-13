package edu.tcu.cs.employeemanagementonline.manager;

import edu.tcu.cs.employeemanagementonline.employee.Employee;
import edu.tcu.cs.employeemanagementonline.employee.EmployeeNotFoundException;
import edu.tcu.cs.employeemanagementonline.employee.EmployeeRepository;
import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ManagerService {

    private final ManagerRepository managerRepository;

    private final EmployeeRepository employeeRepository;

    public ManagerService(ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
    }

    public Manager findById(Integer managerId) {
        return this.managerRepository.findById(managerId)
                .orElseThrow(()-> new ObjectNotFoundException("manager", managerId));
    }

    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    public Manager save(Manager manager){
        return this.managerRepository.save(manager);
    }

    public Manager update(int managerId, Manager update) {
        return this.managerRepository.findById(managerId)
                .map(oldManager -> {
                    oldManager.setName(update.getName() != null ? update.getName() : oldManager.getName());
                    return this.managerRepository.save(oldManager);
                })
                .orElseThrow(() -> new ObjectNotFoundException("manager", managerId));
    }

    public void delete(int managerId) {
        Manager managerToBeDeleted = this.managerRepository.findById(managerId)
                .orElseThrow(() -> new ObjectNotFoundException("manager", managerId));

        // Before deletion, we will un-assign this manager's owned employees.
        managerToBeDeleted.removeAllEmployees();
        this.managerRepository.deleteById(managerId);
    }

    public void assignEmployee(Integer managerId, String employeeId) {
        // Find this Employee by ID from DB.
        Employee employeeToBeAssigned = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundException("employee", employeeId));

        // Find this manager by ID fromm DB.
        Manager manager = this.managerRepository.findById(managerId)
                .orElseThrow(() -> new ObjectNotFoundException("manager", managerId));

        // Artifact assignment
        // We need to see if the employee is already employed by some manager.
        // Kenapa tidak langsung dari employee dan melalui manager dulu
        // Karena Bi-directional alias mesti dihapus dari manager dan employee
        if (employeeToBeAssigned.getOwner() != null) {
            employeeToBeAssigned.getOwner().removeEmployee(employeeToBeAssigned);
        }
        manager.addEmployee(employeeToBeAssigned);
    }
}
