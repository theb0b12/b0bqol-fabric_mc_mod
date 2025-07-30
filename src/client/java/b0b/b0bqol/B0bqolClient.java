package b0b.b0bqol;

import b0b.b0bqol.features.ClientFeature;
import b0b.b0bqol.features.Features;
import b0b.b0bqol.gui.KeyBindingHandler;
import b0b.b0bqol.features.AutoDownwardHopper;
import b0b.b0bqol.features.AutoTopSlab;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class B0bqolClient implements ClientModInitializer {

	public static final String MOD_ID = "b0bqol";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Starting b0bqol mod!");

		KeyBindingHandler.initialize();
		Features.initialize();

		LOGGER.info("Loaded b0bqol mod!");
	}
}