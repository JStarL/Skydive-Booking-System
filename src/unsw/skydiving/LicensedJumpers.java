package unsw.skydiving;

class LicensedJumpers extends Skydivers {
    
    /**
     * Standard Constructor
     * @param name
     * @param license
     */
    public LicensedJumpers(String name, String license) {
        super(name, license);
    }
    
    /**
     * Adjusts given TimeInterval based on the fact that
     * all licensed jumpers repack their gear -
     * and take 10 minutes at the end to do so
     */
    @Override
    public void adjustTimeInterval(TimeInterval t) {
        t.adjustEndTime(10); // 10 minutes repacking
    }

}