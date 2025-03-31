package ru.vladimir.noctyss.event.modules.time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import ru.vladimir.noctyss.event.Controllable;
import ru.vladimir.noctyss.event.EventManager;
import ru.vladimir.noctyss.event.EventType;
import ru.vladimir.noctyss.event.modules.Module;
import ru.vladimir.noctyss.utility.LoggerUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeModifyService implements Module {
    private final List<TimeModificationRule> rules;
    private final World world;
    private final EventType eventType;

    private TimeModifyService(Builder builder) {
        this.rules = builder.getRules();
        this.world = builder.getWorld();
        this.eventType = builder.getEventType();
    }

    @Override
    public void start() {
        int started = 0;
        for (final TimeModificationRule rule : rules) {

            if (rule instanceof Controllable scheduleRule) {
                scheduleRule.start();
            }

            started++;
            LoggerUtility.info(this, "Added '%s' in '%s' for '%s'"
                    .formatted(rule.getClass().getSimpleName(), world.getName(), eventType.name()));
        }
        LoggerUtility.info(this, "Added all '%d' in '%s' for '%s'"
                .formatted(started, world.getName(), eventType.name()));
    }

    @Override
    public void stop() {
        int stopped = 0;
        for (final TimeModificationRule rule : rules) {

            if (rule instanceof Controllable scheduleRule) {
                scheduleRule.stop();
            }

            stopped++;
            LoggerUtility.info(this, "Stopped '%s' in '%s' for '%s'"
                    .formatted(rule.getClass().getSimpleName(), world.getName(), eventType.name()));
        }
        LoggerUtility.info(this, "Stopped all '%d' in '%s' for '%s'"
                .formatted(stopped, world.getName(), eventType.name()));
    }

    @Getter
    @RequiredArgsConstructor
    public static class Builder {
        private final List<TimeModificationRule> rules = new ArrayList<>();
        private final JavaPlugin plugin;
        private final EventManager eventManager;
        private final World world;
        private final EventType eventType;

        public Builder addMidnightLoopModifier(long frequency, long nightLength) {
            final MidnightLoopModifier rule = new MidnightLoopModifier(
                    plugin, world, eventManager, eventType, frequency, nightLength);
            rules.add(rule);
            return this;
        }

        public Builder addAbruptNight(long frequency, long[] nightLength, Random random) {
            final AbruptNight abruptNight = new AbruptNight(plugin, eventManager, eventType, world, nightLength, frequency, random);
            rules.add(abruptNight);
            return this;
        }

        public TimeModifyService build() {
            return new TimeModifyService(this);
        }
    }
}
