package b0b.b0bqol;

import b0b.b0bqol.features.Feature;
import b0b.b0bqol.gui.KeyBindingHandler;
import b0b.b0bqol.features.AutoDownwardHopper;
import b0b.b0bqol.features.AutoTopSlab;

import net.fabricmc.api.ClientModInitializer;

import java.util.ArrayList;
import java.util.List;



public class B0bqolClient implements ClientModInitializer{
	public static List<Feature> featureList = new ArrayList<>();
		@Override
		public void onInitializeClient() {
			// This entrypoint is suitable for setting up client-
			KeyBindingHandler.initialize();
			featureList.add(new AutoDownwardHopper());
			featureList.add(new AutoTopSlab());

		}
}