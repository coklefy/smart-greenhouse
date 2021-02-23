
package com.greenhouse.greenhouse.pump;

import com.greenhouse.greenhouse.Event;


public class AlarmPump implements Event {
	private int umidita;
	
	public AlarmPump(int umidita) {
		this.umidita = umidita;
	}
	
	public int getU() {
		return umidita;
	}
}

