package eu.gs.gslibrary.commands;

import org.bukkit.command.CommandSender;

public interface CommandResponse {

    void cmd(CommandSender sender, String label, CommandArg[] commandArgs);

}
