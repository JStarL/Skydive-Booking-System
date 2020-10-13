package unsw.skydiving;

import java.util.ArrayList;

import org.json.JSONObject;

class TandemJumps extends Jumps {
    private TandemMasters tandemMaster;
    private Skydivers passenger;

    @Override
    public JSONObject generateJumpRun() {
        JSONObject output = new JSONObject();
        output.put("jump-master", tandemMaster.getName());
        output.put("passenger", passenger.getName());
        return output;
    }

    @Override
    public void adjustTimeInterval(TimeInterval t) {
        t.adjustStartTime(-5); // 5 minute briefing before
    }

    @Override
    public ArrayList<Skydivers> getSkydivers() {
        ArrayList<Skydivers> skydivers = new ArrayList<Skydivers>(2);
        if (tandemMaster != null) {
            skydivers.add(tandemMaster);
        }
        if (passenger != null) {
            skydivers.add(passenger);
        }
        return skydivers;
    }

    public TandemMasters getTandemMaster() {
        return tandemMaster;
    }

    public void setTandemMaster(TandemMasters tandemMaster) {
        this.tandemMaster = tandemMaster;
    }

    public Skydivers getPassenger() {
        return passenger;
    }

    public void setPassenger(Skydivers passenger) {
        this.passenger = passenger;
    }

    public TandemJumps(String id, String type, int numJumpers, Skydivers passenger) {
        super(id, type, numJumpers);
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return super.toString() +
            ", tandemMaster: " + tandemMaster.getName() +
            ", passenger: " + passenger.getName();
    }

    @Override
    public void setTeacher(Instructors instructor) {
        if (instructor.getClass() == TandemMasters.class) {
            this.tandemMaster = (TandemMasters) instructor;
        }
    }

}