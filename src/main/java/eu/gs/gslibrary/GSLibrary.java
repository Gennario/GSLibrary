package eu.gs.gslibrary;

import com.cryptomorin.xseries.XMaterial;
import eu.gs.gslibrary.conditions.ConditionsAPI;
import eu.gs.gslibrary.menu.GUI;
import eu.gs.gslibrary.menu.GUIRunnable;
import eu.gs.gslibrary.menu.PlayerGUIHistory;
import eu.gs.gslibrary.particles.Particle;
import eu.gs.gslibrary.particles.ParticleCreator;
import eu.gs.gslibrary.particles.ParticleEffect;
import eu.gs.gslibrary.particles.ParticlesRunnable;
import eu.gs.gslibrary.particles.animations.Circle;
import eu.gs.gslibrary.particles.animations.Point;
import eu.gs.gslibrary.particles.animations.RotatingCircle;
import eu.gs.gslibrary.utils.BungeeUtils;
import eu.gs.gslibrary.utils.actions.ActionsAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownAPI;
import eu.gs.gslibrary.utils.cooldowns.CooldownTask;
import eu.gs.gslibrary.utils.updater.PluginUpdater;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

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

    private Map<UUID, ParticleEffect> particleEffectsMap;
    private ParticlesRunnable particlesRunnable;

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

        particleEffectsMap = new HashMap<>();
        particlesRunnable = new ParticlesRunnable();
        particlesRunnable.runTaskTimerAsynchronously(this, 1, 1);

        cooldownAPI = new CooldownAPI();
        cooldownTask = new CooldownTask();
        cooldownTask.runTaskTimerAsynchronously(this, 0, 20);

        actionsAPI = new ActionsAPI();
        conditionsAPI = new ConditionsAPI();

        Location location = Bukkit.getPlayer("Gennario").getLocation();
        ParticleCreator particleCreator = new ParticleCreator(xyz.xenondevs.particle.ParticleEffect.REDSTONE)
                .setAmount(1)
                .setDustData(74, 255, 92, 0.4f);
        new ParticleEffect(location)
                .setParticle(new Particle(particleCreator))
                .setViewDistance(20)
                .setParticleAnimationExtender(new RotatingCircle(80, 2.0, 100, RotatingCircle.CircleType.VERTICAL, RotatingCircle.CircleDirection.LEFT))
                .start();
        new ParticleEffect(location)
                .setParticle(new Particle(particleCreator))
                .setViewDistance(20)
                .setParticleAnimationExtender(new RotatingCircle(80, 2.0, 120, RotatingCircle.CircleType.VERTICAL, RotatingCircle.CircleDirection.LEFT))
                .start();
        new ParticleEffect(location)
                .setParticle(new Particle(particleCreator))
                .setViewDistance(20)
                .setParticleAnimationExtender(new RotatingCircle(80, 2.0, 100, RotatingCircle.CircleType.HORIZONTAL, RotatingCircle.CircleDirection.LEFT))
                .start();
        new ParticleEffect(location)
                .setParticle(new Particle(particleCreator))
                .setViewDistance(20)
                .setParticleAnimationExtender(new RotatingCircle(80, 2.0, 120, RotatingCircle.CircleType.HORIZONTAL, RotatingCircle.CircleDirection.RIGHT))
                .start();
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
