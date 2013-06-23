package org.amapyon.powerpoint.model;

import static org.amapyon.util.OleUtil.getPropertyByName;
import static org.amapyon.util.OleUtil.invokeByName;

import org.eclipse.swt.SWT;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Shell;

public class Application {
	private static Application instance = null;
	private OleAutomation application = null;

	private Application() {}

	private Application(OleAutomation application) {
		this.application = application;
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

	public Presentations getPresentations() {
		OleAutomation presentations = getPropertyByName(application, "Presentations").getAutomation();
		return new Presentations(presentations);
	}

	public Presentation getActivePresentation() {
		Variant v = getPropertyByName(application, "ActivePresentation");
		System.out.println(v);
		if (v == null) {
			return null;
		}
		OleAutomation p = v.getAutomation();
		System.out.println("ActivePresentation:" + p);
		return new Presentation(p);
	}

	public void quit() {
		invokeByName(application, "Quit");
		instance = null;

	}


}
