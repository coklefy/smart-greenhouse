
package com.greenhouse.greenhouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import herddb.jdbc.HerdDBEmbeddedDataSource;


public class saveconf {
    
    HerdDBEmbeddedDataSource datasource;
     private static Logger log = Logger.getLogger(saveconf.class.getName());
     public saveconf(HerdDBEmbeddedDataSource datasource){
         this.datasource=datasource;
     }
    
    public void saveconfiguration(String conf,String port){
        try ( Connection  conn = datasource.getConnection();){
            String query = "INSERT INTO config (conf,port) values (?,?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query);) {         
                preparedStmt.setString(1,conf);
                preparedStmt.setString(2,port);
                preparedStmt.execute();;
            } catch (SQLException sq) {
                 log.error(sq);
            }

        } catch (SQLException ex) {
            log.error(ex);
        }
       
    }
    
    
    public void createtableconf() {
            try(Connection conn = datasource.getConnection();) {    
                String sql = "CREATE TABLE config (id INTEGER AUTO_INCREMENT,conf STRING,port STRING,PRIMARY KEY (id))";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);

            }catch (SQLException sq){
                log.error(sq);             
            }
        }
    
    public void droptable(String table){
            try(Connection conn = datasource.getConnection();) {    
                String sql = "DROP TABLE "+ table;
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);

            }catch (SQLException sq){
                log.error(sq);             
            }
    }
}

