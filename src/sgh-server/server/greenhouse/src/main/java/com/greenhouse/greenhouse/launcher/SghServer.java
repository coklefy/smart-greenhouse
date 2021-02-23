
package com.greenhouse.greenhouse.launcher;
import com.greenhouse.greenhouse.DataService;
import com.greenhouse.greenhouse.ESP;
import com.greenhouse.greenhouse.GreenHouse;
import com.greenhouse.greenhouse.GreenHouseController;
import com.greenhouse.greenhouse.pump.ObservablePump;
import com.greenhouse.greenhouse.pidlocker.PidFileLocker;
import com.greenhouse.greenhouse.frontend.adminInterface;
import io.vertx.core.Vertx;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;



public class SghServer {

    /**
     * @param args the command line arguments
     */
        private final PidFileLocker pidFileLocker;
        private File basePath;
        public SghServer() {
            basePath= new File(System.getProperty("user.dir","."));
            this.pidFileLocker = new PidFileLocker(basePath.toPath().toAbsolutePath());


        }

        private static SghServer runistance;

    public static void main(String[] args) {
        System.out.println("Greenhouse server");
        Logger log = Logger.getLogger(SghServer.class.getName());
        int dataservicePort = 0;
        String msgServicePort = null;
        final GreenHouse gh;
        final GreenHouseController ghc;
        final ESP esp = new ESP();
        ObservablePump pump = new ObservablePump();
        MsgService msgService = null;
        Properties prop = new Properties();
        InputStream input;

        try {
            try {
                String filename= "conf/server.properties";
                input = SghServer.class.getClassLoader().getResourceAsStream(filename);
                if(input==null){
                    log.error("server.properties not found;");
                    return;
                }
                prop.load(input);
                String servicePort=prop.getProperty("Dataservice.port");
                if(servicePort==null || Integer.parseInt(servicePort)==8081){
                    log.info("Use defaul port");
                    dataservicePort=8081;
                    log.info("Dataservice port= " + dataservicePort);
                }else{
                    dataservicePort = Integer.parseInt(servicePort);
                    log.info("Dataservice.port= " +dataservicePort);
                }
                String servmsgport= prop.getProperty("MsgService.port");
                if(servmsgport == null){
                    log.error("Error, MsgService port not configured \n" +
                            "Please uncomment MsgService.port\n"
                            + "and set variable with the name of serial port"
                            + "Example : --> /dev/ttyACM0"  );
                    System.exit(0);

                }else{
                    msgServicePort =servmsgport;
                }

            } catch (Exception ex) {
                log.error(ex);
            }
            try {
                msgService = new MsgService(msgServicePort,9600);
            } catch (Exception ex) {
                log.error(ex);
            }
            Vertx vertx = Vertx.vertx();
            DataService service = new DataService(dataservicePort,esp);
            gh = new GreenHouse(msgService,pump,esp);
            ghc = new GreenHouseController(msgService,pump,esp);
            msgService.init();

            gh.start();
            ghc.start();
            vertx.deployVerticle(service);
            runistance = new SghServer();
            runistance.start();
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public void start(){
      /* Start admin Front End */
            try {
                /* Allocate and ID to a process, and with this ID can have more info for this process
                ex: if server is ON or OFF */
                pidFileLocker.lock();
                adminInterface fe = new adminInterface(basePath);
                fe.startAdminInterface();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SghServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(SghServer.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
