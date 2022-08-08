package eu.gs.gslibrary.commands;

import eu.gs.gslibrary.language.LanguageAPI;
import eu.gs.gslibrary.utils.TextComponentUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelpMessage {

    public static void getHelpMessage(CommandSender commandSender, int page, CommandAPI commandAPI, String label) {
        LanguageAPI languageAPI = commandAPI.getLanguageAPI();

        int maxPage = 1;
        for(int i = 0; i < 10; i++) {
            if((maxPage)*8 < commandAPI.getSubCommands().size()) {
                maxPage++;
            }else {
                break;
            }
        }

        for (String s : languageAPI.getColoredStringList("commands.help.header", null)) {
            commandSender.sendMessage(s.replace("%page%", ""+page).replace("%max-page%", ""+maxPage));
        }

        int ticks = 0;
        int messages = 0;
        String format = languageAPI.getColoredString("commands.help.format", null);
        for (SubCommand subCommand : commandAPI.getSubCommands()) {
            if ((page - 1) * 8 <= ticks && messages < 8) {
                TextComponent message = TextComponentUtils.create(format.replace("%label%", label).replace("%cmd%", subCommand.getUsage()).replace("%usage%", subCommand.getDescription()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/"+label+" "+subCommand.getUsage()));
                if(commandSender instanceof Player) {
                    ((Player)commandSender).spigot().sendMessage(message);
                }
                messages++;
            }
            ticks++;
        }

        TextComponent space = TextComponentUtils.create(languageAPI.getColoredString("commands.help.pagination.space", null));
        TextComponent previousPage = TextComponentUtils.create(languageAPI.getColoredString("commands.help.pagination.previouspage", null));
        if(page > 1) previousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+label+" help "+(page-1)));
        TextComponent split = TextComponentUtils.create(languageAPI.getColoredString("commands.help.pagination.split", null));
        TextComponent nextPage = TextComponentUtils.create(languageAPI.getColoredString("commands.help.pagination.nextpage", null));
        if(page < maxPage) nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"+label+" help "+(page+1)));
        if(commandSender instanceof Player) {
            ((Player)commandSender).spigot().sendMessage(space, previousPage, split, nextPage);
        }

        for (String s : languageAPI.getColoredStringList("commands.help.footer", null)) {
            commandSender.sendMessage(s);
        }

    }

}
