package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

abstract class Jumps implements Comparable<Jumps> {
    private String id;
    private String type;
    private Flights flight;

    // Skydiver list of some sort
    private int numJumpers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Flights getFlight() {
        return flight;
    }

    public void setFlight(Flights flight) {
        this.flight = flight;
    }

    public int getNumJumpers() {
        return numJumpers;
    }

    public void cancelJumpRelations() {
        // Restore dropzone vacancies
        flight.addDropzoneVacancies(numJumpers);
        // Reduce the current flight load
        flight.subtractCurrentLoad(numJumpers);

        // Clear bookings for each skydiver
        TimeInterval interval = flight.getTimeInterval();
        ArrayList<Skydivers> skydivers = getSkydivers();
        for (Skydivers skydiver : skydivers) {
            skydiver.cancelBooking(interval);
        }

        // Remove jump from flight
        flight.removeJump(this);
    }

    public void makeJumpRelations() {
        // Decrease dropzone vacancies
        flight.subtractDropzoneVacancies(numJumpers);
        // Increase the current flight load
        flight.addCurrentLoad(numJumpers);

        // Clear bookings for each skydiver
        TimeInterval interval = flight.getTimeInterval();
        adjustTimeInterval(interval); // wrt. current jump
        ArrayList<Skydivers> skydivers = getSkydivers();
        for (Skydivers skydiver : skydivers) {

            TimeInterval t2 = interval.clone();

            if (this.getClass() != TandemJumps.class && skydiver != ((TandemJumps) this).getPassenger()) {
                skydiver.adjustTimeInterval(t2);
            }

            skydiver.makeBooking(t2);
        }

        // Add jump to flight
        flight.addJump(this);

    }

    public void setNumJumpers(int numJumpers) {
        this.numJumpers = numJumpers;
    }

    public abstract JSONObject generateJumpRun();

    public static ArrayList<Jumps> sortJumpsForExit(ArrayList<Jumps> jumps) {
        ArrayList<Jumps> funJumps = new ArrayList<Jumps>();
        ArrayList<Jumps> trainingJumps = new ArrayList<Jumps>();
        ArrayList<Jumps> tandemJumps = new ArrayList<Jumps>();

        for (Jumps jump : jumps) {
            if (jump.getClass() == FunJumps.class) {
                funJumps.add(jump);
            } else if (jump.getClass() == TrainingJumps.class) {
                trainingJumps.add(jump);
            } else if (jump.getClass() == TandemJumps.class) {
                tandemJumps.add(jump);
            } else {
                // Excpetion?
            }
        }

        Collections.sort(funJumps);
        Collections.sort(trainingJumps);

        ArrayList<Jumps> sortedJumps = new ArrayList<Jumps>();
        sortedJumps.addAll(funJumps);
        sortedJumps.addAll(trainingJumps);
        sortedJumps.addAll(tandemJumps);

        return sortedJumps;
    }

    @Override
    public int compareTo(Jumps o) {
        return o.numJumpers - this.numJumpers;
    }

    public abstract ArrayList<Skydivers> getSkydivers();

    public Jumps(String id, String type, int numJumpers) {
        this(id, type, numJumpers, null);
    }

    public Jumps(String id, String type, int numJumpers, Flights flight) {
        this.id = id;
        this.type = type;
        this.numJumpers = numJumpers;
        this.flight = flight;
    }

    public abstract void adjustTimeInterval(TimeInterval t);

    @Override
    public String toString() {
        return "id: " + id +
            ", type: " + type + 
            ", flight: " + flight.getId() + 
            ", numJumpers: " + numJumpers;
    }
    
    public abstract void setTeacher(Instructors instructor);

    
}