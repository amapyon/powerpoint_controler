package org.amapyon.powerpoint.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SlideTest {
	Application app;
	Presentations ps;
	Presentation p;

	@Before
	public void setUp() {
		app = Application.getInstance();
		ps = app.getPresentations();
		p = ps.add();
	}

	@After
	public void tearDown() {
		p.close();
		app.quit();
	}


	@Test
	public void testGetSlideIndex() {
		Slide s = p.getSlides().add(1, Slide.ppLayoutText);
		assertThat(s.getSlideIndex(), is(1));
		s = p.getSlides().add(1, Slide.ppLayoutText);
		assertThat(s.getSlideIndex(), is(1));
		s = p.getSlides().add(2, Slide.ppLayoutText);
		assertThat(s.getSlideIndex(), is(2));
	}

}
