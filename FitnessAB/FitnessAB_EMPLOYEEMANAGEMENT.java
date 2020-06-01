import javax.swing.*;
import java.io.*;
import java.sql.*;
import org.sqlite.SQLiteConfig;
import java.util.*;

public class FitnessAB_EMPLOYEEMANAGEMENT {

    public static final String DB_URL = "jdbc:sqlite:FitnessAB";
    public static final String DRIVER = "org.sqlite.JDBC";

    public static void main(String[] args) throws IOException {
        Connection conn = null;

        try {
            Class.forName(DRIVER);
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true); 
            conn = DriverManager.getConnection(DB_URL,config.toProperties());
        } catch (Exception e) {
            System.out.println( e.toString() );
            System.exit(0);
        }

//the database connection code is copied from our last programming project in TIG058 
//with some slight alterations
       while(true){
        String Input = JOptionPane.showInputDialog("Select an action to take:\n" +
                                                   "A - insert a new employee\n" +
                                                   "B - register a sickday \n" +
                                                   "C - Punch in \n" +
                                                   "D - Punch out\n" +
                                                   "E - get employee info \n" +
                                                   "F - info about sick employees \n" +
                                                   "G - info about who teaches a course \n");
                                                   
        if(Input == null){
         System.exit(0);
        }
        
        String SQL = "";
        PreparedStatement statement;

        try{
            switch(Input){

                case "A":
                case "a":
                    //new employee
                    SQL = "insert into EMPLOYEE(FName, EName, PhoneNr, EmailAddress, AdminID) values (?, ?, ?, ?, ?)";
                    statement = conn.prepareStatement(SQL);

                    Input = JOptionPane.showInputDialog("What is the employee first name");
                    statement.setString(1, Input);

                    Input = JOptionPane.showInputDialog("What is the employee last name");
                    statement.setString(2, Input);

                    Input = JOptionPane.showInputDialog("What is the employees phonenumber");
                    statement.setString(3, Input);

                    Input = JOptionPane.showInputDialog("What is the employees email address");
                    statement.setString(4, Input);

                    Input = JOptionPane.showInputDialog("What is the adminId of the new employees admin");
                    int i = Integer.parseInt(Input);
                    statement.setInt(5, i);

                    statement.executeUpdate();
                break;

                case "B":
                case "b":
                    //this is the sickreg use case
                    SQL = "insert into TIME(EmployeeID, locationID, Date, Sickreg) values (?, ?, ?, ?)";
                            statement = conn.prepareStatement(SQL);
                    Input = JOptionPane.showInputDialog("What is your employeeID");
                    i = Integer.parseInt(Input);
                    statement.setInt(1, i);
                    
                    Input = JOptionPane.showInputDialog("What is your work LocationID");
                    i = Integer.parseInt(Input);
                    statement.setInt(2, i);

                    Input = CalEssential.CalDateToString();
                    statement.setString(3, Input);

                    statement.setString(4, "Yes");

                    statement.executeUpdate();
                break;

                case "C":
                case "c":
                    //this is the punch in
                    String EmpID, Date, location, punchin;

                    SQL = "insert into TIME(EmployeeID, Date, LocationID, PunchIn, SickReg) values (?,?,?,?,?)";
                            statement = conn.prepareStatement(SQL);

                    Input = JOptionPane.showInputDialog("What is your employeeID");
                    i = Integer.parseInt(Input);
                    statement.setInt(1, i);
                    EmpID = "" + i;

                    Input = CalEssential.CalDateToString();
                    statement.setString(2, Input);
                    Date = Input;

                    Input = JOptionPane.showInputDialog("What is the Location ID");
                    i = Integer.parseInt(Input);
                    statement.setInt(3, i);
                    location = "" + i;

                    punchin = Calendar.getInstance().getTime().toString();
                    String temp = punchin.substring(11, 16);
                    statement.setString(4, temp);
                    punchin = temp;
                    
                    statement.setString(5, "No");

                    statement.executeUpdate();

                    SQL = ("select TimeeventID from time where EmployeeID = '"+ EmpID +
                           "' and date = '"+ Date + "' and locationID = '"+ location + "' and PunchIn = '" +
                             punchin + "';");
                    statement = conn.prepareStatement(SQL);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()){
                        int timeevent = rs.getInt(1);
                        JOptionPane.showMessageDialog(null, "your TimeeventID is: "+ timeevent);
                    }
                break;

                case "D":
                case "d":
                    Input = JOptionPane.showInputDialog("What is your timeeventID");
                    i = Integer.parseInt(Input);

                    String punchout = Calendar.getInstance().getTime().toString();
                    temp = punchout.substring(11, 16);
                    punchout = temp;

                    SQL = "update time set PunchOut = '"+ punchout + "' where TimeeventID = " + i;

                    statement = conn.prepareStatement(SQL);
                    statement.executeUpdate();
                    
                    JOptionPane.showMessageDialog(null, "you have now been punched out");
                break;
                
                case "E":
                case "e":
                 
                 SQL = "select FNAME, ENAME, PHONENR, EMAILADDRESS, ADMINID from EMPLOYEE where EMPLOYEEID = ?";
                 statement = conn.prepareStatement(SQL);
                 
                 Input = JOptionPane.showInputDialog("Which Employee do you want information about? \n Enter employeeID");
                 i = Integer.parseInt(Input);
                 statement.setInt(1, i);
                 
                 rs = statement.executeQuery();
                 
                 String result = "";
                 while(rs.next()){
                  String fname = rs.getString(1);
                  String ename = rs.getString(2);
                  String phonenr = rs.getString(3);
                  String email = rs.getString(4);
                  int adminid = rs.getInt(5);
                  result = "This is the employee's information: \n Name:" + fname +" " + ename +
                                  "\n Phone#:" + phonenr + "\n Email: " + email + "\n Admin: " + adminid;
                 }               
                  JOptionPane.showMessageDialog(null, result);
                break;
                
                case "F":
                case "f":
                
                 SQL = "select FNAME, ENAME from EMPLOYEE where EMPLOYEEID in"+
                       "(select employeeID from time where sickreg = \"Yes\" and date = ?);";
                 statement = conn.prepareStatement(SQL);
                 
                 Input = JOptionPane.showInputDialog("Input which date you want sickreg info about");
                 statement.setString(1, Input);
                 
                 rs = statement.executeQuery();
                 
                 String resultat = "These employees were sick that day: \n";
                 while(rs.next()){
                  String fname = rs.getString(1);
                  String ename = rs.getString(2);
                  
                  resultat = resultat + "\n" + fname + " " + ename;
                 }
                 
                 JOptionPane.showMessageDialog(null, resultat);
                
                break;
                
                case "G":
                case "g":
                
                 SQL = "select * from course natural join location where courseID in" + 
                       "(select courseid from teachescourse where employeeid = ?);";
                 statement = conn.prepareStatement(SQL);
                 
                 Input = JOptionPane.showInputDialog("please enter the employeeid for the person \n"+
                                                     "for the person you want course information about");
                 i = Integer.parseInt(Input);
                 statement.setInt(1, i);
                 
                 rs = statement.executeQuery();
                 
                 resultat = "these are the courses the employee handles: \n";
                 while(rs.next()){
                  String coursename = rs.getString(2);
                  String time = rs.getString(4);
                  String days = rs.getString(5);
                  String locname = rs.getString(6);
                  String address = rs.getString(7);
                  
                  resultat = resultat + coursename +" "+ time +" "+ days +" "+ locname +" "+ address +"\n";
                 }
                 
                 JOptionPane.showMessageDialog(null, resultat);
                
                break;
                
                default:
                 System.exit(0);
                break;

            }
        }
        catch(SQLException e1){
            JOptionPane.showMessageDialog(null, e1.toString());
        }
       }
    }
}