package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.converter.EmployeeDtoToEmployeeConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.converter.EmployeeToEmployeeDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.EmployeeDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeToEmployeeDtoConverter employeeToEmployeeDtoConverter;

    private final EmployeeDtoToEmployeeConverter employeeDtoToEmployeeConverter;

    public EmployeeController(EmployeeService employeeService, EmployeeToEmployeeDtoConverter employeeToEmployeeDtoConverter, EmployeeDtoToEmployeeConverter employeeDtoToEmployeeConverter) {
        this.employeeService = employeeService;
        this.employeeToEmployeeDtoConverter = employeeToEmployeeDtoConverter;
        this.employeeDtoToEmployeeConverter = employeeDtoToEmployeeConverter;
    }

    @GetMapping("/{employeeId}")
    public Result findEmployeeById(@PathVariable String employeeId) {
        Employee employeeFound = this.employeeService.findById(employeeId);
        EmployeeDto employeeDto = employeeToEmployeeDtoConverter.convert(employeeFound);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", employeeDto);
    }

    @GetMapping
    public Result findAllEmployees() {
        List<Employee> employeeFoundAll = this.employeeService.findAll();
        List<EmployeeDto> employeeDTOs = employeeFoundAll.stream()
                .map(employeeFounds ->
                        this.employeeToEmployeeDtoConverter.convert(employeeFounds))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", employeeDTOs);
    }

    @PostMapping
    public Result addEmployees(@Valid @RequestBody EmployeeDto employeeDto){
        Employee newEmployee = this.employeeDtoToEmployeeConverter.convert(employeeDto);
        Employee savedEmployee = this.employeeService.save(newEmployee);
        EmployeeDto savedEmployeeDto = this.employeeToEmployeeDtoConverter.convert(savedEmployee);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedEmployeeDto);
    }

    @PutMapping("/{employeeId}")
    public Result updateEmployee(@PathVariable String employeeId,@Valid @RequestBody EmployeeDto employeeDto) {
        Employee update = this.employeeDtoToEmployeeConverter.convert(employeeDto);
        Employee updatedEmployee = this.employeeService.update(employeeId, update);
        EmployeeDto updatedEmployeeDto = this.employeeToEmployeeDtoConverter.convert(updatedEmployee);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedEmployeeDto);
    }

    @DeleteMapping("/{employeeId}")
    public Result deleteEmployee(@PathVariable String employeeId){
        this.employeeService.delete(employeeId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
