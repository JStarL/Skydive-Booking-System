package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

public class Dropzones {
    
    /**
     * Dropzone name
     */
    private String name;

    /**
     * Total vacancies in this dropzone
     */
    private int vacancies;

    /**
     * List of instructors in this dropzone
     */
    ArrayList<Instructors> instructors;

    /**
     * Standard constructor
     * @param name name of dropzone
     */
    Dropzones(String name) {
        this.name = name;
        vacancies = 0;
        instructors = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public int getVacancies() {
        return vacancies;
    }

    /**
     * Increase by given number of vacancies
     * @param x
     */
    public void addVacancies(int x) {
        vacancies += x;
    }

    /**
     * Decrease by given number of vacancies
     * @param x
     */
    public void subtractVacancies(int x) {
        vacancies -= x;
    }

    /**
     * Add given teacher to instructors list
     * @param teacher instructor to be added
     */
    public void addInstructor(Instructors teacher) {
        if (teacher == null) {return;}
        instructors.add(teacher);
    }

    /**
     * Allocates a teacher during the given TimeInterval for
     * the given jump type
     * @param jumpType either "training" or "tandem"
     * @param t TimeInterval to be allocated for
     * @param student The teacher should not be this student!
     * @return Allocated Instructor
     */
    public Instructors allocateTeacher(String jumpType, TimeInterval t, Skydivers student) {
        // Sorting such that instructors with fewer jumps preferred
        Collections.sort(instructors);

        switch (jumpType) {
            case "training":
                return allocateInstructor(t, student);

            case "tandem":
                return allocateTandemMaster(t, student);

        }

        return null;
    }

    /**
     * Allocates instructor for the given TimeInterval
     * @param t
     * @param student The teacher should not be this student!
     * @return allocated instructor
     */
    private Instructors allocateInstructor(TimeInterval t, Skydivers student) {
        for (Instructors instructor : instructors) {
            if (instructor.isSkydiverFree(t) &&
                !instructor.getName().equals(student.getName())) {
                return instructor;
            }
        }

        return null;
    }

    /**
     * Allocates tandemMaster for the given TimeInterval
     * @param t
     * @param student The teacher should not be this student!
     * @return allocated tandemMaster
     */
    private Instructors allocateTandemMaster(TimeInterval t, Skydivers student) {
        for (Instructors instructor : instructors) {
            if (instructor.getClass() != TandemMasters.class) {
                continue;
            }

            if (instructor.isSkydiverFree(t) &&
                !instructor.getName().equals(student.getName())) {
                return instructor;
            }
        }

        return null;
    }

    /**
     * A String representation of this dropzone.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return "name: " + name;
    }
    
}