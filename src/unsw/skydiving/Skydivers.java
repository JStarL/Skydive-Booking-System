package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

class Skydivers implements Comparable<Skydivers> {
    private String name;
    private String license;

    // NOTE: All bookings are *mutually exclusive*
    private ArrayList<TimeInterval> bookings;
    private int numJumps;

    Skydivers() {
        numJumps = 0;
        bookings = new ArrayList<TimeInterval>();
    }

    Skydivers(String name, String license) {
        this();
        this.name = name;
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getLicense() {
        return license;
    }

    public void makeBooking(TimeInterval interval) {
        bookings.add(interval.clone());
        Collections.sort(bookings);
    }

    public void cancelBooking(TimeInterval interval) {
        // remove booking which contains TimeInterval
        for (TimeInterval booking : bookings) {
            if (booking.contains(interval)) {
                bookings.remove(booking);
                break;
            }
        }
    }

    public boolean isSkydiverFree(TimeInterval interval) {
        for (TimeInterval booking : bookings) {
            if (booking.isOverlapping(interval)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Skydivers skydiver) {
        return this.numJumps - skydiver.numJumps;
    }

    @Override
    public String toString() {
        return "name: " + name + ", license: " + license;
    }
    
    public void adjustTimeInterval(TimeInterval t) {
        // Do nothing (Hires gear; doesn't repack)
        // A student will be instantiated as a skydiver
    }

}
