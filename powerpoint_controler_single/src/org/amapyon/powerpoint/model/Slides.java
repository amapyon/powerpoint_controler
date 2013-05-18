package org.amapyon.powerpoint.model;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

import static org.amapyon.util.OleUtil.*;

public class Slides {

	private OleAutomation slides = null;

	public Slides(OleAutomation slides) {
		this.slides = slides;
	}

	public int getCount() {
		Variant v = getPropertyByName(slides, "Count");
		return v.getInt();
	}

	public Slide add(int index, int layout) {
		Variant[] vArgs = new Variant[2];
		vArgs[0] = new Variant(index);
		vArgs[1] = new Variant(layout);
		Variant v = invokeByName(slides, "Add", vArgs);
		if (v == null) {
			return null;
		}
		return new Slide(v.getAutomation());
	}

}
