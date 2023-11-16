package payrollsystem;
import java.sql.*;
import javax.swing.JOptionPane;

public class Conn {
    public Connection c;
    public Statement s;
 
    public Conn(){
        try{
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/payrollsystem?zeroDateTimeBehavior=CONVERT_TO_NULL","root","1234");
            System.out.println("connected");
            String query = "SELECT * FROM `login`";
            s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
          //  while(rs.next()){
             //   String log = rs.getString("username");
             //   String pass = rs.getString("password");
             //   System.out.println(log + " " + pass);
           // }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Database could not be connected");
        }
    }

    public static void main(String args[]) {
        new Conn(); // Create an instance of Conn to invoke the constructor
    }
}
