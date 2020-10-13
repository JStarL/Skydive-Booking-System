package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;

public class Flights implements Comparable<Flights> {

    private String id;
    private Dropzones dropzone;
    private int maxLoad;
    private int currLoad;

    private TimeInterval interval;

    private ArrayList<Jumps> jumps;

    Flights(String id, Dropzones dropzone, TimeInterval interval, int maxLoad) {
        this.id = id;
        this.dropzone = dropzone;
        this.interval = interval;
        this.maxLoad = maxLoad;
        this.currLoad = 0;
        jumps = new ArrayList<Jumps>();

        addDropzoneVacancies(maxLoad);
    }

    Flights(String id, Dropzones dropzone, LocalDateTime startTime, LocalDateTime endTime, int maxLoad) {
        this(id, dropzone, new TimeInterval(startTime.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime()),
                maxLoad);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TimeInterval getTimeInterval() {
        return interval;
    }

    public Dropzones getDropzone() {
        return dropzone;
    }

    public String getDropzoneName() {
        return dropzone.getName();
    }

    public static ArrayList<Flights> getFlightsOnDateFromTime(ArrayList<Flights> allFlights, LocalDate date,
            LocalTime time) {

        ArrayList<Flights> reqdFlights = new ArrayList<Flights>();

        Collections.sort(allFlights);

        TimeInterval t = new TimeInterval(date, time, time);
        for (Flights f : allFlights) {
            if (f.interval.compareTo(t) < 0) {
                continue;
            } else if (f.interval.compareDate(date) > 0) {
                break;
            } else {
                reqdFlights.add(f); // Adds references
            }
        }

        return reqdFlights;
    }

    public boolean hasExtraCapacity(int numSkydivers) {
        return ((maxLoad - currLoad) >= numSkydivers);
    }

    // TODO: Modify following 2 methods to work with exceptions?

    // First ensure that hasExtraCapacity is true
    public void addCurrentLoad(int numSkydivers) {
        if (!hasExtraCapacity(numSkydivers)) {
            return;
        }
        currLoad += numSkydivers;
    }

    public void subtractCurrentLoad(int numSkydivers) {
        currLoad -= numSkydivers;
        if (currLoad < 0) {
            currLoad = 0; // Change this?
        }
    }

    public void addDropzoneVacancies(int x) {
        dropzone.addVacancies(x);
    }

    public void subtractDropzoneVacancies(int x) {
        dropzone.subtractVacancies(x);
    }

    public JSONArray generateFlightRuns() {
        JSONArray output = new JSONArray();

        ArrayList<Jumps> sortedJumps = Jumps.sortJumpsForExit(jumps);
        for (Jumps jump : sortedJumps) {
            output.put(jump.generateJumpRun());
        }
        return output;
    }

    public void removeJump(String id) {
        for (Jumps jump : jumps) {
            if (jump.getId().equals(id)) {
                removeJump(jump);
            }
        }
    }

    public void removeJump(Jumps jump) {
        jumps.remove(jump);
    }

    public void addJump(Jumps jump) {
        jumps.add(jump);
    }

    @Override
    public int compareTo(Flights o) {
        return interval.compareTo(o.interval);
    }

    @Override
    public String toString() {
        return "id: " + id +
            ", dropzone: " + dropzone.toString() +
            ", maxload: " + maxLoad +
            ", timeInterval: " + interval.toString();
    }
    
}