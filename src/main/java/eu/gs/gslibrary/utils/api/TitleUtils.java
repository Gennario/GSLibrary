package eu.gs.gslibrary.utils.api;

import com.cryptomorin.xseries.ReflectionUtils;
import eu.gs.gslibrary.utils.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@UtilityClass
public class TitleUtils {

    private static Method a;
    private static Constructor<?> titleConstructor;
    private static Field enumTitleField, enumSubtitleField;

    static {
        try {
            titleConstructor = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getConstructor(ReflectionUtils.getNMSClass("PacketPlayOutTitle")
                    .getDeclaredClasses()[0], ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            a = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
            enumTitleField = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE");
            enumSubtitleField = ReflectionUtils.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE");

        } catch (NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void sendTitleMessage(Player player, String title, String subtitle) {

    }

    public static void sendTitleMessage(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            if (title != null || !title.equals("")) {
                Object enumTitle = enumTitleField.get(null);
                Object titleChat = a.invoke(null, "{\"text\":\"" + Utils.colorize(null, title) + "\"}");
                Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, fadeIn, stay, fadeOut);
                ReflectionUtils.sendPacket(player, titlePacket);
            }

            if (subtitle != null || !subtitle.equals("")) {
                Object enumSubtitle = enumSubtitleField.get(null);
                Object subtitleChat = a.invoke(null, "{\"text\":\"" + Utils.colorize(null, subtitle) + "\"}");
                Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, fadeIn, stay, fadeOut);
                ReflectionUtils.sendPacket(player, subtitlePacket);
            }

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
