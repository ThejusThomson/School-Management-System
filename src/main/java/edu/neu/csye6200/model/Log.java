/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.neu.csye6200.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    
    private String user;
    private String operation;
    private String message;
    private Date date;
    
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static final String logCsvPath = "src/main/java/edu/neu/csye6200/logs.csv";

    public Log(String user, String operation, String message, Date date) {
        this.user = user;
        this.operation = operation;
        this.message = message;
        this.date = date;
    }
    
    public Log(String logCsv){
        String[] logCsvArr = logCsv.split(",");
        this.user = logCsvArr[0];
        this.operation = logCsvArr[1];
        this.message = logCsvArr[2];
        try {
            this.date = this.dateFormat.parse(logCsvArr[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public String createCsvLog(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.user).append(",");
        sb.append(this.operation).append(",");
        sb.append(this.message).append(",");
        String strDate = this.dateFormat.format(this.date);  
        sb.append(strDate);
        return sb.toString();
    }
    
    public static String createCsvLog(String user, String operation, String message){
        Log log = new Log(user, operation, message, new Date(System.currentTimeMillis()));
        return log.createCsvLog();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getDateString(){
        return this.dateFormat.format(this.date);
    }
}
