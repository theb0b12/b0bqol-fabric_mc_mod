package b0b.b0bqol.features;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

public class AutoDownwardHopper extends ClientFeature {

    public AutoDownwardHopper() {
        super("Auto Downwards Hopper");
    }

    public Direction getModifiedDirection() { return Direction.UP; }
    public boolean shouldModify(ClientPlayerEntity player) { return player.getMainHandStack().isOf(Items.HOPPER) && this.getState(); }

}