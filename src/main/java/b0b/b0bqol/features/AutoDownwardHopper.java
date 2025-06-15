package b0b.b0bqol.features;

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

import java.util.HashSet;
import java.util.Set;

public class AutoDownwardHopper {
    private static final Logger LOGGER = LoggerFactory.getLogger("b0bqol-hopper");
    private static final Set<BlockPos> pendingHoppers = new HashSet<>();
    private static ServerWorld pendingWorld = null;
    
    public static void initialize() {
        registerHopperPlacementEvent();
        registerHopperTickHandler();
        LOGGER.info("Hopper feature initialized - Auto downward facing hoppers");
    }
    
    private static void registerHopperPlacementEvent() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // Only run on server side
            if (world.isClient) return ActionResult.PASS;

            // Check if holding a hopper
            if (player.getStackInHand(hand).getItem() == Items.HOPPER) {
                BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
                
                // Store the position to check later
                pendingHoppers.add(placePos);
                pendingWorld = (ServerWorld) world;
                
                LOGGER.debug("Hopper placement detected at {}", placePos);
                return ActionResult.PASS;
            }

            return ActionResult.PASS;
        });
    }
    
    private static void registerHopperTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!pendingHoppers.isEmpty() && pendingWorld != null) {
                Set<BlockPos> toRemove = new HashSet<>();
                
                for (BlockPos pos : pendingHoppers) {
                    // Check if hopper was actually placed
                    if (pendingWorld.getBlockState(pos).getBlock() == Blocks.HOPPER) {
                        // Set the hopper to face down
                        pendingWorld.setBlockState(pos,
                                Blocks.HOPPER.getDefaultState()
                                        .with(HopperBlock.FACING, Direction.DOWN),
                                3);
                        
                        LOGGER.debug("Adjusted hopper at {} to face down", pos);
                        toRemove.add(pos);
                    } else {
                        // If no hopper found, give up
                        toRemove.add(pos);
                    }
                }
                
                // Clean up processed positions
                pendingHoppers.removeAll(toRemove);
                if (pendingHoppers.isEmpty()) {
                    pendingWorld = null;
                }
            }
        });
    }
}