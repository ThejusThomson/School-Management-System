package edu.neu.csye6200.factory;

import edu.neu.csye6200.model.Person;
import edu.neu.csye6200.model.Student;

public class StudentLazySingletonFactory extends AbstractPersonFactory {

  private static StudentLazySingletonFactory instance;

  private StudentLazySingletonFactory() {
    instance = null;
  }

  public static synchronized AbstractPersonFactory getInstance() {
    if (instance == null) {
      instance = new StudentLazySingletonFactory();
    }
    return instance;
  }

  @Override
  public Person getObject(String personCsv) {
    return new Student(personCsv);
  }

}
