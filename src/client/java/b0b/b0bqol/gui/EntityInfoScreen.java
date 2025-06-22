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

        ENTITY_CONFIG.put("baby_turtle", new EntityData("baby turtle", 0.3075000048, 0.1875));
        ENTITY_CONFIG.put("baby_axolotl", new EntityData("baby axolotl", 0.3974999934, 0.1875));
        ENTITY_CONFIG.put("baby_rabbit", new EntityData("baby rabbit", 0.4375, 0.1875));
        ENTITY_CONFIG.put("cod", new EntityData("cod", 0.4875000119, 0.1875));
        ENTITY_CONFIG.put("tadpole", new EntityData("tadpole", 0.4875000119, 0.1875));
        ENTITY_CONFIG.put("endermite", new EntityData("endermite", 0.4875000119, 0.1875));
        ENTITY_CONFIG.put("silverfish", new EntityData("silverfish", 0.4875000119, 0.1875));
        ENTITY_CONFIG.put("baby_bee", new EntityData("baby bee", 0.4875000119, 0.1875));
        ENTITY_CONFIG.put("small_pufferfish", new EntityData("small pufferfish", 0.537499994, 0.1875));
        ENTITY_CONFIG.put("baby_cat", new EntityData("baby cat", 0.537499994, 0.1875));
        ENTITY_CONFIG.put("baby_chicken", new EntityData("baby chicken", 0.537499994, 0.1875));
        ENTITY_CONFIG.put("baby_fox", new EntityData("baby fox", 0.537499994, 0.1875));
        ENTITY_CONFIG.put("baby_ocelot", new EntityData("baby ocelot", 0.537499994, 0.1875));
        ENTITY_CONFIG.put("phantom", new EntityData("phantom", 0.5625, 0.0625));
        ENTITY_CONFIG.put("salmon", new EntityData("salmon", 0.587500006, 0.1875));
        ENTITY_CONFIG.put("tropical_fish", new EntityData("tropical fish", 0.587500006, 0.1875));
        ENTITY_CONFIG.put("axolotl", new EntityData("axolotl", 0.6074999869, 0.1875));
        ENTITY_CONFIG.put("baby_wolf", new EntityData("baby wolf", 0.6125000119, 0.1875));
        ENTITY_CONFIG.put("baby_pig", new EntityData("baby pig", 0.6374999881, 0.1875));
        ENTITY_CONFIG.put("cave_spider", new EntityData("cave spider", 0.6875, 0.1875));
        ENTITY_CONFIG.put("frog", new EntityData("frog", 0.6875, 0.1875));
        ENTITY_CONFIG.put("rabbit", new EntityData("rabbit", 0.6875, 0.1875));
        ENTITY_CONFIG.put("minecart", new EntityData("minecart", 0.6999999881, 0));
        ENTITY_CONFIG.put("small_magma_cube", new EntityData("small magma cube", 0.7076999545, 0.1875));
        ENTITY_CONFIG.put("small_slime", new EntityData("small slime", 0.7076999545, 0.1875));
        ENTITY_CONFIG.put("dolphin", new EntityData("dolphin", 0.7875000238, 0.1875));
        ENTITY_CONFIG.put("bee", new EntityData("bee", 0.7875000238, 0.1875));
        ENTITY_CONFIG.put("baby_panda", new EntityData("baby panda", 0.8125, 0.1875));
        ENTITY_CONFIG.put("baby_zombified_piglin", new EntityData("baby zombified piglin", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("baby_piglin", new EntityData("baby piglin", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("baby_drowned", new EntityData("baby drowned", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("baby_husk", new EntityData("baby husk", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("baby_zombie_villager", new EntityData("baby zombie villager", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("baby_zombie", new EntityData("baby zombie", 0.8125000298, -0.162499994));
        ENTITY_CONFIG.put("allay", new EntityData("allay", 0.8275000229, 0.2274999991));
        ENTITY_CONFIG.put("baby_sheep", new EntityData("baby sheep", 0.8374999762, 0.1875));
        ENTITY_CONFIG.put("baby_goat", new EntityData("baby goat", 0.8374999762, 0.1875));
        ENTITY_CONFIG.put("armadillo", new EntityData("armadillo", 0.8374999762, 0.1875));
        ENTITY_CONFIG.put("baby_zoglin", new EntityData("baby zoglin", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("cat", new EntityData("cat", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("chicken", new EntityData("chicken", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("baby_cow", new EntityData("baby cow", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("fox", new EntityData("fox", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("baby_hoglin", new EntityData("baby hoglin", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("baby_mooshroom", new EntityData("baby mooshroom", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("ocelot", new EntityData("ocelot", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("baby_polar_bear", new EntityData("baby polar bear", 0.8874999881, 0.1875));
        ENTITY_CONFIG.put("baby_donkey", new EntityData("baby donkey", 0.9375, 0.1875));
        ENTITY_CONFIG.put("baby_villager", new EntityData("baby villager", 0.9750000238, 0));
        ENTITY_CONFIG.put("squid", new EntityData("squid", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("glow_squid", new EntityData("glow squid", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("baby_horse", new EntityData("baby horse", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("baby_mule", new EntityData("baby mule", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("baby_skeleton_horse", new EntityData("baby skeleton horse", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("baby_zombie_horse", new EntityData("baby zombie horse", 0.9875000119, 0.1875));
        ENTITY_CONFIG.put("vex", new EntityData("vex", 1.027500011, 0.2274999991));
        ENTITY_CONFIG.put("guardian", new EntityData("guardian", 1.037500024, 0.1875));
        ENTITY_CONFIG.put("baby_strider", new EntityData("baby strider", 1.037500024, 0.1875));
        ENTITY_CONFIG.put("wolf", new EntityData("wolf", 1.037500024, 0.1875));
        ENTITY_CONFIG.put("baby_sniffer", new EntityData("baby sniffer", 1.0625, 0.1875));
        ENTITY_CONFIG.put("parrot", new EntityData("parrot", 1.087499976, 0.1875));
        ENTITY_CONFIG.put("pig", new EntityData("pig", 1.087499976, 0.1875));
        ENTITY_CONFIG.put("baby_llama", new EntityData("baby llama", 1.122500002, 0.1875));
        ENTITY_CONFIG.put("shulker", new EntityData("shulker", 1.1875, 0.1875));
        ENTITY_CONFIG.put("baby_happy_ghast", new EntityData("baby happy ghast", 1.256249987, 0.3062499985));
        ENTITY_CONFIG.put("baby_camel", new EntityData("baby camel", 1.256250024, 0.1875));
        ENTITY_CONFIG.put("drowned", new EntityData("drowned", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("husk", new EntityData("husk", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("piglin", new EntityData("piglin", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("piglin_brute", new EntityData("piglin brute", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("zombie", new EntityData("zombie", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("zombie_villager", new EntityData("zombie villager", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("zombified_piglin", new EntityData("zombified piglin", 1.43750006, -0.5124999881));
        ENTITY_CONFIG.put("skeleton", new EntityData("skeleton", 1.477500021, -0.5124999881));
        ENTITY_CONFIG.put("stray", new EntityData("stray", 1.477500021, -0.5124999881));
        ENTITY_CONFIG.put("bogged", new EntityData("bogged", 1.477500021, -0.5124999881));
        ENTITY_CONFIG.put("sheep", new EntityData("sheep", 1.487499952, 0.1875));
        ENTITY_CONFIG.put("goat", new EntityData("goat", 1.487499952, 0.1875));
        ENTITY_CONFIG.put("baby_zombified_piglin_jockey", new EntityData("baby zombified piglin jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("baby_piglin_jockey", new EntityData("baby piglin jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("baby_drowned_jockey", new EntityData("baby drowned jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("baby_zombie_jockey", new EntityData("baby zombie jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("baby_zombie_villager_jockey", new EntityData("baby zombie villager jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("baby_husk_jockey", new EntityData("baby husk jockey", 1.512500018, 0.1875));
        ENTITY_CONFIG.put("evoker", new EntityData("evoker", 1.537500024, -0.4125000238));
        ENTITY_CONFIG.put("pillager", new EntityData("pillager", 1.537500024, -0.4125000238));
        ENTITY_CONFIG.put("vindicator", new EntityData("vindicator", 1.537500024, -0.4125000238));
        ENTITY_CONFIG.put("cow", new EntityData("cow", 1.587499976, 0.1875));
        ENTITY_CONFIG.put("mooshroom", new EntityData("mooshroom", 1.587499976, 0.1875));
        ENTITY_CONFIG.put("wither_skeleton", new EntityData("wither skeleton", 1.712500095, -0.6875));
        ENTITY_CONFIG.put("baby_villager_jockey", new EntityData("baby villager jockey", 1.862500012, 0.1875));
        ENTITY_CONFIG.put("creeper", new EntityData("creeper", 1.887500048, 0.1875));
        ENTITY_CONFIG.put("strider", new EntityData("strider", 1.887500048, 0.1875));
        ENTITY_CONFIG.put("breeze", new EntityData("breeze", 1.887500048, 0.1875));
        ENTITY_CONFIG.put("villager", new EntityData("villager", 1.950000048, 0));
        ENTITY_CONFIG.put("wandering_trader", new EntityData("wandering trader", 1.950000048, 0));
        ENTITY_CONFIG.put("blaze", new EntityData("blaze", 1.987499952, 0.1875));
        ENTITY_CONFIG.put("llama", new EntityData("llama", 2.057500005, 0.1875));
        ENTITY_CONFIG.put("trader_llama", new EntityData("trader llama", 2.057500005, 0.1875));
        ENTITY_CONFIG.put("snow_golem", new EntityData("snow golem", 2.087499976, 0.1875));
        ENTITY_CONFIG.put("witch", new EntityData("witch", 2.137500048, 0.1875));
        ENTITY_CONFIG.put("zombified_piglin_jockey", new EntityData("zombified piglin jockey", 2.137500048, 0.1875));
        ENTITY_CONFIG.put("piglin_jockey", new EntityData("piglin jockey", 2.137500048, 0.1875));
        ENTITY_CONFIG.put("zombie_villager_jockey", new EntityData("zombie villager jockey", 2.137500048, 0.1875));
        ENTITY_CONFIG.put("armor_stand", new EntityData("armor stand", 2.162500024, 0.1875));
        ENTITY_CONFIG.put("baby_strider_on_strider", new EntityData("baby strider on strider", 2.737500072, 0.1875));
        ENTITY_CONFIG.put("villager_jockey", new EntityData("villager jockey", 2.837500036, 0.1875));
        ENTITY_CONFIG.put("creaking", new EntityData("creaking", 2.887500048, 0.1875));
        ENTITY_CONFIG.put("enderman", new EntityData("enderman", 3.087500095, 0.1875));
        ENTITY_CONFIG.put("zombified_piglin_on_strider", new EntityData("zombified piglin on strider", 3.137500107, 0.1875));
        ENTITY_CONFIG.put("strider_on_strider", new EntityData("strider on strider", 3.587500095, 0.1875));
        ENTITY_CONFIG.put("strider_on_strider_on_strider", new EntityData("strider on strider on strider", 5.287500143, 0.1875));


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