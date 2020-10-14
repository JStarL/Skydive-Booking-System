package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

class FunJumps extends Jumps {
    
    /**
     * List of skydivers in this funJump
     */
    private ArrayList<Skydivers> skydivers;

    /**
     * Constructs a fun jump when flight and skydivers not yet decided
     * @param id
     * @param type
     * @param numJumpers
     */
    public FunJumps(String id, String type, int numJumpers) {
        super(id, type, numJumpers);
        this.skydivers = new ArrayList<Skydivers>();
    }

    /**
     * Constructs a fun jump when flight is unknown, but
     * List of skydivers supplied
     * @param id
     * @param type
     * @param numJumpers
     */
    public FunJumps(String id, String type, int numJumpers, ArrayList<Skydivers> skydivers) {
        this(id, type, numJumpers);
        this.skydivers.addAll(skydivers);
    }
    
    // Getters and Setters

    @Override
    public ArrayList<Skydivers> getSkydivers() {
        return skydivers;
    }

    /**
     * Returns the names / ids of each skydiver
     * in the skydivers list
     * @return
     */
    public ArrayList<String> getSkydiverNames() {
        ArrayList<String> skydiverNames = new ArrayList<String>();
        for (Skydivers skydiver : skydivers) {
            skydiverNames.add(skydiver.getName());
        }

        return skydiverNames;
    }

    /**
     * Add given skydiver to skydivers list
     * @param skydiver
     */
    public void addSkydivers(Skydivers skydiver) {
        skydivers.add(skydiver);
    }

    /**
     * Add given skydivers to skydivers list
     * @param skydivers
     */
    public void addSkydivers(ArrayList<Skydivers> skydivers) {
        this.skydivers.addAll(skydivers);
    }


    @Override
    public JSONObject generateJumpRun() {
        JSONObject output = new JSONObject();

        // Create ArrayList<Strings>, sort, then use to create JSONArray
        ArrayList<String> skydiverNames = getSkydiverNames();
        Collections.sort(skydiverNames); // Lexicographic ordering
        output.put("skydivers", skydiverNames);

        return output;
    }


    @Override
    public void adjustTimeInterval(TimeInterval t) {
        // Nothing to do
        // Fun-Jumps have no briefing, debriefing
    }

    /**
     * A String representation of this funJump.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return super.toString() +
            ", skydivers: " + getSkydiverNames().toString();
    }

    @Override
    public void setTeacher(Instructors instructor) {
        // Do nothing
    }

}