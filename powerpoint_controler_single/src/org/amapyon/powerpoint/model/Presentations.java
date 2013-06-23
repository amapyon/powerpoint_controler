package org.amapyon.powerpoint.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class Presentations implements Iterable<Presentation> {
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

	@Override
	public Iterator<Presentation> iterator() {
		ArrayList<Presentation> p = new ArrayList<Presentation>();
		for (int i = 1; i <= getCount(); i++) {
			p.add(getItem(i));
		}
		return p.iterator();
	}


}
