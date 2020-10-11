package unsw.skydiving;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

public class Flights {

    private String id;
    private String dropzone; // TODO: May change to Dropzone obj ref
    private int maxload;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;


    Flights (String id, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.startDate= start.toLocalDate();
        this.startTime= start.toLocalTime();
        this.endDate= end.toLocalDate();
        this.endTime= end.toLocalTime();

    }

    ArrayList<Flights> getFlightsOnDate (LocalDate date) {
        return null;
    }
}