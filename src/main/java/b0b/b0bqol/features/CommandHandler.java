package b0b.b0bqol.features;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CommandHandler {
    private static final Boat boatFeature = new Boat();

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("boat")
                .requires(source -> source.hasPermissionLevel(2) || source.getEntity() instanceof ServerPlayerEntity)
                .executes(context -> {
                    // Execute with no arguments (default boat)
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                    boatFeature.handleBoatCommand(player, new String[0]);
                    return 1;
                })
                .then(CommandManager.literal("bb")
                        .executes(context -> {
                            // Execute the boat on minecarts command
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            boatFeature.handleBoatCommand(player, new String[]{"bb"});
                            return 1;
                        })
                )
                .then(CommandManager.literal("list")
                        .executes(context -> {
                            // List available commands
                            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                            boatFeature.list(player);
                            return 1;
                        })
                )
        );
    }
}