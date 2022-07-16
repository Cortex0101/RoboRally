package com.roborally.RESTapi;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {
  private final List<Employee> employees;
  EmployeeController(List<Employee> employees) {
    this.employees = employees;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/employees")
  List<Employee> all() {
    return employees;
  }
  // end::get-aggregate-root[]

  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    employees.add(newEmployee);
    return newEmployee;
  }

  // Single item

  @GetMapping("/employees/{id}")
  Employee one(@PathVariable Long id) {
    return employees.stream()
        .filter(employee -> employee.getId().equals(id))
        .findFirst().orElseThrow();
  }

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
    Employee employee = employees.stream()
        .filter(employee1 -> employee1.getId().equals(id))
        .findFirst().orElseThrow();
    employee = newEmployee;
    return employee;
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    employees.removeIf(employee -> employee.getId().equals(id));
  }
}