package com.avob.openadr.model.oadr20b;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Oadr20bFactoryTest {

	@Test
	public void addXMLDurationToTimestampTest() {
		Long duration = null;
		duration = Oadr20bFactory.addXMLDurationToTimestamp(0L, "0");
		assertNull(duration);

		duration = Oadr20bFactory.addXMLDurationToTimestamp(0L, "PT1S");
		assertNotNull(duration);
		assertEquals(Long.valueOf(1000), duration);

		boolean ex = false;
		try {
			duration = Oadr20bFactory.addXMLDurationToTimestamp(0L, "mouaiccool");
		} catch (Exception e) {
			ex = true;
		}
		assertTrue(ex);
	}

	@Test
	public void xmlDurationToMillisecondTest() {
		Long duration = null;
		duration = Oadr20bFactory.xmlDurationToMillisecond("PT1S");
		assertNotNull(duration);
		assertEquals(Long.valueOf(1000), duration);

		boolean ex = false;
		try {
			duration = Oadr20bFactory.xmlDurationToMillisecond("mouaiccool");
		} catch (Exception e) {
			ex = true;
		}
		assertTrue(ex);
	}

	@Test
	public void millisecondToXmlDurationTest() {
		String duration = null;
		duration = Oadr20bFactory.millisecondToXmlDuration(0L);
		assertNotNull(duration);
		assertEquals("PT0S", duration);

		duration = Oadr20bFactory.millisecondToXmlDuration(1000L);
		assertNotNull(duration);
		assertEquals("PT1S", duration);

		Long dur = 25 * 60 * 60 * 1000L + 60 * 1000L + 1000L;
		duration = Oadr20bFactory.millisecondToXmlDuration(dur);
		assertNotNull(duration);
		assertEquals("P1DT1H1M1S", duration);

		boolean ex = false;
		try {
			duration = Oadr20bFactory.millisecondToXmlDuration(null);
		} catch (Exception e) {
			ex = true;
		}
		assertTrue(ex);

	}

	@Test
	public void multiplyXmlDurationTest() {
		String duration = null;
		duration = Oadr20bFactory.multiplyXmlDuration("PT0S", 2);
		assertNotNull(duration);
		assertEquals("PT0S", duration);

		duration = Oadr20bFactory.multiplyXmlDuration("PT1S", 2);
		assertNotNull(duration);
		assertEquals("PT2S", duration);
	}

}
