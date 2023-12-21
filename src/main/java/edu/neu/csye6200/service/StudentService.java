package edu.neu.csye6200.service;

import edu.neu.csye6200.factory.AbstractPersonFactory;
import edu.neu.csye6200.factory.StudentLazySingletonFactory;
import edu.neu.csye6200.model.Student;
import edu.neu.csye6200.repository.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("studentService")
public class StudentService {

  @Autowired
  private StudentRepository studentRepository;

  private AbstractPersonFactory studentFactory = StudentLazySingletonFactory.getInstance();

  public List<Student> getStudents() {
    return this.studentRepository.findAll();
  }

  public void addStudent(String csv) {
    this.studentRepository.save((Student) studentFactory.getObject(csv));
  }

  public void addStudent(Student student) {
    this.studentRepository.save(student);
  }

  public Student getStudentById(Long id) {
    return this.studentRepository.findById(id).get();
  }

  public List<Student> getStudentsById(List<Long> ids){
    return this.studentRepository.findAllById(ids);
  }

  public void deleteStudentById(Long id) {
    this.studentRepository.deleteById(id);
  }

}
