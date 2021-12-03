package com.robertx22.infinite_dungeons.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientInit {

    public static void onInitializeClient() {

        RenderTypeLookup.setRenderLayer(DungeonBlocks.CONTROL_BLOCK.get(), RenderType.translucent());

        RenderingRegistry.registerEntityRenderingHandler(DungeonEntities.TICKING_TNT.get(),
            (IRenderFactory<Entity>) manager -> new SpriteRenderer(manager, Minecraft.getInstance()
                .getItemRenderer()));

    }
}
