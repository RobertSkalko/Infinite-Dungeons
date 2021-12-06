package com.robertx22.infinite_dungeons.main;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DungeonDeffered {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MainID.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MainID.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MainID.MODID);
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MainID.MODID);

    public static void registerDefferedAtStartOfModLoading() {
        IEventBus bus = FMLJavaModLoadingContext.get()
            .getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ENTITIES.register(bus);

        DungeonBlocks.init();
        DungeonItems.init();
        DungeonBlockEntities.init();
        DungeonEntities.init();
    }

}
