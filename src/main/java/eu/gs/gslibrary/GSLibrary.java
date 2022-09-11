package eu.gs.gslibrary;

import eu.gs.gslibrary.commands.CommandAPI;
import eu.gs.gslibrary.commands.SubCommandArg;
import eu.gs.gslibrary.conditions.Condition;
import eu.gs.gslibrary.conditions.ConditionsAPI;
import eu.gs.gslibrary.menu.GUI;
import eu.gs.gslibrary.menu.GUIRunnable;
import eu.gs.gslibrary.menu.PlayerGUIHistory;
import eu.gs.gslibrary.menu.event.GUIPerPlayerItemResponse;
import eu.gs.gslibrary.menu.event.GUIPerPlayerPagination;
import eu.gs.gslibrary.utils.BungeeUtils;
import eu.gs.gslibrary.utils.actions.ActionsAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownTask;
import eu.gs.gslibrary.utils.updater.PluginUpdater;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class GSLibrary extends JavaPlugin {

    private static GSLibrary instance;
    private Map<String, GUI> guis;
    private GUIRunnable guiRunnable;
    private Map<Player, PlayerGUIHistory> playerGUIHistory;

    private CooldownAPI cooldownAPI;
    private CooldownTask cooldownTask;

    private ActionsAPI actionsAPI;
    private ConditionsAPI conditionsAPI;

    private Map<JavaPlugin, PluginUpdater> pluginLoaderMap;

    public static GSLibrary getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        BungeeUtils.init();

        guis = new HashMap<>();
        guiRunnable = new GUIRunnable();
        guiRunnable.runTaskTimerAsynchronously(this, 1, 1);
        playerGUIHistory = new HashMap<>();

        pluginLoaderMap = new HashMap<>();

        cooldownAPI = new CooldownAPI();
        cooldownTask = new CooldownTask();
        cooldownTask.runTaskTimerAsynchronously(this, 0, 20);

        actionsAPI = new ActionsAPI();
        conditionsAPI = new ConditionsAPI();

    }

    @Override
    public void onDisable() {
    }

    public GUI getGUI(String name) {
        return guis.get(name);
    }

    public List<GUI> getGUIList() {
        return new ArrayList<>(guis.values());
    }

    public boolean guiExist(String name) {
        return guis.containsKey(name);
    }

    public void unloadGUI(String name) {
        if(!guiExist(name)) return;
        HandlerList.unregisterAll(getGUI(name));
        guis.remove(name);
    }

    public void unloadAll(List<String> names) {
        for (String name : names) {
            HandlerList.unregisterAll(getGUI(name));
            guis.remove(name);
        }
    }
}
