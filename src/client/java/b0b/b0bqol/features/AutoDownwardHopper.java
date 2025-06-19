package b0b.b0bqol.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import b0b.b0bqol.gui.ConfigScreen;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.HopperBlock;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

public class AutoDownwardHopper extends Feature{
    private static final Logger LOGGER = LoggerFactory.getLogger("b0bqol-hopper");
    private static final Set<BlockPos> pendingHoppers = new HashSet<>();
    private static ServerWorld pendingWorld = null;

    @Override
    public void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getStackInHand(hand).getItem() instanceof BlockItem blockItem && !interacted) {
            if (blockItem.getBlock() instanceof HopperBlock && ConfigScreen.AutoDownwardHopperEnabled) {
                if(hitResult.getSide() != Direction.DOWN) {
                    interacted = true;
                    MinecraftClient mc = MinecraftClient.getInstance();
                    Direction dir = Direction.DOWN;
                    if(hitResult.getSide() != Direction.UP) dir = hitResult.getSide();
                    assert mc.interactionManager != null;
                    mc.interactionManager.interactBlock(mc.player,Hand.MAIN_HAND,new BlockHitResult(hitResult.getBlockPos().toBottomCenterPos().add(dir.getDoubleVector().multiply(1,0,1)).add(0,1,0), Direction.DOWN, hitResult.getBlockPos().add(BlockPos.ofFloored(dir.getDoubleVector().multiply(1,0,1))).add(0,dir == Direction.DOWN ? 1 : 0,0),true));
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
        if(interacted) interacted = false;
    }
}