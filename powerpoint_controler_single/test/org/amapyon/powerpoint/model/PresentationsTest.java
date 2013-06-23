package org.amapyon.powerpoint.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PresentationsTest {
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
		Presentations ps = app.getPresentations();
		assertThat(ps, is(notNullValue()));
	}

	@Test
	public void testAddAndClose() {
		Presentations ps = app.getPresentations();
		ps.add();
		assertThat(ps.getCount(), is(1));
		app.getActivePresentation().close();
		assertThat(ps.getCount(), is(0));
	}

	@Test
	public void testGetItem() {
		Presentations ps = app.getPresentations();
		Presentation p1 = ps.add();
		String name1 = p1.getName();
		Presentation p2 = ps.add();
		String name2 = p2.getName();
		try {
			assertThat(ps.getItem(1).getName(), is(name1));
			assertThat(ps.getItem(2).getName(), is(name2));
		} finally {
			p1.close();
			p2.close();
		}
	}

	@Test
	public void testItretor() {
		Presentations ps = app.getPresentations();
		Presentation p1 = ps.add();
		Presentation p2 = ps.add();

		try {
			Iterator<Presentation> i = ps.iterator();
			assertThat(i.next().getName(), is(p1.getName()));
			assertThat(i.next().getName(), is(p2.getName()));

		} finally {
			p1.close();
			p2.close();
		}
	}
}
