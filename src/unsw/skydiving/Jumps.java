package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

abstract class Jumps implements Comparable<Jumps> {
    
    /**
     * Unique identifier of the jump
     */
    private String id;
    
    /**
     * Type of jump, namely: "fun", "training", "tandem"
     */
    private String type;
    
    /**
     * Each jump is a part of (associated with) exactly one flight,
     * whose reference is flight
     */
    private Flights flight;

    /**
     * The number of jumpers in this jump
     */
    private int numJumpers;

    /* NOTE: Holders of actual Skydivers references implemented
     * in subclasses
     */

    /**
     * Standard constructor for Jumps
     * @param id Unique identifier for jump
     * @param type Type of jump, namely: "fun", "training", "tandem"
     * @param numJumpers Number of jumpers in this jump
     * @param flight The flight of which this is a part
     */
    public Jumps(String id, String type, int numJumpers, Flights flight) {
        this.id = id;
        this.type = type;
        this.numJumpers = numJumpers;
        this.flight = flight;
    }

    /**
     * A useful constructor for a jump whose flight
     * has not been decided yet
     * (initialises flight as null)
     * @param id
     * @param type "fun", "training" or "tandem"
     * @param numJumpers size of jump
     */
    public Jumps(String id, String type, int numJumpers) {
        this(id, type, numJumpers, null);
    }


    // Getters and Setters

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

    public void setNumJumpers(int numJumpers) {
        this.numJumpers = numJumpers;
    } 

    /**
     * Get a list of skydivers associated with a jump
     * (At that given point of execution).
     * Very handy in several calculations
     * @return ArrayList of Skydivers
     */
    public abstract ArrayList<Skydivers> getSkydivers();

    /**
     * Adjust the given TimeInterval based on the
     * type of jump this is:
     * <ul>
     * <li> Tandem Jumps have a 5-minute briefing session</li>
     * <li> Training jumps have a 15-minute debriefing session</li>
     * <li> Fun jumps do nothing </li>
     * </ul>
     * @param t TimeInterval to adjust
     */
    public abstract void adjustTimeInterval(TimeInterval t);

    /**
     * Generates a JSONObject for this jump
     * to be included in a flight's jump-run
     * @return JSONObject representation of this jump
     * as in specification, according to each type of jump
     */
    public abstract JSONObject generateJumpRun();

    /**
     * Sets the teacher of the jump as follows:
     * <ul>
     * <li> Tandem Jumps set the Tandem Master</li>
     * <li> Training jumps set the Instructor</li>
     * <li> Fun jumps do nothing (since there is no teacher)</li>
     * </ul>
     * @param instructor teacher to be set
     */
    public abstract void setTeacher(Instructors instructor);

    /**
     * A String representation of this jump.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return "id: " + id +
            ", type: " + type + 
            ", flight: " + flight.getId() + 
            ", numJumpers: " + numJumpers;
    }
    
    /**
     * Compares jumps based on the number of Jumpers
     * in each jump. Useful for sorting jumps for
     * exit from a flight.
     */
    @Override
    public int compareTo(Jumps o) {
        return o.numJumpers - this.numJumpers;
    }

    /**
     * Sorts the given ArrayList of Jumps to
     * the required sorted order as in the specification:
     * <ol>
     * <li> Largest to smallest fun-jump groups (including individuals)</li>
     * <li> Training jumps (They all have 2 - spec confused?)</li>
     * <li> Tandem jumps</li>
     * </ol>
     * @param jumps ArrayList of Jumps to be sorted for exit
     * @return sorted ArrayList of jumps according to spec
     */
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

        /* NOTE: If a jump is booked earlier,
         * it will be earlier in the ArrayList<Jumps>
         * Since this is a stable sort,
         * booking order is preserved
         */
        Collections.sort(funJumps);
        Collections.sort(trainingJumps); // Not Necessary

        ArrayList<Jumps> sortedJumps = new ArrayList<Jumps>();
        sortedJumps.addAll(funJumps);
        sortedJumps.addAll(trainingJumps);
        sortedJumps.addAll(tandemJumps);

        return sortedJumps;
    }

    /**
     * Register a jump, without specifying a
     * "restoration index" for flights. See
     * makeJumpRelation(int flightJumpsIndex) for more details
     */
    public void makeJumpRelations() {
        makeJumpRelations(-1);
    }

    /**
     * To register a jump invloves updating
     * several aspects of the booking system:
     * <ol>
     * <li> Add this jump to flight's list of jumps </li>
     * <li> Increase flight's current load </li>
     * <li> Decrease Dropzone Vacancies </li>
     * <li> Make bookings for all skydivers of this jump </li>
     * </ol>
     * NOTE: The flight should be set before calling this method
     * @param flightJumpsIndex index to insert jump in the flight's
     * list of jumps. Useful for restoring the previous state
     * of a flight's jumps.
     * <ul>
     * <li> -1 if not to be used
     * <li> i>=0  to insert at position i
     * </ul>
     */
    public void makeJumpRelations(int flightJumpsIndex) {
        
        // Return if no flight registered
        if (flight == null) {return;}

        // Add this jump to flight
        if (flightJumpsIndex == -1) {
            flight.addJump(this);
        } else {
            flight.addJump(this, flightJumpsIndex);
        }

        // Decrease dropzone vacancies
        flight.subtractDropzoneVacancies(numJumpers);

        // Increase the current flight load
        flight.addCurrentLoad(numJumpers);

        // Make bookings for each skydiver
        TimeInterval interval = flight.getTimeInterval();
        // Asjust TimeInterval according to current jump type
        adjustTimeInterval(interval);

        ArrayList<Skydivers> skydivers = getSkydivers();
        
        // DEBUG
        // System.out.println(skydivers.toString());
        
        for (Skydivers skydiver : skydivers) {

            TimeInterval t2 = interval.clone();

            // If this skydiver is not a Tandem Jump Passenger
            if (!(this.getClass() == TandemJumps.class && skydiver == ((TandemJumps) this).getPassenger())) {
                skydiver.adjustTimeInterval(t2);
            }

            skydiver.makeBooking(t2);
        }

    }

    /**
     * To remove a jump from the registry invloves
     * removing a number of dependencies:
     * <ol>
     * <li> Decrease flight's current load </li>
     * <li> Increase Dropzone Vacancies </li>
     * <li> Clearbookings for all skydivers of this jump </li>
     * <li> Remove this jump from flight's list of jumps,
     *      returning its index, for possible restoration </li>
     * </ol>
     * NOTE: The flight should be set before calling this method
     * @return integer specifying position in the flight's jumps list
     * from which the jump was removed. Useful for jump restoration
     */
    public int cancelJumpRelations() {
        
        // Return if no flight registered
        if (flight == null) {return -1;}
        
        // Reduce the current flight load
        flight.subtractCurrentLoad(numJumpers);
        
        // Restore dropzone vacancies
        flight.addDropzoneVacancies(numJumpers);
        
        // Clear bookings for each skydiver

        // Get flight duration
        TimeInterval interval = flight.getTimeInterval();

        ArrayList<Skydivers> skydivers = getSkydivers();
        for (Skydivers skydiver : skydivers) {
            skydiver.cancelBooking(interval);
        }

        // Remove jump from flight
        return flight.removeJump(this);
    }
    
}