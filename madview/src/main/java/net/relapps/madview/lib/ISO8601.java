/*
 * Copyright(c) 2022 RELapps.net
 * https://relapps.net
 *
 * This source code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * https://github.com/rmtron/madview/blob/main/LICENSE
 */
package net.relapps.madview.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility class for handling of ISO 8601 date strings of the following format:
 * "2014-03-06T13:00:00+01:00". It also supports parsing the "Z" UTC timezone.
 */
public class ISO8601 {

    private ISO8601() {
    }

    /**
     * Transform Calendar to ISO 8601 string.
     *
     * @param calendar The value to transform.
     * @return The date as a ISO 8601 text string.
     */
    public static String fromCalendar(Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formatted = df.format(date);
        return formatted;
    }

    /**
     * Transform Timestamp to ISO 8601 string.
     *
     * @param timestamp The value to transform.
     * @return The date as a ISO 8601 text string.
     */
    public static String fromTimestamp(Timestamp timestamp) {
        return fromCalendar(timestamp.getCalendar());
    }

    /**
     * Transform Timestamp to ISO 8601 date string.
     *
     * @param timestamp The value to transform.
     * @return The date as a ISO 8601 text string.
     */
    public static String fromTimestampToDate(Timestamp timestamp) {
        String dateString = String.format("%04d-%02d-%02d",
                timestamp.getYear(), timestamp.getMonth(), timestamp.getDay());
        return dateString;
    }

    /**
     * Get current date-time in ISO8601 Format
     *
     * @return The current date and time in ISO8601.
     */
    public static String now() {
        Calendar timestamp = Calendar.getInstance();
        String result = ISO8601.fromCalendar(timestamp);
        return result;
    }

    /**
     * Transform an ISO 8601 date string to Calendar.
     *
     * @param iso8601string The date string.
     * @return The calendar object, or null if null string as input.
     * @throws ParseException Thrown on parsing error.
     */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        if (iso8601string == null) {
            return null;
        }
        // 2017-09-30T22:00:00.000Z
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);
        if (iso8601string.length() == 10) {
            // Format yyyy-mm-dd
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = df.parse(iso8601string);
            calendar.setTime(date);
        } else {
            String isoString;
            if (iso8601string.contains(".000Z")) {
                isoString = iso8601string.replace(".000Z", "+00:00");
            } else {
                isoString = iso8601string.replace("Z", "+00:00");
            }
            try {
                // Check if ':' is present in the time zone definition
                int pos = isoString.indexOf(':', 19);
                if (pos > 0) {
                    isoString = isoString.substring(0, pos)
                            + isoString.substring(pos + 1);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Invalid length", 0);
            }
            // 2017-09-30T22:00:00.000+0000
            // 2017-10-16T22:45:49+0200 OK
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date date = df.parse(isoString);
            calendar.setTime(date);
        }
        return calendar;
    }

    /**
     * Transform an ISO 8601 date string to a timestamp.
     *
     * @param iso8601string The date string.
     * @return The timestamp object or null if null string as input.
     * @throws ParseException Thrown on parsing error.
     */
    public static Timestamp toTimestamp(final String iso8601string)
            throws ParseException {
        Calendar cal = toCalendar(iso8601string);
        if (cal != null) {
            return new Timestamp(cal);
        } else {
            return null;
        }
    }
}
