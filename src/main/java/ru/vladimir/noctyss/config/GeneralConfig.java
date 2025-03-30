package ru.vladimir.noctyss.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vladimir.noctyss.event.EventType;
import ru.vladimir.noctyss.utility.LoggerUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public final class GeneralConfig implements IConfig {
    private static final String SETTINGS = "settings.";
    private final JavaPlugin plugin;
    private final FileConfiguration fileConfig;
    private int debugLevel;
    private Map<World, List<EventType>> allowedEventWorlds;

    @Override
    public void load() {
        plugin.saveDefaultConfig();
        parse();
        validate();
    }

    private void parse() {
        debugLevel = fileConfig.getInt(SETTINGS + "debug-level", 0);
        allowedEventWorlds = getWorldMap(fileConfig.getConfigurationSection(SETTINGS + "allowed-worlds"));
    }

    private Map<World, List<EventType>> getWorldMap(ConfigurationSection section) {
        if (section == null) {
            LoggerUtility.warn(this,
                    "List of worlds for allowed events is empty and therefore events won't be used");
            return Map.of();
        }

        final Map<World, List<EventType>> worldMap = new HashMap<>();

        for (final String worldName : section.getKeys(false)) {

            final World world = Bukkit.getWorld(worldName);
            if (world == null) {
                LoggerUtility.warn(this, "World not found: %s".formatted(worldName));
                continue;
            }

            final List<EventType> eventTypes = getEventTypes(section.getStringList(worldName));

            worldMap.put(world, eventTypes);
        }

        return worldMap;
    }

    private List<EventType> getEventTypes(List<String> eventTypeNames) {
        final List<EventType> eventTypes = new ArrayList<>();
        for (final String eventTypeName : eventTypeNames) {
            try {
                final EventType eventType = EventType.valueOf(eventTypeName.toUpperCase().trim());
                eventTypes.add(eventType);
            } catch (IllegalArgumentException | NullPointerException e) {
                LoggerUtility.warn(this, "Invalid event type name: %s. %s"
                        .formatted(eventTypeName, e));
            }
        }
        return eventTypes;
    }

    private void validate() {
        if (debugLevel < 0 || debugLevel > 2) {
            LoggerUtility.warn(this, "Invalid debug level '%d'. Setting to default"
                    .formatted(debugLevel));
            debugLevel = 0;
        }

        if (allowedEventWorlds.isEmpty()) {
            LoggerUtility.warn(this,
                    "List of allowed event worlds is null and therefore no events will be handled");
        }
    }

    @Override
    public void reload() {
        load();
    }
}
