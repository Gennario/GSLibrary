package eu.gs.gslibrary;

import eu.gs.gslibrary.commands.CommandAPI;
import eu.gs.gslibrary.commands.SubCommandArg;
import eu.gs.gslibrary.conditions.Condition;
import eu.gs.gslibrary.conditions.ConditionsAPI;
import eu.gs.gslibrary.menu.GUI;
import eu.gs.gslibrary.utils.BungeeUtils;
import eu.gs.gslibrary.utils.actions.ActionsAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownTask;
import eu.gs.gslibrary.utils.updater.PluginUpdater;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class GSLibrary extends JavaPlugin {

    private static GSLibrary instance;
    private Map<String, GUI> guis;

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
        pluginLoaderMap = new HashMap<>();

        cooldownAPI = new CooldownAPI();
        cooldownTask = new CooldownTask();
        cooldownTask.runTaskTimerAsynchronously(this, 0, 20);

        actionsAPI = new ActionsAPI();
        conditionsAPI = new ConditionsAPI();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
