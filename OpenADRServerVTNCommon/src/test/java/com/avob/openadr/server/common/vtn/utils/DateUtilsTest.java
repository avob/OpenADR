package com.avob.openadr.server.common.vtn.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.avob.openadr.server.common.vtn.exception.UnparsableDateFormatException;

public class DateUtilsTest {

    @Test
    public void test() throws UnparsableDateFormatException {
        assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("2017-01-01"));
        assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("2017-01-01 00:00"));
        assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("01/01/2017"));
        assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("01/01/2017 00:00"));
        assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("1483228800000"));

        boolean exception = false;
        try {
            assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp("mouaiccool"));
        } catch (UnparsableDateFormatException e) {
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp(""));
        } catch (UnparsableDateFormatException e) {
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            assertEquals(Long.valueOf(1483228800000L), DateUtils.parseDateAsTimestamp(null));
        } catch (UnparsableDateFormatException e) {
            exception = true;
        }
        assertTrue(exception);
    }
}
