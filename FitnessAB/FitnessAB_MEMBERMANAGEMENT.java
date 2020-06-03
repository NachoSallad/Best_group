import javax.swing.*;
import java.io.*;
import java.sql.*;
import org.sqlite.SQLiteConfig;
import java.util.*;

public class FitnessAB_MEMBERMANAGEMENT {

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
                                                   "A - register new member\n" +
                                                   "B - start new membership \n" +
                                                   "C - See booked courses \n" +
                                                   "D - get member information \n" +
                                                   "E - cancel a membership \n" +
                                                   "F - remove member \n");
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
                  
                  SQL = "insert into member(FName, EName, PhoneNr, Email) values (?,?,?,?);";
                  statement = conn.prepareStatement(SQL);
                  
                     Input = JOptionPane.showInputDialog("What is your first name?");
                     statement.setString(1, Input);
                     
                     Input = JOptionPane.showInputDialog("What is you last name?");
                     statement.setString(2, Input);
                     
                     Input = JOptionPane.showInputDialog("What is your phonenumber?");
                     statement.setString(3, Input);
                     
                     Input = JOptionPane.showInputDialog("What is your email?");
                     statement.setString(4, Input);
                  
                  statement.executeUpdate();
               break;
               
               case "B":
               case "b":
                  
                  SQL = "insert into membership(MemberID, MembershipID, StartDate, EndDate) values (?,?,?,?);";
                  statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("which member is starting a membership? MemberID");
                  i = Integer.parseInt(Input);
                  statement.setInt(1, i);     
                  
                  Input = JOptionPane.showInputDialog("Which membership tier are they getting? \n" +
                                                      "1 - Gold, 1 year \n" +
                                                      "2 - Silver, 6 months \n" +
                                                      "3 - Bronze, 1 month \n");
                  i = Integer.parseInt(Input);
                  statement.setInt(2, i);   
                  
                  statement.setString(3, CalEssential.CalDateToString());
                  
                  if(i == 1){
                     statement.setString(4, CalEssential.addOneToYear());
                  }
                  else if(i == 2){
                     statement.setString(4, CalEssential.addSixToMonth());
                  }
                  else if(i == 3){
                     statement.setString(4, CalEssential.addOneToMonth());
                  }
                  else{
                     JOptionPane.showMessageDialog(null, "please make sure to only define one of the existing membership tiers");
                      break;
                  }
                  
                  statement.executeUpdate();
                  
               break;
               
               case "C":
               case "c":
                  
                  SQL = "select coursename, time, days, name, address from course natural join location where courseid in"+
                        "(select distinct courseid from courseparticipation where memberid = ?);";
                        
                  statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("which member do you want to see booked courses for? memberID");
                   i = Integer.parseInt(Input);
                   statement.setInt(1, i);
                   
                  rs = statement.executeQuery();
                  
                  String resultat = "These courses have been booked by the member: \n";
                  
                  while(rs.next()){
                      String cname = rs.getString(1);
                      String time = rs.getString(2);
                      String days = rs.getString(3);
                      String name = rs.getString(4);
                      String address = rs.getString(5);
                  
                      resultat = resultat + cname + " " + time + " " + days + " " + name + " " + address + "\n";
                  }
                  
                  JOptionPane.showMessageDialog(null, resultat);
              
              
               break; 
               
               case "D":
               case "d":
                  
                  SQL = "select memberid, FName, EName, PhoneNr, Email, membershipid from member left natural join membership where memberid = ?;";
                  statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("what member do you want information about? memberID or * for info on all members");
                   if(Input.equals("*")){
                    SQL = "select memberid, FName, EName, PhoneNr, Email, membershipid from member left natural join membership;";
                    statement = conn.prepareStatement(SQL);
                   } 
                   else{
                    i = Integer.parseInt(Input);
                    statement.setInt(1, i);
                   }
                  rs = statement.executeQuery();
                  
                  resultat = "This is the members information: \n";
                  
                  while(rs.next()){
                     int memid = rs.getInt(1);
                     String fname = rs.getString(2);
                     String ename = rs.getString(3);
                     String phone = rs.getString(4);
                     String email = rs.getString(5);
                     int membid = rs.getInt(6);
                     
                     resultat = resultat + "MemberID: " + memid + "\n Name: " +fname + " " + ename + "\n Phone#: " + phone + 
                                "\n Email: " + email + "\n membershiptier: " + membid + "\n \n";
                  }
                     
                  JOptionPane.showMessageDialog(null, resultat);
               break; 
               
               case "E":
               case "e":
                  
                  SQL = "delete from membership where memberid = ?";
                  statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("Which members membership needs to be cancelled? memberid");
                  i = Integer.parseInt(Input);
                  statement.setInt(1, i);
                  
                  statement.executeUpdate();
                  
                  JOptionPane.showMessageDialog(null, "the membership has been cancelled");
                         
                  
               break; 
               
               case "F":
               case "f":
                  
                  SQL = "delete from member where memberid = ?";
                  statement = conn.prepareStatement(SQL);
                  
                  Input = JOptionPane.showInputDialog("which member do you want to remove? memberid");
                  i = Integer.parseInt(Input);
                  statement.setInt(1, i);
                  
                  int opt = JOptionPane.showConfirmDialog(null, "Are you sure?", "warning", JOptionPane.YES_NO_OPTION);
                   if(opt == 0){
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "The member was deleted");
                   }
                   else{
                    JOptionPane.showMessageDialog(null, "The action was cancelled");
                    break;
                   }
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