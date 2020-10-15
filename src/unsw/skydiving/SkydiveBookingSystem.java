package unsw.skydiving;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Skydive Booking System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a skydive booking system. Input
 * and output is in JSON format.
 *
 * @author Jibitesh Saha, z5280740
 *
 */


public class SkydiveBookingSystem {

    /**
     * List of flights in the booking system.
     */
    private ArrayList<Flights> flights;

    /**
     * List of skydivers in the booking system.
     */
    private ArrayList<Skydivers> skydivers;

    /**
     * List of dropzones in the booking system.
     */
    private ArrayList<Dropzones> dropzones;
    
    /**
     * List of jumps in the booking system.
     */
    private ArrayList<Jumps> jumps;
    
    /**
     * Constructs a skydive booking system. Initially, the system contains no flights, skydivers, jumps or dropzones
     */
    public SkydiveBookingSystem() {
        flights = new ArrayList<Flights>();
        skydivers = new ArrayList<Skydivers>();
        dropzones = new ArrayList<Dropzones>();
        jumps = new ArrayList<Jumps>();
    }

    // Getters and Setters

    /**
     * Given a flightId, reutrns corresponding Flight
     * reference
     * @param flightId id of flight
     * @return Flights reference to the flight
     */
    private Flights getFlight(String flightId) {
        for (Flights flight : flights) {
            if (flight.getId().equals(flightId)) {
                return flight;       
            }
        }
        return null;
    }

    /**
     * Given the name of a dropzone, return a Dropzones
     * reference to the same from the system's list of Dropzones
     * @param dropzoneName name of Dropzone
     * @return Dropzones whose name was provided
     */
    private Dropzones getDropzone(String dropzoneName) {
        for (Dropzones dropzone : dropzones) {
            if(dropzone.getName().equals(dropzoneName)) {
                return dropzone;
            }
        }

        return null;
    }

/**
     * Given the id of a Jump request,
     * return the corresponding Jump object,
     * or null if not found
     * @param id id of jump object
     * @return jump reference, or null if not found
     */
    private Jumps getRequest(String id) {
        for (Jumps j : jumps) {
            if (j.getId().equals(id)) {
                return j;
            }
        }

        return null;
    }

    /**
     * Get the skydiver whose name is given, null if
     * not found
     * @param name name of skydiver
     * @return Sydivers reference with given name,
     * null if not found
     */
    private Skydivers getSkydiver(String name) {
        for (Skydivers s : skydivers) {
            if (s.getName().equals(name)) {
                return s;
            }
        }

        return null;
    }

    /**
     * Uses the given name to create a dropzone,
     * and returns the reference to created dropzone
     * @param dropzoneName name of Dropzone to be created
     * @return Dropzone reference of created object
     */
    private Dropzones addDropzone(String dropzoneName) {
        Dropzones dropzone = new Dropzones(dropzoneName);
        dropzones.add(dropzone);
        return dropzone;
    }

    /**
     * Adds the given flight to the booking system's
     * list of flights. NOTE: Flights are inserted in
     * chronological order. If two flights have the same date
     * and start time, the flight which was registered earlier will
     * remain before
     * @param flight flight to add to flights list
     */
    private void addFlight(Flights flight) {
        
        flights.add(flight);
        Collections.sort(flights);

    }

    /**
     * Adds skydiver to this booking system's list of skydivers
     * @param skydiver skydiver to be added
     */
    private void addSkydiver(Skydivers skydiver) {
        skydivers.add(skydiver);
    }

    /**
     * Removes jump with given id from this
     * booking system's list of jumps. Returns index
     * from which jump was removed
     * @param id id of jump
     * @return index of removal, -1 if jump not found
     */
    private int removeJump(String id) {
        for (int i = 0; i < jumps.size(); i++) {
            if (jumps.get(i).getId().equals(id)) {
                jumps.remove(i);
                return i;
            }
        }
        // jump not found
        return -1;
    }

    /**
     * Convert given JSONArray to an array of Strings
     * @param arr
     * @return ArrayList<String> corresponding to JSONArray
     */
    private ArrayList<String> jsonArrayToStringsList(JSONArray arr) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            strings.add(arr.getString(i));
        }
        return strings;
    }

    /**
     * Converts an ArrayList of Skydiver name to the corresponding
     * list of Skydiver references
     * @param strings List of Skydiver names
     * @return List of Skydiver references
     */
    private ArrayList<Skydivers> arrayListofStringsToSkydivers(ArrayList<String> strings) {
        ArrayList<Skydivers> divers = new ArrayList<Skydivers>();
        Skydivers tmp = null;
        for (String skydiverName : strings) {
            tmp = getSkydiver(skydiverName);
            if (tmp != null) {
                divers.add(tmp);
            }
        }
        return divers;
    }


    /**
     * Given the JSONObject input from stdin,
     * process it, and make the corresponding updates
     * in the system. Finally, output details of success / 
     * failure (for request, change and jump-run).
     * @param json the input JSONObject
     */
    private void processCommand(JSONObject json) {

        String id, type;
        LocalDateTime startTime, endTime;
        ArrayList<String> divers;
        JSONObject obj;
        Jumps jump;
        switch (json.getString("command")) {

        case "flight":
            id = json.getString("id");
            int maxload = json.getInt("maxload");
            startTime = LocalDateTime.parse(json.getString("starttime"));
            endTime = LocalDateTime.parse(json.getString("endtime"));
            String dropzone = json.getString("dropzone");
            
            createFlight(id, dropzone, startTime, endTime, maxload);

            break;

        case "skydiver":
            String skydiver = json.getString("skydiver");
            String license = json.getString("licence");
            if (json.has("dropzone")) {
                createSkydiver(skydiver, license, json.getString("dropzone"));
            } else {
                createSkydiver(skydiver, license);
            }

            break;
        
        case "request":
            id = json.getString("id");
            startTime = LocalDateTime.parse(json.getString("starttime"));
            type = json.getString("type");
            divers = new ArrayList<String>();
            
            switch (type) {
                case "fun":
                    JSONArray JSONdivers = json.getJSONArray("skydivers");
                    divers.addAll(jsonArrayToStringsList(JSONdivers));
                    break;
                
                case "tandem":
                    divers.add(json.getString("passenger"));
                    break;

                case "training":
                    divers.add(json.getString("trainee"));
                    break;
            }

            obj = new JSONObject();
            jump = makeRequest(id, type, startTime, divers);
            
            if (jump != null) {
                // Sucess JSON Object
                Flights allocatedFlight = jump.getFlight();
                obj.put("flight", allocatedFlight.getId());
                obj.put("dropzone", allocatedFlight.getDropzone().getName());
                obj.put("status", "success");
            } else {
                // Failure JSON Object
                obj.put("status", "rejected");
            }

            System.out.println(obj.toString());
            break;
        
        case "cancel":
            id = json.getString("id");
            cancelRequest(id);    
            break;
        
        case "change":
            id = json.getString("id");
            startTime = LocalDateTime.parse(json.getString("starttime"));
            type = json.getString("type");
            divers = new ArrayList<String>();
            switch (type) {
                case "fun":
                    JSONArray JSONdivers = json.getJSONArray("skydivers");
                    divers.addAll(jsonArrayToStringsList(JSONdivers));
                    break;
                
                case "tandem":
                    divers.add(json.getString("passenger"));
                    break;

                case "training":
                    divers.add(json.getString("trainee"));
                    break;
            }

            obj = new JSONObject();
            jump = changeRequest(id, type, startTime, divers);
            if (jump != null) {
                // Sucess JSON Object
                Flights allocatedFlight = jump.getFlight();
                obj.put("flight", allocatedFlight.getId());
                obj.put("dropzone", allocatedFlight.getDropzone().getName());
                obj.put("status", "success");
            } else {
                // Failure JSON Object
                obj.put("status", "rejected");
            }
            
            System.out.println(obj.toString());
            break;
        
        case "jump-run":
            id = json.getString("id");
            printJumpRun(id);
            break;
        }
    }

    /**
     * Create a Flight using provided details
     * @param id id of Flight to be created
     * @param dropzone name of Dropzone hosting the flight
     * @param start the LocalDateTime of the start of the flight
     * @param end the LocalDateTime of the end of the flight
     * @param maxload the maximum load the flight can take
     */
    private void createFlight(String id, String dropzone, LocalDateTime start, LocalDateTime end, int maxload) {
        
        // Get dropzone if exists
        Dropzones flightDropzone = getDropzone(dropzone);

        if (flightDropzone == null) {
            flightDropzone = addDropzone(dropzone);
        }

        addFlight(new Flights(id, flightDropzone, start, end, maxload));

    }


    /**
     * Creates a Skydiver using given id and license.
     * This version is for:
     * <ul>
     * <li> Students (Skydivers, no special treatment), and </li> 
     * <li> Licensed Jumpers </li>
     * </ul>
     * @param id
     * @param license "student" or "licenced-jumper"
     */
    private void createSkydiver(String id, String license) {
        Skydivers skydiver = null;
        switch (license) {
            case "student":
                skydiver = new Skydivers(id, license);
                break;
        
            case "licenced-jumper":
                skydiver = new LicensedJumpers(id, license);
                break;
            
            default:
                // ERROR!
                break;
        }

        addSkydiver(skydiver);
    }

    /**
     * Creates an Instructor using given id, license and
     * home dropzone.
     * This overloaded version is for:
     * <ul>
     * <li> Instructors, and </li> 
     * <li> Tandem Masters </li>
     * </ul>
     * @param id
     * @param license "student" or "licenced-jumper"
     * @param dropzone name of home dropzone of skydiver
     */
    private void createSkydiver(String id, String license, String dropzone) {
        // Version for instructors and tandem-masters
        
        // Get dropzone if exists
        Dropzones flightDropzone = getDropzone(dropzone);

        if (flightDropzone == null) {
            flightDropzone = addDropzone(dropzone);
        }

        Instructors teacher = null;
        switch (license) {
            case "instructor":
                teacher = new Instructors(id, license, flightDropzone);
                break;
        
            case "tandem-master":
                teacher = new TandemMasters(id, license, flightDropzone);
                break;
            
            default:
                // ERROR!
                break;
        }

        flightDropzone.addInstructor(teacher);

        addSkydiver(teacher);
    }

    /**
     * Make a request for a jump, by specifying:
     * <ul>
     * <li> Type of jump </li>
     * <li> Starting Date & Time  of Availability </li>
     * <li> A list of divers </li>
     * </ul>
     * @param id
     * @param type "fun", "tandem", or "training"
     * @param start a LocalDateTime representing the 
     * @param divers String List of skydiver names
     * @return The jump object as the result of the request,
     * or null if the request failed
     */
    private Jumps makeRequest(String id, String type, LocalDateTime start, ArrayList<String> divers) {
        /* STEPS:
            1. Get Flights on given date from given time
            2. For each flight, check the following:
                a. Check load can be added
                b. Check TimeInterval availability of each skydiver
                    - Take into account type of jump to adjust time
                c. If training / tandem jump, allocateInstructor()
                    p. Use dropzone to decide
                    q. Identify instructor with least jumps
                    r. Check availability of instructor
            3. Update system, for booking - or fail & reject
                a. Add load to flight
                b. Add booking to all skydivers
                c. increment numJumps for skydivers
                d. Output status and JSON object

        */


        Jumps jump = null;
        boolean needTeacherAllocation = false;
        switch (type) {
            case "fun":
                jump = new FunJumps(id, type, divers.size(), arrayListofStringsToSkydivers(divers));
                needTeacherAllocation = false;
                break;
        
            case "tandem":
                jump = new TandemJumps(id, type, 2, getSkydiver(divers.get(0)));
                needTeacherAllocation = true;
                // Since a 5 minute briefing session is necessary
                // Actual starting point for looking for flights
                // is 5 minutes later
                start = start.plusMinutes(5);
                break;

            case "training":
                jump = new TrainingJumps(id, type, 2, getSkydiver(divers.get(0)));
                needTeacherAllocation = true;
                break;
        }

        ArrayList<Flights> possibleFlights = Flights.getFlightsOnDateFromTime(flights, start.toLocalDate(), start.toLocalTime());

        // DEBUG
        // System.out.println(possibleFlights.size());

        Flights flight = determineBooking(jump, possibleFlights, needTeacherAllocation);
        
        // DEBUG
        // System.out.println(flight);

        if (flight != null) {
            // flight is free, can book
            bookJump(jump, flight);
            return jump;
        } else {
            // Failed to book a flight
            return null;
        }
    }

    /**
     * Given a Jump and a list of possible flights to be allocated,
     * decides which flight to choose and returns it. Also checks for
     * and allocates a teacher if necessary
     * @param jump the jump object which needs allocation
     * @param flights a list of possible flights
     * @param needTeacherAllocation is teacher allocation necessary?
     * @return
     */
    private Flights determineBooking(Jumps jump, ArrayList<Flights> flights, boolean needTeacherAllocation) {
        TimeInterval flightDuration;

        for (Flights flight : flights) {

            if(!flight.hasExtraCapacity(jump.getNumJumpers())) {
                continue;
            }

            flightDuration = flight.getTimeInterval().clone();
            // Adjusted to include briefing / debriefing timings
            jump.adjustTimeInterval(flightDuration);

            for(Skydivers skydiver : jump.getSkydivers()) {
                
                TimeInterval skydiverDuration = flightDuration.clone();

                // If this is a tandem jump, skydiver will be 
                // a passenger, since tandem master hasn't been allocated yet
                if (jump.getClass() != TandemJumps.class) {
                    skydiver.adjustTimeInterval(skydiverDuration);
                }

                // If this skydiver is not free, check next flight
                if (!skydiver.isSkydiverFree(skydiverDuration)) {
                    continue;
                }
            }

            if (needTeacherAllocation) {
                TimeInterval instructorInterval = flightDuration.clone();

                // Instructos and tandem masters in
                // training and tandem jumps respectively
                // always repack gear (10 minutes)
                instructorInterval.adjustEndTime(10); 

                Instructors teacher = flight.getDropzone().allocateTeacher(jump.getType(), instructorInterval, jump.getSkydivers().get(0));

                if (teacher == null) {
                    // fail current flight
                    continue;
                }

                jump.setTeacher(teacher);

            }

            // Reached here, all GOOD
            return flight;
        }

        return null;
    }

    /**
     * Update booking system to book the jump, i.e.
     * form connections between jump, flight, Skydivers
     * and TimeIntervals
     * @param jump jump to be booked
     * @param flight flight to be booked
     */
    private void bookJump(Jumps jump, Flights flight) {
        jump.setFlight(flight);
        jump.makeJumpRelations();
        jumps.add(jump);

    }

    /**
     * Cancels the request (i.e. jump) with given id.
     * Return the index in the jumps list of this booking
     * sysem from which it was removed, for restoration
     * @param id id of Jump
     * @return
     */
    private int cancelRequest(String id) {
        /* STEPS:
            1. Get Jump object;
            2. Restore flight->dropzone->vacancies
            3. Reduce flight load
            4. Get Flight's time interval
            5. Free up all skydiver bookings
            5. Remove from Flights
            5. Remove from jumps (here)

        */
        Jumps request = getRequest(id);
        if (request == null) {return -1;}

        return cancelRequest(request);
        
    }

    /**
     * Overloaded version of cancelRequest(String id),
     * cancelling directly using given jump reference
     * @param jump jump to be removed
     * @return index in this system's jump list from which
     * jump was removed, otherwise -1
     */
    private int cancelRequest(Jumps jump) {
        jump.cancelJumpRelations();
        return removeJump(jump.getId());
    }

    /**
     * Changes request whose id is given, by cancelling given request,
     * and then making a new request using new details.
     * If the new request fails, restores old request
     * @param id id of request (i.e. jump)
     * @param type type of jump
     * @param start start of jump
     * @param divers skydivers int he jump
     * @return new Jumps object (i.e. request), 
     * or null if the change failed
     */
    private Jumps changeRequest(String id, String type, LocalDateTime start, ArrayList<String> divers) {
        
        Jumps initialRequest = getRequest(id);

        if (initialRequest == null) {return null;}

        // First, cancel request
        int indexInFlights = initialRequest.cancelJumpRelations();
        int indexInSystem = removeJump(id);

        // Next, try to make request
        Jumps newRequest = makeRequest(id, type, start, divers);

        // if new request failed, restore previous
        if (newRequest == null) {
            initialRequest.makeJumpRelations(indexInFlights);
            jumps.add(indexInSystem, initialRequest);
            return null;         
        } else {
            return newRequest;
        }
    }

    /**
     * Prints the jump-run associated with flight
     * having given flightId, after generating its
     * JSONArray
     * @param flightId id of flight whose jump-run
     * is to be printed
     */
    private void printJumpRun(String flightId) {
        Flights flight = getFlight(flightId);
        if (flight == null) {return;}
        
        JSONArray flightArray = flight.generateFlightRuns();
        System.out.println(flightArray.toString());
    }

    public static void main(String[] args) {
        SkydiveBookingSystem system = new SkydiveBookingSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}

