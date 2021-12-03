package com.robertx22.infinite_dungeons.main;

import com.robertx22.infinite_dungeons.entity.TickingTNTEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;

public class DungeonEntities {

    public static RegistryObject<EntityType<?>> TICKING_TNT = DungeonDeffered.ENTITIES.register("ticking_tnt", ()
        -> EntityType.Builder.of(TickingTNTEntity::new, EntityClassification.MISC)
        .build("ticking_tnt"));
    ;

    public static void init() {

    }
}
