Entities:

- Booking System
    - Dropzones
    - Flights
    - Skydivers
    - Jumps

- Skydivers
    - 4 types:
        - Student
        - Licenced-Jumper
        - Instructor
        - Tandem-Master

- Dropzones

- Flights
    - Id UNIQUE
    - Start and End time
    - Max. Skydiver Load

- Jumps
    - 3 types:
        - Tandem - 2 Passengers
            - 5 minute briefing before flight
            - Tandem Master: Repacks gear
            - Passenger: 
                - Attached to above
                - has no gear of own
                - Doesn't repack
        - Fun-Jump
        - Training - 2 passengers
            - 15 minute debriefing after flight
            - Instructor: Debriefs, then repacks gear
            - Student: Skydiver of whatever classification, debriefs, then repacks depending on classification
    - Num. Skydivers

- JSON Parser

Relationships:
- Skydivers - Action - Jumps?
- Flights - Provide actual support for - Jumps
- Tandem-Master - is-a - Instructor = inheritance
- Instructors (& Tandem-Masters) - is-a - Licensed-Jumper = inheritance (multi-level)
- All Jumpers - is-a - Student = Inheritance / Interface
Unknown:


- Gear and repacking?

- Qualifications? Skydiving Licensing?

Actions:
- Jumps: Register, Update, Cancel

Info:
- Parachutes require *10 minutes* to repack
- Overall sequence:
    - (Briefing) Jump (Debriefing) (Repacking)
- System needs some way of recording that...
    - Skydiver X is booked for ... intervals

Qs:
- Under what *circumstances* is a skydiver a student?
- Under what *conditions* does a skydiver **not** have to repack?
- What are the *properties* of a student?
- For student, superclass or interface?
- How does the booking system allocate: skydiver -> flight?
    - Via "time"
    - Extras like, max. capacity...