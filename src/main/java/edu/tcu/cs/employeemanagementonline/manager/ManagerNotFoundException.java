package edu.tcu.cs.employeemanagementonline.manager;

public class ManagerNotFoundException extends RuntimeException{
    public ManagerNotFoundException(Integer id) {super("Could not find manager with Id " + id + " :(");
    }
}
