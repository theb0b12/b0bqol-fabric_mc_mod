package b0b.b0bqol.gui;

import b0b.b0bqol.features.ClientFeature;
import b0b.b0bqol.features.Features;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class ConfigScreen extends Screen {

    private final Screen parent;

    private ButtonWidget configHotkeyButton;
    private ButtonWidget entityInfoScreenButton;

    private final HashMap<ClientFeature, ButtonWidget> booleanButtons = new HashMap<>();

    // Track which button is waiting for input
    private boolean waitingForKeyInput = false;
    private ButtonType waitingButtonType = ButtonType.NONE;

    private enum ButtonType {
        NONE,
        CONFIG_HOTKEY,
        ENTITY_INFO_HOTKEY
    }

    public ConfigScreen(Text title) {
        this(null, title);
    }

    public ConfigScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 4;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;

        // Title
        this.addDrawableChild(new TextWidget(centerX - 50, startY - 30, 100, 20,
                Text.literal("Mod Configuration").formatted(Formatting.WHITE), this.textRenderer));

        for (int i = 0; i < Features.features.size(); i++) {
            var f = Features.features.get(i);
            var button = ButtonWidget.builder(
                    Text.literal(f.name + ": " + (f.getState() ? "ON" : "OFF"))
                            .formatted(f.getState() ? Formatting.GREEN : Formatting.RED),
                    onPress -> {
                        f.setState(!f.getState());
                        updateButtons();
                    }
            ).dimensions(centerX - buttonWidth / 2, startY + (i * spacing), buttonWidth, buttonHeight).build();
            this.addDrawableChild(button);
            booleanButtons.put(f, button);
        }

        // Hotkey change button
        configHotkeyButton = ButtonWidget.builder(
                        Text.literal("Config Hotkey: " + KeyBindingHandler.getConfigKeyBinding().getBoundKeyLocalizedText().getString()),
                        button -> {
                            if (!waitingForKeyInput) {
                                waitingForKeyInput = true;
                                waitingButtonType = ButtonType.CONFIG_HOTKEY;
                                updateButtonTexts();
                            }
                        })
                .dimensions(centerX - buttonWidth/2, startY + spacing * 2, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(configHotkeyButton);

        entityInfoScreenButton = ButtonWidget.builder(
                        Text.literal("Entity Info Screen Hotkey: " +KeyBindingHandler.getEntityConfigKeyBinding().getBoundKeyLocalizedText().getString()),
                        button -> {
                            if (!waitingForKeyInput) {
                                waitingForKeyInput = true;
                                waitingButtonType = ButtonType.ENTITY_INFO_HOTKEY;
                                updateButtonTexts();
                            }
                        })
                .dimensions(centerX - buttonWidth/2, startY + spacing * 3, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(entityInfoScreenButton);

        // Done button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> this.close())
                .dimensions(centerX - 50, startY + spacing * 4, 100, buttonHeight)
                .build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKeyInput) {
            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
                switch (waitingButtonType) {
                    case CONFIG_HOTKEY:
                        KeyBindingHandler.updateConfigKeyBinding(keyCode);
                        break;
                    case ENTITY_INFO_HOTKEY:
                        KeyBindingHandler.updateEntityConfigKeyBinding(keyCode);
                        break;
                }

            }
            waitingForKeyInput = false;
            waitingButtonType = ButtonType.NONE;
            updateButtonTexts();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateButtons() {
        booleanButtons.forEach((f, b) ->
                b.setMessage(Text.literal(f.name + ": " + (f.getState() ? "ON" : "OFF"))
                .formatted(f.getState() ? Formatting.GREEN : Formatting.RED)));
    }

    private void updateButtonTexts() {
        if (configHotkeyButton != null) {
            if (waitingForKeyInput && waitingButtonType == ButtonType.CONFIG_HOTKEY) {
                configHotkeyButton.setMessage(Text.literal("Press any key...").formatted(Formatting.YELLOW));
            } else {
                configHotkeyButton.setMessage(
                        Text.literal("Config Hotkey: " + KeyBindingHandler.getConfigKeyBinding().getBoundKeyLocalizedText().getString())
                );
            }
        }

        if (entityInfoScreenButton != null) {
            if (waitingForKeyInput && waitingButtonType == ButtonType.ENTITY_INFO_HOTKEY) {
                entityInfoScreenButton.setMessage(Text.literal("Press any key...").formatted(Formatting.YELLOW));
            } else {
                entityInfoScreenButton.setMessage(
                        Text.literal("Entity Info Screen Hotkey: " + KeyBindingHandler.getEntityConfigKeyBinding().getBoundKeyLocalizedText().getString())
                );
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        // Instructions text
        if (waitingForKeyInput) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Press ESC to cancel").formatted(Formatting.GRAY),
                    this.width / 2, this.height - 30, 0xFFFFFF);
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false; // Don't pause the game when config is open
    }
}