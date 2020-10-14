package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

class Skydivers implements Comparable<Skydivers> {
    
    /**
     * Name / id of the skydiver
     */
    private String name;

    /**
     * Type of skydiver, one of: "student", "licenced-jumper",
     * "instructor", "tandem-master"
     */
    private String license;

    /**
     * The bookings of this skydiver, as a list of TimeIntervals
     * of each booking. NOTE: bookings are mutually exclusive
     */
    private ArrayList<TimeInterval> bookings;
    
    /**
     * The total number of jumps this skydiver has been booked into
     */
    private int numJumps;

    /**
     * Default constructor for common initialisations
     */
    private Skydivers() {
        numJumps = 0;
        bookings = new ArrayList<TimeInterval>();
    }

    /**
     * Standard constructor for a skydiver
     * @param name id of skydiver
     * @param license type of skydiver
     */
    Skydivers(String name, String license) {
        this();
        this.name = name;
        this.license = license;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }
    
    public String getLicense() {
        return license;
    }

    /**
     * A String representation of this skydiver.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return "name: " + name + ", license: " + license;
    }
    
    /**
     * Print the name and bookings of this skydiver,
     */
    public void printBookings() {
        System.out.println("name: " + name + ", bookings: " + bookings.toString());
    }

    /**
     * Comparing skydivers for sorting by number of Jumps booked
     */
    @Override
    public int compareTo(Skydivers skydiver) {
        return this.numJumps - skydiver.numJumps;
    }

    /**
     * Add a booking to the skydiver's existing bookings.
     * Bookings inserted chronologically. Adds its own copy
     * (i.e. a new) TimeInterval
     * @param interval
     */
    public void makeBooking(TimeInterval interval) {
        if (interval == null) {return;}
        bookings.add(interval.clone());
        Collections.sort(bookings);
        numJumps++;
    }

    /**
     * Cancel a particular booking of this skydiver, such that
     * this booking contains given TimeInterval
     * @param interval booking to be cancelled will contain this interval
     */
    public void cancelBooking(TimeInterval interval) {
        if (interval == null) {return;}
        // remove booking which contains TimeInterval
        for (TimeInterval booking : bookings) {
            if (booking.contains(interval)) {
                bookings.remove(booking);
                break;
            }
        }
        numJumps--;
    }

    /**
     * Checks whether a skydiver is free in the given time interval
     * by checking if it overlaps with an existing booking
     * @param interval Is the skydiver free in this TimeIntervaL?
     * @return true if skydiver is free, false otherwise
     */
    public boolean isSkydiverFree(TimeInterval interval) {
        if (interval == null) {return false;}
        for (TimeInterval booking : bookings) {
            if (interval.isOverlapping(booking)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adjusts given imeInterval based on current object
     * type - i.e. skydiver. Students get instantiated as
     * skydivers, and as such no adjustments take place.
     * NOTE however, subclasses do cause changes
     * @param t
     */    
    public void adjustTimeInterval(TimeInterval t) {
        // Do nothing (Student Hires gear; doesn't repack)
    }

}
