package ru.vladimir.noctyss.command;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.vladimir.noctyss.Noctyss;
import ru.vladimir.noctyss.command.list.ActiveEventListCommand;
import ru.vladimir.noctyss.command.list.ReloadConfigCommand;
import ru.vladimir.noctyss.command.list.StartEventCommand;
import ru.vladimir.noctyss.command.list.StopEventCommand;
import ru.vladimir.noctyss.command.list.eventinformation.EventInfoCommand;
import ru.vladimir.noctyss.config.MessageConfig;
import ru.vladimir.noctyss.event.GlobalEventScheduler;
import ru.vladimir.noctyss.utility.MessageUtil;

import java.util.List;

@RequiredArgsConstructor
public final class NoctyssCommand extends SubCommandManager implements TabExecutor {
    private final Noctyss noctyss;
    private final JavaPlugin plugin;
    private final PluginManager pluginManager;
    private final GlobalEventScheduler globalEventScheduler;
    private final MessageConfig messageConfig;

    public void init() {
        registerCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length < 1) {
            sendFeedback(sender, messageConfig.getCommandUsage());
            return true;
        }

        final SubCommandWrapper wrapper = getWrapper(args[0]);
        if (wrapper == null) {
            sendFeedback(sender, messageConfig.getUnknownCommand());
            return true;
        }

        if (!wrapper.hasPermission(sender)) {
            sendFeedback(sender, messageConfig.getNoPermission());
            return true;
        }

        wrapper.command().onCommand(sender, args);
        return true;
    }

    private void sendFeedback(CommandSender sender, Component message, Object... values) {
        MessageUtil.sendMessage(sender, message, values);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!hasAnyPermission(sender)) return null;
        if (args.length == 1) return getFirstAliases(sender);

        final SubCommandWrapper wrapper = getWrapper(args[0]);
        if (wrapper == null) return List.of();

        if (wrapper.hasPermission(sender)) return wrapper.command().onTabComplete(sender, args);
        else return List.of();
    }

    private void registerCommands() {
        addSubCommand(
                new ActiveEventListCommand(messageConfig),
                "list",
                new Permission("noctyss.event.info")
        );

        addSubCommand(
                new ReloadConfigCommand(noctyss, messageConfig),
                "reload",
                new Permission("noctyss.reload")
        );

        addSubCommand(
                new EventInfoCommand(plugin, pluginManager, messageConfig),
                "info",
                new Permission("noctyss.event.info")
        );

        addSubCommand(
                new StartEventCommand(globalEventScheduler, messageConfig),
                "start",
                new Permission("noctyss.event.start")
        );

        addSubCommand(
                new StopEventCommand(messageConfig),
                "stop",
                new Permission("noctyss.event.stop")
        );
    }
}
