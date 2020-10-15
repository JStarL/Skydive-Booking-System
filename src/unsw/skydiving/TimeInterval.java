package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeInterval implements Comparable<TimeInterval> {
    
    /**
     * The LocalDate of the interval
     */
    private LocalDate date;

    /**
     * The starting LocalTime of the interval
     */
    private LocalTime start;
    /**
     * The ending LocalTime of the interval
     */
    private LocalTime end;

    /**
     * Standard Constructor for TimeInterval
     * @param date A LocalDate object specifying the date
     * @param start A LocalTime object specifying the date
     * @param end A LocalTime object specifying the date
     */
    public TimeInterval(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    /**
     * Special constructor, initialising a 'moment in time' as a TimeInterval
     * @param dateTime A LocalDateTime object specifying the moment in time
     */
    public TimeInterval(LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate();
        this.start = dateTime.toLocalTime();
        this.end = dateTime.toLocalTime();
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * Checks whether this interval overlaps with the given TimeInterval
     * @param interval TimeInterval object to be checked against
     * @return true if intervals overlap, false otherwise
     */
    public boolean isOverlapping(TimeInterval interval) {
        if (interval == null) {
            return false;
        }

        if (!date.equals(interval.date)) {
            return false;
        }

        // Overlapping if start1 < end2 && start2 < end1
        return this.start.isBefore(interval.end) && interval.start.isBefore(this.end);
    }

    /**
     * Checks whether this interval contians the given interval.
     * this contains t if t lies within the boundaries of this
     * @param t Target TimeInterval to be checked with
     * @return true if this contains t, false otherwise
     */
    public boolean contains(TimeInterval t) {
        if (t == null) {
            return false;
        }

        if (!date.equals(t.date)) {
            return false;
        }

        return this.start.compareTo(t.start) <= 0 && this.end.compareTo(t.end) >= 0;
    }

    /**
     * Comparison is made with the date and startTime,
     * i.e. if two intervals on the same date start at the
     * same time, regardless of when they end, they are equivalent
     * for sorting purposes
     */
    @Override
    public int compareTo(TimeInterval t) {

        int compareToValue = date.compareTo(t.date);
        if (compareToValue != 0) {
            return compareToValue;
        }

        compareToValue = start.compareTo(t.start);
        if (compareToValue != 0) {
            return compareToValue;
        }

        // If two flight times have t1.compareTo(t2) = 0
        // Then the flight which was registered first
        // will remain before

        return 0;
    }

    /**
     * Compare this TimeInterval's date with the given date
     * aacording to the compareTo() definition
     * @param date to be compared with
     * @return integer: negative = current is less than,
     * positive = current is more than,
     * zero = current is same as
     */
    public int compareDate(LocalDate date) {
        return this.date.compareTo(date);
    }

    /**
     * Adjusts the start time of this TimeInterval by minutes,
     * minutes can be positive, negative, or zero.
     * @param minutes integer to alter startTime
     */
    public void adjustStartTime(int minutes) {
        if (minutes > 0) {
            start = start.plusMinutes(minutes);
        } else if (minutes < 0) {
            minutes *= -1;
            start = start.minusMinutes(minutes);
        }
    }

    /**
     * Adjusts the end time of this TimeInterval by minutes,
     * minutes can be positive, negative, or zero.
     * @param minutes integer to alter endTime
     */
    public void adjustEndTime(int minutes) {
        if (minutes > 0) {
            end = end.plusMinutes(minutes);
        } else if (minutes < 0) {
            minutes *= -1;
            end = end.minusMinutes(minutes);
        }
    }

    /**
     * Returns a new copy of this TimeInterval
     * @return TimeInterval clone of this
     */
    public TimeInterval clone() {
        return new TimeInterval(date, start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        TimeInterval t = (TimeInterval) obj;

        return date.equals(t.date) && start.equals(t.start) && end.equals(t.end);

    }

    /**
     * A String representation of this TimeInterval.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return "date: " + date.toString() +
            ", start: " + start.toString() +
            ", end: " + end.toString();
    }

}