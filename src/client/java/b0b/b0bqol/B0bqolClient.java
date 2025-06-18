package b0b.b0bqol;

import b0b.b0bqol.gui.KeyBindingHandler;
import b0b.b0bqol.features.AutoDownwardHopper;
import b0b.b0bqol.features.AutoTopSlab;

import net.fabricmc.api.ClientModInitializer;



public class B0bqolClient implements ClientModInitializer{

		@Override
		public void onInitializeClient() {
			// This entrypoint is suitable for setting up client-
			KeyBindingHandler.initialize();
			AutoDownwardHopper.initialize();
			AutoTopSlab.initialize();

		}
}