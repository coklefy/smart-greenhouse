
package com.greenhouse.greenhouse;

import com.greenhouse.greenhouse.pump.ObservablePump;
import com.greenhouse.greenhouse.pump.DonePump;
import com.greenhouse.greenhouse.pump.OvertimePump;
import com.greenhouse.greenhouse.pump.AlarmPump;
import com.greenhouse.greenhouse.launcher.MsgService;
import java.io.IOException;
import org.apache.log4j.Logger;



public class GreenHouseController extends BasicEventLoopController{
	private enum State {AUTO,MANUAL};
	private State state;
	private final ObservablePump pump;
	private final MsgService monitor;
	private final ESP esp;
        static Logger log = Logger.getLogger(GreenHouseController.class.getName());
	public GreenHouseController(MsgService monitor,ObservablePump pump,ESP esp) throws IOException{
           // PropertyConfigurator.configure(GreenHouseController.class.getResourceAsStream("log4j.properties"));
		this.pump = pump;
		this.monitor = monitor;
		this.esp = esp;
		pump.addObserver(this);
		monitor.addObserver(this);
		esp.addObserver(this);

		state = State.AUTO;
	}

	@Override
	protected void processEvent(Event ev) {
		try {
			switch (state){
			    case MANUAL:
			      if (ev instanceof AutoMode && state != State.AUTO){
			        state = State.AUTO;
			        esp.setRegular();

			      }
			      break;
			    case AUTO:
			      if (ev instanceof ManualMode && state != State.MANUAL){
			        state = State.MANUAL;
			        esp.setManual();

              log.info("Manual mode");
				  } else if (ev instanceof AlarmPump){
			          pump.setOpen(((AlarmPump) ev).getU());
                log.info("Pump open");
				  } else if (ev instanceof DonePump){
                pump.setClose();
                log.info("Pump close");
			      } else if (ev instanceof OvertimePump){
			    	    pump.overtimeClose();
			    	    esp.setRegular();
                log.info("Overtime pump close");
			      }
			      break;
			}
		} catch (Exception ex){
                        log.error(ex.toString());
		}
	}
}
