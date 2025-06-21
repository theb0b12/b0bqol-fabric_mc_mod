package b0b.b0bqol.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;
import java.util.stream.Collectors;

public class EntityInfoScreen extends Screen {

    private final Screen parent;
    private ButtonWidget hotkeyButton;
    private ButtonWidget doneButton;
    private TextFieldWidget searchField;
    private boolean waitingForKeyInput = false;
    private int scrollOffset = 0;
    private final int maxVisibleEntries = 12;
    private String searchText = "";

    // Entity data structure
    public static class EntityData {
        public final String name;
        public final double height;
        public final double sittingHeight;
        public boolean enabled;

        public EntityData(String name, double height, double sittingHeight) {
            this.name = name;
            this.height = height;
            this.sittingHeight = sittingHeight;
            this.enabled = true;
        }
    }

    // Entity configuration map
    public static final Map<String, EntityData> ENTITY_CONFIG = new LinkedHashMap<>();

    static {
        // Initialize with your table data

        ENTITY_CONFIG.put("baby_turtle", new EntityData("baby turtle", 0.3075000047683716, 0.1875));
        ENTITY_CONFIG.put("baby_axolotl", new EntityData("baby axolotl", 0.3974999934434891, 0.1875));
        ENTITY_CONFIG.put("baby_rabbit", new EntityData("baby rabbit", 0.4375, 0.1875));
        ENTITY_CONFIG.put("cod", new EntityData("cod", 0.48750001192092896, 0.1875));
        ENTITY_CONFIG.put("tadpole", new EntityData("tadpole", 0.48750001192092896, 0.1875));
        ENTITY_CONFIG.put("endermite", new EntityData("endermite", 0.48750001192092896, 0.1875));
        ENTITY_CONFIG.put("silverfish", new EntityData("silverfish", 0.48750001192092896, 0.1875));
        ENTITY_CONFIG.put("baby_bee", new EntityData("baby bee", 0.48750001192092896, 0.1875));
        ENTITY_CONFIG.put("small_pufferfish", new EntityData("small pufferfish", 0.5374999940395355, 0.1875));
        ENTITY_CONFIG.put("baby_cat", new EntityData("baby cat", 0.5374999940395355, 0.1875));
        ENTITY_CONFIG.put("baby_chicken", new EntityData("baby chicken", 0.5374999940395355, 0.1875));
        ENTITY_CONFIG.put("baby_fox", new EntityData("baby fox", 0.5374999940395355, 0.1875));
        ENTITY_CONFIG.put("baby_ocelot", new EntityData("baby ocelot", 0.5374999940395355, 0.1875));
        ENTITY_CONFIG.put("phantom", new EntityData("phantom", 0.5625, 0.0625));
        ENTITY_CONFIG.put("salmon", new EntityData("salmon", 0.5875000059604645, 0.1875));
        ENTITY_CONFIG.put("tropical_fish", new EntityData("tropical fish", 0.5875000059604645, 0.1875));
        // Add more entities as needed
    }

    public EntityInfoScreen(Text title) {
        this(null, title);
    }

    public EntityInfoScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int topY = 20;
        int buttonWidth = 200;
        int buttonHeight = 20;

        // Title
        this.addDrawableChild(new TextWidget(centerX - 75, topY, 150, 20,
                Text.literal("Entity Configuration").formatted(Formatting.WHITE), this.textRenderer));

        // Search field
        searchField = new TextFieldWidget(this.textRenderer, centerX - 100, topY + 30, 200, 20, Text.literal("Search"));
        searchField.setPlaceholder(Text.literal("Search entities..."));
        searchField.setChangedListener(text -> {
            searchText = text.toLowerCase();
            scrollOffset = 0;
        });
        this.addDrawableChild(searchField);

        // Hotkey change button
        hotkeyButton = ButtonWidget.builder(
                        Text.literal("Entity Config Hotkey: " + KeyBindingHandler.getEntityConfigKeyBinding().getBoundKeyLocalizedText().getString()),
                        button -> {
                            if (!waitingForKeyInput) {
                                waitingForKeyInput = true;
                                updateHotkeyButton();
                            }
                        })
                .dimensions(centerX - 100, this.height - 60, buttonWidth, buttonHeight)
                .build();
        this.addDrawableChild(hotkeyButton);

        // Done button
        doneButton = ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> this.close())
                .dimensions(centerX - 50, this.height - 35, 100, buttonHeight)
                .build();
        this.addDrawableChild(doneButton);
    }

    private List<EntityData> getFilteredEntities() {
        if (searchText.isEmpty()) {
            return new ArrayList<>(ENTITY_CONFIG.values());
        }

        return ENTITY_CONFIG.values().stream()
                .filter(entity -> {
                    // Search by name
                    if (entity.name.toLowerCase().contains(searchText)) {
                        return true;
                    }
                    // Search by height (convert to string for partial matching)
                    if (String.valueOf(entity.height).contains(searchText)) {
                        return true;
                    }
                    // Search by sitting height
                    if (String.valueOf(entity.sittingHeight).contains(searchText)) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKeyInput) {
            if (keyCode != 256) { // 256 is escape key
                KeyBindingHandler.updateEntityConfigKeyBinding(keyCode);
                waitingForKeyInput = false;
                updateHotkeyButton();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        List<EntityData> filtered = getFilteredEntities();
        int maxScroll = Math.max(0, filtered.size() - maxVisibleEntries);
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) verticalAmount));
        return true;
    }

    private void updateHotkeyButton() {
        if (hotkeyButton != null) {
            if (waitingForKeyInput) {
                hotkeyButton.setMessage(Text.literal("Press any key...").formatted(Formatting.YELLOW));
            } else {
                hotkeyButton.setMessage(
                        Text.literal("Entity Config Hotkey: " + KeyBindingHandler.getEntityConfigKeyBinding().getBoundKeyLocalizedText().getString())
                );
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        // Add solid background overlay
        context.fill(0, 0, this.width, this.height, 0xC0101010);
        super.render(context, mouseX, mouseY, delta);

        int startY = 100;
        int itemHeight = 18;

        renderEntityList(context, mouseX, mouseY, startY, itemHeight);

        // Instructions
        if (waitingForKeyInput) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Press ESC to cancel").formatted(Formatting.GRAY),
                    this.width / 2, this.height - 80, 0xFFFFFF);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Use scroll wheel to navigate • Search by name or height values").formatted(Formatting.GRAY),
                    this.width / 2, this.height - 80, 0xFFFFFF);
        }
    }

    private void renderEntityList(DrawContext context, int mouseX, int mouseY, int startY, int itemHeight) {
        List<EntityData> filtered = getFilteredEntities();

        // Header with background
        int headerY = startY - 20;
        context.fill(20, headerY - 2, this.width - 20, headerY + 16, 0x80000000);
        context.drawText(this.textRenderer, "Entity Name", 30, headerY + 4, 0xFFFFFF, true);
        context.drawText(this.textRenderer, "Height", this.width - 280, headerY + 4, 0xFFFFFF, true);
        context.drawText(this.textRenderer, "Sitting Height", this.width - 150, headerY + 4, 0xFFFFFF, true);

        // Entity entries
        for (int i = 0; i < Math.min(maxVisibleEntries, filtered.size() - scrollOffset); i++) {
            EntityData entity = filtered.get(scrollOffset + i);
            int y = startY + i * itemHeight;

            // Alternating row background
            if (i % 2 == 0) {
                context.fill(20, y - 1, this.width - 20, y + itemHeight - 1, 0x20FFFFFF);
            }

            // Highlight hovered row
            if (mouseY >= y - 1 && mouseY <= y + itemHeight - 1 && mouseX >= 20 && mouseX <= this.width - 20) {
                context.fill(20, y - 1, this.width - 20, y + itemHeight - 1, 0x40FFFFFF);
            }

            // Entity name
            context.drawText(this.textRenderer, entity.name, 30, y + 4, 0xFFFFFF, false);

            // Height
            context.drawText(this.textRenderer, String.valueOf(entity.height), this.width - 280, y + 4, 0xCCCCCC, false);

            // Sitting Height
            context.drawText(this.textRenderer, String.valueOf(entity.sittingHeight), this.width - 150, y + 4, 0xCCCCCC, false);
        }

        // Show result count
        String resultText = filtered.size() == ENTITY_CONFIG.size() ?
                String.format("Showing %d entities", filtered.size()) :
                String.format("Showing %d of %d entities", filtered.size(), ENTITY_CONFIG.size());

        context.drawText(this.textRenderer, resultText, 30, startY + maxVisibleEntries * itemHeight + 10, 0x888888, false);

        renderScrollBar(context, startY, itemHeight, filtered.size());
    }

    private void renderScrollBar(DrawContext context, int startY, int itemHeight, int totalItems) {
        if (totalItems > maxVisibleEntries) {
            int totalHeight = maxVisibleEntries * itemHeight;
            int scrollBarHeight = Math.max(20, totalHeight * maxVisibleEntries / totalItems);
            int scrollBarY = startY + (totalHeight - scrollBarHeight) * scrollOffset / (totalItems - maxVisibleEntries);

            // Scroll bar track
            context.fill(this.width - 15, startY, this.width - 10, startY + totalHeight, 0x40FFFFFF);
            // Scroll bar thumb
            context.fill(this.width - 15, scrollBarY, this.width - 10, scrollBarY + scrollBarHeight, 0x80FFFFFF);
        }
    }

    @Override
    public void close() {
        // Save configuration here if you have a config system
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}