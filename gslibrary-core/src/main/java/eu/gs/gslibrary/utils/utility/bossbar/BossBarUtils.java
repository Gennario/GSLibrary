package eu.gs.gslibrary.utils.utility.bossbar;
import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class BossBarUtils {

    private final BossBar bossBar;
    private final List<Player> players = new ArrayList<>();
    private final BossBarTimer bossBarTimer;

    public BossBarUtils(String title, BarColor color, BarStyle style, BarFlag flag) {
        this.bossBar = Bukkit.getServer().createBossBar(title, color, style, flag);
        this.bossBarTimer = new BossBarTimer(this);
    }

    public BossBarUtils startTask(int seconds) {
        bossBarTimer.startTask(seconds);
        return this;
    }

    public BossBarUtils setTitle(String title) {
        bossBar.setTitle(title);
        return this;
    }

    public BossBarUtils setVisible(boolean visible) {
        bossBar.setVisible(visible);
        return this;
    }

    public BossBarUtils setColor(BarColor color) {
        bossBar.setColor(color);
        return this;
    }

    public BossBarUtils setStyle(BarStyle visible) {
        bossBar.setStyle(visible);
        return this;
    }

    public BossBarUtils setProgress(double progress) {
        bossBar.setProgress(progress);
        return this;
    }

    public BossBarUtils removeFlag(BarFlag flag) {
        bossBar.removeFlag(flag);
        return this;
    }


    public double getProgress() {
        return bossBar.getProgress();
    }

    public String getTitle() {
        return bossBar.getTitle();
    }

    public BarColor getColor() {
        return bossBar.getColor();
    }

    public BarStyle getStyle() {
        return bossBar.getStyle();
    }

    public List<Player> getPlayers() {
        return bossBar.getPlayers();
    }


    public BossBarUtils addPlayer(Player player) {
        Preconditions.checkNotNull(player, "player");

        bossBar.addPlayer(player);
        players.add(player);
        return this;
    }

    public BossBarUtils addPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
        return this;
    }

    public BossBarUtils removePlayer(Player player) {
        Preconditions.checkNotNull(player, "player");

        bossBar.removePlayer(player);
        return this;
    }

    public BossBarUtils removePlayers() {
        Iterator<Player> iterable = players.iterator();
        while (iterable.hasNext()) {
            final Player player = iterable.next();
            removePlayer(player);
            iterable.remove();
        }

        players.clear();
        return this;
    }

    public BossBarUtils remove() {
        removePlayers();
        bossBar.removeAll();
        return this;
    }

}
