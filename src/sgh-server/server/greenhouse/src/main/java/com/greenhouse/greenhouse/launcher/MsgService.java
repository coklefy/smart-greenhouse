
package com.greenhouse.greenhouse.launcher;

import com.greenhouse.greenhouse.CommChannel;
import com.greenhouse.greenhouse.ExtendedSerialCommChannel;
import com.greenhouse.greenhouse.MsgEvent;
import com.greenhouse.greenhouse.Observable;
import org.apache.log4j.Logger;

public class MsgService extends Observable {
	private CommChannel channel;
	private String port;
	private int rate;
        static Logger log = Logger.getLogger(MsgService.class.getName());
	public MsgService(String port, int rate){
		this.port = port;
		this.rate = rate;
	}
	
	void init(){
		try {
			channel = new ExtendedSerialCommChannel(port, rate);	
			log.info("Waiting Arduino for rebooting...");		
			Thread.sleep(4000);
			log.info("Ready.");		
		} catch (Exception e) {
                    log.error(e);
		}
		
		new Thread(() -> {
			while (true) {
				try {
					if(channel.isMsgAvailable()) {
						String msg = channel.receiveMsg();
						System.out.println("[Received] "+msg);
						this.notifyEvent(new MsgEvent(msg));
					}
				} catch (Exception ex) {
                                        log.error(ex);
				}
			}
		}).start();
	}
	
	public void sendMsg(String msg) {
		channel.sendMsg(msg);
                log.info("Message service sent " +msg);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
                        log.error(e.toString());
		}
	}
	
}

