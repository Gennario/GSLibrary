package eu.gs.gslibrary.utils.updater;

import eu.gs.gslibrary.GSLibrary;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PluginUpdater {

    private int resourceId;
    private JavaPlugin plugin;

    private Map<String, String> data;

    private String pluginVersion, sitesVersion;

    public PluginUpdater(int resourceId, JavaPlugin plugin) {
        this.resourceId = resourceId;
        this.plugin = plugin;
        this.data = new HashMap<>();

        try {
            checkVersions();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GSLibrary.getInstance().getPluginLoaderMap().put(plugin, this);
    }

    public void sendLoadMessage() {
        PluginDescriptionFile description = plugin.getDescription();
        pluginVersion = description.getVersion();
        System.out.println(ChatColor.GRAY+""+ChatColor.STRIKETHROUGH+"                                                                          ");
        System.out.println(ChatColor.WHITE+"Loading plugin "+ChatColor.GREEN+description.getName()+ChatColor.WHITE+" v."+ChatColor.GREEN+description.getVersion());
        System.out.println(ChatColor.GRAY+""+ChatColor.STRIKETHROUGH+"                                                                          ");
        System.out.println("");
        for (String key : data.keySet()) {
            System.out.println(ChatColor.WHITE+" "+key+": "+ChatColor.GREEN+data.get(key));
        }
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
        System.out.println("");
        System.out.println(ChatColor.WHITE+"Thanks for selecting "+ChatColor.GREEN+"Gennario's Studios"+ChatColor.WHITE+" development team.");
        System.out.println(ChatColor.GRAY+""+ChatColor.STRIKETHROUGH+"                                                                          ");
    }

    public void checkVersions() throws IOException {
        if(resourceId == 0) return;
        URLConnection con = new URL("https://api.polymart.org/v1/getResourceInfoSimple/?resource_id="+this.resourceId+"&key=version").openConnection();
        this.sitesVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
    }

    public void unload() {
        GSLibrary.getInstance().getPluginLoaderMap().remove(plugin);
    }

}
