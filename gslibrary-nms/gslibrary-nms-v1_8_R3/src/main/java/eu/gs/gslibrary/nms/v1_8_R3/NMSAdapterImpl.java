package eu.gs.gslibrary.nms.v1_8_R3;

import eu.gs.gslibrary.nms.NMSAdapter;
import eu.gs.gslibrary.nms.utils.EntityEquipmentSlot;
import eu.gs.gslibrary.nms.utils.reflect.R;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NMSAdapterImpl implements NMSAdapter {

    private IChatBaseComponent s(String s) {
        return IChatBaseComponent.ChatSerializer.a(s);
    }

    private ItemStack i(org.bukkit.inventory.ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack);
    }

    private BlockPosition blockPos(Location l) {
        return new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /*
     *  General
     */

    @Override
    public void sendPacket(Player player, Object packet) {
        if (packet instanceof Packet) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
        }
    }

    /*
     *  Player
     */

    @Override
    public ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.a().channel.pipeline();
    }

    /*
     *  Packets
     */

    @Override
    public Object updateTimePacket(long worldAge, long day) {
        return new PacketPlayOutUpdateTime(worldAge, day, true);
    }

    @Override
    public Object packetGameState(int mode, float value) {
        return new PacketPlayOutGameStateChange(mode, value);
    }

    @Override
    public Object packetTimes(int in, int stay, int out) {
        return new PacketPlayOutTitle(in, stay, out);
    }

    @Override
    public Object packetTitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, s(text));
    }

    @Override
    public Object packetSubtitleMessage(String text) {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, s(text));
    }

    @Override
    public Object packetActionbarMessage(String text) {
        return new PacketPlayOutChat(s(text), (byte) 2);
    }

    @Override
    public Object packetJsonMessage(String text) {
        return new PacketPlayOutChat(s(text));
    }

    @Override
    public Object packetResetTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null);
    }

    @Override
    public Object packetClearTitle() {
        return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null);
    }

    @Override
    public Object packetHeaderFooter(String header, String footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        R r = new R(packet);
        r.set("a", s(header));
        r.set("b", s(footer));
        return packet;
    }

    @Override
    public Object packetBlockAction(Location l, int action, int param, int blockType) {
        BlockPosition position = blockPos(l);
        Block block = Block.getByCombinedId(blockType).getBlock();
        return new PacketPlayOutBlockAction(position, block, param, blockType);
    }

    @Override
    public Object packetBlockChange(Location l, int blockId, byte blockData) {
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange();
        R r = new R(packet);
        r.set("a", blockPos(l));
        packet.block = Block.getByCombinedId(blockId << 4 | (blockData & 15));
        return packet;
    }

    /*
     *  Entity Metadata
     */

    @Override
    public Object packetEntityMetadata(int eid, Object... objects) {
        return packetEntityMetadata(eid, Arrays.asList(objects));
    }

    @Override
    public Object packetEntityMetadata(int eid, List<Object> objects) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", objects);
        return packet;
    }

    @Override
    public Object getMetaCustom(int id, Class<?> type, Object value) {
        return null;
    }

    @Override
    public Object getMetaEntityRemainingAir(int airTicksLeft) {
        return new DataWatcher.WatchableObject(2, 1, airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.WatchableObject(4, 2, name);
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.WatchableObject(0, 3, (byte) (visible ? 1 : 0));
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.WatchableObject(0, 4, (byte) (silenced ? 1 : 0));
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.WatchableObject(0, 5, (byte) (gravity ? 1 : 0));
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte data = 0;
        data += onFire ? 1 : 0;
        data += crouched ? 2 : 0;
        data += sprinting ? 8 : 0;
        data += swimming ? 10 : 0;
        data += invisible ? 20 : 0;
        data += glowing ? 40 : 0;
        data += flyingElytra ? 80 : 0;

        return new DataWatcher.WatchableObject(0, 0, data);
    }

    // -------------------- Armor Stand Start -------------------- //

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0;
        data += small ? 1 : 0;
        data += arms ? 2 : 0;
        data += noBasePlate ? 8 : 0;
        data += marker ? 10 : 0;

        return new DataWatcher.WatchableObject(0, 10, data);
    }

    @Override
    public Object getMetaArmorStandRotationHead(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 16, v);
    }

    @Override
    public Object getMetaArmorStandRotationBody(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 17, v);
    }

    @Override
    public Object getMetaArmorStandRotationLeftArm(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 18, v);
    }

    @Override
    public Object getMetaArmorStandRotationRightArm(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 19, v);
    }

    @Override
    public Object getMetaArmorStandRotationLeftLeg(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 20, v);
    }

    @Override
    public Object getMetaArmorStandRotationRightLeg(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.WatchableObject(7, 21, v);
    }

    // -------------------- Armor Stand End -------------------- //

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.WatchableObject(5, 8, i(itemStack));
    }

    /*
     *  Entity
     */

    private static final Map<String, Integer> ENTITY_TYPE_NAME_ID_MAP;

    static {
        ENTITY_TYPE_NAME_ID_MAP = R.getFieldValue(EntityTypes.class, "g");
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        return ENTITY_TYPE_NAME_ID_MAP == null ? type.getTypeId() : ENTITY_TYPE_NAME_ID_MAP.get(type.getName());
    }

    @Override
    public Object packetEntityAnimation(int eid, int animation) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", animation);
        return packet;
    }

    @Override
    public Object packetTeleportEntity(int eid, Location l, boolean onGround) {
        return new PacketPlayOutEntityTeleport(
                eid,
                MathHelper.floor(l.getX() * 32.0),
                MathHelper.floor(l.getY() * 32.0),
                MathHelper.floor(l.getZ() * 32.0),
                (byte) ((int) (l.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (l.getPitch() * 256.0F / 360.0F)),
                onGround
        );
    }

    @Override
    public Object packetSpawnEntity(int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", MathHelper.floor(l.getX() * 32.0D));
        r.set("c", MathHelper.floor(l.getY() * 32.0D));
        r.set("d", MathHelper.floor(l.getZ() * 32.0D));
        r.set("h", MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        r.set("i", MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        r.set("j", getEntityTypeId(type));
        return packet;
    }

    @Override
    public Object packetSpawnEntityLiving(int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", getEntityTypeId(type));
        r.set("c", MathHelper.floor(l.getX() * 32.0D));
        r.set("d", MathHelper.floor(l.getY() * 32.0D));
        r.set("e", MathHelper.floor(l.getZ() * 32.0D));
        r.set("i", (byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        r.set("j", (byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        r.set("k", (byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        r.set("g", MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        return packet;
    }

    @Override
    public Object packetSetEquipment(int eid, EntityEquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        return new PacketPlayOutEntityEquipment(eid, slot.getLegacySlotId(), i(itemStack));
    }

    @Override
    public Object packetUpdatePassengers(int eid, int... passengers) {
        PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
        R r = new R(packet);
        r.set("a", 0);
        r.set("b", eid);
        if (passengers != null && passengers.length > 0) {
            r.set("c", passengers[0]);
        } else {
            r.set("c", -1);
        }
        return packet;
    }

    @Override
    public Object packetRemoveEntity(int eid) {
        return new PacketPlayOutEntityDestroy(eid);
    }

}
