package unsw.skydiving;

class LicensedJumpers extends Skydivers {
    
    public LicensedJumpers(String name, String license) {
        super(name, license);
    }
    
    @Override
    public void adjustTimeInterval(TimeInterval t) {
        t.adjustEndTime(10); // 10 minutes repacking
    }

    public static void adjustIntervalForLicensedJumpers(TimeInterval t) {
        t.adjustEndTime(10); // 10 minutes repacking
    }

    
}