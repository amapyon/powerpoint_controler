package org.amapyon.powerpoint.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.amapyon.powerpoint.model.Slides;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SlidesTest {

	Application app;
	Presentations ps;
	Presentation p;

	@Before
	public void setUp() {
		app = Application.getInstance();
		ps = app.getPresentations();
		ps.add();
		p = app.getActivePresentation();
	}

	@After
	public void tearDown() {
		p.close();
		app.quit();
	}



	@Test
	public void testGetInstance() {
	}

	@Test
	public void testAdd() {
		Slides slides = p.getSlides();
		assertThat(slides.getCount(), is(0));
		Slide slide = slides.add(1, Slide.ppLayoutText);
		assertThat(slide, is(notNullValue()));
		assertThat(slides.getCount(), is(1));
	}


}
