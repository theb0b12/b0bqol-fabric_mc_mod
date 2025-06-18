package b0b.b0bqol.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.BlockItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class AutoTopSlab {
    private static final Logger LOGGER = LoggerFactory.getLogger("b0bqol-slab");
    private static final Set<BlockPos> pendingSlabs = new HashSet<>();
    private static ServerWorld pendingWorld = null;
    
    public static void initialize() {
        registerSlabPlacementEvent();
        registerSlabTickHandler();
        LOGGER.info("Slab feature initialized - Auto top slab placement");
    }
    
    private static void registerSlabPlacementEvent() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // Only run on server side
            if (world.isClient) return ActionResult.PASS;
            
            // Check if holding a slab item
            if (player.getStackInHand(hand).getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof SlabBlock) {
                    BlockPos placePos = hitResult.getBlockPos().offset(hitResult.getSide());
                    
                    // Store the position to check later for slab adjustment
                    pendingSlabs.add(placePos);
                    pendingWorld = (ServerWorld) world;
                    
                    LOGGER.debug("Slab placement detected at {}", placePos);
                    return ActionResult.PASS;
                }
            }

            return ActionResult.PASS;
        });
    }
    
    private static void registerSlabTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!pendingSlabs.isEmpty() && pendingWorld != null) {
                Set<BlockPos> toRemove = new HashSet<>();
                
                for (BlockPos pos : pendingSlabs) {
                    var blockState = pendingWorld.getBlockState(pos);
                    // Check if slab was actually placed
                    if (blockState.getBlock() instanceof SlabBlock) {
                        // Check if it's a bottom slab (default placement)
                        if (blockState.get(SlabBlock.TYPE) == SlabType.BOTTOM) {
                            // Change it to a top slab
                            pendingWorld.setBlockState(pos,
                                    blockState.with(SlabBlock.TYPE, SlabType.TOP),
                                    3);
                            
                            LOGGER.debug("Adjusted slab at {} to top slab", pos);
                        }
                        toRemove.add(pos);
                    } else {
                        // If no slab found, give up
                        toRemove.add(pos);
                    }
                }
                
                // Clean up processed positions
                pendingSlabs.removeAll(toRemove);
                if (pendingSlabs.isEmpty()) {
                    pendingWorld = null;
                }
            }
        });
    }
}