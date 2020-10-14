package unsw.skydiving;

class Instructors extends LicensedJumpers {
    
    /**
     * This is the home dropzone of this instructor
     */
    private Dropzones homeDropzone;

    /**
     * Standard Constructor
     * @param name
     * @param license
     * @param homeDropzone a dropzone, the home dropzone
     */
    public Instructors(String name, String license, Dropzones homeDropzone) {
        super(name, license);
        this.homeDropzone = homeDropzone;
    }

    /**
     * A String representation of this instructor.
     * Easy for debugging prints :)
     */
    @Override
    public String toString() {
        return super.toString() + ", dropzone: " + homeDropzone;
    }

}