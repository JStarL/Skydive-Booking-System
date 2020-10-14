package unsw.skydiving;

import java.util.ArrayList;

import org.json.JSONObject;

class TrainingJumps extends Jumps {
    
    /**
     * The instructor of this training jump
     */
    private Instructors instructor;
    
    /**
     * The student of this training jump
     */
    private Skydivers trainee;

    /**
     * Constructor when flight and instructor unknown
     * @param id
     * @param type
     * @param numJumpers
     * @param trainee
     */
    public TrainingJumps(String id, String type, int numJumpers, Skydivers trainee) {
        super(id, type, numJumpers);
        this.trainee = trainee;
    }

    /**
     * Constructor when flight unknown
     * @param id
     * @param type
     * @param numJumpers
     * @param flight
     * @param trainee
     */
    public TrainingJumps(String id, String type, int numJumpers, Flights flight, Skydivers trainee) {
        super(id, type, numJumpers, flight);
        this.trainee = trainee;
    }

    // Getters and Setters

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


    @Override
    public JSONObject generateJumpRun() {
        JSONObject output = new JSONObject();
        output.put("instructor", instructor.getName());
        output.put("trainee", trainee.getName());
        return output;
    }

    @Override
    public void adjustTimeInterval(TimeInterval t) {
        t.adjustEndTime(15); // 15 minutes debriefing for training
    }

    @Override
    public ArrayList<Skydivers> getSkydivers() {
        ArrayList<Skydivers> skydivers = new ArrayList<Skydivers>(2);
        if (instructor != null) {
            skydivers.add(instructor);
        }
        if (trainee != null) {
            skydivers.add(trainee);
        }
        return skydivers;
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