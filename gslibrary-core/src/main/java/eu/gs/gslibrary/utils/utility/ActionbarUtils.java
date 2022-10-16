package eu.gs.gslibrary.utils.utility;

import com.cryptomorin.xseries.ReflectionUtils;
import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.scheduler.Scheduler;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * It sends an actionbar message to the player
 */
@UtilityClass
public class ActionbarUtils {

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(GSLibrary.class);
    private static final MethodHandle CHAT_COMPONENT_TEXT;
    private static final MethodHandle PACKET;
    private static final Object CHAT_MESSAGE_TYPE;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> packetPlayOutChatClass = ReflectionUtils.getNMSClass("PacketPlayOutChat");
        Class<?> iChatBaseComponentClass = ReflectionUtils.getNMSClass("IChatBaseComponent");

        MethodHandle packet = null;
        MethodHandle chatComp = null;
        Object chatMsgType = null;

        try {
            // Game Info Message Type
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + ReflectionUtils.VERSION + ".ChatMessageType");
            for (Object obj : chatMessageTypeClass.getEnumConstants()) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMsgType = obj;
                    break;
                }
            }

            // JSON Message Builder
            Class<?> chatComponentTextClass = ReflectionUtils.getNMSClass("ChatComponentText");
            chatComp = lookup.findConstructor(chatComponentTextClass, MethodType.methodType(void.class, String.class));

            // Packet Constructor
            packet = lookup.findConstructor(packetPlayOutChatClass, MethodType.methodType(void.class, iChatBaseComponentClass, chatMessageTypeClass));
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException ignored) {
            try {
                // Game Info Message Type
                chatMsgType = (byte) 2;

                // JSON Message Builder
                Class<?> chatComponentTextClass = ReflectionUtils.getNMSClass("ChatComponentText");
                chatComp = lookup.findConstructor(chatComponentTextClass, MethodType.methodType(void.class, String.class));

                // Packet Constructor
                packet = lookup.findConstructor(packetPlayOutChatClass, MethodType.methodType(void.class, iChatBaseComponentClass, byte.class));
            } catch (NoSuchMethodException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        CHAT_MESSAGE_TYPE = chatMsgType;
        CHAT_COMPONENT_TEXT = chatComp;
        PACKET = packet;
    }

    /**
     * Sends an action bar to a player.
     *
     * @param player  the player to send the action bar to.
     * @param message the message to send.
     * @see #sendActionBar(Player, String, long)
     * @since 1.0.0
     */
    public static void sendActionBar(Player player, String message) {
        Objects.requireNonNull(player, "Cannot send action bar to null player");
        Object packet = null;

        try {
            Object component = CHAT_COMPONENT_TEXT.invoke(Utils.colorize(player, message));
            packet = PACKET.invoke(component, CHAT_MESSAGE_TYPE);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ReflectionUtils.sendPacket(player, packet);
    }

    /**
     * It sends an empty action bar to the player
     *
     * @param player The player you want to send the action bar to.
     */
    public void clearActionBar(Player player) {
        sendActionBar(player, "§r");
    }

    /**
     * Sends an action bar all the online players.
     *
     * @param message the message to send.
     * @see #sendActionBar(Player, String)
     * @since 1.0.0
     */
    public static void sendAllActionBar(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) sendActionBar(player, message);
    }

    /**
     * It loops through all online players and sends them an empty action bar
     */
    public static void clearAllActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) sendActionBar(player, "§r");
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * Plugin instance should be changed in this method for the schedulers.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send. The message will not be updated.
     * @param callable the condition for the action bar to continue.
     * @see #sendActionBar(Player, String, long)
     * @since 1.0.0
     */
    public static void sendActionBarWhile(Player player, String message, Callable<Boolean> callable) {
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
                sendActionBar(player, message);
            }
            // Re-sends the messages every 2 seconds so it doesn't go away from the player's screen.
        }, 0, 40);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     * <p>
     * If the caller returns true, the action bar will continue.
     * If the caller returns false, action bar will not be sent anymore.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send. The message will be updated.
     * @param callable the condition for the action bar to continue.
     * @see #sendActionBarWhile(Player, String, Callable)
     * @since 1.0.0
     */
    public static void sendActionBarWhile(Player player, Callable<String> message, Callable<Boolean> callable) {
        Scheduler.asyncTimer(new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!callable.call()) {
                        cancel();
                        return;
                    }
                    sendActionBar(player, message.call());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 0, 40);
    }

    /**
     * Sends an action bar to a player for a specific amount of ticks.
     *
     * @param player   the player to send the action bar to.
     * @param message  the message to send.
     * @param duration the duration to keep the action bar in ticks.
     * @see #sendActionBarWhile(Player, String, Callable)
     * @since 1.0.0
     */
    public static void sendActionBar(Player player, String message, long duration) {
        if (duration < 1) return;

        Scheduler.asyncTimer(new BukkitRunnable() {
            long repeater = duration;

            @Override
            public void run() {
                sendActionBar(player, message);
                repeater -= 40L;
                if (repeater - 40L < -20L) cancel();
            }
        }, 0, 40);
    }
}
