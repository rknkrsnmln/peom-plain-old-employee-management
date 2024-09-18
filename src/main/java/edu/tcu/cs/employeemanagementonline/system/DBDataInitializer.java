package edu.tcu.cs.employeemanagementonline.system;

import edu.tcu.cs.employeemanagementonline.employee.Employee;
import edu.tcu.cs.employeemanagementonline.employee.EmployeeRepository;
import edu.tcu.cs.employeemanagementonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.employeemanagementonline.hogwartsuser.UserRepository;
import edu.tcu.cs.employeemanagementonline.manager.Manager;
import edu.tcu.cs.employeemanagementonline.manager.ManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {


    private final EmployeeRepository employeeRepository;

    private final ManagerRepository managerRepository;

    private final UserRepository userRepository;


    public DBDataInitializer(EmployeeRepository employeeRepository, ManagerRepository managerRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Employee a1 = new Employee();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Employee a2 = new Employee();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Employee a3 = new Employee();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Employee a4 = new Employee();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Employee a5 = new Employee();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Employee a6 = new Employee();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        Manager w1 = new Manager();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addEmployee(a1);
        w1.addEmployee(a3);

        Manager w2 = new Manager();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addEmployee(a2);
        w2.addEmployee(a4);

        Manager w3 = new Manager();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        w3.addEmployee(a5);

        managerRepository.save(w1);
        managerRepository.save(w2);
        managerRepository.save(w3);

        employeeRepository.save(a6);

        // Create some users.
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

        this.userRepository.save(u1);
        this.userRepository.save(u2);
        this.userRepository.save(u3);

    }
}
