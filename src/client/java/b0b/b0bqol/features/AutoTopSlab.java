package b0b.b0bqol.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b0b.b0bqol.gui.ConfigScreen;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class AutoTopSlab extends Feature {
    private static final Logger LOGGER = LoggerFactory.getLogger("b0bqol-auto-top-slab");

    // Static field that the mixin can access to get the modified hit result
    public static BlockHitResult modifiedHitResult = null;

    @Override
    public void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        // Reset any previous modification
        modifiedHitResult = null;

        // Early return if feature disabled
        if (!ConfigScreen.AutoTopSlabEnabled) {
            return;
        }

        // Check if holding a slab item
        if (!isHoldingSlab(player, hand)) {
            return;
        }

        try {
            // Create modified hit result that forces top slab placement
            modifiedHitResult = createTopSlabHitResult(hitResult);

            LOGGER.debug("Auto top slab: Modified hit result for position {}", hitResult.getBlockPos());

        } catch (Exception e) {
            LOGGER.error("Error in auto top slab modification", e);
            modifiedHitResult = null;
        }
    }

    /**
     * Check if the player is holding a slab item
     */
    private boolean isHoldingSlab(ClientPlayerEntity player, Hand hand) {
        return player.getStackInHand(hand).getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof SlabBlock;
    }

    /**
     * Create a modified hit result that forces top slab placement
     */
    private BlockHitResult createTopSlabHitResult(BlockHitResult originalHitResult) {
        BlockPos targetPos = originalHitResult.getBlockPos();
        Direction side = originalHitResult.getSide();

        // Calculate the target position based on the face clicked
        BlockPos finalPos = calculateTargetPosition(targetPos, side);

        // Create a hit position that ensures top slab placement
        // We use Y coordinate > 0.5 to force top slab placement
        Vec3d topSlabHitPos = new Vec3d(
                finalPos.getX() + 0.5,  // Center X
                finalPos.getY() + 0.75, // High Y (> 0.5) to ensure top placement
                finalPos.getZ() + 0.5   // Center Z
        );

        return new BlockHitResult(
                topSlabHitPos,
                Direction.UP,  // Always use UP direction for consistent top placement
                finalPos,
                false
        );
    }

    /**
     * Calculate the target position based on which face was clicked
     */
    private BlockPos calculateTargetPosition(BlockPos clickedPos, Direction clickedSide) {
        return switch (clickedSide) {
            case UP -> clickedPos.up();      // Place above the clicked block
            case DOWN -> clickedPos;         // Place on the clicked block (will be top slab due to hit position)
            case NORTH, SOUTH, EAST, WEST -> clickedPos.offset(clickedSide); // Place adjacent
        };
    }

    /**
     * Static utility method to check if auto top slab should be applied
     */
    public static boolean shouldApplyAutoTopSlab(ClientPlayerEntity player, Hand hand) {
        return ConfigScreen.AutoTopSlabEnabled &&
                player.getStackInHand(hand).getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof SlabBlock;
    }

    /**
     * Get the current feature state
     */
    public static boolean isEnabled() {
        return ConfigScreen.AutoTopSlabEnabled;
    }
}