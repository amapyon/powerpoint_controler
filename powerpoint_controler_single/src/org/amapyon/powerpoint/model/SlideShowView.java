package org.amapyon.powerpoint.model;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class SlideShowView {
	public static final int ppSlideShowBlackScreen = 3;
	public static final int ppSlideShowWhiteScreen = 4;

	private OleAutomation view = null;

	SlideShowView(OleAutomation view) {
		this.view = view;
	}

	public void previous() {
		invokeByName(view, "Previous");
	}

	public void next() {
		invokeByName(view, "Next");

	}

	public void gotoSlide(int page) {
		invokeByName(view, "GotoSlide", page);
	}

	public void exit() {
		invokeByName(view, "Exit");
	}

	public int getCurrentShowPosition() {
		Variant v = getPropertyByName(view, "CurrentShowPosition");
		return v.getInt();
	}

	public int getState() {
		Variant v = getPropertyByName(view, "State");
		return v.getInt();
	}

	public void blackScreen() {
		setPropertyByName(view, "State", ppSlideShowBlackScreen);
	}

	public void whiteScreen() {
		setPropertyByName(view, "State", ppSlideShowWhiteScreen);
	}

	public void first() {
		invokeByName(view, "First");
	}

	public void last() {
		invokeByName(view, "Last");
	}

}
