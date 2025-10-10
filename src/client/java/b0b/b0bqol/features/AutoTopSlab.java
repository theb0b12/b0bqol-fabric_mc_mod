package b0b.b0bqol.features;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

public class AutoTopSlab extends ClientFeature {

    public AutoTopSlab() {
        super("Auto Top Slab");
    }

    public Direction getModifiedDirection() { return Direction.DOWN; }
    public boolean shouldModify(ClientPlayerEntity player) { return player.getMainHandStack().isOf(Items.HOPPER) && this.getState(); }

}