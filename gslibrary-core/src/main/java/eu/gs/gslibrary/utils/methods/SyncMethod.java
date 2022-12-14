package eu.gs.gslibrary.utils.methods;

import eu.gs.gslibrary.GSLibrary;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * It runs a method in the main thread
 */
public abstract class SyncMethod {

    public abstract void action();

    public SyncMethod() {
        new BukkitRunnable() {
            @Override
            public void run() {
                action();
            }
        }.runTask(GSLibrary.getInstance());
    }

}