package ru.vladimir.noctyss.event.modules.notification.storage;

import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import ru.vladimir.noctyss.event.EventType;
import ru.vladimir.noctyss.utility.LoggerUtility;

import java.util.*;

@RequiredArgsConstructor
public class PlayerNotificationService {
    private final PlayerNotificationStorage storage;
    private final PlayerNotificationSerializer serializer;
    private Map<World, Map<EventType, Map<String, Set<UUID>>>> data = new HashMap<>();

    public void init() {
        storage.save();
        updateData();
        LoggerUtility.info(this, "initialised");
    }

    public void updateStorage() {
        storage.store(serializer.serialize(data));
    }

    public void updateData() {
        data = serializer.deserialize(storage.retrieve());
    }

    @NotNull
    private Map<EventType, Map<String, Set<UUID>>> getDataOfWorld(World world) {
        if (!data.containsKey(world)) {
            data.put(world, new HashMap<>());
        }
        return data.get(world);
    }

    @NotNull
    private Map<String, Set<UUID>> getDataOfWorldEvent(World world, EventType eventType) {
        final Map<EventType, Map<String, Set<UUID>>> eventMap = getDataOfWorld(world);
        if (!eventMap.containsKey(eventType)) {
            eventMap.put(eventType, new HashMap<>());
        }
        return eventMap.get(eventType);
    }

    @NotNull
    private Set<UUID> getDataOfWorldEventRule(World world, EventType eventType, String rule) {
        final Map<String, Set<UUID>> rulePlayerIds = getDataOfWorldEvent(world, eventType);
        if (!rulePlayerIds.containsKey(rule)) {
            rulePlayerIds.put(rule, new HashSet<>());
        }
        return rulePlayerIds.get(rule);
    }

    @NotNull
    public Set<UUID> getExcludedPlayersFor(World world, EventType eventType, String rule) {
        final Map<String, Set<UUID>> rulePlayerIds = getDataOfWorldEvent(world, eventType);
        if (!rulePlayerIds.containsKey(rule)) return Set.of();
        return Set.copyOf(rulePlayerIds.get(rule));
    }

    public void addNewExcludedPlayerIds(World world, EventType eventType, String rule, Set<UUID> playerIds) {
        final Set<UUID> excludedPlayerIds = getDataOfWorldEventRule(world, eventType, rule);
        excludedPlayerIds.addAll(playerIds);
        updateStorage();
    }
}
