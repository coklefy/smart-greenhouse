
package com.greenhouse.greenhouse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;


public final class configuration {
    private String dataservicePort;
    private String frontendPort;
    private String messageServicePort;
    private Properties prop = new Properties();
    private InputStream input;
    static Logger log = Logger.getLogger(configuration.class.getName());
    public configuration(){
        try {
            readconf();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setDataservice(String port){
        this.dataservicePort=port;
    }
    
    public void setFrontend(String port){
        this.frontendPort=port;
    }
    public void setMessageService(String port){
        this.messageServicePort=port;
    }
    
    public String getDataservice(){
        return dataservicePort;
    }
    public String getFrontend(){
        return frontendPort;
    }
    
    public String getMessageService(){
        return messageServicePort;
    }
    
    
    public void readconf() throws IOException{
                String filename= "conf/server.properties";
                input = configuration.class.getClassLoader().getResourceAsStream(filename);
                prop.load(input);
                String servicePort=prop.getProperty("Dataservice.port");
                setDataservice(servicePort);
          
                String servmsgport= prop.getProperty("MsgService.port");
                setMessageService(servmsgport);
                String frontendport= prop.getProperty("FrontEnd.port");
                setFrontend(frontendport);

    }
 
}

