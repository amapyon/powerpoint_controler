package org.amapyon.powerpoint.model;

import static org.amapyon.util.OleUtil.invokeByName;

import org.eclipse.swt.ole.win32.OleAutomation;

public class SlideShowWindow {
	private OleAutomation slideShowWindow = null;

	public SlideShowWindow(OleAutomation slideShowWindow) {
		this.slideShowWindow = slideShowWindow;
	}

	public void activate() {
		invokeByName(slideShowWindow, "Activate");
	}

}
