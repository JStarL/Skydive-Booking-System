package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

class FunJumps extends Jumps {
    private ArrayList<Skydivers> skydivers;

    @Override
    public JSONObject generateJumpRun() {
        JSONObject output = new JSONObject();

        // Create ArrayList<Strings>, sort, then use to create JSONArray
        ArrayList<String> skydiverNames = getSkydiverNames();
        Collections.sort(skydiverNames); // Lexicographic
        output.put("skydivers", skydiverNames);

        return output;
    }

    private ArrayList<String> getSkydiverNames() {
        ArrayList<String> skydiverNames = new ArrayList<String>();
        for (Skydivers skydiver : skydivers) {
            skydiverNames.add(skydiver.toString());
        }

        return skydiverNames;
    }

    @Override
    public ArrayList<Skydivers> getSkydivers() {
        return skydivers;
    }

    public void addSkydivers(Skydivers skydiver) {
        skydivers.add(skydiver);
    }

    public void addSkydivers(ArrayList<Skydivers> skydivers) {
        this.skydivers.addAll(skydivers);
    }

    public FunJumps(String id, String type, int numJumpers) {
        super(id, type, numJumpers);
    }

    public FunJumps(String id, String type, int numJumpers, ArrayList<Skydivers> skydivers) {
        super(id, type, numJumpers);
        this.skydivers = new ArrayList<Skydivers>();
        this.skydivers.addAll(skydivers);
    }

    public FunJumps(String id, String type, int numJumpers, Flights flight) {
        super(id, type, numJumpers, flight);
    }

    @Override
    public void adjustTimeInterval(TimeInterval t) {
        // Nothing to do
        // Fun-Jumps have no briefing, debriefing

    }

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