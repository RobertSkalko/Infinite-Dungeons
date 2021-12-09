package com.robertx22.infinite_dungeons.main;

import com.robertx22.infinite_dungeons.configs.IDConfig;
import com.robertx22.infinite_dungeons.db_init.RegistryInit;
import com.robertx22.infinite_dungeons.exile_events.IDExileEvents;
import com.robertx22.infinite_dungeons.packets.BuyPacket;
import com.robertx22.infinite_dungeons.packets.StartDungeonPacket;
import com.robertx22.library_of_exile.main.Packets;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(MainID.MODID)
public class CommonInit {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(MainID.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public CommonInit() {

        DungeonDeffered.registerDefferedAtStartOfModLoading();
        ModLoadingContext.get()
            .registerConfig(ModConfig.Type.SERVER, IDConfig.SPEC);

        final IEventBus bus = FMLJavaModLoadingContext.get()
            .getModEventBus();

        bus.addListener(this::commonSetupEvent);
        bus.addListener(this::interMod);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::clientSetup);
        });

        DungeonEvents.init();

        DungeonPortalsEvents.reg();

        Packets.registerClientToServerPacket(NETWORK, new StartDungeonPacket("", ""), 0);
        Packets.registerClientToServerPacket(NETWORK, new BuyPacket(1), 1);

        IDExileEvents.register();

        System.out.println("Infinite Dungeons Loaded.");
    }

    public void interMod(InterModProcessEvent event) {
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {

        RegistryInit.init();

        DungeonCapabilities.reg();
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        ClientInit.onInitializeClient();
    }

}
