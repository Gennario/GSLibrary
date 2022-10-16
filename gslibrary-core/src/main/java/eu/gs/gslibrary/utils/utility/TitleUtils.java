package eu.gs.gslibrary.utils.utility;

import com.cryptomorin.xseries.ReflectionUtils;
import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.scheduler.Scheduler;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * It sends a title message to a player
 */
@UtilityClass
public class TitleUtils {

    private static final Object TIMES;
    private static final Object TITLE;
    private static final Object SUBTITLE;
    private static final Object CLEAR;

    private static final MethodHandle PACKET;
    private static final MethodHandle CHAT_COMPONENT_TEXT;

    static {
        Class<?> chatComponentText = ReflectionUtils.getNMSClass("ChatComponentText");
        Class<?> packet = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
        Class<?> titleTypes = packet.getDeclaredClasses()[0];
        MethodHandle packetCtor = null;
        MethodHandle chatComp = null;

        Object times = null;
        Object title = null;
        Object subtitle = null;
        Object clear = null;

        for (Object type : titleTypes.getEnumConstants()) {
            switch (type.toString()) {
                case "TIMES":
                    times = type;
                    break;
                case "TITLE":
                    title = type;
                    break;
                case "SUBTITLE":
                    subtitle = type;
                    break;
                case "CLEAR":
                    clear = type;
            }
        }

        try {
            chatComp = MethodHandles.lookup().findConstructor(chatComponentText,
                    MethodType.methodType(void.class, String.class));

            packetCtor = MethodHandles.lookup().findConstructor(packet,
                    MethodType.methodType(void.class, titleTypes,
                            ReflectionUtils.getNMSClass("IChatBaseComponent"),
                            int.class, int.class, int.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        TITLE = title;
        SUBTITLE = subtitle;
        TIMES = times;
        CLEAR = clear;

        PACKET = packetCtor;
        CHAT_COMPONENT_TEXT = chatComp;
    }

    /**
     * It sends a title and subtitle to a player
     *
     * @param player   The player to send the title to.
     * @param title    The title to send
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in ticks it takes for the title to fade in.
     * @param stay     How long the title will stay on the screen
     * @param fadeOut  The time in ticks for the title to fade out.
     */
    public static void sendTitle(Player player,
                                 String title, String subtitle,
                                 int fadeIn, int stay, int fadeOut) {
        Objects.requireNonNull(player, "Cannot send title to null player");
        if (title == null && subtitle == null) return;

        String titleString = Utils.colorize(player, title);
        String subtitleString = Utils.colorize(player, subtitle);

        try {
            Object timesPacket = PACKET.invoke(TIMES, CHAT_COMPONENT_TEXT.invoke(titleString), fadeIn, stay, fadeOut);
            ReflectionUtils.sendPacket(player, timesPacket);

            if (title != null) {
                Object titlePacket = PACKET.invoke(TITLE, CHAT_COMPONENT_TEXT.invoke(titleString), fadeIn, stay, fadeOut);
                ReflectionUtils.sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                Object subtitlePacket = PACKET.invoke(SUBTITLE, CHAT_COMPONENT_TEXT.invoke(subtitleString), fadeIn, stay, fadeOut);
                ReflectionUtils.sendPacket(player, subtitlePacket);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * It creates a packet that tells the client to clear the title
     *
     * @param player The player to send the title to
     */
    public static void clearTitle(Player player) {
        Objects.requireNonNull(player, "Cannot clear title from null player");
        Object clearPacket = null;

        try {
            clearPacket = PACKET.invoke(CLEAR, null, -1, -1, -1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        ReflectionUtils.sendPacket(player, clearPacket);
    }

    /**
     * Sends an action bar all the online players.
     *
     * @param title    The title to be displayed
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in ticks it takes for the title to fade in.
     * @param stay     How long the title will stay on the screen
     * @param fadeOut  The time in ticks it takes for the title to fade out.
     * @see #sendTitle(Player, String, String, int, int, int)
     * @since 1.0.0
     */
    public static void sendAllTitle(String title, String subtitle,
                                    int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * It loops through all online players and clears their title
     */
    public static void clearAllTitle() {
        for (Player player : Bukkit.getOnlinePlayers()) clearTitle(player);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * Plugin instance should be changed in this method for the schedulers.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   The player to send the title to.
     * @param title    The title to be displayed
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in ticks it takes for the title to fade in.
     * @param stay     How long the title will stay on the screen
     * @param fadeOut  The time in ticks it takes for the title to fade out.
     * @param callable the condition for the action bar to continue.
     * @see #sendTitle(Player, String, String, int, int, int)
     * @since 1.0.0
     */
    public static void sendTitleWhile(Player player,
                                      String title, String subtitle,
                                      int fadeIn, int stay, int fadeOut,
                                      Callable<Boolean> callable) {
        Scheduler.asyncTimer(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!callable.call()) {
                        cancel();
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
            }
        }, 0, 40);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   The player to send the action bar to.
     * @param title    The title to send. The title will be updated.
     * @param subtitle The subtitle to send. The subtitle will be updated.
     * @param fadeIn   The time in ticks it takes for the title to fade in.
     * @param stay     How long the title will stay on the screen
     * @param fadeOut  The time in ticks it takes for the title to fade out.
     * @param callable the condition for the action bar to continue.
     * @see #sendTitleWhile(Player, String, String, int, int, int, Callable)
     * @since 1.0.0
     */
    public static void sendTitleWhile(Player player,
                                      Callable<String> title, Callable<String> subtitle,
                                      int fadeIn, int stay, int fadeOut,
                                      Callable<Boolean> callable) {
        Scheduler.asyncTimer(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!callable.call()) {
                        cancel();
                        return;
                    }
                    sendTitle(player, title.call(), subtitle.call(), fadeIn, stay, fadeOut);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 40);
    }

    /**
     * It sends a title to a player for a certain duration
     *
     * @param player   The player to send the title to.
     * @param title    The title to be displayed
     * @param subtitle The subtitle to send.
     * @param fadeIn   The time in ticks it takes for the title to fade in.
     * @param stay     How long the title will stay on the screen
     * @param fadeOut  The time in ticks it takes for the title to fade out.
     * @param duration The duration of the title in milliseconds.
     */
    public static void sendTitle(Player player,
                                 String title, String subtitle,
                                 int fadeIn, int stay, int fadeOut,
                                 long duration) {
        if (duration < 1) return;

        Scheduler.asyncTimer(new BukkitRunnable() {
            long repeater = duration;

            @Override
            public void run() {
                sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
                repeater -= 40L;
                if (repeater - 40L < -20L) cancel();
            }
        }, 0, 40);
    }

}
