package edu.tcu.cs.employeemanagementonline.manager;

import edu.tcu.cs.employeemanagementonline.employee.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Manager implements Serializable {

    @Id
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

    public List<Employee> getArtifacts() {
        return employees;
    }

    public void setArtifacts(List<Employee> employees) {
        this.employees = employees;
    }

    public void addArtifact(Employee employee) {
        employee.setOwner(this);
        this.employees.add(employee);
    }

    public Integer getNumberOfArtifacts() {
        return this.employees.size();
    }
}
