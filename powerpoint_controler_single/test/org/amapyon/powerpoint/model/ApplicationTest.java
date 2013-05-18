package org.amapyon.powerpoint.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

	Application app;

	@Before
	public void setUp() {
		app = Application.getInstance();

	}

	@After
	public void tearDown() {
		app.quit();
	}


	@Test
	public void testGetInstance() {
		System.out.println(app);
	}

	@Test
	public void testGetActivePresentation() {
		Presentation p = app.getActivePresentation();
		assertThat(p, is(nullValue()));
	}

	@Test
	public void testGetPresentationsZero() {
		Presentations ps = app.getPresentations();
		assertThat(ps.getCount(), is(0));
	}



}
