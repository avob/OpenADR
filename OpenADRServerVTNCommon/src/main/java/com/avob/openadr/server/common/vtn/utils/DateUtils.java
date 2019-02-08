package com.avob.openadr.server.common.vtn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.avob.openadr.server.common.vtn.exception.UnparsableDateFormatException;
import com.google.common.collect.Lists;

public class DateUtils {

    private static final List<SimpleDateFormat> formats = Lists.newArrayList();

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        formats.add(simpleDateFormat);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH::mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        formats.add(simpleDateFormat);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        formats.add(simpleDateFormat);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        formats.add(simpleDateFormat);
    }

    private DateUtils() {
    }

    public static Long parseDateAsTimestamp(String date) throws UnparsableDateFormatException {
        if (date != null) {
            try {
                return Long.valueOf(date);
            } catch (NumberFormatException e) {
                for (SimpleDateFormat f : formats) {
                    try {
                        Date d = f.parse(date);
                        return d.getTime();
                    } catch (ParseException ex) {
                    }

                }
            }
        }
        throw new UnparsableDateFormatException();

    }
}
