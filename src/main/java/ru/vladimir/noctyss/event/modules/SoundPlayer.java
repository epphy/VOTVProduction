package ru.vladimir.noctyss.event.modules;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.vladimir.noctyss.utility.LoggerUtility;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public final class SoundPlayer implements Module {
    private final JavaPlugin plugin;
    private final World world;
    private final Random random;
    private final List<Sound> sounds;
    private final long frequency;
    private int taskId = -1;

    @Override
    public void start() {
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin, this::playSound, frequency, frequency).getTaskId();
        LoggerUtility.info(this, "Started scheduler for world '%s'.".formatted(world.getName()));
    }

    private void playSound() {
        final Sound sound = getSound();
        if (sound == null) {
            LoggerUtility.warn(this, "Could not play a sound because sound is null");
            return;
        }

        final float randomVolume = random.nextFloat(1.0f, 5.0f);
        final float randomPitch = random.nextFloat(1.0f, 5.0f);

        for (final Player player : world.getPlayers()) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.stopAllSounds();
                player.playSound(player, sound, randomVolume, randomPitch);
            });
        }
    }

    @Nullable
    private Sound getSound() {
        if (sounds.isEmpty()) {
            LoggerUtility.warn(this, "In world '%s', the sounds list is empty");
            return null;
        }

        if (sounds.size() == 1) {
            return sounds.getFirst();
        }

        final int randomIndex = random.nextInt(sounds.size() - 1);
        return sounds.get(randomIndex);
    }

    @Override
    public void stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            LoggerUtility.info(this, "Stopped scheduler for %s".formatted(world.getName()));
        } else {
            LoggerUtility.info(this, "Cannot stop scheduler for %s".formatted(world.getName()));
        }
    }
}
