package unsw.skydiving;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;

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

    ArrayList<Flights> flights;
    ArrayList<Skydivers> skydivers;
    ArrayList<Dropzones> dropzones;
    ArrayList<Jumps> jumps;
    /**
     * Constructs a skydive booking system. Initially, the system contains no flights, skydivers, jumps or dropzones
     */
    public SkydiveBookingSystem() {
        flights = new ArrayList<Flights>();
        skydivers = new ArrayList<Skydivers>();
        dropzones = new ArrayList<Dropzones>();
        jumps = new ArrayList<Jumps>();
    }

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
                obj.put("dropzone", allocatedFlight.getDropzoneName());
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
                obj.put("dropzone", allocatedFlight.getDropzoneName());
                obj.put("status", "success");
            } else {
                // Failure JSON Object
                obj.put("status", "rejected");
            }
            break;
        
        case "jump-run":
            id = json.getString("id");
            printJumpRun(id);
            break;
        }
    }

    private ArrayList<String> jsonArrayToStringsList(JSONArray arr) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < arr.length(); i++) {
            strings.add(arr.getString(i));
        }
        return strings;
    }

    private Dropzones addDropzone(String dropzoneName) {
        Dropzones dropzone = new Dropzones(dropzoneName);
        dropzones.add(dropzone);
        return dropzone;
    }

    private void createFlight(String id, String dropzone, LocalDateTime start, LocalDateTime end, int maxload) {
        
        // Get dropzone if exists
        Dropzones flightDropzone = getDropzone(dropzone);

        if (flightDropzone == null) {
            flightDropzone = addDropzone(dropzone);
        }

        addFlight(new Flights(id, flightDropzone, start, end, maxload));

    }

    private void addFlight(Flights flight) {
        
        boolean inserted = false;
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).compareTo(flight) > 0) {
                flights.add(i, flight);
                inserted = true;
                break;
            }
        }

        if (!inserted) {
            flights.add(flight);
        }

        // DEBUG
        // System.out.println(flight);
    }

    private void createSkydiver(String id, String license) {
        // Version for students and licensed-jumpers
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

    private void addSkydiver(Skydivers skydiver) {
        skydivers.add(skydiver);
        // DEBUG
        // System.out.println(skydiver);
    }

    private Dropzones getDropzone(String dropzoneName) {
        for (Dropzones dropzone : dropzones) {
            if(dropzone.getName().equals(dropzoneName)) {
                return dropzone;
            }
        }

        return null;
    }

    private ArrayList<Skydivers> arrayListofStringsToSkydivers(ArrayList<String> strings) {
        ArrayList<Skydivers> divers = new ArrayList<Skydivers>();
        for (String skydiverName : strings) {
            divers.add(getSkydiver(skydiverName));
        }
        return divers;
    }

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
                d. Update system, for booking - or fail & reject
                    p. Add load to flight
                    q. Add booking to all skydivers
                    f. increment numJumps for skydivers
                e. Output status and JSON object

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
                start = start.plusMinutes(5);
                break;

            case "training":
                jump = new TrainingJumps(id, type, 2, getSkydiver(divers.get(0)));
                needTeacherAllocation = true;
                break;
        }


        ArrayList<Flights> possibleFlights = Flights.getFlightsOnDateFromTime(flights, start.toLocalDate(), start.toLocalTime());

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

    private Flights determineBooking(Jumps jump, ArrayList<Flights> flights, boolean needTeacherAllocation) {
        TimeInterval flightDuration;
        boolean allSkydiversFree = true;
        // DEBUG
        // System.out.println(jump.getSkydivers().size());
        for (Flights flight : flights) {
            allSkydiversFree = true;

            if(!flight.hasExtraCapacity(jump.getNumJumpers())) {
                continue;
            }

            flightDuration = flight.getTimeInterval();

            for(Skydivers skydiver : jump.getSkydivers()) {
                
                TimeInterval skydiverDuration = flightDuration.clone();

                jump.adjustTimeInterval(skydiverDuration);

                if (jump.getClass() != TandemJumps.class) {
                    skydiver.adjustTimeInterval(skydiverDuration);
                }

                // DEBUG
                // System.out.println(skydiverDuration);
                // skydiver.printBookings();
                if (!skydiver.isSkydiverFree(skydiverDuration)) {
                    allSkydiversFree = false;
                    break;
                }
            }

            if (!allSkydiversFree) {
                continue;
            }
            
            if (needTeacherAllocation) {
                TimeInterval instructorInterval = (TimeInterval) flightDuration.clone();

                jump.adjustTimeInterval(instructorInterval);
                // Instructos and tandem masters in
                // training and tandem jumps respectively
                // always repack gear (10 minutes)
                instructorInterval.adjustEndTime(10); 

                Instructors teacher = flight.getDropzone().allocateTeacher(jump.getType(), instructorInterval, jump.getSkydivers().get(0));
                // DEBUG
                // System.out.println(teacher);

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

    /*
    private void adjustTimeInterval(TimeInterval t, Jumps jump, Skydivers skydiver) {
        
        adjustTimeIntervalJumps(t, jump);

        if (jump.getClass() == FunJumps.class) {
            skydiver.adjustTimeInterval(t);

        } else if (jump.getClass() == TandemJumps.class) {
            // if skydiver is passenger, don't adjust time interval

            if (((TandemJumps)jump).getPassenger() != skydiver) {
                skydiver.adjustTimeInterval(t);
            }
        } else if (jump.getClass() == TrainingJumps.class) {
            skydiver.adjustTimeInterval(t);

        }

    }
    */

    /*
    private void adjustTimeIntervalJumps(TimeInterval t, Jumps jump) {
        if (jump.getClass() == FunJumps.class) {

        } else if (jump.getClass() == TandemJumps.class) {
            TandemJumps.adjustTimeInterval(t);

        } else if (jump.getClass() == TrainingJumps.class) {
            TrainingJumps.adjustTimeInterval(t);

        }
    }
    */

    private void bookJump(Jumps jump, Flights flight) {
        jump.setFlight(flight);
        jump.makeJumpRelations();
        jumps.add(jump);

        // DEBUG
        // System.out.println(jump);
    }

    private Jumps getRequest(String id) {
        for (Jumps j : jumps) {
            if (j.getId().equals(id)) {
                return j;
            }
        }

        return null;
    }

    private Skydivers getSkydiver(String name) {
        for (Skydivers s : skydivers) {
            if (s.getName().equals(name)) {
                return s;
            }
        }

        return null;
    }

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

    private int cancelRequest(Jumps jump) {
        jump.cancelJumpRelations();
        
        for (int i = 0; i < jumps.size(); i++) {
            if (jumps.get(i) == jump) {
                jumps.remove(i);
                return i;
            }
        }

        return -1;
    }

    private Jumps changeRequest(String id, String type, LocalDateTime start, ArrayList<String> divers) {
        
        Jumps initialRequest = getRequest(id);

        if (initialRequest == null) {return null;}

        int index = cancelRequest(initialRequest);

        Jumps newRequest = makeRequest(id, type, start, divers);

        if (newRequest == null) {
            restoreRequest(initialRequest, index);
            return null;         
        } else {
            return newRequest;
        }
    }

    private void restoreRequest(Jumps jump, int index) {
        jump.makeJumpRelations();
        jumps.add(index, jump);

    }

    private void printJumpRun(String flightId) {
        for (Flights flight : flights) {
            if (flight.getId().equals(flightId)) {
                JSONArray flightArray = flight.generateFlightRuns();
                System.out.println(flightArray.toString());
                break;
            }
        }
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
