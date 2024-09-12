package edu.tcu.cs.employeemanagementonline.manager;

import edu.tcu.cs.employeemanagementonline.employee.Employee;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Manager implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Employee> employees = new ArrayList<>();

    public Manager() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        employee.setOwner(this);
        this.employees.add(employee);
    }

    public Integer getNumberOfEmployees() {
        return this.employees.size();
    }

    public void removeAllEmployees() {
        this.employees.stream().forEach(employee -> employee.setOwner(null));
        this.employees = new ArrayList<>();
    }

    public void removeEmployee(Employee employeeToBeAssigned) {
        employeeToBeAssigned.setOwner(null);
        this.employees.remove(employeeToBeAssigned);
    }
}
