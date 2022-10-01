package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.GSLibrary;
import org.bukkit.scheduler.BukkitRunnable;

public class GUIRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (GUI gui : GSLibrary.getInstance().getGuis().values()) {
            if (gui.getUpdateTime() > 0) {
                if (gui.getUpdateTick() >= gui.getUpdateTime()) {
                    gui.setUpdateTick(0);

                    for (PlayerGUIData guiData : gui.getPlayerGUIDataMap().values()) {
                        if (guiData.checkOpened()) guiData.updateGui(false);
                    }
                } else {
                    gui.setUpdateTick(gui.getUpdateTick() + 1);
                }
            }
        }
    }
}
