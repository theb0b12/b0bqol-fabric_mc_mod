package b0b.b0bqol.features;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlacementHandler {

    public static ActionResult handlePlaceBlock(
            ClientPlayerInteractionManager instance,
            ClientPlayerEntity player,
            Hand hand,
            BlockHitResult hitResult) {
        BlockPos posIn = hitResult.getBlockPos();
        Vec3d hitVec = hitResult.getPos();

        return tryPlaceBlock(instance, player, posIn, hitResult.getSide(), hitVec, hand);
    }

    private static ActionResult tryPlaceBlock(
            ClientPlayerInteractionManager controller,
            ClientPlayerEntity player,
            BlockPos posIn,
            Direction sideIn,
            Vec3d hitVec,
            Hand hand) {
        boolean flexible = Features.AUTO_DOWNWARD_HOPPER.getState() || Features.AUTO_TOP_SLAB.getState();

        if (flexible) {
            return handleFlexibleBlockPlacement(controller, player, posIn, sideIn, hitVec, hand);
        } else {
            return processRightClickBlockWrapper(controller, player, posIn, sideIn, hitVec, hand);
        }
    }

    private static ActionResult handleFlexibleBlockPlacement(
            ClientPlayerInteractionManager controller,
            ClientPlayerEntity player,
            BlockPos pos,
            Direction side,
            Vec3d hitVec,
            Hand hand) {
        // You should probably handle these inside of AutoDownwardHopper.java and AutoTopSlab.java
        BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
        ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hitResult));
        BlockPos newPos = getPlacementPositionForTargetedPosition(player.clientWorld, pos,
                side, ctx);

        if (player.getMainHandStack().isOf(Items.HOPPER) && Features.AUTO_DOWNWARD_HOPPER.getState())
            return processRightClickBlockWrapper(controller, player, newPos,
                    Features.AUTO_DOWNWARD_HOPPER.getModifiedDirection(), hitVec, hand);

        if (player.getMainHandStack().isIn(ItemTags.SLABS) && Features.AUTO_TOP_SLAB.getState())
            return processRightClickBlockWrapper(controller, player, newPos,
                    Features.AUTO_TOP_SLAB.getModifiedDirection(), hitVec, hand);

        return processRightClickBlockWrapper(controller, player, pos, side, hitVec, hand);
    }

    private static ActionResult processRightClickBlockWrapper(
            ClientPlayerInteractionManager controller,
            ClientPlayerEntity player,
            BlockPos posIn,
            Direction sideIn,
            Vec3d hitVecIn,
            Hand hand) {
        BlockHitResult context = new BlockHitResult(hitVecIn, sideIn, posIn, false);
        return controller.interactBlock(player, hand, context);
    }

    private static boolean canPlaceBlockIntoPosition(World world,
                                                     BlockPos pos,
                                                     ItemPlacementContext useContext) {
        BlockState state = world.getBlockState(pos);
        return state.canReplace(useContext) || state.isLiquid() || state.isReplaceable();
    }

    private static BlockPos getPlacementPositionForTargetedPosition(World world,
                                                                    BlockPos pos,
                                                                    Direction side,
                                                                    ItemPlacementContext useContext) {
        if (canPlaceBlockIntoPosition(world, pos, useContext))
        {
            return pos;
        }

        return pos.offset(side);
    }

}
