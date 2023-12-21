/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.neu.csye6200.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long personId;
  private Long age;
  private String firstName;
  private String lastName;
  private String phoneNo;
  private String address;

  public Person(){}

  public Person(Long age, String firstName, String lastName, String phoneNo, String address) {
    this.age = age;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNo = phoneNo;
    this.address = address;
  }

  public Person(String personCsv) {
    String[] personArr = personCsv.split(",");
    this.age = Long.parseLong(personArr[0]);
    this.firstName = personArr[1];
    this.lastName = personArr[2];
    this.phoneNo = personArr[3];
    this.address = personArr[4];
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public Long getAge() {
    return age;
  }

  public void setAge(Long age) {
    this.age = age;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getFullName() {
    return this.getFirstName() + " " + this.getLastName();
  }
}
