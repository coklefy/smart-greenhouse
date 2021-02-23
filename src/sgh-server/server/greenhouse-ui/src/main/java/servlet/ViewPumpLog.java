
package servlet;

import herddb.jdbc.HerdDBDataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

//get pump status
public class ViewPumpLog extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
                PrintWriter out = response.getWriter();
        HerdDBDataSource datasource = new HerdDBDataSource();
        try (Connection connection = datasource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM pump")) {
                JSONArray jArray = new JSONArray();
                int i =0;
            while(rs.next()){
                String data= rs.getString("data");
                String info= rs.getString("info");
                JSONObject obj = new JSONObject();
                obj.put("data", data);
                obj.put("info",info);
                jArray.put(obj);
                i++;
            }
            datasource.close();
            out.print(jArray);
        } catch (SQLException ex) {
            Logger.getLogger(ReadInHerdServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
           Logger.getLogger(ReadInHerdServlet.class.getName()).log(Level.SEVERE, null, e);     
        }
        
        
    }


}










