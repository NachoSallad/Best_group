import javax.swing.*;
import java.io.*;
import java.sql.*;
import org.sqlite.SQLiteConfig;
import java.util.*;

public class FitnessAB_COURSEENROLLMENT {

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
                                                   "A - sign member up for course\n" +
                                                   "B - cancel course participation\n" +
                                                   "C - view all courses \n" +
                                                   "D - create new course \n" +
                                                   "E - pay for course \n" +
                                                   "F - view payments");
        if(Input == null){
         System.exit(0);
        }
        
      
      
        String SQL = "";
        PreparedStatement statement;
        ResultSet rs;
        int i;
        
        try{
            switch(Input){
               
               case "A":
               case "a":
                  
                  SQL = "insert into courseparticipation(memberID, courseID) values(?, ?);";
                     statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("What member is signing up for the course? memberid");
                     i = Integer.parseInt(Input);
                     statement.setInt(1, i);
                  
                  Input = JOptionPane.showInputDialog("What course is the member signing up for? course id");
                      i = Integer.parseInt(Input);
                      statement.setInt(2, i);
                  
                  statement.executeUpdate();
                  
                  SQL = "select coursename, name from course natural join location where courseid = ?;";
                      statement = conn.prepareStatement(SQL);
                      statement.setInt(1, i);
                  
                  rs = statement.executeQuery();
                  
                  String resultat = "";
                  while(rs.next()){
                     String cname = rs.getString(1);
                     String loc = rs.getString(2);
                     
                     resultat = "The member was signed up for " + cname + " at " + loc;
                  }
                  
                  JOptionPane.showMessageDialog(null, resultat);
               break;
               
               case "B":
               case "b":
                  
                  SQL = "delete from courseparticipation where memberId = ? and courseid = ?;";
                      statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("Which member is cancelling participation? memberid");
                  i = Integer.parseInt(Input);
                  statement.setInt(1, i);
                  
                  Input = JOptionPane.showInputDialog("Which course is the member cancelling participation in? courseid");
                  i = Integer.parseInt(Input);
                  statement.setInt(2, i);
                  
                  statement.executeUpdate();
                  JOptionPane.showMessageDialog(null, "The participation has been cancelled");
               
               
               break;
               
               case "C":
               case "c":
                  
                  SQL = "select * from course natural join location;";
                     statement = conn.prepareStatement(SQL);
                  
                  rs = statement.executeQuery();
                  
                  resultat = "These are all the current courses: \n";
                  
                  while(rs.next()){
                     int cid = rs.getInt(1);
                     String cname = rs.getString(2);
                     String time = rs.getString(4);
                     String days = rs.getString(5);
                     String loc = rs.getString(6);
                     String address = rs.getString(7);
                     
                     resultat = resultat + cid + " " + cname + " " + time + " " + days + " " +
                                loc + " " + address + "\n";
                  }              
                     
                  JOptionPane.showMessageDialog(null, resultat);
                  
               break;
               
               case "D":
               case "d":
                  
                  SQL = "insert into course(coursename, locationid, time, days) values (?, ?, ?, ?);";
                     statement = conn.prepareStatement(SQL);
                     
                  Input = JOptionPane.showInputDialog("What is the new coursename?");
                     statement.setString(1, Input);
                  
                  Input = JOptionPane.showInputDialog("in what location will the course take place? locationID");
                     statement.setString(2, Input);
                     
                  Input = JOptionPane.showInputDialog("at what time will the course take place?");
                     statement.setString(3, Input);
                     
                  Input = JOptionPane.showInputDialog("What days of the week will the course take place? M T W TH F ST S");
                     statement.setString(4, Input);
                     
                  statement.executeUpdate();
                  
                  JOptionPane.showMessageDialog(null, "the course has been added"); 
                  
               break;
               
               case "E":
               case "e":
                  
                  SQL = "insert into coursepayments(memberid, adminid, course, fee, date) values(?,?,?,?,?);";
                     statement = conn.prepareStatement(SQL);
                     
                  Input = JOptionPane.showInputDialog("which member is making the payment? memberid");
                     i = Integer.parseInt(Input);
                     statement.setInt(1, i);
                     
                  Input = JOptionPane.showInputDialog("which admin is handling it? adminid");
                     i = Integer.parseInt(Input);
                     statement.setInt(2, i);
                  
                  Input = JOptionPane.showInputDialog("which course is the payment for? courseid");
                     i = Integer.parseInt(Input);
                     statement.setInt(3, i); 
                  
                  Input = JOptionPane.showInputDialog("What is the amount in kr of the payment");
                     statement.setString(4, Input);
                  
                  statement.setString(5, CalEssential.CalDateToString());
                  
                  statement.executeUpdate();
                  
               
               break;
               
               case "F":
               case "f":
                  
                  SQL = "select fname, ename, coursename, fee, \"Date\", adminid from coursepayment natural join member natural join course where memberid = ?;";
                     statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("which member do you want payment information about? memberid");
                     i = Integer.parseInt(Input);
                     statement.setInt(1, i);
                     
                  rs = statement.executeQuery();
                  
                  resultat = "These payments have been made by this member: \n";
                  while(rs.next()){
                     String fname = rs.getString(1);
                     String ename = rs.getString(2);
                     String cname = rs.getString(3);
                     String fee = rs.getString(4);
                     String date = rs.getString(5);
                     int adid = rs.getInt(6);
                     
                     resultat = resultat + fname +" "+ ename +" "+ cname +" "+ fee +" "+ date +" "+ adid;
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
