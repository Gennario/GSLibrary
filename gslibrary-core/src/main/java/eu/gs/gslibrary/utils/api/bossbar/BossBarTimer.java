package eu.gs.gslibrary.utils.api.bossbar;
import com.google.common.collect.Lists;
import eu.gs.gslibrary.GSLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.function.Consumer;

public class BossBarTimer {

    /**
     * Parts to be executed
     */
    private final List<Consumer<Player>> parts = Lists.newArrayList();


    /**
     * The task
     */
    private BukkitTask task;

    /**
     * Bossbar
     */
    private float progressMinus;
    private BossBarUtils bossBar;

    public BossBarTimer(BossBarUtils bossBar) {
        this.bossBar = bossBar;

        this.init();
    }

    public void init() {
        parts.add((player) -> {
            if (bossBar == null) return;

            double newProgress = bossBar.getProgress() - progressMinus;
            if (newProgress <= 0) {
                bossBar.removePlayers();

                bossBar = null;
            } else {
                bossBar.setProgress(newProgress);
            }

        });
    }

    public void startTask(int seconds) throws IllegalStateException {
        stopTask();

        this.progressMinus = 1.0f / seconds;

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(GSLibrary.getInstance(), () ->
                bossBar.getPlayers().forEach((player) -> parts.forEach((c) -> c.accept(player))), 20, 20);
    }

    public void stopTask() {
        if (task == null) return;
        task.cancel();
        task = null;
        bossBar = null;
    }
}
