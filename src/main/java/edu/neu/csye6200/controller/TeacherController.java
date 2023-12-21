package edu.neu.csye6200.controller;

import edu.neu.csye6200.model.Teacher;
import edu.neu.csye6200.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeacherController {

  @Autowired
  private TeacherService teacherService;

  public List<Teacher> getTeachers() {
    return this.teacherService.getTeachers();
  }

  public Teacher getTeacher(Long teacherId){
    return this.teacherService.getTeacherById(teacherId);
  }

  public void addTeacher(Teacher teacher){
    this.teacherService.addTeacher(teacher);
  }

  public void updateTeacher(Teacher teacher){
    this.teacherService.addTeacher(teacher);
  }

  public void deleteTeacher(Teacher teacher){
    this.teacherService.deleteTeacherById(teacher.getPersonId());
  }

  public void deleteTeacher(Long teacherId){
    this.teacherService.deleteTeacherById(teacherId);
  }

}
