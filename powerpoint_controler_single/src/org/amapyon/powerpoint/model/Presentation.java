package org.amapyon.powerpoint.model;

import static org.amapyon.util.OleUtil.getPropertyByName;
import static org.amapyon.util.OleUtil.invokeByName;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

public class Presentation {

	OleAutomation presentation = null;

	public Presentation(OleAutomation presentation) {
		this.presentation = presentation;
	}

	public String getName() {
		return getPropertyByName(presentation, "Name").getString();
	}

	public SlideShowView run() {
		System.out.println(getName());
		OleAutomation slideShowSettings = getPropertyByName(presentation, "SlideShowSettings").getAutomation();
		OleAutomation slideShow = invokeByName(slideShowSettings, "Run").getAutomation();
		OleAutomation view = getPropertyByName(slideShow, "View").getAutomation();

		return new SlideShowView(view);
	}

	public void close() {
		invokeByName(presentation, "Close");
	}

	public Slides getSlides() {
		Variant v = getPropertyByName(presentation, "Slides");
		return new Slides(v.getAutomation());
	}

}
