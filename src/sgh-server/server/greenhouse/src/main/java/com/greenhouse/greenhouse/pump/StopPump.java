
package com.greenhouse.greenhouse.pump;

import com.greenhouse.greenhouse.Event;

public class StopPump implements Event {
	private Pump source;
	
	public StopPump(Pump source) {
		this.source = source;
	}
	
	public Pump getSourcePump() {
		return source;
	}
}

