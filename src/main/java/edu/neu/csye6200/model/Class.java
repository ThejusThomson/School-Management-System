package edu.neu.csye6200.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Class {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long classId;
  private String name;
  private Long teacherId;
  private String studentsPipeList;
  private String term;

  @Transient
  private Teacher teacher;

  @Transient
  private List<Student> students;

  public Class() {
  }

  public Class(String name, Long teacherId, String studentsPipeList, String term) {
    this.name = name;
    this.teacherId = teacherId;
    this.studentsPipeList = studentsPipeList;
    this.term = term;
  }

  public Class(String classCsv) {
    String[] classArr = classCsv.split(",");
    this.name = classArr[0];
    this.teacherId = Long.parseLong(classArr[1]);
    this.studentsPipeList = classArr[2];
    this.term=classArr[3];
  }

  public Long getClassId() {
    return classId;
  }

  public void setClassId(Long classId) {
    this.classId = classId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(Long teacherId) {
    this.teacherId = teacherId;
  }

  public String getStudentsPipeList() {
    return studentsPipeList;
  }

  public void setStudentsPipeList(String studentsPipeList) {
    this.studentsPipeList = studentsPipeList;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public void addStudent(Long studentId) {
    if (this.studentsPipeList.isEmpty()) {
      this.studentsPipeList = String.valueOf(studentId);
    } else {
      this.studentsPipeList += "|" + studentId;
    }
  }
}
