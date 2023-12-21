package edu.neu.csye6200.service;

import edu.neu.csye6200.factory.AbstractPersonFactory;
import edu.neu.csye6200.factory.TeacherEagerSingletonFactory;
import edu.neu.csye6200.model.Teacher;
import edu.neu.csye6200.repository.TeacherRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherService")
public class TeacherService {

  @Autowired
  private TeacherRepository teacherRepository;
  private AbstractPersonFactory teacherFactory = TeacherEagerSingletonFactory.getInstance();

  public List<Teacher> getTeachers() {
    return this.teacherRepository.findAll();
  }

  public void addTeacher(String csv) {
    this.teacherRepository.save((Teacher) teacherFactory.getObject(csv));
  }

  public void addTeacher(Teacher teacher) {
    this.teacherRepository.save(teacher);
  }

  public Teacher getTeacherById(Long id) {
    return this.teacherRepository.findById(id).get();
  }

  public void deleteTeacherById(Long id) {
    this.teacherRepository.deleteById(id);
  }

}
