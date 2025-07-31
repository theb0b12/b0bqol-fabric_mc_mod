package b0b.b0bqol;

import b0b.b0bqol.features.Boat;
import b0b.b0bqol.features.CommandHandler;
import b0b.b0bqol.features.MainFeature;
import b0b.b0bqol.features.MainFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.data.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class B0bqol implements ModInitializer {
	public static final String MOD_ID = "b0bqol";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Feature list to keep track of all loaded features
	private static final List<MainFeature> featureList = new ArrayList<>();




	public void onInitialize() {
		LOGGER.info("Initializing B0bqol mod for Minecraft 1.21.3...");

		// Initialize all features
		featureList.add(new Boat());

		// Register server commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandHandler.registerCommands(dispatcher);
		});

		LOGGER.info("B0bqol mod fully loaded! All features active.");
	}

	// Getter for feature list (useful for other parts of the mod)
	public static List<MainFeature> getFeatureList() {
		return featureList;
	}
}