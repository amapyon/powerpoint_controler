package org.amapyon.powerpoint.model;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class Slide {
	public static final int ppLayoutTitle = 1;
	public static final int ppLayoutText = 2;
	public static final int ppLayoutTitleOnly = 11;
	public static final int ppLayoutBlank = 12;

	private OleAutomation slide = null;

	public Slide(OleAutomation slide) {
		this.slide  = slide;
	}

	public int getSlideIndex() {
		Variant v = getPropertyByName(slide, "SlideIndex");
		return v.getInt();
	}

}
