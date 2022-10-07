package eu.gs.gslibrary.nms.v1_18_R2;

import com.mojang.datafixers.util.Pair;
import eu.gs.gslibrary.nms.NMSAdapter;
import eu.gs.gslibrary.nms.utils.EntityEquipmentSlot;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.Vector3f;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;

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
                return EnumItemSlot.a;
            case OFFHAND:
                return EnumItemSlot.b;
            case FEET:
                return EnumItemSlot.c;
            case LEGS:
                return EnumItemSlot.d;
            case CHEST:
                return EnumItemSlot.e;
            case HEAD:
                return EnumItemSlot.f;
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
            ((CraftPlayer) player).getHandle().b.a((Packet<?>) packet);
        }
    }

    /*
     *  Player
     */

    @Override
    public ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().b.a().m.pipeline();
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
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.writeByte(mode);
        pds.writeFloat(value);
        return new PacketPlayOutGameStateChange(pds);
    }

    @Override
    public Object packetTimes(int in, int stay, int out) {
        return new ClientboundSetTitlesAnimationPacket(in, stay, out);
    }

    @Override
    public Object packetTitleMessage(String text) {
        return new ClientboundSetTitleTextPacket(s(text));
    }

    @Override
    public Object packetSubtitleMessage(String text) {
        return new ClientboundSetSubtitleTextPacket(s(text));
    }

    @Override
    public Object packetActionbarMessage(String text) {
        return new PacketPlayOutChat(s(text), ChatMessageType.c, null);
    }

    @Override
    public Object packetJsonMessage(String text) {
        return new PacketPlayOutChat(s(text), ChatMessageType.a, null);
    }

    @Override
    public Object packetResetTitle() {
        return new ClientboundClearTitlesPacket(true);
    }

    @Override
    public Object packetClearTitle() {
        return new ClientboundClearTitlesPacket(false);
    }

    @Override
    public Object packetHeaderFooter(String header, String footer) {
        return new PacketPlayOutPlayerListHeaderFooter(s(header), s(footer));
    }

    @Override
    public Object packetBlockAction(Location l, int action, int param, int blockType) {
        BlockPosition position = blockPos(l);
        Block block = Block.a(blockType).b();
        return new PacketPlayOutBlockAction(position, block, param, blockType);
    }

    @Override
    public Object packetBlockChange(Location l, int blockId, byte blockData) {
        return new PacketPlayOutBlockChange(blockPos(l), Block.a(blockId << 4 | (blockData & 15)));
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
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.d(eid);
        List<DataWatcher.Item<?>> items = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof DataWatcher.Item) {
                items.add((DataWatcher.Item<?>) object);
            }
        }
        DataWatcher.a(items, pds);
        return new PacketPlayOutEntityMetadata(pds);
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
        return getEntityTypes(type).map(IRegistry.W::a).orElse(-1);
    }

    @Override
    public double getEntityHeight(EntityType type) {
        return getEntityTypes(type).map((t) -> t.m().b).orElse(0f);
    }

    @Override
    public double getEntityWidth(EntityType type) {
        return getEntityTypes(type).map((t) -> t.m().a).orElse(0f);
    }

    @Override
    public Object packetEntityAnimation(int eid, int animation) {
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.d(eid);
        pds.writeByte(animation);
        return new PacketPlayOutAnimation(pds);
    }

    @Override
    public Object packetTeleportEntity(int eid, Location l, boolean onGround) {
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.d(eid);
        pds.writeDouble(l.getX());
        pds.writeDouble(l.getY());
        pds.writeDouble(l.getZ());
        pds.writeByte((byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        pds.writeByte((byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        pds.writeBoolean(onGround);
        return new PacketPlayOutEntityTeleport(pds);
    }

    @Override
    public Object packetSpawnEntity(int eid, UUID id, EntityType type, Location l) {
        return new PacketPlayOutSpawnEntity(
                eid,
                id,
                l.getX(),
                l.getY(),
                l.getZ(),
                l.getYaw(),
                l.getPitch(),
                getEntityTypes(type).orElse(null),
                0,
                Vec3D.a
        );
    }

    @Override
    public Object packetSpawnEntityLiving(int eid, UUID id, EntityType type, Location l) {
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.d(eid);
        pds.a(id);
        pds.d(getEntityTypeId(type));
        pds.writeDouble(l.getX());
        pds.writeDouble(l.getY());
        pds.writeDouble(l.getZ());
        pds.writeByte((byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        pds.writeByte((byte) ((int) (l.getPitch() * 256.0F / 360.0F)));
        pds.writeByte((byte) ((int) (l.getYaw() * 256.0F / 360.0F)));
        pds.writeShort(0);
        pds.writeShort(0);
        pds.writeShort(0);
        return new PacketPlayOutSpawnEntityLiving(pds);
    }

    @Override
    public Object packetSetEquipment(int eid, EntityEquipmentSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        return new PacketPlayOutEntityEquipment(eid, Collections.singletonList(new Pair<>(slot(slot), i(itemStack))));
    }

    @Override
    public Object packetUpdatePassengers(int eid, int... passengers) {
        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.buffer());
        pds.d(eid);
        pds.a(passengers);
        return new PacketPlayOutMount(pds);
    }

    @Override
    public Object packetRemoveEntity(int eid) {
        return new PacketPlayOutEntityDestroy(eid);
    }

}
