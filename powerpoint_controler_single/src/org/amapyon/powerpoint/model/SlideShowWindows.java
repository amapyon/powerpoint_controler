package org.amapyon.powerpoint.model;

import static org.amapyon.util.OleUtil.invokeByName;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

public class SlideShowWindows {

	private OleAutomation slideShowWindows = null;

	public SlideShowWindows(OleAutomation slideShowWindows) {
		this.slideShowWindows = slideShowWindows;
	}

	public SlideShowWindow getItem(int index) {
		Variant v = invokeByName(slideShowWindows, "Item", index);
		return new SlideShowWindow(v.getAutomation());
	}


}
