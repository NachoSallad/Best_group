import java.util.*;

public class CalEssential{

 public static String CalDateToString(){
    String CalDate = Calendar.getInstance().getTime().toString();
    
    String day = CalDate.substring(8, 10);
    String month = CalDate.substring(4, 7);
    String year = CalDate.substring(25, 29);
    
    switch(month){
     case "Jan":
      month = "01";
     break;
     case "Feb":
      month = "02";
     break;
     case "Mar":
      month = "03";
     break;
     case "Apr":
      month = "04";
     break;
     case "May":
      month = "05";
     break;
     case "Jun":
      month = "06";
     break;
     case "Jul":
      month = "07";
     break;
     case "Aug":
      month = "08";
     break;
     case "Sep":
      month = "09";
     break;
     case "Oct":
      month = "10";
     break;
     case "Nov":
      month = "11";
     break;
     case "Dec":
      month = "12";
     break;
    }
 String result = day +"."+ month +"."+ year;
 return result;
 
 }
 
 public static String addOneToYear(){
 
    String CalDate = CalDateToString();
    
    String rest = CalDate.substring(0, 6); //00.00.0000
    String year = CalDate.substring(6);
    
    int i = Integer.parseInt(year);
    i = i + 1;
    
    year = "" + i;
    
    String result = "" + rest + year;
    return result;
 }
 
 public static String addOneToMonth(){
   
   String CalDate = CalDateToString();
   
   String day = CalDate.substring(0, 2);
   String month = CalDate.substring(3, 5);
   String year = CalDate.substring(6);
   
   int i = Integer.parseInt(month);
   
   i = i + 1;
    
   if(i > 12){
      i = i - 12;
      
      String junk = addOneToYear();
      year = junk.substring(6);
   }
    
   
   month = String.format("%02d", i);
   
   String result = day +"."+ month +"."+ year;
   return result;
 }  
 
  public static String addSixToMonth(){
   
   String CalDate = CalDateToString();
   
   String day = CalDate.substring(0, 2);
   String month = CalDate.substring(3, 5); //00.00.0000
   String year = CalDate.substring(6);
   
   int i = Integer.parseInt(month);
   
   i = i + 6;
    
   if(i > 12){
      i = i - 12;
      
      String junk = addOneToYear();
      year = junk.substring(6);
   }
    
   
   month = String.format("%02d", i);
   
   String result = day +"."+ month +"."+ year;
   return result;
 }  

}
