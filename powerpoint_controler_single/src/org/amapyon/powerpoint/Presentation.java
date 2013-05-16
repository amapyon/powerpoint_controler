package org.amapyon.powerpoint;

import static org.amapyon.util.OleUtil.getPropertyByName;
import static org.amapyon.util.OleUtil.invokeByName;

import org.eclipse.swt.ole.win32.OleAutomation;

public class Presentation {

	OleAutomation presentation = null;

	public Presentation(OleAutomation presentation) {
		this.presentation = presentation;
	}

	public String getName() {
		return getPropertyByName(presentation, "Name").getString();
	}

	public SlideShow run() {
		System.out.println(getName());
		OleAutomation slideShowSettings = getPropertyByName(presentation, "SlideShowSettings").getAutomation();
		OleAutomation slideShow = invokeByName(slideShowSettings, "Run").getAutomation();
		OleAutomation view = getPropertyByName(slideShow, "View").getAutomation();

		return new SlideShow(view);
	}

}
