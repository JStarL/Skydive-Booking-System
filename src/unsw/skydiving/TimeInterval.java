package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeInterval implements Comparable<TimeInterval> {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;

    public TimeInterval(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public TimeInterval(LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate();
        this.start = dateTime.toLocalTime();
        this.end = dateTime.toLocalTime();
    }

    public boolean timeLiesIn(LocalTime time) {
        return start.compareTo(time) <= 0 && end.compareTo(time) >= 0;
    }

    public int compareDate(LocalDate date) {
        return this.date.compareTo(date);
    }

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

    public boolean contains(TimeInterval t) {
        if (t == null) {
            return false;
        }

        if (!date.equals(t.date)) {
            return false;
        }

        return this.start.compareTo(t.start) <= 0 && this.end.compareTo(t.end) >= 0;
    }

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

        // Assume Stable Sort

        // compareToValue = end.compareTo(t.end);
        // if (compareToValue != 0) {
        // return compareToValue;
        // }

        return 0;
    }

    public void adjustStartTime(int minutes) {
        if (minutes > 0) {
            start = start.plusMinutes(minutes);
        } else if (minutes < 0) {
            minutes *= -1;
            start = start.minusMinutes(minutes);
        }
    }

    public void adjustEndTime(int minutes) {
        if (minutes > 0) {
            end = end.plusMinutes(minutes);
        } else if (minutes < 0) {
            minutes *= -1;
            end = end.minusMinutes(minutes);
        }
    }

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

    @Override
    public String toString() {
        return "date: " + date.toString() +
            ", start: " + start.toString() +
            ", end: " + end.toString();
    }

}