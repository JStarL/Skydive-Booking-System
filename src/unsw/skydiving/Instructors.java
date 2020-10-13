package unsw.skydiving;

class Instructors extends LicensedJumpers {
    private Dropzones homeDropzone;

    public Instructors(String name, String license, Dropzones homeDropzone) {
        super(name, license);
        this.homeDropzone = homeDropzone;
    }

    @Override
    public String toString() {
        return super.toString() + ", dropzone: " + homeDropzone;
    }

}