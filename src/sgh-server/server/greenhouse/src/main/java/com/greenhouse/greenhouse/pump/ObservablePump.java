
package com.greenhouse.greenhouse.pump;

import com.greenhouse.greenhouse.Observable;


public class ObservablePump extends Observable implements Pump {

	private boolean isOpen;
	
	public ObservablePump(){
		this.isOpen = false;
	}
	
	@Override
	public synchronized boolean isOpen() {
		return isOpen;
	}
	
	@Override
	public void setOpen(int U) {
		isOpen = true;
		this.notifyEvent(new StartPump(this,U));
	}
	
	@Override
	public void setClose() {
		isOpen = false;
		this.notifyEvent(new StopPump(this));
	}
	
	public void overtimeClose() {
		isOpen = false;
	}
}

