package unsw.skydiving;

import java.util.ArrayList;

import org.json.JSONObject;

class TrainingJumps extends Jumps {
    private Instructors instructor;
    private Skydivers trainee;

    @Override
    public JSONObject generateJumpRun() {
        JSONObject output = new JSONObject();
        output.put("instructor", instructor.toString());
        output.put("trainee", trainee.toString());
        return output;
    }

    @Override
    public void adjustTimeInterval(TimeInterval t) {
        t.adjustEndTime(15); // 15 minutes debriefing for training
    }

    @Override
    public ArrayList<Skydivers> getSkydivers() {
        ArrayList<Skydivers> skydivers = new ArrayList<Skydivers>(2);
        skydivers.add(instructor);
        skydivers.add(trainee);
        return skydivers;
    }

    public Instructors getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructors instructor) {
        this.instructor = instructor;
    }

    public Skydivers getTrainee() {
        return trainee;
    }

    public void setTrainee(Skydivers trainee) {
        this.trainee = trainee;
    }

    public TrainingJumps(String id, String type, int numJumpers, Skydivers trainee) {
        super(id, type, numJumpers);
        this.trainee = trainee;
    }

    public TrainingJumps(String id, String type, int numJumpers, Flights flight, Skydivers trainee) {
        super(id, type, numJumpers, flight);
        this.trainee = trainee;
    }

    @Override
    public String toString() {
        return super.toString() +
            ", instructor: " + instructor.getName() +
            ", trainee: " + trainee.getName();
    }

    @Override
    public void setTeacher(Instructors instructor) {
        this.instructor = instructor;
    }

}