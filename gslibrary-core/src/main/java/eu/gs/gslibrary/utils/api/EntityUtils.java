package eu.gs.gslibrary.utils.api;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import com.google.common.base.Enums;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.gs.gslibrary.utils.Utils;
import eu.gs.gslibrary.utils.items.ItemSystem;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.Lootable;

import java.util.*;

public class EntityUtils {

    public Entity spawnEntity(Location location, EntityType entityType) {
        return location.getWorld().spawnEntity(location, entityType);
    }

    public Entity spawnEntity(Location location, Section section) {
        EntityType entityType = EntityType.valueOf(section.getString("type", "ARMOR_STAND"));

        Entity entity = spawnEntity(location, entityType);
        entity = editEntity(entity, section);

        return entity;
    }

    public Entity editEntity(Entity entity, Section section) {
        if (section.contains("name")) {
            entity.setCustomName(Utils.colorize(null, section.getString("name")));
            entity.setCustomNameVisible(true);
        }

        if (section.contains("glowing")) entity.setGlowing(section.getBoolean("glowing"));
        if (section.contains("gravity")) entity.setGravity(section.getBoolean("gravity"));
        if (section.contains("silent")) entity.setSilent(section.getBoolean("silent"));
        if (section.contains("fire-ticks")) entity.setFireTicks(section.getInt("fire-ticks"));
        if (section.contains("fall-distance")) entity.setFallDistance(section.getInt("fall-distance"));
        if (section.contains("invulnerable")) entity.setInvulnerable(section.getBoolean("invulnerable"));

        int live = section.getInt("ticks-lived");
        if (live > 0) entity.setTicksLived(live);

        if (XMaterial.supports(13)) {
            if (entity instanceof Lootable) {
                Lootable lootable = (Lootable) entity;
                long seed = section.getLong("seed");
                if (seed != 0) lootable.setSeed(seed);

            }

            if (entity instanceof Boss) {
                Boss boss = (Boss) entity;
                Section bossBarSection = section.getSection("bossbar");

                if (bossBarSection != null) {
                    BossBar bossBar = boss.getBossBar();
                    editBossBar(bossBar, bossBarSection);
                }
            }
        }

        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (section.contains("health")) {
                double hp = section.getDouble("health");
                living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                living.setHealth(hp);
            }

            if (XMaterial.supports(14)) living.setAbsorptionAmount(section.getInt("absorption"));
            if (section.contains("AI")) living.setAI(section.getBoolean("AI"));
            if (section.contains("can-pickup-items")) living.setCanPickupItems(section.getBoolean("can-pickup-items"));
            if (section.contains("collidable")) living.setCollidable(section.getBoolean("collidable"));
            if (section.contains("gliding")) living.setGliding(section.getBoolean("gliding"));
            if (section.contains("remove-when-far-away")) living.setRemoveWhenFarAway(section.getBoolean("remove-when-far-away"));
            if (XMaterial.supports(13) && section.contains("swimming")) living.setSwimming(section.getBoolean("swimming"));

            if (section.contains("max-air")) living.setMaximumAir(section.getInt("max-air"));
            if (section.contains("no-damage-ticks")) living.setNoDamageTicks(section.getInt("do-damage-ticks"));
            if (section.contains("remaining-air")) living.setRemainingAir(section.getInt("remaining-air"));
            XPotion.addEffects(living, section.getStringList("effects"));

            Section equip = section.getSection("equipment");
            if (equip != null) {
                EntityEquipment equipment = living.getEquipment();

                ItemStack helmet = ItemSystem.itemFromConfig(section.getSection("helmet"));
                equipment.setHelmet(helmet);

                ItemStack chestplate = ItemSystem.itemFromConfig(section.getSection("chestplate"));
                equipment.setChestplate(chestplate);

                ItemStack leggings = ItemSystem.itemFromConfig(section.getSection("leggings"));
                equipment.setLeggings(leggings);

                ItemStack boots = ItemSystem.itemFromConfig(section.getSection("boots"));
                equipment.setBoots(boots);

                ItemStack mainHand = ItemSystem.itemFromConfig(section.getSection("mainHand"));
                equipment.setItemInMainHand(mainHand);

                ItemStack offHand = ItemSystem.itemFromConfig(section.getSection("offHand"));
                equipment.setItemInOffHand(offHand);
            }

            if (living instanceof Ageable) { // and Breedable
                Ageable ageable = (Ageable) living;
                if (section.contains("breed")) ageable.setBreed(section.getBoolean("breed"));
                if (section.contains("baby")) {
                    if (section.getBoolean("baby")) ageable.setBaby();
                    else ageable.setAdult();
                }

                int age = section.getInt("age", 0);
                if (age > 0) ageable.setAge(age);

                if (section.contains("age-lock")) ageable.setAgeLock(section.getBoolean("age-lock"));

                if (living instanceof Animals) {
                    Animals animals = (Animals) living;
                    int loveModeTicks = section.getInt("love-mode");
                    if (loveModeTicks != 0) animals.setLoveModeTicks(loveModeTicks);

                    if (living instanceof Tameable) {
                        Tameable tam = (Tameable) living;
                        tam.setTamed(section.getBoolean("tamed"));
                    }
                }
            }
            if (living instanceof Sittable) {
                Sittable sit = (Sittable) living;
                sit.setSitting(section.getBoolean("sitting"));
            }
            if (living instanceof Spellcaster) {
                Spellcaster caster = (Spellcaster) living;
                String spell = section.getString("spell");
                if (spell != null) caster.setSpell(Enums.getIfPresent(Spellcaster.Spell.class, spell).or(Spellcaster.Spell.NONE));
            }
            if (living instanceof AbstractHorse) {
                AbstractHorse horse = (AbstractHorse) living;
                if (section.contains("domestication")) horse.setDomestication(section.getInt("domestication"));
                if (section.contains("jump-strength")) horse.setJumpStrength(section.getDouble("jump-strength"));
                if (section.contains("max-domestication")) horse.setMaxDomestication(section.getInt("max-domestication"));
            }

            if (living instanceof Enderman) {
                Enderman enderman = (Enderman) living;
                String block = section.getString("carrying");

                if (block != null) {
                    Optional<XMaterial> mat = XMaterial.matchXMaterial(block);
                    if (mat.isPresent()) {
                        ItemStack item = mat.get().parseItem();
                        if (item != null) enderman.setCarriedMaterial(item.getData());
                    }
                }
            } else if (living instanceof Sheep) {
                Sheep sheep = (Sheep) living;
                boolean sheared = section.getBoolean("sheared");
                if (sheared) sheep.setSheared(true);
            } else if (living instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) living;
                rabbit.setRabbitType(Enums.getIfPresent(Rabbit.Type.class, section.getString("color")).or(Rabbit.Type.WHITE));
            } else if (living instanceof Bat) {
                Bat bat = (Bat) living;
                if (!section.getBoolean("awake")) bat.setAwake(false);
            } else if (living instanceof Wolf) {
                Wolf wolf = (Wolf) living;
                wolf.setAngry(section.getBoolean("angry"));
                wolf.setCollarColor(Enums.getIfPresent(DyeColor.class, section.getString("color")).or(DyeColor.GREEN));
            } else if (living instanceof Creeper) {
                Creeper creeper = (Creeper) living;
                creeper.setExplosionRadius(section.getInt("explosion-radius"));
                creeper.setMaxFuseTicks(section.getInt("max-fuse-ticks"));
                creeper.setPowered(section.getBoolean("powered"));
            } else if (XMaterial.supports(10)) {
                if (XMaterial.supports(11)) {
                    if (living instanceof Llama) {
                        Llama llama = (Llama) living;
                        if (section.contains("strength")) llama.setStrength(section.getInt("strength"));
                        com.google.common.base.Optional<Llama.Color> color = Enums.getIfPresent(Llama.Color.class, section.getString("color"));
                        if (color.isPresent()) llama.setColor(color.get());
                    } else if (XMaterial.supports(12)) {
                        if (living instanceof Parrot) {
                            Parrot parrot = (Parrot) living;
                            parrot.setVariant(Enums.getIfPresent(Parrot.Variant.class, section.getString("color")).or(Parrot.Variant.RED));
                        }

                        if (XMaterial.supports(15)) {
                            if (living instanceof Bee) {
                                Bee bee = (Bee) living;
                                // Anger time ticks.
                                bee.setAnger(section.getInt("anger") * 20);
                                bee.setHasNectar(section.getBoolean("nectar"));
                                bee.setHasStung(section.getBoolean("stung"));
                                bee.setCannotEnterHiveTicks(section.getInt("disallow-hive") * 20);
                            }
                        }
                    }
                }
            }
        } else if (entity instanceof EnderSignal) {
            EnderSignal signal = (EnderSignal) entity;
            signal.setDespawnTimer(section.getInt("despawn-timer"));
            signal.setDropItem(section.getBoolean("drop-item"));
        } else if (entity instanceof ExperienceOrb) {
            ExperienceOrb orb = (ExperienceOrb) entity;
            orb.setExperience(section.getInt("exp"));
        } else if (entity instanceof Explosive) {
            Explosive explosive = (Explosive) entity;
            explosive.setIsIncendiary(section.getBoolean("incendiary"));
        } else if (entity instanceof EnderCrystal) {
            EnderCrystal crystal = (EnderCrystal) entity;
            crystal.setShowingBottom(section.getBoolean("show-bottom"));
        }

        return entity;
    }

    @SuppressWarnings("Guava")
    private static void editBossBar(BossBar bossBar, Section section) {
        String title = section.getString("title");
        if (title != null) bossBar.setTitle(Utils.colorize(null, title));

        String colorStr = section.getString("color");
        if (colorStr != null) {
            com.google.common.base.Optional<BarColor> color = Enums.getIfPresent(BarColor.class, colorStr.toUpperCase(Locale.ENGLISH));
            if (color.isPresent()) bossBar.setColor(color.get());
        }

        String styleStr = section.getString("style");
        if (styleStr != null) {
            com.google.common.base.Optional<BarStyle> style = Enums.getIfPresent(BarStyle.class, styleStr.toUpperCase(Locale.ENGLISH));
            if (style.isPresent()) bossBar.setStyle(style.get());
        }

        List<String> flagList = section.getStringList("flags");
        if (!flagList.isEmpty()) {
            Set<BarFlag> flags = EnumSet.noneOf(BarFlag.class);
            for (String flagName : flagList) {
                BarFlag flag = Enums.getIfPresent(BarFlag.class, flagName.toUpperCase(Locale.ENGLISH)).orNull();
                if (flag != null) flags.add(flag);
            }

            for (BarFlag flag : BarFlag.values()) {
                if (flags.contains(flag)) bossBar.addFlag(flag);
                else bossBar.removeFlag(flag);
            }
        }
    }

}
