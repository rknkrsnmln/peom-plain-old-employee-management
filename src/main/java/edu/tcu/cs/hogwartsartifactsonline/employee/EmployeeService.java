package edu.tcu.cs.hogwartsartifactsonline.employee;

import edu.tcu.cs.hogwartsartifactsonline.employee.utils.IdWorker;
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

    public Employee findById(String artifactId) {
        return this.employeeRepository.findById(artifactId)
                .orElseThrow(()-> new EmployeeNotFoundException(artifactId));
    }

    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    public Employee save(Employee newEmployee) {
        newEmployee.setId(idWorker.nextId() + "");
        return this.employeeRepository.save(newEmployee);
    }

    public Employee update(String artifactId, Employee update){
        return this.employeeRepository.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    return this.employeeRepository.save(oldArtifact);
                }).orElseThrow(() -> new EmployeeNotFoundException(artifactId));
    }

    public void delete(String artifactId) {
        this.employeeRepository.findById(artifactId)
                .orElseThrow(() -> new EmployeeNotFoundException(artifactId));
        this.employeeRepository.deleteById(artifactId);
    }
}
