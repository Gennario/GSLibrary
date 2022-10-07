package eu.gs.gslibrary.nms.v1_15_R1;

import eu.gs.gslibrary.nms.NMSAdapter;
import eu.gs.gslibrary.nms.utils.EntityEquipmentSlot;
import eu.gs.gslibrary.nms.utils.reflect.R;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    private EnumItemSlot slot(EntityEquipmentSlot slot) {
        switch (slot) {
            case MAINHAND:
                return EnumItemSlot.MAINHAND;
            case OFFHAND:
                return EnumItemSlot.OFFHAND;
            case FEET:
                return EnumItemSlot.FEET;
            case LEGS:
                return EnumItemSlot.LEGS;
            case CHEST:
                return EnumItemSlot.CHEST;
            case HEAD:
                return EnumItemSlot.HEAD;
            default:
                break;
        }
        return null; // This should never happen...
    }

    /*
     *  General
     */

    @Override
    public void sendPacket(Player player, Object packet) {
        if (packet instanceof Packet<?>) {
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
        return new PacketPlayOutChat(s(text), ChatMessageType.GAME_INFO);
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
        return new DataWatcher.Item<>(new DataWatcherObject<>(1, DataWatcherRegistry.b), airTicksLeft);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(2, DataWatcherRegistry.e), s(name));
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(3, DataWatcherRegistry.i), visible);
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(4, DataWatcherRegistry.i), silenced);
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(5, DataWatcherRegistry.i), gravity);
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

        return new DataWatcher.Item<>(new DataWatcherObject<>(0, DataWatcherRegistry.a), data);
    }

    // -------------------- Armor Stand Start -------------------- //

    @Override
    public Object getMetaArmorStandProperties(boolean small, boolean arms, boolean noBasePlate, boolean marker) {
        byte data = 0;
        data += small ? 1 : 0;
        data += arms ? 2 : 0;
        data += noBasePlate ? 8 : 0;
        data += marker ? 10 : 0;

        return new DataWatcher.Item<>(new DataWatcherObject<>(13, DataWatcherRegistry.a), data);
    }

    @Override
    public Object getMetaArmorStandRotationHead(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(16, DataWatcherRegistry.k), v);
    }

    @Override
    public Object getMetaArmorStandRotationBody(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(17, DataWatcherRegistry.k), v);
    }

    @Override
    public Object getMetaArmorStandRotationLeftArm(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(18, DataWatcherRegistry.k), v);
    }

    @Override
    public Object getMetaArmorStandRotationRightArm(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(19, DataWatcherRegistry.k), v);
    }

    @Override
    public Object getMetaArmorStandRotationLeftLeg(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(20, DataWatcherRegistry.k), v);
    }

    @Override
    public Object getMetaArmorStandRotationRightLeg(float x, float y, float z) {
        Vector3f v = new Vector3f(x, y, z);
        return new DataWatcher.Item<>(new DataWatcherObject<>(21, DataWatcherRegistry.k), v);
    }

    // -------------------- Armor Stand End -------------------- //

    @Override
    public Object getMetaItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(8, DataWatcherRegistry.g), i(itemStack));
    }

    /*
     *  Entity
     */

    private Optional<EntityTypes<?>> getEntityTypes(EntityType type) {
        return EntityTypes.a(type.getKey().getKey().toLowerCase());
    }

    @Override
    public int getEntityTypeId(EntityType type) {
        return getEntityTypes(type).map(IRegistry.ENTITY_TYPE::a).orElse(-1);
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return getEntityTypes(type).map((t) -> t.k().height).orElse(0f);
    }

    @Override
    public double getEntityWidth(EntityType type) {
        return getEntityTypes(type).map((t) -> t.k().width).orElse(0f);
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
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", l.getX());
        r.set("c", l.getY());
        r.set("d", l.getZ());
        r.set("e", (byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        r.set("f", (byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        r.set("h", onGround);
        return packet;
    }

    @Override
    public Object packetSpawnEntity(int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", id);
        r.set("c", l.getX());
        r.set("d", l.getY());
        r.set("e", l.getZ());
        r.set("i", MathHelper.d(l.getPitch() * 256.0F / 360.0F));
        r.set("j", MathHelper.d(l.getYaw() * 256.0F / 360.0F));
        r.set("k", getEntityTypes(type).orElse(null));
        return packet;
    }

    @Override
    public Object packetSpawnEntityLiving(int eid, UUID id, EntityType type, Location l) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", id);
        r.set("c", getEntityTypeId(type));
        r.set("d", l.getX());
        r.set("e", l.getY());
        r.set("f", l.getZ());
        r.set("j", (byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        r.set("k", (byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        r.set("l", (byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        return packet;
    }

    @Override
    public Object packetSetEquipment(int eid, EntityEquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        return new PacketPlayOutEntityEquipment(eid, slot(slot), i(itemStack));
    }

    @Override
    public Object packetUpdatePassengers(int eid, int... passengers) {
        PacketPlayOutMount packet = new PacketPlayOutMount();
        R r = new R(packet);
        r.set("a", eid);
        r.set("b", passengers);
        return packet;
    }

    @Override
    public Object packetRemoveEntity(int eid) {
        return new PacketPlayOutEntityDestroy(eid);
    }

}
