package edu.neu.csye6200.controller;

import edu.neu.csye6200.model.Student;
import edu.neu.csye6200.service.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentsController {

  @Autowired
  private StudentService studentService;

  public List<Student> getStudents(){
    return this.studentService.getStudents();
  }

  public Student getStudent(Long studentId){
    return this.studentService.getStudentById(studentId);
  }

  public void addStudent(Student student){
    this.studentService.addStudent(student);
  }

  public void updateStudent(Student student){
    this.studentService.addStudent(student);
  }

  public void deleteStudent(Student student){
    this.studentService.deleteStudentById(student.getPersonId());
  }

  public void deleteStudent(Long studentId){
    this.studentService.deleteStudentById(studentId);
  }

}
