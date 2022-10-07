package eu.gs.gslibrary.nms.utils;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.EntityType;

@Getter
public enum EntitySizeEnum {
    ARMOR_STAND(1.975f, 0.5f),
    ITEM(0.25f, 0.25f),
    ;

    private final float height;
    private final float width;

    EntitySizeEnum(float height, float width) {
        this.height = height;
        this.width = width;
    }

    public static EntitySizeEnum fromEntityType(@NonNull EntityType type) {
        for (EntitySizeEnum value : values()) {
            if (value.name().equalsIgnoreCase(type.name())) {
                return value;
            }
        }
        return null;
    }

}
