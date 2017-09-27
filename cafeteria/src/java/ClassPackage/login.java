
package ClassPackage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class login {
    
     public static String loginCheck(UserClassItems object)
     {
        String query="select * from `user` where username=? and password=?";
        try
        {
            Connection con=dbconnection.getConnection();
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,object.getUsername());
            ps.setString(2,object.getPassword());
          
            ResultSet rs=ps.executeQuery();
            if(rs.next())
            {
                return "true";
            }
            else
            {
                return "false";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }        
        return "error";
    }

}
