package eu.gs.gslibrary.menu;

import eu.gs.gslibrary.menu.event.GUIItemResponse;
import eu.gs.gslibrary.utils.HeadManager;
import eu.gs.gslibrary.utils.Pair;
import eu.gs.gslibrary.utils.replacement.Replacement;
import eu.gs.gslibrary.utils.Utils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class GUINormalItem extends GUIItem {

    private String slotChar;
    private boolean update;
    private GUIItemResponse response;

    private Material material;
    private String skinData;
    private byte data;
    private int amount, customModelData;
    private String name;
    private List<String> lore;
    private boolean unbreakable;
    private List<Pair<Enchantment, Integer>> enchantments;
    private List<ItemFlag> itemFlags;
    private List<Pair<Attribute, Collection<AttributeModifier>>> attributeModifiers;

    private Replacement replacement;

    public GUINormalItem(Material material) {
        this.material = material;
        this.lore = new ArrayList<>();
        this.enchantments = new ArrayList<>();
        this.itemFlags = new ArrayList<>();
        this.attributeModifiers = new ArrayList<>();
        this.update = false;
    }

    public GUINormalItem(Material material, byte data) {
        this.material = material;
        this.data = data;
        this.enchantments = new ArrayList<>();
        this.itemFlags = new ArrayList<>();
        this.attributeModifiers = new ArrayList<>();
        this.update = false;
    }

    public GUINormalItem(Material material, byte data, int amount) {
        this.material = material;
        this.data = data;
        this.amount = amount;
        this.enchantments = new ArrayList<>();
        this.itemFlags = new ArrayList<>();
        this.attributeModifiers = new ArrayList<>();
        this.update = false;
    }

    public GUINormalItem(ItemStack itemStack) {
        itemStack = itemStack.clone();
        this.enchantments = new ArrayList<>();
        this.itemFlags = new ArrayList<>();
        this.attributeModifiers = new ArrayList<>();
        this.update = false;

        material = itemStack.getType();
        data = itemStack.getData().getData();
        amount = itemStack.getAmount();
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta.hasCustomModelData()) customModelData = meta.getCustomModelData();
            if (meta.hasDisplayName()) name = meta.getDisplayName();
            if (meta.hasLore()) lore = meta.getLore();
            if (meta.isUnbreakable()) unbreakable = meta.isUnbreakable();
            if (!meta.getItemFlags().isEmpty()) {
                itemFlags.addAll(meta.getItemFlags());
            }
            for (Enchantment enchantment : meta.getEnchants().keySet()) {
                registerEnchantment(enchantment, meta.getEnchants().get(enchantment));
            }
            for (Attribute attribute : meta.getAttributeModifiers().keySet()) {
                registerAttributeModifier(attribute, meta.getAttributeModifiers().get(attribute));
            }
        }
    }

    public void setHeadSkinPlayer(String playerName) {
        skinData = "player---" + playerName;
    }

    public void setHeadSkinBase64(String base64) {
        skinData = "base64---" + base64;
    }

    public void registerEnchantment(Enchantment enchantment, int level) {
        enchantments.add(new Pair<>(enchantment, level));
    }

    public void registerAttributeModifier(Attribute attribute, Collection<AttributeModifier> attributeModifier) {
        attributeModifiers.add(new Pair<>(attribute, attributeModifier));
    }

    public void registerItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
    }

    @Override
    public GUIItemResponse getResponse() {
        return response;
    }

    @Override
    public GUINormalItem setResponse(GUIItemResponse response) {
        this.response = response;
        return this;
    }

    @Override
    public boolean isUpdate() {
        return update;
    }

    @Override
    public GUINormalItem setUpdate(boolean update) {
        this.update = update;
        return this;
    }

    @Override
    public ItemStack loadItem(Player player, Replacement replacement) {
        if (this.replacement != null) {
            replacement = this.replacement;
        }
        ItemStack itemStack = new ItemStack(material);
        if (data > 0) itemStack.setData(new MaterialData(material, data));
        if (skinData != null) {
            if (skinData.startsWith("player---")) {
                itemStack = HeadManager.convert(HeadManager.HeadType.PLAYER_HEAD, Utils.colorize(player, skinData.replaceFirst("player---", "")));
            } else if (skinData.startsWith("base64---")) {
                itemStack = HeadManager.convert(HeadManager.HeadType.BASE64, Utils.colorize(player, skinData.replaceFirst("base64---", "")));
            }
        }
        if (amount > 0) itemStack.setAmount(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (customModelData > 0) itemMeta.setCustomModelData(customModelData);
        if (name != null) itemMeta.setDisplayName(Utils.colorize(player, replacement.replace(player, name)));
        if (lore != null && !lore.isEmpty()) {
            List<String> l = new ArrayList<>();
            for (String s : lore) {
                l.add(Utils.colorize(player, replacement.replace(player, s)));
            }
            itemMeta.setLore(l);
        }
        if (unbreakable) itemMeta.setUnbreakable(true);
        if (!enchantments.isEmpty()) {
            for (Pair<Enchantment, Integer> pair : enchantments) {
                itemMeta.addEnchant(pair.getKey(), pair.getValue(), true);
            }
        }
        if (!itemFlags.isEmpty()) {
            for (ItemFlag flag : itemFlags) {
                itemMeta.addItemFlags(flag);
            }
        }
        if (!attributeModifiers.isEmpty()) {
            for (Pair<Attribute, Collection<AttributeModifier>> pair : attributeModifiers) {
                itemMeta.addAttributeModifier(pair.getKey(), (AttributeModifier) pair.getValue());
            }
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public GUINormalItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public GUINormalItem setName(String name) {
        this.name = name;
        return this;
    }

    public GUINormalItem setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public GUINormalItem setAttributeModifiers(List<Pair<Attribute, Collection<AttributeModifier>>> attributeModifiers) {
        this.attributeModifiers = attributeModifiers;
        return this;
    }

    public GUINormalItem setData(byte data) {
        this.data = data;
        return this;
    }

    public GUINormalItem setEnchantments(List<Pair<Enchantment, Integer>> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public GUINormalItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public GUINormalItem setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public GUINormalItem setSkinData(String skinData) {
        this.skinData = skinData;
        return this;
    }

    public GUINormalItem setSlotChar(String slotChar) {
        this.slotChar = slotChar;
        return this;
    }

    public GUINormalItem setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public GUINormalItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public GUINormalItem setReplacement(Replacement replacement) {
        this.replacement = replacement;
        return this;
    }
}
