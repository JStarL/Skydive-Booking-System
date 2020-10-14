package unsw.skydiving;

import java.util.ArrayList;
import java.util.Collections;

public class Dropzones {
    private String name;
    private int vacancies;
    ArrayList<Instructors> instructors;

    Dropzones(String name) {
        this.name = name;
        vacancies = 0;
        instructors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void addVacancies(int x) {
        vacancies += x;
    }

    public void subtractVacancies(int x) {
        vacancies -= x;
    }

    public void addInstructor(Instructors teacher) {
        if (teacher == null) {return;}
        instructors.add(teacher);
    }

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

    public Instructors allocateInstructor(TimeInterval t, Skydivers student) {
        for (Instructors instructor : instructors) {
            if (instructor.isSkydiverFree(t) &&
                !instructor.getName().equals(student.getName())) {
                return instructor;
            }
        }

        return null;
    }

    public Instructors allocateTandemMaster(TimeInterval t, Skydivers student) {
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

    @Override
    public String toString() {
        return "name: " + name;
    }
    
}