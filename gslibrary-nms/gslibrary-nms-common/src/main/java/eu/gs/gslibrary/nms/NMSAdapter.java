package eu.gs.gslibrary.nms;

import eu.gs.gslibrary.nms.utils.EntityEquipmentSlot;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * This interface should be implemented for every single Minecraft version, that
 * we want to support in our plugins. It is used to access certain NMS functionalities,
 * that are, of course, different in each version.
 * <p>
 * I hate NMS and it's developers. :-)
 *
 * @author d0by
 */
public interface NMSAdapter {

    /*
     *  General
     */

    /**
     * Sends a packet to a player.
     *
     * @param player The player to send the packet to.
     * @param packet The packet to send.
     */
    void sendPacket(Player player, Object packet);

    /**
     * Sends a packet to all online players.
     *
     * @param packet The packet to send.
     */
    default void sendGlobalPacket(Object packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendPacket(player, packet);
        }
    }

    /**
     * Sends a packet to all players in a world.
     *
     * @param world  The world to send the packet to.
     * @param packet The packet to send.
     */
    default void sendWorldPacket(World world, Object packet) {
        for (Player player : world.getPlayers()) {
            sendPacket(player, packet);
        }
    }

    /**
     * Sends a packet to all players in a world within
     * a certain radius around a location.
     *
     * @param radius The radius to send the packet to.
     * @param l      The location to send the packet to.
     * @param packet The packet to send.
     */
    default void sendRangedPacket(double radius, Location l, Object packet) {
        World world = l.getWorld();
        if (world == null) {
            return;
        }

        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(l) < radius * radius) {
                sendPacket(player, packet);
            }
        }
    }

    /*
     *  Player
     */

    /**
     * Get the player's channel pipeline.
     *
     * @param player The player to get the pipeline of.
     * @return The player's channel pipeline.
     */
    ChannelPipeline getPipeline(Player player);

    /*
     *  Packets
     */

    Object updateTimePacket(long worldAge, long day);

    Object packetGameState(int mode, float value);

    Object packetTimes(int in, int stay, int out);

    Object packetTitleMessage(String text);

    Object packetSubtitleMessage(String text);

    Object packetActionbarMessage(String text);

    Object packetJsonMessage(String text);

    Object packetResetTitle();

    Object packetClearTitle();

    Object packetHeaderFooter(String header, String footer);

    Object packetBlockAction(Location l, int action, int param, int blockType);

    Object packetBlockChange(Location l, int blockId, byte blockData);

    /*
     *  Entity Metadata
     */

    Object packetEntityMetadata(int eid, Object... objects);

    Object packetEntityMetadata(int eid, List<Object> objects);

    Object getMetaCustom(int id, Class<?> type, Object value);

    Object getMetaEntityRemainingAir(int airTicksLeft);

    Object getMetaEntityCustomName(String name);

    Object getMetaEntityCustomNameVisible(boolean visible);

    Object getMetaEntitySilenced(boolean silenced);

    Object getMetaEntityGravity(boolean gravity);

    Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming,
                                   boolean invisible, boolean glowing, boolean flyingElytra);

    Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker);

    Object getMetaItemStack(ItemStack itemStack);

    /*
     *  Entity
     */

    int getEntityTypeId(EntityType type);

    double getEntityHeight(EntityType type);

    Object packetEntityAnimation(int eid, int animation);

    Object packetTeleportEntity(int eid, Location l, boolean onGround);

    Object packetSpawnEntity(int eid, UUID id, EntityType type, Location l);

    Object packetSpawnEntityLiving(int eid, UUID id, EntityType type, Location l);

    Object packetSetEquipment(int eid, EntityEquipmentSlot slot, ItemStack itemStack);

    Object packetUpdatePassengers(int eid, int... passengers);

    Object packetRemoveEntity(int eid);


}
