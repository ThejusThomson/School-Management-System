package edu.neu.csye6200.controller;

import edu.neu.csye6200.model.Class;
import edu.neu.csye6200.model.Student;
import edu.neu.csye6200.model.Teacher;
import edu.neu.csye6200.service.ClassService;
import edu.neu.csye6200.service.StudentService;
import edu.neu.csye6200.service.TeacherService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassController {

  @Autowired
  private ClassService classService;

  @Autowired
  private StudentService studentService;

  @Autowired
  private TeacherService teacherService;

  public List<Class> getClasses() {
    List<Class> classes = this.classService.getClasses();
    for (Class clzz : classes) {
      fillClassData(clzz);
    }
    return classes;
  }

  private void fillClassData(Class clzz) {
    Long teacherId = clzz.getTeacherId();
    Teacher teacher = teacherService.getTeacherById(teacherId);
    List<Long> studentIds = Arrays.stream(clzz.getStudentsPipeList().split("\\|")).map(Long::valueOf).collect(Collectors.toList());
    List<Student> students = studentService.getStudentsById(studentIds);
    clzz.setTeacher(teacher);
    clzz.setStudents(students);
  }

  public Class getClassById(Long classId) {
    Class clzz = this.classService.getClassById(classId);
    fillClassData(clzz);
    return clzz;
  }

  public void deleteClass(Long classId) {
    this.classService.deleteClassById(classId);
  }

  public void deleteClass(Class clzz) {
    this.classService.deleteClassById(clzz.getClassId());
  }

  public void addClass(Class clzz) {
    this.classService.addClass(clzz);
  }

  public void addStudent(Class clzz, Student student) {
    clzz.addStudent(student.getPersonId());
    this.classService.addClass(clzz);
  }

  public void updateClass(Class clzz) {
    this.addClass(clzz);
  }

  public boolean checkIfStudentInClass(Student student) {
    List<Class> classes = getClasses();
    for (Class clzz: classes){
      Optional<Student> studentOptional = clzz.getStudents().stream().filter(stu-> Objects.equals(stu.getPersonId(), student.getPersonId())).findFirst();
      if(studentOptional.isPresent()){
        return true;
      }
    }
    return false;
  }

  public boolean checkIfTeacherInClass(Teacher teacher) {
    List<Class> classes = getClasses();
    Optional<Class> classOptional = classes.stream().filter(teach -> Objects.equals(teach.getTeacherId(), teacher.getPersonId())).findFirst();
    return classOptional.isPresent();
  }
}
