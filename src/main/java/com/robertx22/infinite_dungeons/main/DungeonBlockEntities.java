package com.robertx22.infinite_dungeons.main;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class DungeonBlockEntities {

    public static RegistryObject<TileEntityType<?>> CONTROL_BLOCK = DungeonDeffered.BLOCK_ENTITIES.register("control_block",
        () -> TileEntityType.Builder.of(ControlBlockEntity::new, DungeonBlocks.CONTROL_BLOCK.get())
            .build(null));

    public static void init() {

    }

}
