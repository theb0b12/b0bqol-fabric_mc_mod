package b0b.b0bqol.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private ButtonWidget AutoDownwardHopperButton;
    private ButtonWidget AutoTopSlabButton;
    private ButtonWidget hotkeyButton;
    private ButtonWidget doneButton;

    // Config values - you might want to move these to a separate config class
    public static boolean AutoDownwardHopperEnabled = true;
    public static boolean AutoTopSlabEnabled = true;
    private boolean waitingForKeyInput = false;

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

        // AutoDownwardHopper toggle
        AutoDownwardHopperButton = ButtonWidget.builder(
                        Text.literal("Auto Downward Hopper: " + (AutoDownwardHopperEnabled ? "ON" : "OFF"))
                                .formatted(AutoDownwardHopperEnabled ? Formatting.GREEN : Formatting.RED),
                        button -> {
                            AutoDownwardHopperEnabled = !AutoDownwardHopperEnabled;
                            updateButtonTexts();
                            // Save config here if you have a config system
                        })
                .dimensions(centerX - buttonWidth/2, startY, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(AutoDownwardHopperButton);

        // AutoTopSlab 2 toggle
        AutoTopSlabButton = ButtonWidget.builder(
                        Text.literal("Auto Top Slab: " + (AutoTopSlabEnabled ? "ON" : "OFF"))
                                .formatted(AutoTopSlabEnabled ? Formatting.GREEN : Formatting.RED),
                        button -> {
                            AutoTopSlabEnabled = !AutoTopSlabEnabled;
                            updateButtonTexts();
                            // Save config here if you have a config system
                        })
                .dimensions(centerX - buttonWidth/2, startY + spacing, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(AutoTopSlabButton);

        // Hotkey change button
        hotkeyButton = ButtonWidget.builder(
                        Text.literal("Config Hotkey: " + KeyBindingHandler.getConfigKeyBinding().getBoundKeyLocalizedText().getString()),
                        button -> {
                            if (!waitingForKeyInput) {
                                waitingForKeyInput = true;
                                updateButtonTexts();
                            }
                        })
                .dimensions(centerX - buttonWidth/2, startY + spacing * 2, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(hotkeyButton);

        // Done button
        doneButton = ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> this.close())
                .dimensions(centerX - 50, startY + spacing * 4, 100, buttonHeight)
                .build();
        this.addDrawableChild(doneButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKeyInput) {
            // Don't allow escape key as hotkey
            if (keyCode != 256) { // 256 is escape key
                KeyBindingHandler.updateConfigKeyBinding(keyCode);
                waitingForKeyInput = false;
                updateButtonTexts();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateButtonTexts() {
        if (AutoDownwardHopperButton != null) {
            AutoDownwardHopperButton.setMessage(
                    Text.literal("Auto Downward Hopper: " + (AutoDownwardHopperEnabled ? "ON" : "OFF"))
                            .formatted(AutoDownwardHopperEnabled ? Formatting.GREEN : Formatting.RED)
            );
        }

        if (AutoTopSlabButton != null) {
            AutoTopSlabButton.setMessage(
                    Text.literal("Auto Top Slab: " + (AutoTopSlabEnabled ? "ON" : "OFF"))
                            .formatted(AutoTopSlabEnabled ? Formatting.GREEN : Formatting.RED)
            );
        }

        if (hotkeyButton != null) {
            if (waitingForKeyInput) {
                hotkeyButton.setMessage(Text.literal("Press any key...").formatted(Formatting.YELLOW));
            } else {
                hotkeyButton.setMessage(
                        Text.literal("Config Hotkey: " + KeyBindingHandler.getConfigKeyBinding().getBoundKeyLocalizedText().getString())
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