package servlet;



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
import herddb.jdbc.HerdDBDataSource;
import org.json.*;

//read umidity
public class ReadInHerdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        HerdDBDataSource datasource = new HerdDBDataSource();
        try (Connection connection = datasource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM greenhouse")) {
                JSONArray jArray = new JSONArray();
                int i =0;
            while(rs.next()){
                int id= rs.getInt("id");
                String data= rs.getString("data");
                String nome= rs.getString("name");
                int valore= rs.getInt("valore");
                JSONObject obj = new JSONObject();
                obj.put("data", data);
                obj.put("name",nome);
                obj.put("valore", valore);
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


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}



