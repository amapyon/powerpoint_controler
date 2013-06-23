package org.amapyon.powerpoint;

import org.amapyon.powerpoint.model.Application;
import org.amapyon.powerpoint.model.Presentation;

public class Main {
	public static void main(String[] args) {
//		Application application = Application.getInstance();
//
//		for (Presentation presentation : application.getPresentations()) {
//			System.out.println(presentation.getName());
//		}

//		Presentation activePresentation = application.getActivePresentation();
//		System.out.println(activePresentation.getName());

//		for(Presentation presentation : application.getPresentations()) {
//			System.out.println(presentation.getName());
//		}

//		SlideShow slideShow = application.getPresentation(0).run();

//		slideShow.next();


		new Server(8000).start();
	}
}
