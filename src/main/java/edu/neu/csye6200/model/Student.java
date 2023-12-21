package edu.neu.csye6200.model;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Student extends Person{

  private Double grade;
  private Timestamp regTime;

  public Student(){}

  public Student(Long age, String firstName, String lastName, String phoneNo, String address, Double grade, Timestamp regTime) {
    super(age, firstName, lastName, phoneNo, address);
    this.grade = grade;
    this.regTime = regTime;
  }

  public Student(String studentCsv){
    super(studentCsv);
    String[] studentArr = studentCsv.split(",");
    this.grade = Double.parseDouble(studentArr[5]);
    this.regTime = new Timestamp(new Date(studentArr[6]).getTime());
  }

  public Double getGrade() {
    return grade;
  }

  public void setGrade(Double grade) {
    this.grade = grade;
  }

  public Timestamp getRegTime() {
    return regTime;
  }

  public void setRegTime(Timestamp regTime) {
    this.regTime = regTime;
  }
}
