package eu.gs.gslibrary.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TextComponentUtils {

    public static TextComponent create(String message) {
        TextComponent textComponent = null;
        if(Integer.parseInt(Utils.getMinecraftVersion(Bukkit.getServer()).split("\\.")[1]) >= 16) {
            textComponent = new TextComponent(new ComponentBuilder().appendLegacy(message).create());
        }else {
            textComponent = new TextComponent(message);
        }
        return textComponent;
    }

    public void setHover(TextComponent textComponent, String hover) {
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
    }

    public void setClick(TextComponent textComponent, ClickEvent.Action actionType, String value) {
        textComponent.setClickEvent(new ClickEvent(actionType, value));
    }

    public static void send(Player p, TextComponent... components) {
        p.spigot().sendMessage(components);
    }

    public static void broadcast(TextComponent... components) {
        Bukkit.spigot().broadcast(components);
    }

}
