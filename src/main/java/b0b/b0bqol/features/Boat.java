package b0b.b0bqol.features;

import net.minecraft.block.Blocks;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Boat extends MainFeature {

    public void handleBoatCommand(ServerPlayerEntity player, String[] args) {
        ServerWorld world = player.getServerWorld();

        if (world == null) {
            return;
        }

        // Check if this is the "bb" (boat on minecarts) command
        if (args.length > 0 && args[0].equalsIgnoreCase("bb")) {
            spawnBoatOnMinecarts(player, world);
        }
    }

    private void spawnBoatOnMinecarts(ServerPlayerEntity player, ServerWorld world) {
        if (player == null || world == null) {
            return;
        }

        // Get player's facing direction
        Direction facing = player.getHorizontalFacing();

        // Calculate spawn position 1 block away in facing direction
        BlockPos playerPos = player.getBlockPos();
        BlockPos spawnPos = playerPos.down(3).offset(facing);

        // Place support block at ground level
        world.setBlockState(spawnPos, Blocks.RED_STAINED_GLASS.getDefaultState());

        // Spawn first minecart on the block
        MinecartEntity minecart1 = new MinecartEntity(EntityType.FURNACE_MINECART, world);
        minecart1.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5);
        world.spawnEntity(minecart1);

        // Spawn second minecart stacked on top of the first
        MinecartEntity minecart2 = new MinecartEntity(EntityType.FURNACE_MINECART, world);
        minecart2.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1+0.6999999881, spawnPos.getZ() + 0.5);
        world.spawnEntity(minecart2);

        // Spawn boat on top of the minecarts
        BoatEntity boat = new BoatEntity(EntityType.OAK_BOAT, world, () -> Items.OAK_BOAT);
        boat.setPosition(spawnPos.getX() + 0.5, spawnPos.getY() + 1+0.6999999881+0.6999999881, spawnPos.getZ() + 0.5);
        world.spawnEntity(boat);

    }

    // You can also add a method to list available boat types
    public void list(ServerPlayerEntity player) {
        if (player != null) {
            player.sendMessage(Text.literal("§6Available boat types:"), false);
            player.sendMessage(Text.literal("§7bb - spawns normal BOAT on stack Furnace MCs"), false);
        }
    }
}