package eu.gs.gslibrary.utils;

import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.utils.cooldowns.Cooldown;
import eu.gs.gslibrary.utils.cooldowns.CooldownAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownDoneResponse;
import eu.gs.gslibrary.utils.methods.AsyncMethod;
import eu.gs.gslibrary.utils.replacement.Replacement;
import io.netty.util.concurrent.CompleteFuture;
import jline.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

// System util class
public final class Action {

    public static void run(String action, Player player, @Nullable int amount) {
        String prefix = action.split("]")[0];
        String text = "";
        if (action.split("]").length > 1) text = action.replace(action.split("]")[0]+"]", "");
        switch (prefix) {
            case "[CLOSE":
                player.closeInventory();
                break;
            case "[MENU":
                if (GSLibrary.getInstance().getGuis().containsKey(text)) {
                    GSLibrary.getInstance().getGuis().get(text).open(new Replacement((player1, string) -> string), true, player);
                }
                break;
            case "[MESSAGE":
                player.sendMessage(Utils.colorize(player, text).replace("%amount%", amount+""));
                break;
            case "[PLAYER_CMD":
                player.performCommand(text.replace("%player%", player.getName()).replace("%amount%", amount+""));
                break;
            case "[CONSOLE_CMD":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text.replace("%player%", player.getName()).replace("%amount%", amount+""));
                break;
        }
    }
}
