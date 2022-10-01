package eu.gs.gslibrary.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import eu.gs.gslibrary.GSLibrary;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

@UtilityClass
public class BungeeUtils {

    private static boolean initialized = false;

    /**
     * It registers the plugin channel "BungeeCord" to the server
     */
    public static void init() {
        if (initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(GSLibrary.getInstance(), "BungeeCord");
        initialized = true;
    }

    public static void destroy() {
        if (!initialized) return;
        Messenger messenger = Bukkit.getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(GSLibrary.getInstance(), "BungeeCord");
        initialized = false;
    }

    /**
     * It sends a message to the BungeeCord server, telling it to connect the player to the specified server
     *
     * @param player The player you want to send to the server.
     * @param server The server you want to connect to.
     */
    public static void connect(Player player, String server) {
        if (!initialized) init();
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(GSLibrary.getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception ignored) {
        }
    }
}
