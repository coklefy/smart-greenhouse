
package com.greenhouse.greenhouse.frontend;

import com.greenhouse.greenhouse.configuration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class adminInterface implements adminInterfaceInterface{
    private int adminServerPort = 0;
    private final String adminServerHost = "localhost";
    private Server adminserver;
    static Logger log = Logger.getLogger(adminInterface.class.getName());
    private File basePath; 
    Properties prop = new Properties();
    InputStream input;
    configuration conf = new configuration();
    public adminInterface(File basePath) throws IOException{
        this.basePath= basePath;
        
    }
    
    @Override    
    public void startAdminInterface() throws Exception{
        readConf(basePath);
        adminserver = new Server(new InetSocketAddress(adminServerHost, adminServerPort));
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        adminserver.setHandler(contexts);
        
        File webUI = new File(basePath, "web/ui");
        if (webUI.isDirectory()) {
            WebAppContext webApp = new WebAppContext(webUI.getAbsolutePath(), "/ui");  
            webApp.setDescriptor("web/ui/WEB-INF/web.xml");
            webApp.setWelcomeFiles(new String[]{"index.html"});
            log.info("web app trovato+  " + webApp );
            adminserver.setHandler(webApp);
        } else {
            log.error("Cannot find " + webUI.getAbsolutePath() + " directory. Web UI will not be deployed");
        }
        adminserver.start();
        String uiUrl = "http://" + adminServerHost + ":" + adminServerPort + "/ui";
        log.info("Base Admin UI url: " + uiUrl);
        
    }
    
    
    public void readConf(File basePath) throws IOException{
                String filename= "conf/server.properties";
                input = adminInterface.class.getClassLoader().getResourceAsStream(filename);
                if(input==null){
                    log.error("server.properties not found;");
                    return;
                }
                prop.load(input);
                String fePort=prop.getProperty("FrontEnd.port");
                if(fePort==null || Integer.parseInt(fePort)==8001){
                    log.info("Use defaul port");
                    adminServerPort=8001;
                    conf.setFrontend(fePort);
                    log.info("FrontEnd port= " + adminServerPort);
                }else{
                    adminServerPort = Integer.parseInt(fePort);
                    conf.setFrontend(fePort);
                    log.info("FrontEnd.port= " +adminServerPort);
                }
        
    }
    
}

