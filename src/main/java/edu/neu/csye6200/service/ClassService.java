package edu.neu.csye6200.service;

import edu.neu.csye6200.factory.ClassSimpleFactory;
import edu.neu.csye6200.model.Class;
import edu.neu.csye6200.repository.ClassRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("classService")
public class ClassService {

  @Autowired
  private ClassRepository classRepository;

  private ClassSimpleFactory classFactory = new ClassSimpleFactory();

  public List<Class> getClasses() {
    return this.classRepository.findAll();
  }

  public void addClass(String csv) {
    this.classRepository.save(classFactory.getObject(csv));
  }

  public void addClass(Class clzz) {
    this.classRepository.save(clzz);
  }

  public Class getClassById(Long id) {
    return this.classRepository.findById(id).get();
  }

  public void deleteClassById(Long id) {
    this.classRepository.deleteById(id);
  }

}
