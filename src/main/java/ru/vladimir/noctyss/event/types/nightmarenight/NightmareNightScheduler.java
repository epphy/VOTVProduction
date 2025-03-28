package ru.vladimir.noctyss.event.types.nightmarenight;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vladimir.noctyss.api.EventAPI;
import ru.vladimir.noctyss.config.MessageConfig;
import ru.vladimir.noctyss.config.NightmareNightConfig;
import ru.vladimir.noctyss.event.EventManager;
import ru.vladimir.noctyss.event.EventType;
import ru.vladimir.noctyss.event.modules.notification.storage.PlayerNotificationService;
import ru.vladimir.noctyss.event.types.EventScheduler;
import ru.vladimir.noctyss.utility.GameTimeUtility;
import ru.vladimir.noctyss.utility.LoggerUtility;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
public final class NightmareNightScheduler implements EventScheduler {
    private static final int CHANCE_RANGE = 100;
    private static final long DELAY = 0L;
    private final JavaPlugin plugin;
    private final PlayerNotificationService service;
    private final PluginManager pluginManager;
    private final EventManager eventManager;
    private final NightmareNightConfig config;
    private final MessageConfig messageConfig;
    private final Random random;
    private final Set<World> checkedWorlds = new HashSet<>();
    private Set<World> worlds;
    private int eventChance;
    private int taskId = -1;

    @Override
    public void start() {
        if (config.isEventEnabled()) {
            cache();
            taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
                    plugin, this::processWorlds, DELAY, config.getCheckFrequency()).getTaskId();
            LoggerUtility.info(this, "Started scheduler");
        }
    }

    private void processWorlds() {
        for (final World world : worlds) {
            if (world == null) {
                LoggerUtility.warn(this, "Could not find world because it's null");
                continue;
            }

            if (!GameTimeUtility.isNight(world)) {
                checkedWorlds.remove(world);
                continue;
            }

            if (checkedWorlds.contains(world)) {
                continue;
            }

            if (!passesChance() || EventAPI.isEventActive(world, EventType.NIGHTMARE_NIGHT))
                continue;

            checkedWorlds.add(world);
            final NightmareNightInstance eventInstance = new NightmareNightInstance(
                    plugin, service, eventManager, pluginManager, config, messageConfig, world);
            eventManager.startEvent(world, EventType.NIGHTMARE_NIGHT, eventInstance);
            LoggerUtility.info(this, "Scheduling event in: %s".formatted(world.getName()));
        }
    }

    private boolean passesChance() {
        final int randomNumber = random.nextInt(CHANCE_RANGE);
        return randomNumber <= eventChance;
    }

    @Override
    public void stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            LoggerUtility.info(this, "Stopped scheduler");
        } else {
            LoggerUtility.info(this, "Scheduler is not active");
        }
    }

    private void cache() {
        worlds = EventAPI.getWorldsWithAllowedEvent(EventType.NIGHTMARE_NIGHT);
        eventChance = config.getEventChance();
        LoggerUtility.info(this, "Cached fields");
    }
}
