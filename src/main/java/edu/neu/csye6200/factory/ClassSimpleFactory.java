package edu.neu.csye6200.factory;

import edu.neu.csye6200.model.Class;

public class ClassSimpleFactory {

  public Class getObject(String classCsv){
    return new Class(classCsv);
  }

}
