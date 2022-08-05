package eu.gs.gslibrary.utils.api;

import com.cryptomorin.xseries.ReflectionUtils;
import eu.gs.gslibrary.utils.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@UtilityClass
public class ActionbarUtils {

    private static Method a;
    private static Constructor<?> constructor;

    static {
        try {
            constructor = ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), byte.class);
            a = ReflectionUtils.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);

        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void sendActionbar(Player player, String message) {
        try {
            Object icbc = a.invoke(null, "{\"text\":\"" + Utils.colorize(null, message) + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);

            ReflectionUtils.sendPacket(player, packet);

        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
