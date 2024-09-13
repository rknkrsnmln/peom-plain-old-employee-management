package edu.tcu.cs.employeemanagementonline.manager;

import edu.tcu.cs.employeemanagementonline.manager.converter.ManagerDtoToManager;
import edu.tcu.cs.employeemanagementonline.manager.converter.ManagerToManagerDto;
import edu.tcu.cs.employeemanagementonline.manager.dto.ManagerDto;
import edu.tcu.cs.employeemanagementonline.system.Result;
import edu.tcu.cs.employeemanagementonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/managers")
public class ManagerController {

    private final ManagerService managerService;

    private final ManagerDtoToManager managerDtoToManager; // untuk mengkonversi

    private final ManagerToManagerDto managerToManagerDto; // sama.

    public ManagerController(ManagerService managerService, ManagerDtoToManager managerDtoToManager, ManagerToManagerDto managerToManagerDto) {
        this.managerService = managerService;
        this.managerDtoToManager = managerDtoToManager;
        this.managerToManagerDto = managerToManagerDto;
    }

    @GetMapping
    public Result findAllManagers() {
        List<Manager> foundManagers = this.managerService.findAll();

        // Convert list of foundManagers to a list of ManagerDtos.
        List<ManagerDto> managerDtos = foundManagers.stream()
                .map(managerFounds ->
                        this.managerToManagerDto.convert(managerFounds))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", managerDtos);
    }

    @GetMapping("/{managerId}")
    public Result findManagerById(@PathVariable Integer managerId) {
        Manager foundManager = this.managerService.findById(managerId);
        ManagerDto managerDto = this.managerToManagerDto.convert(foundManager);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", managerDto);
    }

    @PostMapping
    public Result addManager(@Valid @RequestBody ManagerDto managerDto) {
        Manager newManager = this.managerDtoToManager.convert(managerDto);
        Manager savedManager = this.managerService.save(newManager);
        ManagerDto savedManagerDto = this.managerToManagerDto.convert(savedManager);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedManagerDto);
    }

    @PutMapping("/{managerId}")
    public Result updateManager(@PathVariable Integer managerId, @Valid @RequestBody ManagerDto managerDto) {
        Manager update = this.managerDtoToManager.convert(managerDto);
        Manager updateManger = this.managerService.update(managerId, update);
        ManagerDto updatedManagerDto = this.managerToManagerDto.convert(updateManger);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedManagerDto);
    }

    @DeleteMapping("/{managerId}")
    public Result deleteManager(@PathVariable Integer managerId) {
        this.managerService.delete(managerId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @PutMapping("/{managerId}/employees/{employeeId}")
    public Result assignEmployee(@PathVariable Integer managerId, @PathVariable String employeeId) {
        this.managerService.assignEmployee(managerId, employeeId);
        return new Result(true, StatusCode.SUCCESS, "Employee Assignment Success");
    }
}
