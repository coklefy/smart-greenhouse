
package com.greenhouse.greenhouse;
import com.greenhouse.greenhouse.pump.DonePump;
import com.greenhouse.greenhouse.pump.AlarmPump;
import org.apache.log4j.Logger;

public class ESP extends Observable{
	private enum State{REGULAR,IRRIGATION,MANUAL};
	private State state;
	private final int Umin = 30;
	private final int delta = 5;
	static Logger log = Logger.getLogger(ESP.class.getName());
	public ESP() {
		state = State.REGULAR;
	}

	public void checkMin(int U) {
		log.info("Checking value");
		switch(state) {
		case REGULAR:
			if(U < Umin) {
				this.notifyEvent(new AlarmPump(U));
				state = State.IRRIGATION;
				log.info("Value under minimun value");
			}
			break;
		case IRRIGATION:
			if(U > (Umin+delta)) {
				this.notifyEvent(new DonePump());
				state = State.REGULAR;
				log.info("value ok");
			}
			break;
		case MANUAL:
        log.info("manual mode, break");
			break;
		}
	}

	public void setRegular() {
		this.state = State.REGULAR;
	}

	public void setManual() {
		this.state = State.MANUAL;
	}

	public void sendUmMsg(int um) {
		this.notifyEvent(new Logumid(um));
	}

}
