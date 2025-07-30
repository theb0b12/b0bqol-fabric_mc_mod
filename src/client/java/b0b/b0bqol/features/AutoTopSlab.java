package b0b.b0bqol.features;

import net.minecraft.util.math.Direction;

public class AutoTopSlab extends ClientFeature {

    public AutoTopSlab() {
        super("Auto Top Slab");
    }

    public Direction getModifiedDirection() { return Direction.DOWN; }

}