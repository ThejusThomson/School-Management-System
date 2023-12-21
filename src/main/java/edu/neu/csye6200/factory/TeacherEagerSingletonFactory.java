package edu.neu.csye6200.factory;

import edu.neu.csye6200.model.Person;
import edu.neu.csye6200.model.Teacher;

public class TeacherEagerSingletonFactory extends AbstractPersonFactory {

  private static final AbstractPersonFactory instance = new TeacherEagerSingletonFactory();

  private TeacherEagerSingletonFactory() {
  }

  public static AbstractPersonFactory getInstance() {
    return instance;
  }

  @Override
  public Person getObject(String teacherCsv) {
    return new Teacher(teacherCsv);
  }

}
