package eu.gs.gslibrary.commands;

import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CommandAPI {

    private final JavaPlugin plugin;
    private final String name;

    private String description;
    private List<String> aliases;

    private boolean help;
    private CommandResponse emptyCommandResponse;

    private final List<SubCommand> subCommands;

    public CommandAPI(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        this.description = "Powered by GSLibrary";
        this.aliases = new ArrayList<>();

        this.help = false;

        this.subCommands = new ArrayList<>();
    }

    public CommandAPI setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommandAPI setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandAPI setAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public CommandAPI addAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    public CommandAPI setEmptyCommandResponse(CommandResponse emptyCommandResponse) {
        this.emptyCommandResponse = emptyCommandResponse;
        return this;
    }

    public SubCommand addCommand(String command) {
        SubCommand subCommand = new SubCommand(command);
        subCommands.add(subCommand);
        return subCommand;
    }

    public CommandAPI setHelp(boolean help) {
        this.help = help;
        return this;
    }

    public void buildCommand() {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, GSLibrary.getInstance());
            pluginCommand.setExecutor((sender, cmd, label, args) -> {
                if (args.length == 0) {
                    if (help) {

                    } else if (emptyCommandResponse != null) {
                        emptyCommandResponse.cmd(sender, label, null);
                    }
                } else {
                    String s = args[0];
                    for (SubCommand command : subCommands) {
                        if (command.getCommand().equalsIgnoreCase(s) || command.getAliases().contains(s.toLowerCase())) {
                            if (command.getPermission() != null) {
                                if (!sender.hasPermission(command.getPermission())) return false;
                            }
                            List<CommandArg> commandArgs = new ArrayList<>();
                            int c = 0;
                            for (String arg : args) {
                                if (c == 0) {
                                    c++;
                                    continue;
                                }
                                commandArgs.add(new CommandArg(this, arg));
                                c++;
                            }
                            int i = 0;
                            int required = 0;
                            for (SubCommandArg commandArg : command.getSubCommandArgs()) {
                                if (i + 1 > commandArgs.size()) {
                                    break;
                                }else {
                                    if (commandArg.getType().equals(SubCommandArg.CommandArgType.REQUIRED)) {
                                        required++;
                                    }
                                    boolean correctType = true;
                                    switch (commandArg.getValue()) {
                                        case INT:
                                            if (!commandArgs.get(i).isInt()) correctType = false;
                                            break;
                                        case LONG:
                                            if (!commandArgs.get(i).isLong()) correctType = false;
                                            break;
                                        case FLOAT:
                                            if (!commandArgs.get(i).isFloat()) correctType = false;
                                            break;
                                        case DOUBLE:
                                            if (!commandArgs.get(i).isDouble()) correctType = false;
                                            break;
                                        case ENTITY:
                                            if (!commandArgs.get(i).isEntity()) correctType = false;
                                            break;
                                        case PLAYER:
                                            if (!commandArgs.get(i).isPlayer()) correctType = false;
                                            break;
                                        case MATERIAL:
                                            if (!commandArgs.get(i).isMaterial()) correctType = false;
                                            break;
                                    }
                                    if (!correctType) {
                                        invalidValueMessage(sender, commandArg);
                                        return false;
                                    }

                                    i++;
                                }
                            }

                            System.out.println(required+" -- "+commandArgs.size());

                            if(required < commandArgs.size() || commandArgs.isEmpty() || commandArgs.size()-1 > command.getSubCommandArgs().size()) {
                                usageMessage(sender, command);
                                return true;
                            }

                            command.getResponse().cmd(sender, label, commandArgs.toArray(new CommandArg[0]));

                        }
                    }
                }
                return false;
            });
            //pluginCommand.setTabCompleter(new GoldsTabCompleter());
            if (description != null) pluginCommand.setDescription(description);
            pluginCommand.setAliases(aliases);
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(GSLibrary.getInstance().getServer().getPluginManager());
            commandMap.register(GSLibrary.getInstance().getDescription().getName(), pluginCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void usageMessage(CommandSender sender, SubCommand subCommand) {
        sender.sendMessage("Usage: " + subCommand.getUsage());
    }

    public void invalidValueMessage(CommandSender sender, SubCommandArg commandArg) {
        sender.sendMessage("Invalid value arg: " + commandArg.getValue().name().toLowerCase());
    }
}
