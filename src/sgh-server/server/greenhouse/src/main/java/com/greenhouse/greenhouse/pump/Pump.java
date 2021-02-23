
package com.greenhouse.greenhouse.pump;


import java.io.IOException;

public interface Pump {
	boolean isOpen();
	void setOpen(int U) throws IOException;
	void setClose() throws IOException;
}

