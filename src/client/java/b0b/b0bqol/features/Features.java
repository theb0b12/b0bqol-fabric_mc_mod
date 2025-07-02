package b0b.b0bqol.features;

import java.util.ArrayList;

public class Features {

    public static final AutoDownwardHopper AUTO_DOWNWARD_HOPPER = new AutoDownwardHopper();
    public static final AutoTopSlab AUTO_TOP_SLAB = new AutoTopSlab();

    public static final ArrayList<Feature> features = new ArrayList<>();

    // Used if any feature needs to be initialized differently and adding every feature to a list
    public static void initialize() {
        features.add(AUTO_DOWNWARD_HOPPER);
        features.add(AUTO_TOP_SLAB);
    }

}
