package eu.gs.gslibrary.utils.scheduler;

import eu.gs.gslibrary.GSLibrary;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class Scheduler {

    /* Sync method */


    /**
     * It runs a runnable on the main thread
     *
     * @param runnable The runnable to run
     * @return A BukkitTask object.
     */
    public static BukkitTask sync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(GSLibrary.getInstance(), runnable);
    }

    /**
     * It runs a BukkitRunnable synchronously
     *
     * @param bukkitRunnable The runnable to run.
     * @return A BukkitTask
     */
    public static BukkitTask sync(BukkitRunnable bukkitRunnable) {
        return bukkitRunnable.runTask(GSLibrary.getInstance());
    }

    /**
     * It runs a runnable after a delay
     *
     * @param runnable The runnable to run
     * @param delay    The delay in ticks before the task is run.
     * @return A BukkitTask
     */
    public static BukkitTask syncLater(Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLater(GSLibrary.getInstance(), runnable, delay);
    }

    /**
     * It runs a BukkitRunnable after a delay
     *
     * @param bukkitRunnable The runnable to run.
     * @param delay The delay in ticks before the task is run.
     * @return A BukkitTask
     */
    public static BukkitTask syncLater(BukkitRunnable bukkitRunnable, int delay) {
        return bukkitRunnable.runTaskLater(GSLibrary.getInstance(), delay);
    }

    /**
     * It runs a task on the main thread every X ticks
     *
     * @param runnable The runnable to run.
     * @param interval The interval in ticks to run the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask syncTimer(Runnable runnable, int interval) {
        return Bukkit.getScheduler().runTaskTimer(GSLibrary.getInstance(), runnable, 0, interval);
    }

    /**
     * It runs a bukkit runnable every interval ticks
     *
     * @param bukkitRunnable The runnable you want to run.
     * @param interval The interval in ticks to run the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask syncTimer(BukkitRunnable bukkitRunnable, int interval) {
        return bukkitRunnable.runTaskTimer(GSLibrary.getInstance(), 0, interval);
    }

    /**
     * It runs a task on the main thread, and repeats it every X ticks
     *
     * @param runnable The runnable to run.
     * @param delay    The amount of ticks to wait before running the task.
     * @param interval The interval in ticks to wait between each execution of the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask syncTimer(Runnable runnable, int delay, int interval) {
        return Bukkit.getScheduler().runTaskTimer(GSLibrary.getInstance(), runnable, delay, interval);
    }

    /**
     * It runs a BukkitRunnable in the main thread, with a delay and interval
     *
     * @param bukkitRunnable The runnable you want to run.
     * @param delay The delay before the task is run.
     * @param interval The amount of time in ticks to wait between each execution of the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask syncTimer(BukkitRunnable bukkitRunnable, int delay, int interval) {
        return bukkitRunnable.runTaskTimer(GSLibrary.getInstance(), delay, interval);
    }

    /**
     * It runs a runnable every interval ticks
     *
     * @param runnable The runnable to run.
     * @param interval The interval in ticks between each execution of the runnable.
     * @return The task ID.
     */
    public static int syncRepeating(Runnable runnable, int interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(GSLibrary.getInstance(), runnable, 0, interval);
    }

    /**
     * It runs a runnable every interval ticks, after a delay of delay ticks
     *
     * @param runnable The runnable to run.
     * @param delay    The delay in ticks before the task is run.
     * @param interval The interval in ticks between each execution of the task.
     * @return The task ID.
     */
    public static int syncRepeating(Runnable runnable, int delay, int interval) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(GSLibrary.getInstance(), runnable, delay, interval);
    }

    /**
     * It runs a runnable on the main thread
     *
     * @param runnable The runnable to be executed.
     * @return The task ID.
     */
    public static int syncDelay(Runnable runnable) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(GSLibrary.getInstance(), runnable, 0);
    }

    /**
     * It runs a runnable after a delay
     *
     * @param runnable The runnable to run.
     * @param delay    The delay in ticks before the task is run.
     * @return The task ID.
     */
    public static int syncDelay(Runnable runnable, int delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(GSLibrary.getInstance(), runnable, delay);
    }




    /* Async method */


    /**
     * It runs a runnable asynchronously
     *
     * @param runnable The runnable to run
     * @return A BukkitTask object.
     */
    public static BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(GSLibrary.getInstance(), runnable);
    }

    /**
     * It runs a bukkit runnable asynchronously
     *
     * @param bukkitRunnable The runnable to run.
     * @return A BukkitTask
     */
    public static BukkitTask async(BukkitRunnable bukkitRunnable) {
        return bukkitRunnable.runTaskAsynchronously(GSLibrary.getInstance());
    }

    /**
     * Runs a task asynchronously after a delay.
     *
     * @param runnable The runnable to run
     * @param delay    The delay in ticks before the task is run.
     * @return A BukkitTask object.
     */
    public static BukkitTask asyncLater(Runnable runnable, int delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(GSLibrary.getInstance(), runnable, delay);
    }

    /**
     * It runs a BukkitRunnable asynchronously after a delay
     *
     * @param bukkitRunnable The runnable to run.
     * @param delay The delay in ticks before the task is run.
     * @return A BukkitTask
     */
    public static BukkitTask asyncLater(BukkitRunnable bukkitRunnable, int delay) {
        return bukkitRunnable.runTaskLaterAsynchronously(GSLibrary.getInstance(), delay);
    }

    /**
     * It runs a task asynchronously every X ticks
     *
     * @param runnable The runnable to run.
     * @param interval The interval in ticks to run the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask asyncTimer(Runnable runnable, int interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(GSLibrary.getInstance(), runnable, 0, interval);
    }

    /**
     * It runs a bukkit runnable asynchronously every interval ticks
     *
     * @param bukkitRunnable The runnable to run.
     * @param interval The interval in ticks between each execution of the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask asyncTimer(BukkitRunnable bukkitRunnable, int interval) {
        return bukkitRunnable.runTaskTimerAsynchronously(GSLibrary.getInstance(), 0, interval);
    }

    /**
     * It runs a task asynchronously (in a separate thread) with a delay and interval
     *
     * @param runnable The runnable to run
     * @param delay    The amount of ticks to wait before running the task.
     * @param interval The interval in ticks to wait between each execution of the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask asyncTimer(Runnable runnable, int delay, int interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(GSLibrary.getInstance(), runnable, delay, interval);
    }

    /**
     * It runs a bukkit runnable asynchronously, with a delay and interval
     *
     * @param bukkitRunnable The runnable you want to run.
     * @param delay The delay before the task starts
     * @param interval The interval in ticks between each execution of the task.
     * @return A BukkitTask object.
     */
    public static BukkitTask asyncTimer(BukkitRunnable bukkitRunnable, int delay, int interval) {
        return bukkitRunnable.runTaskTimerAsynchronously(GSLibrary.getInstance(), delay, interval);
    }

    /**
     * It runs a runnable every interval ticks
     *
     * @param runnable The runnable to run.
     * @param interval The interval in ticks between each execution of the runnable.
     * @return The task ID.
     */
    public static int asyncRepeating(Runnable runnable, int interval) {
        return Bukkit.getScheduler().scheduleAsyncRepeatingTask(GSLibrary.getInstance(), runnable, 0, interval);
    }

    /**
     * It runs a runnable asynchronously, repeating it every interval ticks, after a delay of delay ticks
     *
     * @param runnable The runnable to run
     * @param delay    The delay in ticks before the task is run.
     * @param interval The interval in ticks between each execution of the runnable.
     * @return The task ID.
     */
    public static int asyncRepeating(Runnable runnable, int delay, int interval) {
        return Bukkit.getScheduler().scheduleAsyncRepeatingTask(GSLibrary.getInstance(), runnable, delay, interval);
    }

    /**
     * It runs a runnable on the next tick
     *
     * @param runnable The runnable to be executed.
     * @return The task ID.
     */
    public static int asyncDelay(Runnable runnable) {
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(GSLibrary.getInstance(), runnable, 0);
    }

    /**
     * It runs a runnable asynchronously after a delay
     *
     * @param runnable The runnable to run
     * @param delay    The delay in ticks before the task is run.
     * @return The task ID.
     */
    public static int asyncDelay(Runnable runnable, int delay) {
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(GSLibrary.getInstance(), runnable, delay);
    }

    /**
     * Run the given runnable asynchronously, and if it throws an exception, print the stack trace.
     *
     * @param runnable The runnable to run.
     */
    public static void completableFeather(Runnable runnable) {
        CompletableFuture.runAsync(runnable).whenComplete(((unused, throwable) -> {
            if (throwable == null) return;
            throwable.printStackTrace();
        }));
    }

    /**
     * It cancels a task with the given id
     *
     * @param id The id of the task to cancel.
     */
    public static void cancelTask(int id) {
        Bukkit.getScheduler().cancelTask(id);
    }


}
