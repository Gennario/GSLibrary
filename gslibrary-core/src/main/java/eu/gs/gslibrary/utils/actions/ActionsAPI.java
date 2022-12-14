package eu.gs.gslibrary.utils.actions;

import com.cryptomorin.xseries.XSound;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.GSLibrary;
import eu.gs.gslibrary.menu.PlayerGUIHistory;
import eu.gs.gslibrary.utils.BungeeUtils;
import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.utility.ActionbarUtils;
import eu.gs.gslibrary.utils.utility.TextUtils;
import eu.gs.gslibrary.utils.utility.TitleUtils;
import eu.gs.gslibrary.utils.replacement.Replacement;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionsAPI {

    private final Map<String, ActionResponse> actions;
    private final Replacement replacement;

    public ActionsAPI() {
        this.actions = new HashMap<>();
        this.replacement = new Replacement((player, string) -> string.replace("%player%", player.getName()));

        loadDefaults();
    }

    public void loadDefaults() {
        /* Connect to server action */
        addAction("connect", (player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                BungeeUtils.connect(player, replacement.replace(player, data.getString("value")));
            }
        });

        /* Console and player command */
        addAction("console-cmd", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacement.replace(player, data.getString("value")));
            } else if (data.isExist("values")) {
                for (String s : data.getListString("values")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), replacement.replace(player, s));
                }
            } else {
                System.out.println("Some console-cmd action are missing correct data");
            }
        });

        /* Console and player command */
        addAction("message", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                player.sendMessage(Utils.colorize(player, replacement.replace(player, data.getString("value"))));
            } else if (data.isExist("values")) {
                for (String s : data.getListString("values")) {
                    player.sendMessage(Utils.colorize(player, replacement.replace(player, s)));
                }
            } else {
                System.out.println("Some console-cmd action are missing correct data");
            }
        });

        /* Console and player command */
        addAction("broadcast", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                Bukkit.broadcastMessage(Utils.colorize(player, replacement.replace(player, data.getString("value"))));
            } else if (data.isExist("values")) {
                for (String s : data.getListString("values")) {
                    Bukkit.broadcastMessage(Utils.colorize(player, replacement.replace(player, s)));
                }
            } else {
                System.out.println("Some console-cmd action are missing correct data");
            }
        });

        addAction("player-cmd", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                player.chat("/" + replacement.replace(player, data.getString("value")));
            } else if (data.isExist("values")) {
                for (String s : data.getListString("values")) {
                    player.chat("/" + replacement.replace(player, s));
                }
            } else {
                System.out.println("Some player-cmd action are missing correct data");
            }
        });

        /* Close inventory action */
        addAction("close-inv", (player, identifier, data, replacement) -> {
            player.closeInventory();
        });

        /* Open gui action */
        addAction("open-gui", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                String gui = replacement.replace(player, data.getString("value"));
                if (GSLibrary.getInstance().guiExist(gui)) {
                    GSLibrary.getInstance().getGUI(gui).open(replacement, true, player);
                } else {
                    System.out.println("Some open-gui action trying to open non existing gui");
                }
            } else {
                System.out.println("Some open-gui action are missing correct data");
            }
        });

        /* Open gui action */
        addAction("gui-back", (player, identifier, data, replacement) -> {
            if (GSLibrary.getInstance().getPlayerGUIHistory().containsKey(player)) {
                PlayerGUIHistory playerGUIHistory = GSLibrary.getInstance().getPlayerGUIHistory().get(player);
                if (playerGUIHistory.getCurrent() != null) {
                    playerGUIHistory.back().guiOpen(player);
                }
            }
        });

        /* Open gui action */
        addAction("gui-forward", (player, identifier, data, replacement) -> {
            if (GSLibrary.getInstance().getPlayerGUIHistory().containsKey(player)) {
                PlayerGUIHistory playerGUIHistory = GSLibrary.getInstance().getPlayerGUIHistory().get(player);
                if (playerGUIHistory.getCurrent() != null) {
                    playerGUIHistory.forward().guiOpen(player);
                }
            }
        });

        /* Actionbar action */
        addAction("actionbar", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                ActionbarUtils.sendActionBar(player, replacement.replace(player, data.getString("value")));
            } else {
                System.out.println("Some actionbar action are missing correct data");
            }
        });
        addAction("actionbar-all", (player, identifier, data, replacement) -> {
            if (data.isExist("value")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ActionbarUtils.sendActionBar(onlinePlayer, replacement.replace(onlinePlayer, data.getString("value")));
                }
            } else {
                System.out.println("Some actionbar-all action are missing correct data");
            }
        });

        /* Title action */
        addAction("title", (player, identifier, data, replacement) -> {
            if (data.isExist("title") && data.isExist("subtitle")) {
                String title = data.getString("title");
                String subtitle = data.getString("subtitle");
                int fadeIn = 20;
                int stay = 60;
                int fadeOut = 20;
                if (data.isExist("fade-in")) fadeIn = data.getInt("fade-in");
                if (data.isExist("stay")) stay = data.getInt("stay");
                if (data.isExist("fade-out")) fadeOut = data.getInt("fade-out");
                TitleUtils.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
            } else {
                System.out.println("Some title action are missing correct data");
            }
        });
        addAction("title-all", (player, identifier, data, replacement) -> {
            if (data.isExist("title") && data.isExist("subtitle")) {
                String title = data.getString("title");
                String subtitle = data.getString("subtitle");
                int fadeIn = 20;
                int stay = 60;
                int fadeOut = 20;
                if (data.isExist("fade-in")) fadeIn = data.getInt("fade-in");
                if (data.isExist("stay")) stay = data.getInt("stay");
                if (data.isExist("fade-out")) fadeOut = data.getInt("fade-out");
                for (Player op : Bukkit.getOnlinePlayers()) {
                    TitleUtils.sendTitle(op, title, subtitle, fadeIn, stay, fadeOut);
                }
            } else {
                System.out.println("Some title action are missing correct data");
            }
        });

        /* Sound action */
        addAction("sound", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                float volume = 60;
                float pitch = 20;
                if (data.isExist("volume")) volume = data.getFloat("volume");
                if (data.isExist("pitch")) pitch = data.getFloat("pitch");
                Sound sound = XSound.valueOf(data.getString("value")).parseSound();
                if (sound != null) {
                    player.playSound(player.getLocation(), sound, volume, pitch);
                }
            }
        }));
        addAction("sound-all", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                float volume = 60;
                float pitch = 20;
                if (data.isExist("volume")) volume = data.getFloat("volume");
                if (data.isExist("pitch")) pitch = data.getFloat("pitch");
                Sound sound = XSound.valueOf(data.getString("value")).parseSound();
                if (sound != null) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.playSound(player.getLocation(), sound, volume, pitch);
                    }
                }
            }
        }));

        /* Gamemode action */
        addAction("gamemode", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                player.setGameMode(GameMode.valueOf(replacement.replace(player, data.getString("value")).toUpperCase()));
            }
        }));
        addAction("gamemode-all", ((player, identifier, data, replacement1) -> {
            if (data.isExist("value")) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setGameMode(GameMode.valueOf(replacement.replace(onlinePlayer, data.getString("value")).toUpperCase()));
                }
            }
        }));

        /* Fly action */
        addAction("fly-toggle", (player, identifier, data, replacement1) -> {
            player.setAllowFlight(!player.getAllowFlight());
        });
        addAction("fly-enabled", (player, identifier, data, replacement1) -> {
            player.setAllowFlight(true);
        });
        addAction("fly-disabled", (player, identifier, data, replacement1) -> {
            player.setAllowFlight(false);
        });
        addAction("fly-toggle-all", (player, identifier, data, replacement1) -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setAllowFlight(!onlinePlayer.getAllowFlight());
            }
        });
        addAction("firework", (player, identifier, data, replacement1) -> {
            String fireworkType = data.getString("value", "BALL_LARGE");
            int fireworkPower = data.getInt("power", 1);
            boolean fireworkFlicker = data.getBoolean("flicker", true);
            boolean fireworkTrail = data.getBoolean("trail", true);

            List<Color> fireworkColors = new ArrayList<>();
            data.getListString("join_settings.firework.colors").forEach(c -> {
                Color color = TextUtils.getColor(c);
                if (color != null) fireworkColors.add(color);
            });

            Firework f = player.getWorld().spawn(player.getLocation(), Firework.class);
            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(FireworkEffect.builder()
                    .flicker(fireworkFlicker)
                    .trail(fireworkTrail)
                    .with(FireworkEffect.Type.valueOf(fireworkType))
                    .withColor(fireworkColors).build());
            fm.setPower(fireworkPower);
            f.setFireworkMeta(fm);
        });
    }

    public void addAction(String identifier, ActionResponse response) {
        actions.put(identifier, response);
    }


    public void useAction(Player player, Section... actionConfigurations) {
        for (Section section : actionConfigurations) {
            if (section.contains("conditions")) {
                boolean allow = true;
                for (String s : section.getSection("conditions").getRoutesAsStrings(false)) {
                    if (!GSLibrary.getInstance().getConditionsAPI().check(player, section.getSection("conditions." + s), replacement))
                        allow = false;
                }

                if (!allow) {
                    continue;
                }
            }

            String type = section.getString("type");
            if (!actions.containsKey(type)) {
                System.out.println("Action " + type + " doesn't exist! Please try something else...");
                return;
            }
            ActionData data = new ActionData(section);
            actions.get(type).action(player, type, data, replacement);
        }
    }

    public void useAction(Player player, Replacement replacement, Section... actionConfigurations) {
        for (Section section : actionConfigurations) {
            if (section.contains("conditions")) {
                boolean allow = true;
                for (String s : section.getSection("conditions").getRoutesAsStrings(false)) {
                    if (!GSLibrary.getInstance().getConditionsAPI().check(player, section.getSection("conditions." + s), replacement))
                        allow = false;
                }
                if (!allow) {
                    continue;
                }
            }

            String type = section.getString("type");
            if (!actions.containsKey(type)) {
                System.out.println("Action " + type + " doesn't exist! Please try something else...");
                return;
            }
            ActionData data = new ActionData(section);
            actions.get(type).action(player, type, data, replacement);
        }
    }

}
