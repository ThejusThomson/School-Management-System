/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package edu.neu.csye6200.views;

import edu.neu.csye6200.ApplicationFrame;
import edu.neu.csye6200.controller.ClassController;
import edu.neu.csye6200.controller.StudentsController;
import edu.neu.csye6200.controller.TeacherController;
import edu.neu.csye6200.model.Class;
import edu.neu.csye6200.model.Log;
import edu.neu.csye6200.model.Student;
import edu.neu.csye6200.model.Teacher;
import edu.neu.csye6200.util.FileUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class TabbedPane extends javax.swing.JFrame {

    @Autowired
    private StudentsController studentsController;

    @Autowired
    private TeacherController teacherController;

    @Autowired
    private ClassController classController;

    @Autowired
    @Lazy
    private ApplicationFrame appFrame;

    /**
     * Creates new form TabbedPane
     */
    public TabbedPane() {
        initComponents();
    }

    @PostConstruct
    public void init() {
        fillStudentsTable();
        fillTeacherTable();
        fillAddClassesTeacherTable();
        fillAddClassesStudentsTable();
        fillClassesTable();
        reinitStudentsTabFields();
        reinitTeachersTabFields();
        reinitAddClassTabFields();
        fillClassesAllStudentsTable();
        fillLogs();
    }

    public void fillStudentsTable() {
        List<Student> students = studentsController.getStudents();
        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
        model.setRowCount(0);

        for (Student student : students) {
            model.addRow(
                new Object[]{student.getPersonId(), student.getFirstName(), student.getLastName(), student.getAge(), student.getPhoneNo(),
                    student.getAddress(), student.getGrade(), student.getRegTime()});
        }
    }

    public void fillClassesAllStudentsTable() {
        fillClassesStudentsTableCommon(classesAllStudentsTable);
    }

    public void fillAddClassesStudentsTable() {
        fillClassesStudentsTableCommon(addClassStudentsTable);
    }

    public void fillLogs() {
        List<String> logCsvStrings = FileUtil.readFile(Log.logCsvPath);
        List<Log> logs = new ArrayList<>();
        for(String logCsv:logCsvStrings){
            logs.add(new Log(logCsv));
        }
        DefaultTableModel model = (DefaultTableModel) logsTable.getModel();
        model.setRowCount(0);
        for (Log log : logs) {
            model.addRow(new Object[]{log.getUser(), log.getOperation(), log.getMessage(), log.getDateString()});
        }
    }

    public void insertLog(String operation, String message){
        String logCsv = Log.createCsvLog("test", operation, message);
        FileUtil.writeFile(logCsv, Log.logCsvPath);
        fillLogs();
    }

    public void fillClassesStudentsTableCommon(JTable table) {
        List<Student> students = studentsController.getStudents();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Student student : students) {
            model.addRow(
                new Object[]{student.getPersonId(), student.getFirstName(), student.getLastName(), student.getAge(), student.getGrade()});
        }
    }

    public void fillStudentsTableByName(JTable table){
      List<Student> studentsList = studentsController.getStudents();
      Function<Student, String> byFirstName = Student::getFirstName;
      Function<Student, String> byLastName = Student::getLastName;
      Comparator<Student> lastThenFirst = Comparator.comparing(byLastName).thenComparing(byFirstName);
      studentsList = studentsList.stream().sorted(lastThenFirst).collect(Collectors.toList());

      DefaultTableModel model = (DefaultTableModel) table.getModel();
      model.setRowCount(0);

      for (Student student : studentsList) {
          System.out.println("Student Name : "+ student.getFirstName());
          model.addRow(
              new Object[]{student.getPersonId(), student.getFirstName(), student.getLastName(), student.getAge(), student.getPhoneNo(),
                  student.getAddress(), student.getGrade(), student.getRegTime()});
      }
  }

  public void fillTeachersTableByName(JTable table){
      List<Teacher> teachersList = teacherController.getTeachers();
      Function<Teacher, String> byFirstName = Teacher::getFirstName;
      Function<Teacher, String> byLastName = Teacher::getLastName;
      Comparator<Teacher> lastThenFirst = Comparator.comparing(byLastName).thenComparing(byFirstName);
      teachersList = teachersList.stream().sorted(lastThenFirst).collect(Collectors.toList());

      DefaultTableModel model = (DefaultTableModel) table.getModel();
      model.setRowCount(0);

      for (Teacher teacher : teachersList) {
          model.addRow(
              new Object[]{teacher.getPersonId(), teacher.getFirstName(), teacher.getLastName(), teacher.getAge(), teacher.getPhoneNo(),
                  teacher.getAddress(), teacher.getSalary()});
      }
  }

    public void fillTeacherTable() {
        fillTeacherTableCommon(teacherTable);
    }

    public void fillAddClassesTeacherTable() {
        fillTeacherTableCommon(classesTeacherTableTable);
    }

    public void fillTeacherTableCommon(JTable table) {
        List<Teacher> teachers = teacherController.getTeachers();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Teacher teacher : teachers) {
            model.addRow(
                    new Object[]{teacher.getPersonId(), teacher.getFirstName(), teacher.getLastName(), teacher.getAge(), teacher.getPhoneNo(),
                        teacher.getAddress(), teacher.getSalary()});
        }
    }

    public void fillClassesTable() {
        List<Class> classes = classController.getClasses();
        DefaultTableModel model = (DefaultTableModel) classesTabClassTable.getModel();
        model.setRowCount(0);

        for (Class clzz : classes) {
            model.addRow(new Object[]{clzz.getClassId(), clzz.getName(), clzz.getTeacher().getFirstName() + " " + clzz.getTeacher().getLastName(), clzz.getTerm()});
        }
    }

    public void reinitStudentsTabFields() {
        this.firstnameField.setText("");
        this.lastnameField.setText("");
        this.ageField.setText("");
        this.phonenoField.setText("");
        this.addressField.setText("");
        this.gradeField.setText("");
    }

    public void reinitTeachersTabFields() {
        this.teacherFirstnameField.setText("");
        this.teacherLastnameField.setText("");
        this.teacherAgeField.setText("");
        this.teacherPhonenoField.setText("");
        this.teacherAddressField.setText("");
        this.teacherSalaryField.setText("");
    }

    public void reinitAddClassTabFields() {
        this.classNameField.setText("");
    }

    public void handleClassSelection(ListSelectionEvent event) {
        try {
            Long selectedClassId = Long.valueOf(this.classesTabClassTable.getValueAt(this.classesTabClassTable.getSelectedRow(), 0).toString());
            Class clzz = this.classController.getClassById(selectedClassId);
            DefaultTableModel model = (DefaultTableModel) this.classesTabStudentsTable.getModel();
            model.setRowCount(0);
            for (Student student : clzz.getStudents()) {
                model.addRow(
                        new Object[]{student.getPersonId(), student.getFirstName(), student.getLastName(), student.getAge(), student.getGrade()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetClassPaneStudentsTable() {
        DefaultTableModel model = (DefaultTableModel) this.classesTabStudentsTable.getModel();
        model.setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new JTable();
        jFrame1 = new javax.swing.JFrame();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        firstnameLabel = new javax.swing.JLabel();
        lastnameLabel = new javax.swing.JLabel();
        phonenoLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        gradeLabel = new javax.swing.JLabel();
        firstnameField = new javax.swing.JTextField();
        lastnameField = new javax.swing.JTextField();
        phonenoField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        gradeField = new javax.swing.JTextField();
        addStudentButton = new javax.swing.JButton();
        deleteStudentButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ageField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        studentsTableScroll = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        studentsTable = new JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        teachersFirstnameLabel = new javax.swing.JLabel();
        teachersLastnameLabel = new javax.swing.JLabel();
        teachersAgeLabel = new javax.swing.JLabel();
        teachersPhonenoLabel = new javax.swing.JLabel();
        teachersAddressLabel = new javax.swing.JLabel();
        teachersSalaryLabel = new javax.swing.JLabel();
        teacherFirstnameField = new javax.swing.JTextField();
        teacherLastnameField = new javax.swing.JTextField();
        teacherAgeField = new javax.swing.JTextField();
        teacherPhonenoField = new javax.swing.JTextField();
        teacherAddressField = new javax.swing.JTextField();
        teacherSalaryField = new javax.swing.JTextField();
        teacherAddButton = new javax.swing.JButton();
        teacherDeleteButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        teacherScrollPane = new javax.swing.JScrollPane();
        teacherTable = new JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        classnameTitle = new javax.swing.JLabel();
        termLabel = new javax.swing.JLabel();
        termDropDown = new javax.swing.JComboBox<>();
        classNameField = new javax.swing.JTextField();
        classesTeacherTable = new javax.swing.JScrollPane();
        classesTeacherTableTable = new JTable();
        selectTeacherLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        addClassStudentsScrollPane = new javax.swing.JScrollPane();
        addClassStudentsTable = new JTable();
        addClassButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        classesTabClassScrollPane = new javax.swing.JScrollPane();
        classesTabClassTable = new JTable();
        jLabel3 = new javax.swing.JLabel();
        classesTabStudentsScroll = new javax.swing.JScrollPane();
        classesTabStudentsTable = new JTable();
        deleteClassButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        classesAllStudentsTable = new JTable();
        jLabel5 = new javax.swing.JLabel();
        updateClassButton = new javax.swing.JToggleButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        logsTable = new JTable();
        projectTitleLabel = new javax.swing.JLabel();
        logout = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabbedPane.setBackground(new java.awt.Color(0, 0, 0));
        tabbedPane.setForeground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(242, 242, 242));

        jPanel2.setBackground(new java.awt.Color(242, 242, 242));

        firstnameLabel.setBackground(new java.awt.Color(255, 255, 102));
        firstnameLabel.setText("Firstname:");

        lastnameLabel.setText("Lastname:");

        phonenoLabel.setText("PhoneNo:");

        addressLabel.setText("Address:");

        gradeLabel.setText("Grade:");

        firstnameField.setBackground(new java.awt.Color(204, 204, 255));
        firstnameField.setToolTipText("Firstname");
        firstnameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstnameFieldActionPerformed(evt);
            }
        });

        lastnameField.setBackground(new java.awt.Color(204, 204, 255));
        lastnameField.setToolTipText("Lastname");

        phonenoField.setBackground(new java.awt.Color(204, 204, 255));
        phonenoField.setToolTipText("PhoneNo");

        addressField.setBackground(new java.awt.Color(204, 204, 255));
        addressField.setToolTipText("Address");

        gradeField.setBackground(new java.awt.Color(204, 204, 255));
        gradeField.setToolTipText("Grade");

        addStudentButton.setBackground(new java.awt.Color(0, 255, 255));
        addStudentButton.setText("Add");
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        deleteStudentButton.setBackground(new java.awt.Color(0, 255, 255));
        deleteStudentButton.setText("Delete");
        deleteStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStudentButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Age");

        ageField.setBackground(new java.awt.Color(204, 204, 255));
        ageField.setToolTipText("Age");

        jButton2.setBackground(new java.awt.Color(0, 255, 255));
        jButton2.setText("Sort By Name");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(deleteStudentButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addStudentButton)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(221, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lastnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phonenoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gradeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(96, 96, 96)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lastnameField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ageField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(phonenoField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addressField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(gradeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(firstnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(96, 96, 96)
                                .addComponent(firstnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstnameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastnameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phonenoLabel)
                    .addComponent(phonenoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gradeLabel)
                    .addComponent(gradeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteStudentButton)
                    .addComponent(addStudentButton)
                    .addComponent(jButton2))
                .addGap(28, 28, 28))
        );

        studentsTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        studentsTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Firstname", "Lastname", "Age", "PhoneNo", "Address", "Grade", "RegTime"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(studentsTable);

        studentsTableScroll.setViewportView(jScrollPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentsTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(studentsTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Students", jPanel1);

        jPanel4.setBackground(new java.awt.Color(242, 242, 242));

        teachersFirstnameLabel.setText("Firstname:");

        teachersLastnameLabel.setText("Lastname:");

        teachersAgeLabel.setText("Age:");

        teachersPhonenoLabel.setText("PhoneNo:");

        teachersAddressLabel.setText("Address:");

        teachersSalaryLabel.setText("Salary:");

        teacherFirstnameField.setBackground(new java.awt.Color(204, 204, 255));
        teacherFirstnameField.setToolTipText("Firstname");
        teacherFirstnameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherFirstnameFieldActionPerformed(evt);
            }
        });

        teacherLastnameField.setBackground(new java.awt.Color(204, 204, 255));
        teacherLastnameField.setToolTipText("Lastname");

        teacherAgeField.setBackground(new java.awt.Color(204, 204, 255));
        teacherAgeField.setToolTipText("Age");
        teacherAgeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherAgeFieldActionPerformed(evt);
            }
        });

        teacherPhonenoField.setBackground(new java.awt.Color(204, 204, 255));
        teacherPhonenoField.setToolTipText("PhoneNo");

        teacherAddressField.setBackground(new java.awt.Color(204, 204, 255));
        teacherAddressField.setToolTipText("Address");

        teacherSalaryField.setBackground(new java.awt.Color(204, 204, 255));
        teacherSalaryField.setToolTipText("Salary");

        teacherAddButton.setBackground(new java.awt.Color(0, 255, 255));
        teacherAddButton.setText("Add");
        teacherAddButton.setToolTipText("Add");
        teacherAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherAddButtonActionPerformed(evt);
            }
        });

        teacherDeleteButton.setBackground(new java.awt.Color(0, 255, 255));
        teacherDeleteButton.setText("Delete");
        teacherDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherDeleteButtonActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 255, 255));
        jButton1.setText("Sort By Name");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(189, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(teacherDeleteButton)
                        .addGap(18, 18, 18)
                        .addComponent(teacherAddButton))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(teachersFirstnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teachersLastnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teachersAgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teachersPhonenoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teachersAddressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(teachersSalaryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(89, 89, 89)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(teacherFirstnameField)
                            .addComponent(teacherLastnameField)
                            .addComponent(teacherAgeField)
                            .addComponent(teacherPhonenoField)
                            .addComponent(teacherAddressField)
                            .addComponent(teacherSalaryField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE))))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teachersFirstnameLabel)
                    .addComponent(teacherFirstnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teachersLastnameLabel)
                    .addComponent(teacherLastnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teachersAgeLabel)
                    .addComponent(teacherAgeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teachersPhonenoLabel)
                    .addComponent(teacherPhonenoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teachersAddressLabel)
                    .addComponent(teacherAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teachersSalaryLabel)
                    .addComponent(teacherSalaryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teacherAddButton)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(teacherDeleteButton)
                        .addComponent(jButton1)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        teacherScrollPane.setBackground(new java.awt.Color(102, 102, 102));
        teacherScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        teacherScrollPane.setForeground(new java.awt.Color(102, 102, 102));

        teacherTable.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Firstname", "Lastname", "Age", "PhoneNo", "Address", "Salary"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        teacherScrollPane.setViewportView(teacherTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(teacherScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(113, 113, 113))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Teachers", jPanel3);

        jPanel6.setBackground(new java.awt.Color(242, 242, 242));

        classnameTitle.setText("Classname:");

        termLabel.setText("Term:");

        termDropDown.setBackground(new java.awt.Color(204, 204, 255));
        termDropDown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring2023", "Fall2023", "Spring2024", "Fall2024" }));
        termDropDown.setSelectedItem(termDropDown);
        termDropDown.setToolTipText("Term");
        termDropDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termDropDownActionPerformed(evt);
            }
        });

        classNameField.setBackground(new java.awt.Color(204, 204, 255));
        classNameField.setToolTipText("Classname");
        classNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classNameFieldActionPerformed(evt);
            }
        });

        classesTeacherTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        classesTeacherTableTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Firstname", "Lastname"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classesTeacherTable.setViewportView(classesTeacherTableTable);

        selectTeacherLabel.setText("Select teacher from below table");
        selectTeacherLabel.setToolTipText("");

        jLabel1.setText("Select students from below table");

        addClassStudentsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addClassStudentsTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Firstname", "Lastname", "Age", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addClassStudentsScrollPane.setViewportView(addClassStudentsTable);

        addClassButton.setBackground(new java.awt.Color(0, 255, 255));
        addClassButton.setText("Add class");
        addClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addClassStudentsScrollPane)
                    .addComponent(classesTeacherTable, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addComponent(selectTeacherLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(classnameTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(termLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(termDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(classNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(207, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(classnameTitle)
                            .addComponent(classNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(termDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(termLabel)))
                    .addComponent(addClassButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectTeacherLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(classesTeacherTable, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addClassStudentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Add Class", jPanel5);

        classesTabClassScrollPane.setBackground(new java.awt.Color(102, 102, 102));
        classesTabClassScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        classesTabClassTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Classname", "Teacher", "Term"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classesTabClassTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event){
                handleClassSelection(event);
            }
        });
        classesTabClassScrollPane.setViewportView(classesTabClassTable);

        jLabel3.setText("Select class from list to view students in class");

        classesTabStudentsScroll.setBackground(new java.awt.Color(102, 102, 102));
        classesTabStudentsScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        classesTabStudentsTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Firstname", "Lastname", "Age", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classesTabStudentsScroll.setViewportView(classesTabStudentsTable);

        deleteClassButton.setBackground(new java.awt.Color(0, 255, 255));
        deleteClassButton.setText("Delete class");
        deleteClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClassButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Students in selected class");

        classesAllStudentsTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        classesAllStudentsTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "First Name", "Last Name", "Age", "Grade"
            }
        ));
        jScrollPane3.setViewportView(classesAllStudentsTable);

        jLabel5.setText("All students");

        updateClassButton.setBackground(new java.awt.Color(0, 255, 255));
        updateClassButton.setText("Update Class");
        updateClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateClassButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addComponent(classesTabClassScrollPane)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                        .addComponent(updateClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addComponent(classesTabStudentsScroll)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(deleteClassButton)
                    .addComponent(updateClassButton))
                .addGap(1, 1, 1)
                .addComponent(classesTabClassScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classesTabStudentsScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Classes", jPanel7);

        jPanel8.setBackground(new java.awt.Color(242, 242, 242));

        logsTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "User", "Operation", "Message", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(logsTable);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Logs", jPanel8);

        projectTitleLabel.setBackground(new java.awt.Color(0, 0, 0));
        projectTitleLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        projectTitleLabel.setText("School Management System");

        logout.setBackground(new java.awt.Color(0, 255, 255));
        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel6.setText("Admin Portal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabbedPane)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(projectTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logout)
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectTitleLabel)
                    .addComponent(logout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.getAccessibleContext().setAccessibleName("tabbedPane");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClassButtonActionPerformed
        // TODO add your handling code here:
        int selectedClassRowId = this.classesTabClassTable.getSelectedRow();
        Long selectedClassId = Long.valueOf(this.classesTabClassTable.getValueAt(selectedClassRowId, 0).toString());
        Class clzz = this.classController.getClassById(selectedClassId);
        classController.deleteClass(selectedClassId);
        insertLog("Deleted class", "Deleted Class ID: " + clzz.getClassId() + " | Class Name: " + clzz.getName());
        init();
        resetClassPaneStudentsTable();
    }//GEN-LAST:event_deleteClassButtonActionPerformed

    private void addClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassButtonActionPerformed
        // TODO add your handling code here:
        String className = this.classNameField.getText();
        String term = this.termDropDown.getSelectedItem().toString();
        int[] selectedTeacherRowId = this.classesTeacherTableTable.getSelectedRows();
        if (selectedTeacherRowId.length != 1) {
            JOptionPane.showMessageDialog(this, "Invalid teachers selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Long selectedTeacherId = Long.valueOf(this.classesTeacherTableTable.getValueAt(selectedTeacherRowId[0], 0).toString());
        int[] selectedStudentRowId = this.addClassStudentsTable.getSelectedRows();
        if (selectedStudentRowId.length < 1) {
            JOptionPane.showMessageDialog(this, "Select at least 1 student!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder studentIdsPipeList = new StringBuilder();
        for (int i = 0; i < selectedStudentRowId.length; i++) {
            studentIdsPipeList.append(this.addClassStudentsTable.getValueAt(selectedStudentRowId[i], 0));
            if (i < selectedStudentRowId.length - 1) {
                studentIdsPipeList.append("|");
            }
        }
        Class clzz = new Class(className, selectedTeacherId, studentIdsPipeList.toString(), term);
        this.classController.addClass(clzz);
        insertLog("Add class", "Added class ID: " + clzz.getClassId() + " | Class Name: " + clzz.getName());
        JOptionPane.showMessageDialog(this, "Class added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
        init();
    }//GEN-LAST:event_addClassButtonActionPerformed

    private void classNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_classNameFieldActionPerformed

    private void termDropDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termDropDownActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_termDropDownActionPerformed

    private void teacherDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherDeleteButtonActionPerformed
        // TODO add your handling code here:
        int rowId = this.teacherTable.getSelectedRow();
        if (rowId < 0) {
            JOptionPane.showMessageDialog(this, "No rows selected!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultTableModel model = (DefaultTableModel) this.teacherTable.getModel();
            Long personId = Long.valueOf(model.getValueAt(rowId, 0).toString());
            Teacher teacher = this.teacherController.getTeacher(personId);
            if(this.classController.checkIfTeacherInClass(teacher)){
                JOptionPane.showMessageDialog(this, "Teacher enrolled in class!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.teacherController.deleteTeacher(personId);
            insertLog("Deleted teacher", "Deleted teacher ID: " + teacher.getPersonId() + " | Teacher Name: " + teacher.getFullName());
            init();
        }
    }//GEN-LAST:event_teacherDeleteButtonActionPerformed

    private void teacherAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherAddButtonActionPerformed
        // TODO add your handling code here:
        String firstName = teacherFirstnameField.getText();
        String lastName = teacherLastnameField.getText();
        Long age;
        try{
            age = Long.valueOf(teacherAgeField.getText());
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(this, "Invalid age value!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phoneNo = teacherPhonenoField.getText();
        String address = teacherAddressField.getText();
        Double salary;
        try{
            salary = Double.valueOf(teacherSalaryField.getText());
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(this, "Invalid salary value!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Teacher teacher = new Teacher(age, firstName, lastName, phoneNo, address, salary);
        this.teacherController.addTeacher(teacher);
        insertLog("Add teacher", "Added teacher ID: " + teacher.getPersonId() + " | Teacher Name: " + teacher.getFullName());
        init();
    }//GEN-LAST:event_teacherAddButtonActionPerformed

    private void teacherAgeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherAgeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherAgeFieldActionPerformed

    private void teacherFirstnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherFirstnameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherFirstnameFieldActionPerformed

    private void deleteStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStudentButtonActionPerformed
        // TODO add your handling code here:
        int rowId = this.studentsTable.getSelectedRow();
        if (rowId < 0) {
            JOptionPane.showMessageDialog(this, "No rows selected!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultTableModel model = (DefaultTableModel) this.studentsTable.getModel();
            Long personId = Long.parseLong(model.getValueAt(rowId, 0).toString());
            Student student = this.studentsController.getStudent(personId);
            if(this.classController.checkIfStudentInClass(student)){
                JOptionPane.showMessageDialog(this, "Student enrolled in class!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.studentsController.deleteStudent(personId);
            insertLog("Deleted student", "Deleted student ID: " + student.getPersonId() + " | Student Name: " + student.getFullName());
            init();
        }
    }//GEN-LAST:event_deleteStudentButtonActionPerformed

    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        // TODO add your handling code here:
        String firstName = firstnameField.getText();
        String lastName = lastnameField.getText();
        Long age;
        try{
            age = Long.valueOf(ageField.getText());
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(this, "Invalid age value!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phoneNo = phonenoField.getText();
        String address = addressField.getText();
        Double grade;
        try{
            grade = Double.valueOf(gradeField.getText());
        } catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(this, "Invalid grade value!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Student student = new Student(age, firstName, lastName, phoneNo, address, grade, new Timestamp(System.currentTimeMillis()));
        this.studentsController.addStudent(student);
        insertLog("Add student", "Added student ID: " + student.getPersonId() + " | Student Name: " + student.getFullName());
        init();
    }//GEN-LAST:event_addStudentButtonActionPerformed

    private void firstnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstnameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstnameFieldActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        insertLog("Logout", "User logged out!");
        this.appFrame.clearField();
        this.appFrame.setVisible(true);

    }//GEN-LAST:event_logoutActionPerformed

    private void updateClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateClassButtonActionPerformed
        // TODO add your handling code here:
        int rowClassId = this.classesTabClassTable.getSelectedRow();
        int[] selectedStudentRowId = this.classesAllStudentsTable.getSelectedRows();
        if (rowClassId < 0) {
            JOptionPane.showMessageDialog(this, "No classes selected!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (selectedStudentRowId.length <= 0) {
            JOptionPane.showMessageDialog(this, "No students selected!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuilder studentIdsPipeList = new StringBuilder();
            for (int i = 0; i < selectedStudentRowId.length; i++) {
                studentIdsPipeList.append(this.addClassStudentsTable.getValueAt(selectedStudentRowId[i], 0));
                if (i < selectedStudentRowId.length - 1) {
                    studentIdsPipeList.append("|");
                }
            }

            Long selectedClassId = Long.valueOf(this.classesTabClassTable.getValueAt(rowClassId, 0).toString());
            Class clzz = classController.getClassById(selectedClassId);
            clzz.setStudentsPipeList(studentIdsPipeList.toString());
            this.classController.addClass(clzz);
            JOptionPane.showMessageDialog(this, "Class updated successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
            insertLog("Update class", "Updated class ID: " + clzz.getClassId() + " | class name: " + clzz.getName());
            init();
        }
    }//GEN-LAST:event_updateClassButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        fillTeachersTableByName(teacherTable);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        fillStudentsTableByName(studentsTable);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TabbedPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TabbedPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TabbedPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TabbedPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TabbedPane().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClassButton;
    private javax.swing.JScrollPane addClassStudentsScrollPane;
    private JTable addClassStudentsTable;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField ageField;
    private javax.swing.JTextField classNameField;
    private JTable classesAllStudentsTable;
    private javax.swing.JScrollPane classesTabClassScrollPane;
    private JTable classesTabClassTable;
    private javax.swing.JScrollPane classesTabStudentsScroll;
    private JTable classesTabStudentsTable;
    private javax.swing.JScrollPane classesTeacherTable;
    private JTable classesTeacherTableTable;
    private javax.swing.JLabel classnameTitle;
    private javax.swing.JButton deleteClassButton;
    private javax.swing.JButton deleteStudentButton;
    private javax.swing.JTextField firstnameField;
    private javax.swing.JLabel firstnameLabel;
    private javax.swing.JTextField gradeField;
    private javax.swing.JLabel gradeLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private JTable jTable1;
    private javax.swing.JTextField lastnameField;
    private javax.swing.JLabel lastnameLabel;
    private javax.swing.JButton logout;
    private JTable logsTable;
    private javax.swing.JTextField phonenoField;
    private javax.swing.JLabel phonenoLabel;
    private javax.swing.JLabel projectTitleLabel;
    private javax.swing.JLabel selectTeacherLabel;
    private JTable studentsTable;
    private javax.swing.JScrollPane studentsTableScroll;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton teacherAddButton;
    private javax.swing.JTextField teacherAddressField;
    private javax.swing.JTextField teacherAgeField;
    private javax.swing.JButton teacherDeleteButton;
    private javax.swing.JTextField teacherFirstnameField;
    private javax.swing.JTextField teacherLastnameField;
    private javax.swing.JTextField teacherPhonenoField;
    private javax.swing.JTextField teacherSalaryField;
    private javax.swing.JScrollPane teacherScrollPane;
    private JTable teacherTable;
    private javax.swing.JLabel teachersAddressLabel;
    private javax.swing.JLabel teachersAgeLabel;
    private javax.swing.JLabel teachersFirstnameLabel;
    private javax.swing.JLabel teachersLastnameLabel;
    private javax.swing.JLabel teachersPhonenoLabel;
    private javax.swing.JLabel teachersSalaryLabel;
    private javax.swing.JComboBox<String> termDropDown;
    private javax.swing.JLabel termLabel;
    private javax.swing.JToggleButton updateClassButton;
    // End of variables declaration//GEN-END:variables
}
