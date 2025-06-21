package b0b.b0bqol.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b0b.b0bqol.gui.ConfigScreen;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AutoDownwardHopper extends Feature {
    private static final Logger LOGGER = LoggerFactory.getLogger("b0bqol-hopper");
    private boolean interacted = false;

    @Override
    public void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getStackInHand(hand).getItem() instanceof BlockItem blockItem && !interacted) {
            if (blockItem.getBlock() instanceof HopperBlock && ConfigScreen.AutoDownwardHopperEnabled) {
                if (hitResult.getSide() != Direction.DOWN) {
                    interacted = true;
                    MinecraftClient mc = MinecraftClient.getInstance();
                    Direction dir = Direction.DOWN;
                    if (hitResult.getSide() != Direction.UP) dir = hitResult.getSide();

                    BlockPos targetPos = hitResult.getBlockPos().add(BlockPos.ofFloored(dir.getDoubleVector().multiply(1,0,1))).add(0, dir == Direction.DOWN ? 1 : 0, 0);
                    BlockHitResult newHitResult = new BlockHitResult(
                            targetPos.toBottomCenterPos().add(0, 1, 0),
                            Direction.DOWN,
                            targetPos,
                            true
                    );

                    assert mc.interactionManager != null;
                    ActionResult result = mc.interactionManager.interactBlock(mc.player, hand, newHitResult);
                    cir.setReturnValue(result);
                }
            }
        }
        if (interacted) interacted = false;
    }
}