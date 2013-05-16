package org.amapyon.powerpoint;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class SlideShow {
	private OleAutomation view = null;

	SlideShow(OleAutomation view) {
		this.view = view;
	}

	public void prev() {
		invokeByName(view, "Previous");
	}

	public void next() {
		invokeByName(view, "Next");

	}

	public void jump(int page) {
		invokeByName(view, "GotoSlide", page);
	}

	public void quit() {
		invokeByName(view, "Exit");
	}

	public int getCurrentPage() {
		Variant v = getPropertyByName(view, "CurrentShowPosition");
		return v.getInt();
	}

	public int getState() {
		Variant v = getPropertyByName(view, "State");
		return v.getInt();

	}

	public void blackScreen() {
		setPropertyByName(view, "State", 3); //ppSlideShowBlackScreen
	}

	public void whiteScreen() {
		setPropertyByName(view, "State", 4); //ppSlideShowWhiteScreen
	}

	public void first() {
		invokeByName(view, "First");
	}

	public void last() {
		invokeByName(view, "Last");
	}

}
