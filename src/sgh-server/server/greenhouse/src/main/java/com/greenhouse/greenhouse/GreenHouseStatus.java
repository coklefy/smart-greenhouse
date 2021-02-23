
package com.greenhouse.greenhouse;

import herddb.jdbc.HerdDBEmbeddedDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.apache.log4j.Logger;


public class GreenHouseStatus {
    HerdDBEmbeddedDataSource datasource;
      static Logger log = Logger.getLogger(GreenHouseStatus.class.getName());
    public GreenHouseStatus(){
        this.datasource= new HerdDBEmbeddedDataSource();
    }
    
    private String mode="";
    public void setmode(String mode){
        this.mode=mode;
        try {
            savemode(mode);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(GreenHouseStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getmode(){
        return this.mode;
    }
    
    public void savemode(String msg) throws SQLException{
        try (Connection con = datasource.getConnection();
                            Statement statement = con.createStatement();
                            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM mode")) {
            
            if(rs.getInt("count(*)") > 0){
                    String query = "UPDATE mode SET info=? where id= ?";
                      try (PreparedStatement preparedStmt = con.prepareStatement(query);) {  
                    preparedStmt.setString(1,msg);
                    preparedStmt.setInt(1,1);                
                    preparedStmt.execute();
                } catch (SQLException sq) {
                     log.error(sq);
                }
            }
            else{
                    String query = "INSERT INTO mode (id,info) values (?.?)";
                    try (PreparedStatement preparedStmt = con.prepareStatement(query);) {      
                    preparedStmt.setInt(1,1);
                    preparedStmt.setString(2,msg);
                    preparedStmt.execute();
                } catch (SQLException sq) {
                       log.error(sq);
                }
            }
        }
    }

        
     

}




























































