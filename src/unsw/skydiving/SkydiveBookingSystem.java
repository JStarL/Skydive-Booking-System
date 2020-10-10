package unsw.skydiving;

import java.time.LocalDateTime;
import java.util.Scanner;

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
     * Constructs a skydive booking system. Initially, the system contains no flights, skydivers, jumps or dropzones
     */
    public SkydiveBookingSystem() {
        // TODO Auto-generated constructor stub
    }

    private void processCommand(JSONObject json) {

        switch (json.getString("command")) {

        case "flight":
            String id = json.getString("id");
            int maxload = json.getInt("maxload");
            LocalDateTime starttime = LocalDateTime.parse(json.getString("starttime"));
            LocalDateTime endtime = LocalDateTime.parse(json.getString("endtime"));
            String dropzone = json.getString("dropzone");
            
            // TODO - add flight

            break;

        // TODO Implement other commands
        
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
