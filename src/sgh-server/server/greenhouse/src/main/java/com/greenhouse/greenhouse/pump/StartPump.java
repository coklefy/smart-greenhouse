
package com.greenhouse.greenhouse.pump;

import com.greenhouse.greenhouse.Event;
import herddb.jdbc.HerdDBEmbeddedDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;



public final class StartPump implements Event {
	private Pump source;
	private String message;
        private HerdDBEmbeddedDataSource dataSource;
        private SimpleDateFormat data;
        private static Logger log = Logger.getLogger(StartPump.class.getName());
	public StartPump(Pump source,int umidita) {
             dataSource = new HerdDBEmbeddedDataSource();
		this.source = source;
		this.message = "Start";
		if(umidita < 10) {
			message += "2";
      save("Send message for active pump,minimum flow rate");
		} else if (umidita >= 10 && umidita < 20) {
			message += "1";
      save("Send message for active pump,average range");
		} else {
			message += "0";
                         save("Send message for active pump,maximum flow rate");
		}
	}

	public Pump getSourcePump() {
		return source;
	}

	public String getMessage() {
		return message;
	}


        public void save(String msg){
        try ( Connection  conn = dataSource.getConnection();){

            data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date();
            String query = "INSERT INTO pump (data,info)values (?, ?)";
            try (PreparedStatement preparedStmt = conn.prepareStatement(query);) {
                preparedStmt.setString(1,data.format(date));
                preparedStmt.setString(2,msg);
                preparedStmt.execute();

            } catch (SQLException sq) {
                 log.error(sq);

            }

        } catch (SQLException ex) {
            log.error(ex);
        }

    }



}
