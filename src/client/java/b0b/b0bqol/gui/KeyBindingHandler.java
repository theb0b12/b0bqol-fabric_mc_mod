package b0b.b0bqol.gui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {

    private static KeyBinding configKeyBinding;
    private static KeyBinding entityConfigKeyBinding;

    public static void initialize() {
        // Register the key binding
        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.b0bqol.config", // Translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F12, // F12 key
                "category.b0bqol.general" // Category translation key
        ));
        entityConfigKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.b0bqol.entity_config", // Translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F10, // Default key: F10
                "category.b0bqol.general" // Category
        ));

        // Register tick event to check for key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyBinding.wasPressed()) {
                MinecraftClient mc = MinecraftClient.getInstance();

                // Toggle: if CustomScreen is open, close it; otherwise open it
                if (client.currentScreen instanceof ConfigScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new ConfigScreen(Text.empty()));
                }
            }
            while (entityConfigKeyBinding.wasPressed()) {
                if (client.currentScreen instanceof EntityInfoScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new EntityInfoScreen(Text.literal("Entity Configuration")));
                }
            }
        });
    }
    public static KeyBinding getConfigKeyBinding() {
        return configKeyBinding;
    }
    public static KeyBinding getEntityConfigKeyBinding() {return entityConfigKeyBinding;}

    public static void updateConfigKeyBinding(int newKeyCode) {
        configKeyBinding.setBoundKey(InputUtil.fromKeyCode(newKeyCode, 0));
        KeyBinding.updateKeysByCode();
    }
    public static void updateEntityConfigKeyBinding(int keyCode) {
        entityConfigKeyBinding.setBoundKey(InputUtil.fromKeyCode(keyCode, 0));
        KeyBinding.updateKeysByCode();
    }
}