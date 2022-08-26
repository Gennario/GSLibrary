package eu.gs.gslibrary.commands;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.language.Language;
import eu.gs.gslibrary.language.LanguageAPI;
import eu.gs.gslibrary.utils.replacement.Replacement;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private LanguageAPI languageAPI;

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

    public CommandAPI setLanguageAPI(LanguageAPI languageAPI) {
        this.languageAPI = languageAPI;
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
            if (help) {
                addCommand("help")
                        .setDescription("Cmd for plugin help message")
                        .setUsage("help [page]")
                        .setAliases("?")
                        .setSubCommandArgs(new SubCommandArg("page", SubCommandArg.CommandArgType.OPTIONAL, SubCommandArg.CommandArgValue.INT))
                        .setResponse((sender, label, commandArgs) -> {
                            int page = 1;
                            if(commandArgs.length > 0) {
                                page = commandArgs[0].getAsInt();
                            }
                            CommandHelpMessage.getHelpMessage(sender, page, this, label);
                        });
            }
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            PluginCommand pluginCommand = constructor.newInstance(name, GSLibrary.getInstance());
            pluginCommand.setExecutor((sender, cmd, label, args) -> {
                if (args.length == 0) {
                    if (help) {
                        sender.sendMessage(languageAPI.getMessage("commands.usage", null, new Replacement((player, string) -> {
                            return string.replace("%label%", label).replace("%help%", "help [page]");
                        })).toArray(new String[0]));
                        return true;
                    } else if (emptyCommandResponse != null) {
                        emptyCommandResponse.cmd(sender, label, null);
                    }
                } else {
                    String s = args[0];
                    for (SubCommand command : subCommands) {
                        if (command.getCommand().equalsIgnoreCase(s) || command.getAliases().contains(s.toLowerCase())) {
                            if (command.getPermission() != null) {
                                if (!sender.hasPermission(command.getPermission())) {
                                    sender.sendMessage(languageAPI.getMessage("commands.no-perms", null, null).toArray(new String[0]));
                                    return false;
                                }
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
                                    if (commandArg.getType().equals(SubCommandArg.CommandArgType.REQUIRED)) {
                                        required++;
                                    }
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
                                        sender.sendMessage(languageAPI.getMessage("commands.invalid-usage", null, new Replacement((player, string) -> {
                                            return string.replace("%value%", commandArg.getName());
                                        })).toArray(new String[0]));
                                        return false;
                                    }

                                    i++;
                                }
                            }

                            if(required > commandArgs.size() || (commandArgs.isEmpty() && required != 0) || commandArgs.size() > command.getSubCommandArgs().size()) {
                                sender.sendMessage(languageAPI.getMessage("commands.usage", null, new Replacement((player, string) -> {
                                    return string.replace("%label%", label).replace("%help%", command.getUsage());
                                })).toArray(new String[0]));
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
            pluginCommand.setTabCompleter((sender, command, label, args) -> {
                List<String> list = new ArrayList<>();

                if(args.length == 1) {
                    for (SubCommand subCommand : subCommands) {
                        list.add(subCommand.getCommand());
                    }
                }else {
                    for (SubCommand subCommand : subCommands) {
                        if(subCommand.getCommand().equalsIgnoreCase(args[0])) {
                            int count = args.length-2;
                            if((subCommand.getSubCommandArgs().size()-1) >= count) {
                                switch (subCommand.getSubCommandArgs().get(count).getValue()) {
                                    case STRING:
                                    case INT:
                                    case LONG:
                                    case FLOAT:
                                    case DOUBLE:
                                        list.add("[<"+subCommand.getSubCommandArgs().get(count).getName()+">]");
                                        return list;
                                    case MATERIAL:
                                        for (Material material : Material.values()) {
                                            list.add(material.name());
                                        }
                                        return list;
                                    case ENTITY:
                                        for (EntityType entityType : EntityType.values()) {
                                            list.add(entityType.name());
                                        }
                                        return list;
                                    case PLAYER:
                                        for (Player entityType : Bukkit.getOnlinePlayers()) {
                                            list.add(entityType.getName());
                                        }
                                        return list;
                                }
                            }
                        }
                    }
                }

                return list;
            });
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(GSLibrary.getInstance().getServer().getPluginManager());
            commandMap.register(GSLibrary.getInstance().getDescription().getName(), pluginCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
