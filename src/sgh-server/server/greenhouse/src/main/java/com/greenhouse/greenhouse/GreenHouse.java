
package com.greenhouse.greenhouse;
import com.greenhouse.greenhouse.pump.ObservablePump;
import com.greenhouse.greenhouse.pump.OvertimePump;
import com.greenhouse.greenhouse.pump.StartPump;
import com.greenhouse.greenhouse.pump.StopPump;
import com.greenhouse.greenhouse.launcher.MsgService;
import herddb.jdbc.HerdDBEmbeddedDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


public class GreenHouse  extends BasicEventLoopController{
    private MsgService monitor;
    private  ObservablePump pump;
    private ESP esp;
  //  Log log = new Log();

    static Logger log = Logger.getLogger(GreenHouse.class.getName());
    private final HerdDBEmbeddedDataSource dataSource;
    private SimpleDateFormat data;
    GreenHouseStatus status;
    public GreenHouse(final MsgService monitor, final ObservablePump pump, final ESP esp) throws IOException{
        log.info("Start greenhouse server...");
        dataSource = new HerdDBEmbeddedDataSource();
        status = new GreenHouseStatus();
        this.pump=pump;
        this.monitor= monitor;
        this.esp=esp;
        monitor.addObserver(this);
        pump.addObserver(this);
        esp.addObserver(this);   
    }
    
     
    @Override
        protected void processEvent(Event ev) {
            try{
                if(ev instanceof MsgEvent){
                    if (((MsgEvent) ev).getMsg().equals("Start")){
                        log.info("pump is open");
                        save("pump is open");
                    }else if(((MsgEvent) ev).getMsg().equals("Stop")){
                         log.info("pump is close");
                         save("pump is close");
                    }else if(((MsgEvent) ev).getMsg().equals("StopT")){ 
                         log.info("pump is open for overtime");
                         save("pump is open for overtime");
                        monitor.notifyEvent(new OvertimePump());
                    }else if(((MsgEvent) ev).getMsg().equals("ManIn")) {
                        log.info("Manual mode");
                        status.setmode("Manual mode");
                        monitor.notifyEvent(new ManualMode());
                    }else if(((MsgEvent) ev).getMsg().equals("ManOut")) {
                        log.info("Automatic mode");
                        status.setmode("Automatic mode");
                        monitor.notifyEvent(new AutoMode());
                    }        
                }else if (ev instanceof StartPump){
                    monitor.sendMsg(((StartPump) ev).getMessage());
                }else if(ev instanceof StopPump){
                    monitor.sendMsg("Stop");
                }else if(ev instanceof Logumid){
                    monitor.sendMsg("umid:" + ((Logumid) ev).getUm());
                }
            } catch (Exception ex){
                 log.error(ex.getMessage());
            }
            
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


















