
package com.greenhouse.greenhouse;
import herddb.jdbc.HerdDBEmbeddedDataSource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class DataService extends AbstractVerticle{
    	private int port;
	private ESP esp;
        HerdDBEmbeddedDataSource dataSource;
        SimpleDateFormat data;
        static Logger log = Logger.getLogger(DataService.class.getName());
        configuration conf = new configuration();
	public DataService(int port,final ESP esp) throws IOException {		
            this.dataSource = new HerdDBEmbeddedDataSource();
            log.info(dataSource);         
            this.port = port;
            this.esp = esp;

	}
      
     

    @Override
    public  void start(){  
                try {
                    saveconf config = new saveconf(dataSource);
                    config.droptable("config");
                    config.droptable("greenhouse");
                    config.droptable("pump");
                    config.droptable("mode");
                    createtable();
                    createtablepump();
                    createtablemode();
                    config.createtableconf();
                    config.saveconfiguration("Dataservice.port", conf.getDataservice());
                    config.saveconfiguration("Frontend.port", conf.getFrontend());
                    config.saveconfiguration("Messageservice.port", conf.getMessageService());

                 log.info("I'm initializing the server");
                    Router router = Router.router(vertx);
                    router.route().handler(BodyHandler.create());
                    router.post("/api/data").handler(this::handleAddNewData);
                    router.get("/api/data").handler(this::handleGetData);
                    vertx
                            .createHttpServer()
                            .requestHandler(router::accept)
                            .listen(port);
                   
                    log.info("Service ready");
                    
                } catch (SecurityException ex) {
                    log.error(ex.toString());
                }
    }
    
    	private void handleAddNewData(RoutingContext routingContext) {
                HttpServerResponse response = routingContext.response();
		JsonObject res = routingContext.getBodyAsJson();
		if (res == null) {
			sendError(400, response);
		} else {
			if(res.getInteger("umidita") != null) {
                            log.info("umidità= " + res.getInteger("umidita"));
                            save(res.getInteger("umidita").toString());
                            esp.sendUmMsg(res.getInteger("umidita"));
                            esp.checkMin(res.getInteger("umidita"));
                             response.setStatusCode(200).end();
			} else {
				response.setStatusCode(400).end();
                        }
                }
        }
        private void handleGetData(RoutingContext routingContext) {
		routingContext.response()
			.setStatusCode(400).end();
	}
        
        private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}
    public void save(String msg){
            log.info("insert umidity= "+msg + " in database");

        try ( Connection  conn = dataSource.getConnection();){            
            data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            java.util.Date date = new java.util.Date();
            String query = "INSERT INTO greenhouse (data,name,valore)values (?, ?, ?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query);) {         
                preparedStmt.setString(1,data.format(date));
                preparedStmt.setString (2, "umidita");
                preparedStmt.setString(3,msg);
                preparedStmt.execute();
                log.info("value umidity= "+msg + " inserted in database");

            } catch (SQLException sq) {
                 log.error(sq);

            }

        } catch (SQLException ex) {
            log.error(ex);
        }

    }

    
       public void close() {
            if (dataSource != null) {
                dataSource.close();
            }
       }
    
    private void createtable() {
        try(Connection conn = dataSource.getConnection();) {    
              log.info("creating greenhouse table");
            String sql = "CREATE TABLE greenhouse (id INTEGER AUTO_INCREMENT,data STRING ,name STRING ,valore STRING,PRIMARY KEY (id))";
            log.info("Let's try to creating table");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            log.info( "table created....");
            conn.close();
        }catch (SQLException sq){
            log.error(sq);             
        }

    }
    
        private void createtablepump() {
        try(Connection conn = dataSource.getConnection();) {    
            log.info("creating pump table");
            String sql = "CREATE TABLE pump (id INTEGER AUTO_INCREMENT,data STRING,info STRING,PRIMARY KEY (id))";
            log.info("Let's try to creating table");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            log.info( "table created....");
            conn.close();
        }catch (SQLException sq){
            log.error(sq);             
        }
    }
        
    private void createtablemode() {
        try (Connection conn = dataSource.getConnection();) {
            log.info("creating mode table");
            String sql = "CREATE TABLE mode (id INTEGER ,info STRING,PRIMARY KEY (id))";
            log.info("Let's try to creating table");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            log.info("table created....");
            conn.close();
        } catch (SQLException sq) {
            log.error(sq);
        }
    }


}














