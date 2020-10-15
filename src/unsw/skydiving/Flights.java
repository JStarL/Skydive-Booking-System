package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;

public class Flights implements Comparable<Flights> {

    /**
     * Unique identifier of the flight
     */
    private String id;

    /**
     * Every flight is hosted from a dropzone,
     * whose reference is here
     */
    private Dropzones dropzone;

    /**
     * The flight's maxload. Unfortunately, the APF's
     * regulations are pretty strict
     */
    private int maxLoad;

    /**
     * The flight's current load. Useful for determining
     * whether or not a flight can accomodate a jump's
     * skydivers
     */
    private int currLoad;

    /**
     * The duration of the flight, represented as a TimeInterval
     */
    private TimeInterval interval;

    /**
     * An ArrayList of Jumps hosted by this flight.
     * Jumps are maintained in chronological order,
     * i.e. the order in which they were initially booked
     */
    private ArrayList<Jumps> jumps;

    /**
     * Standard Flight Constructor.
     * Also adds to dropzone vacancies when new Flight created
     * @param id Unique identifier
     * @param dropzone dropzone hosting flight
     * @param interval duration of flight
     * @param maxLoad maximum flight load
     */
    Flights(String id, Dropzones dropzone, TimeInterval interval, int maxLoad) {
        this.id = id;
        this.dropzone = dropzone;
        this.interval = interval;
        this.maxLoad = maxLoad;
        this.currLoad = 0;
        jumps = new ArrayList<Jumps>();

        addDropzoneVacancies(maxLoad);
    }

    /**
     * A Convenience constructor supporting
     * LocalDateTime objects representing
     * flight start and end
     * @param id
     * @param dropzone
     * @param startTime start of the flight as a LocalDateTime
     * @param endTime start of the flight as a LocalDateTime
     * @param maxLoad
     */
    Flights(String id, Dropzones dropzone, LocalDateTime startDateTime, LocalDateTime endDateTime, int maxLoad) {
        this(id, dropzone, new TimeInterval(startDateTime.toLocalDate(), startDateTime.toLocalTime(), endDateTime.toLocalTime()),
                maxLoad);

    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public TimeInterval getTimeInterval() {
        return interval;
    }

    public Dropzones getDropzone() {
        return dropzone;
    }

    /**
     * Increase dropzone vacancies
     * of this flight's dropzone
     * @param x integer
     */
    public void addDropzoneVacancies(int x) {
        dropzone.addVacancies(x);
    }

    /**
     * Decrease dropzone vacancies
     * of this flight's dropzone
     * @param x integer
     */
    public void subtractDropzoneVacancies(int x) {
        dropzone.subtractVacancies(x);
    }

    /**
     * Checks whether this flight has the extra capacity
     * to fit the given number of skydivers
     * @param numSkydivers number of skydivers
     * @return true if there is space, false otherwise
     */
    public boolean hasExtraCapacity(int numSkydivers) {
        return ((maxLoad - currLoad) >= numSkydivers);
    }

    /**
     * Add given number of skydivers to this flight's
     * current load. If this flight doesn't have the space,
     * do nothing
     * @param numSkydivers number of skydivers
     */
    public void addCurrentLoad(int numSkydivers) {
        if (!hasExtraCapacity(numSkydivers)) {
            return;
        }
        currLoad += numSkydivers;
    }

    /**
     * Subtract given number of skydivers from this flight's
     * current load. If this flight doesn't have that many skydivers,
     * flush to zero instead
     * @param numSkydivers number of skydivers
     */
    public void subtractCurrentLoad(int numSkydivers) {
        currLoad -= numSkydivers;
        if (currLoad < 0) {
            currLoad = 0;
        }
    }

    /**
     * Appends jump to this flight's list of jumps
     * @param jump to be added
     */
    public void addJump(Jumps jump) {
        jumps.add(jump);
    }

    /**
     * Inserts jump to this flight's list of jumps
     * at the specified index.
     * Overloaded to allow restoration of jump in
     * this flight's list of jumps.
     * @param jump to be added
     * @param index at which to add
     */
    public void addJump(Jumps jump, int index) {
        jumps.add(index, jump);
    }

    /**
     * Remove jump with id from this flight's jumps list.
     * Returns index of the jump which was removed,
     * possibly for jump restoration
     * @param id id of jump to be removed
     * @return index of jump in flight's jumps list
     */
    public int removeJump(String id) {
        for (Jumps jump : jumps) {
            if (jump.getId().equals(id)) {
                return removeJump(jump);
            }
        }
        return -1;
    }

    /**
     * Remove jump from this flight's jumps list.
     * Returns index of the jump which was removed,
     * possibly for jump restoration
     * @param jump jump to be removed
     * @return index of jump in flight's jumps list
     */
    public int removeJump(Jumps jump) {
        if (jump == null) {return -1;}

        for (int i = 0; i < jumps.size(); i++) {
            if (jump == jumps.get(i)) {
                jumps.remove(jump);
                return i;
            }
        }
        return -1;
    }

    /**
     * A String representation of this flight.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return "flightId: " + id +
            ", dropzone: " + dropzone.toString() +
            ", maxload: " + maxLoad +
            ", timeInterval: " + interval.toString();
    }

    /**
     * To compare flights chronologically,
     * we compare their intervals.
     * Useful for sorting flights,
     * and grouping flights by date.
     */
    @Override
    public int compareTo(Flights o) {
        return this.interval.compareTo(o.interval);
    }

    /**
     * To fulfill a request for a jump, a list of flights
     * on a given date and from a given time onwards
     * is required. This method returns such a list,
     * given a list of all flights
     * @param allFlights all flights in the booking system
     * @param date date on which flights are required
     * @param time look for flights from time onwards 
     * @return possible flights for a given request
     */
    public static ArrayList<Flights> getFlightsOnDateFromTime(ArrayList<Flights> allFlights, LocalDate date,
            LocalTime time) {

        // List of all flights on date from time
        ArrayList<Flights> reqdFlights = new ArrayList<Flights>();

        // Sort flights chronologically
        Collections.sort(allFlights);

        // Create starting point
        TimeInterval t = new TimeInterval(date, time, time);
        for (Flights f : allFlights) {
            // DEBUG
            // System.out.println(f);
            if (f.interval.compareTo(t) < 0) { 
                // f is before starting point
                continue;
            } else if (f.interval.compareDate(date) > 0) {  
                // moved beyond date, stop checking
                break;
            } else {
                // Add flight
                reqdFlights.add(f);
            }
        }

        return reqdFlights;
    }

    /**
     * Generates a jump-run for this flight
     * by composing JSONObjects from constituent jumps
     * and ordering them in the appropriate sort order
     * for exit (according to the specification)
     * @return JSONArray of the final jump-run
     */
    public JSONArray generateFlightRuns() {
        JSONArray output = new JSONArray();

        ArrayList<Jumps> sortedJumps = Jumps.sortJumpsForExit(jumps);
        for (Jumps jump : sortedJumps) {
            output.put(jump.generateJumpRun());
        }

        return output;
    }
    
}