package ru.vladimir.noctyss.command.list.eventinformation;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class EventInfoInventory {
    private static final int INVENTORY_SIZE = 27;
    private static final Component INVENTORY_TITLE = Component.text("Noctyss: Events Information");

    private static final Component INFORMATION_ICON_NAME = MiniMessage.miniMessage().deserialize(
            "<!i><white>Information</white>");
    private static final List<Component> INFORMATION_ICON_LORE = List.of(
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> Learn about all <gold>events</gold> and how to configure them. </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> Each event has its own <yellow>settings</yellow> and effects. </gray>"),
            MiniMessage.miniMessage().deserialize("<!i><gray> You can customize certain aspects to fit your world. </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> <aqua>Notifications</aqua> will inform you of event progress. </gray>"),
            MiniMessage.miniMessage().deserialize("<!i><gray> Adjust what they display at different times. </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> <red>Sleeping</red> is <bold>disabled</bold> during active events. </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> For more details, check the <blue>plugin documentation</blue>. </gray>"),
            MiniMessage.miniMessage().deserialize(" ")
    );

    private static final Component NIGHTMARE_NIGHT_ICON_NAME = MiniMessage.miniMessage().deserialize(
            "<!i><gradient:#660000:#222222>Nightmare Night</gradient>");
    private static final List<Component> NIGHTMARE_NIGHT_ICON_LORE = List.of(
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> A <dark_red>dark</dark_red> and <red>ominous</red> night approaches... </gray>"),
            MiniMessage.miniMessage().deserialize("<!i><gray> During this event, you may encounter: </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><white> - <dark_red>Longer night duration</dark_red> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <red>Unsettling ambient sounds</red> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <dark_gray>Reduced vision</dark_gray> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <gold>Increased monster spawns</gold> </white>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> Be cautious... <dark_red>you're never alone</dark_red>. </gray>"),
            MiniMessage.miniMessage().deserialize(" ")
    );

    private static final Component SUDDEN_NIGHT_ICON_NAME = MiniMessage.miniMessage().deserialize(
            "<!i><gradient:#F69806:#F68406>Sudden Night</gradient>");
    private static final List<Component> SUDDEN_NIGHT_ICON_LORE = List.of(
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> The <gold>sun vanishes</gold>, and the world is plunged into <dark_gray>darkness</dark_gray>. </gray>"),
            MiniMessage.miniMessage().deserialize("<!i><gray> During this event, you will experience: </gray>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><white> - <dark_gray>Instant nightfall</dark_gray> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <gray>Complete silence</gray> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <gold>Unsettling music</gold> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <blue>All entities freeze</blue> </white>"),
            MiniMessage.miniMessage().deserialize("<!i><white> - <red>No mobs will spawn</red> </white>"),
            MiniMessage.miniMessage().deserialize(" "),
            MiniMessage.miniMessage().deserialize("<!i><gray> Something feels <dark_red>wrong</dark_red>... Stay alert. </gray>"),
            MiniMessage.miniMessage().deserialize(" ")
    );

    private final EventInfoInventoryHolder holder;
    private final Inventory inventory;

    public EventInfoInventory() {
        holder = new EventInfoInventoryHolder();
        inventory = Bukkit.createInventory(holder, INVENTORY_SIZE, INVENTORY_TITLE);
        setupDesign();
        setupIcons();
    }

    private void setupDesign() {
        final ItemStack designLightBluePanel = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        final int[] designLightBluePanelSlots = new int[] {0, 1, 2, 6, 7, 8, 9, 17, 21, 22, 23};

        final ItemStack designGrayPanel = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final int[] designGrayPanelSlots = new int[] {4, 5, 6, 18, 19, 20, 24, 25, 26};

        final ItemMeta designMeta = designGrayPanel.getItemMeta();
        designMeta.displayName(Component.empty());

        designLightBluePanel.setItemMeta(designMeta);
        designGrayPanel.setItemMeta(designMeta);

        for (int i : designLightBluePanelSlots) {
            inventory.setItem(i, designLightBluePanel);
        }

        for (int i : designGrayPanelSlots) {
            inventory.setItem(i, designGrayPanel);
        }
    }

    private void setupIcons() {
        final ItemStack informationIcon = new ItemStack(Material.PAPER);
        final ItemMeta informationIconMeta = informationIcon.getItemMeta();
        informationIconMeta.displayName(INFORMATION_ICON_NAME);
        informationIconMeta.lore(INFORMATION_ICON_LORE);
        informationIcon.setItemMeta(informationIconMeta);

        final ItemStack nightmareNightIcon = new ItemStack(Material.BLACK_CONCRETE);
        final ItemMeta nightmareNightIconMeta = nightmareNightIcon.getItemMeta();
        nightmareNightIconMeta.displayName(NIGHTMARE_NIGHT_ICON_NAME);
        nightmareNightIconMeta.lore(NIGHTMARE_NIGHT_ICON_LORE);
        nightmareNightIcon.setItemMeta(nightmareNightIconMeta);

        final ItemStack suddenNightIcon = new ItemStack(Material.CLOCK);
        final ItemMeta suddenNightIconMeta = suddenNightIcon.getItemMeta();
        suddenNightIconMeta.displayName(SUDDEN_NIGHT_ICON_NAME);
        suddenNightIconMeta.lore(SUDDEN_NIGHT_ICON_LORE);
        suddenNightIcon.setItemMeta(suddenNightIconMeta);

        inventory.setItem(4, informationIcon);
        inventory.setItem(10, nightmareNightIcon);
        inventory.setItem(11, suddenNightIcon);
    }

    class EventInfoInventoryHolder implements InventoryHolder {
        @Override
        public @NotNull Inventory getInventory() {
            return inventory;
        }
    }
}
