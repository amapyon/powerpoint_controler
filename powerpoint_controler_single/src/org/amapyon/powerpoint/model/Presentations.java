package org.amapyon.powerpoint.model;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class Presentations {
	private OleAutomation presentations = null;

	public Presentations(OleAutomation presentations) {
		this.presentations = presentations;
	}

	public int getCount() {
		Variant v = getPropertyByName(presentations, "Count");
		return v.getInt();
	}

	public Presentation add() {
		Variant v = invokeByName(presentations, "Add");
		return new Presentation(v.getAutomation());
	}

	public Presentation getItem(int index) {
		Variant v = invokeByName(presentations, "Item", index);
		return new Presentation(v.getAutomation());
	}


}
