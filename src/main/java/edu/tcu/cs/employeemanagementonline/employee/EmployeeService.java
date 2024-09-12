package edu.tcu.cs.employeemanagementonline.employee;

import edu.tcu.cs.employeemanagementonline.employee.utils.IdWorker;
import edu.tcu.cs.employeemanagementonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final IdWorker idWorker;

    public EmployeeService(EmployeeRepository employeeRepository, IdWorker idWorker) {
        this.employeeRepository = employeeRepository;
        this.idWorker = idWorker;
    }

    public Employee findById(String employeeId) {
        return this.employeeRepository.findById(employeeId)
                .orElseThrow(()-> new ObjectNotFoundException("employee", employeeId));
    }

    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    public Employee save(Employee newEmployee) {
        newEmployee.setId(idWorker.nextId() + "");
        return this.employeeRepository.save(newEmployee);
    }

    public Employee update(String employeeId, Employee update){
        return this.employeeRepository.findById(employeeId)
                .map(oldEmployee -> {
                    oldEmployee.setName(update.getName() != null ? update.getName() : oldEmployee.getName());
                    oldEmployee.setDescription(update.getDescription() != null ? update.getDescription() : oldEmployee.getDescription());
                    oldEmployee.setImageUrl(update.getImageUrl() != null ? update.getImageUrl() : oldEmployee.getImageUrl());
                    return this.employeeRepository.save(oldEmployee);
                }).orElseThrow(() -> new ObjectNotFoundException("employee", employeeId));
    }

    public void delete(String employeeId) {
        this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundException("employee", employeeId));
        this.employeeRepository.deleteById(employeeId);
    }
}
