
package ClassPackage;
import java.sql.Connection;
import java.sql.DriverManager;

public class dbconnection
{
    
    static final String URL="jdbc:mysql://192.168.12.99:3306/";
    static final String DATABASE_NAME="canteenecof";
    static final String USERNAME="canteenuser";
    static final String PASSWORD="canteenpp";
    
            public static Connection getConnection()
    {
        Connection con=null;
        
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
            con=DriverManager.getConnection(URL+DATABASE_NAME,USERNAME,PASSWORD);
               
        }catch(Exception e){
            //e.printStackTrace();
                           }
        return con;
    }
}
