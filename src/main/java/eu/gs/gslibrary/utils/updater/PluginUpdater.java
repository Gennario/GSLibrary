package eu.gs.gslibrary.utils.updater;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@Getter
@Setter
public class PluginUpdater {

    private int resourceId;
    private JavaPlugin plugin;

    private String pluginVersion, sitesVersion;

    public PluginUpdater(int resourceId, JavaPlugin plugin, boolean sendMessage) {
        this.resourceId = resourceId;
        this.plugin = plugin;

        checkVersions();

        if(sendMessage) sendLoadMessage();
    }

    public void sendLoadMessage() {
        PluginDescriptionFile description = plugin.getDescription();
        pluginVersion = description.getVersion();
        System.out.println(ChatColor.WHITE+"Loading plugin "+ChatColor.GREEN+description.getName()+ChatColor.WHITE+" v."+ChatColor.GREEN+description.getVersion());
        System.out.println(ChatColor.DARK_GREEN+"---------------------------------------------------------------");
        System.out.println("");
        System.out.println(ChatColor.WHITE+"This plugin is running on "+ChatColor.GREEN+description.getVersion()+ChatColor.WHITE+"...");
        if(sitesVersion != null) System.out.println(ChatColor.WHITE+"Current plugin version on polymart is "+ChatColor.GREEN+sitesVersion+ChatColor.WHITE+"...");
        if(sitesVersion != null) {
            if(pluginVersion == sitesVersion) {
                System.out.println(ChatColor.DARK_GREEN+"So your plugin is on the latest version...");
            }else {
                System.out.println(ChatColor.DARK_GREEN+"So your plugin is outdated. Please update the plugin...");
            }
        }
        System.out.println("");
        System.out.println(ChatColor.WHITE+" Plugin author: "+ChatColor.YELLOW+description.getAuthors());
        System.out.println(ChatColor.WHITE+" Plugin dependencies: "+ChatColor.GREEN+description.getDepend());
        System.out.println(ChatColor.WHITE+" Plugin soft-dependencies: "+ChatColor.GREEN+description.getSoftDepend());
        System.out.println("");
        System.out.println(ChatColor.WHITE+"Thanks for selecting "+ChatColor.GREEN+"Gennario's Studios"+ChatColor.WHITE+" development team.");
        System.out.println(ChatColor.DARK_GREEN+"---------------------------------------------------------------");
    }

    public void checkVersions() {
        if(resourceId == 0) return;
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.polymart.org/v1/getResourceInfoSimple/?resource_id="+this.resourceId+"&key=version").openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    sitesVersion = scanner.next();
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }



}
