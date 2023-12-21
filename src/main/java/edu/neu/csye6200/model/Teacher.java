package edu.neu.csye6200.model;

import javax.persistence.Entity;

@Entity
public class Teacher extends Person {

  private Double salary;

  public Teacher() {
  }

  public Teacher(Long age, String firstName, String lastName, String phoneNo, String address, Double salary) {
    super(age, firstName, lastName, phoneNo, address);
    this.salary = salary;
  }

  public Teacher(String teacherCsv) {
    super(teacherCsv);
    String[] teacherArr = teacherCsv.split(",");
    this.salary = Double.parseDouble(teacherArr[5]);
  }

  public Double getSalary() {
    return salary;
  }

  public void setSalary(Double salary) {
    this.salary = salary;
  }
}
