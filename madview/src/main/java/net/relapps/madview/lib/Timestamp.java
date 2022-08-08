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

import java.util.Calendar;
import java.util.Date;

/**
 * Represents an immutable date and time (timestamp).
 *
 * @author RMT (2008)
 */
/**
 * Represents a time stamp.
 */
public class Timestamp implements Comparable<Timestamp> {

    /**
     * Creates a new instance of TimeStamp initialized to the current time.
     */
    public Timestamp() {
        _cal = Calendar.getInstance();
    }

    /**
     * Creates a new instance of TimeStamp initialized with the given value.
     *
     * @param date The date/time value.
     */
    public Timestamp(Date date) {
        _cal = Calendar.getInstance();
        _cal.setTime(date);
    }

    /**
     * Creates a new instance of TimeStamp initialized with the given value.
     *
     * @param millis The date/time value in milliseconds.
     */
    public Timestamp(long millis) {
        _cal = Calendar.getInstance();
        _cal.setTimeInMillis(millis);
    }

    /**
     * Assign value to the timestamp.
     *
     * @param year The year.
     * @param month The month (1-12).
     * @param date The date (1-31).
     * @param hour The hour( 0-23).
     * @param min The minute (0-59).
     * @param sec The second (0-59).
     */
    public Timestamp(int year, int month, int date, int hour, int min, int sec) {
        _cal = Calendar.getInstance();
        _cal.set(year, month - 1, date, hour, min, sec);
    }

    /**
     * Assign from a calendar value.
     *
     * @param calendar The calendar value.
     */
    Timestamp(Calendar calendar) {
        _cal = calendar;
    }

    /**
     * Returns the current time.
     *
     * @return The current time.
     */
    public static Timestamp getCurrent() {
        return new Timestamp();
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    @Override
    public int compareTo(Timestamp timeStamp) {
        return _cal.compareTo(timeStamp._cal);
    }

    /**
     * Returns the day
     *
     * @return The day (1-31).
     */
    public int getDay() {
        return _cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the hour
     *
     * @return The hour (0-23).
     */
    public int getHour() {
        return _cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Returns the minute.
     *
     * @return The minute( 0-59).
     */
    public int getMinute() {
        return _cal.get(Calendar.MINUTE);
    }

    /**
     * Returns the month.
     *
     * @return The month (1-12).
     */
    public int getMonth() {
        return _cal.get(Calendar.MONTH) + 1;
    }

    /**
     * Returns the second.
     *
     * @return The second( 0-59).
     */
    public int getSecond() {
        return _cal.get(Calendar.SECOND);
    }

    /**
     * Returns the timestamp value in milli seconds.
     *
     * @return The timestamp value in milliseconds.
     */
    public long getTimeInMillis() {
        return _cal.getTimeInMillis();
    }

    /**
     * Returns the time as a string with format "hh:mm:ss".
     *
     * @return The time (hh:mm:ss).
     */
    public String getTimeString() {
        return String.format(
                "%02d:%02d:%02d",
                _cal.get(Calendar.HOUR_OF_DAY),
                _cal.get(Calendar.MINUTE),
                _cal.get(Calendar.SECOND));
    }

    /**
     * Returns the week day.
     *
     * @return The week day (1 - Monday, 2 - Tuesday, ... 7 - Sunday)
     */
    public int getWeekDay() {
        int day = _cal.get(Calendar.DAY_OF_WEEK);
        int weekDay = 0;
        switch (day) {
            case Calendar.MONDAY:
                weekDay = 1;
                break;
            case Calendar.TUESDAY:
                weekDay = 2;
                break;
            case Calendar.WEDNESDAY:
                weekDay = 3;
                break;
            case Calendar.THURSDAY:
                weekDay = 4;
                break;
            case Calendar.FRIDAY:
                weekDay = 5;
                break;
            case Calendar.SATURDAY:
                weekDay = 6;
                break;
            case Calendar.SUNDAY:
                weekDay = 7;
                break;
        }
        return weekDay;
    }

    /**
     * Returns the year.
     *
     * @return The year.
     */
    public int getYear() {
        return _cal.get(Calendar.YEAR);
    }

    /**
     * Convert to date.
     *
     * @return A Date object.
     */
    public Date toDate() {
        return new Date(getTimeInMillis());
    }

    /**
     * Returns a string representing the date (yyyy/mm/dd).
     *
     * @return The date string.
     */
    public String toDateString() {
        return String.format(
                "%04d/%02d/%02d",
                _cal.get(Calendar.YEAR),
                _cal.get(Calendar.MONTH) + 1,
                _cal.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Returns a string representing the date (yyyy/mm/dd).
     *
     * @return The date string.
     */
    public String toISODateString() {
        return String.format(
                "%04d-%02d-%02d",
                _cal.get(Calendar.YEAR),
                _cal.get(Calendar.MONTH) + 1,
                _cal.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Returns a string representation of the timestamp.
     *
     * @return The timestamp as a string (yyyy/mm/dd hh:mm:ss)
     */
    @Override
    public String toString() {
        return String.format(
                "%04d/%02d/%02d %02d:%02d:%02d",
                _cal.get(Calendar.YEAR),
                _cal.get(Calendar.MONTH) + 1,
                _cal.get(Calendar.DAY_OF_MONTH),
                _cal.get(Calendar.HOUR_OF_DAY),
                _cal.get(Calendar.MINUTE),
                _cal.get(Calendar.SECOND));
    }

    /**
     * Returns the timestamp as a calendar value.
     *
     * @return The calendar value.
     */
    @SuppressWarnings("ReturnOfDateField")
    Calendar getCalendar() {
        return _cal;
    }
    private final Calendar _cal;
}
