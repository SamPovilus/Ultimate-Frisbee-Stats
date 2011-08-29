// from http://www.java2s.com/Code/Java/Database-SQL-JDBC/ConvertjavasqlTimestamptolongforeasycompare.htm
public class DateLabel {
  public static void main(String[] args) {
    java.util.Date today = new java.util.Date();
    java.sql.Timestamp ts1 = new java.sql.Timestamp(today.getTime());
    java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf("2005-04-06 09:01:10");

    long tsTime1 = ts1.getTime();
    long tsTime2 = ts2.getTime();
    
    System.out.println(tsTime1);
    System.out.println(tsTime2);
  }
}