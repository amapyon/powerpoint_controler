package org.amapyon.powerpoint;

import static org.amapyon.util.OleUtil.getPropertyByName;
import static org.amapyon.util.OleUtil.invokeByName;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.widgets.Shell;

public class Application {
	private static Application instance = null;
	private OleAutomation application = null;
	private ArrayList<Presentation> presentations = new ArrayList<Presentation>();

	private Application() {}

	private Application(OleAutomation application) {
		this.application = application;
		setPresentations();
	}

	public static Application getInstance() {
		if (instance == null) {
			Shell shell = new Shell();
			OleFrame frame = new OleFrame(shell, SWT.NONE);
			OleControlSite site = new OleControlSite(frame, SWT.NONE, "PowerPoint.Application");
			OleAutomation application = new OleAutomation(site);
			instance = new Application(application);
		}
		return instance;
	}

	public ArrayList<Presentation> getPresentations() {
		return presentations;
	}

	public Presentation getPresentation(int index) {
		return presentations.get(index);
	}

	private void setPresentations() {
		OleAutomation presentations = getPropertyByName(application, "Presentations").getAutomation();
		int count = getPropertyByName(presentations, "Count").getInt();
		System.out.println(count);

		for(int i = 1; i <= count; i++) {
			OleAutomation p = invokeByName(presentations, "Item", i).getAutomation();
			Presentation presentation = new Presentation(p);
			this.presentations.add(presentation);
		}
	}

	public Presentation getActivePresentation() {
		OleAutomation p = getPropertyByName(application, "ActivePresentation").getAutomation();
		return new Presentation(p);
	}

}
