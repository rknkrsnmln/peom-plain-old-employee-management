package edu.tcu.cs.hogwartsartifactsonline.employee;

public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String id) {
        super("Could not find artifact with Id " + id + " :(");
    }
}
