
package com.greenhouse.greenhouse;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


public class Log {
    public static Logger logger;
    public static FileHandler fh;
    public Log () throws IOException{
                // check if logs dir exists
        File logDir = new File("./logs/"); 
        if( !(logDir.exists()) ){
            logDir.mkdir();
        }
        File f = new File("logs/service.log");
        if(!f.exists())
        {
            f.createNewFile();
        }
        fh= new FileHandler("logs/service.log", true);
        logger = Logger.getLogger(Log.class.getName());
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        
    }
}

