package com.robertx22.infinite_dungeons.util;

import com.robertx22.infinite_dungeons.gui.DungeonShopScreen;
import com.robertx22.infinite_dungeons.gui.PickDungeonScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientOnly {

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void openPickDungeonScreen() {
        Minecraft.getInstance()
            .setScreen(new PickDungeonScreen());
    }

    public static void openDungeonShopScreen() {
        Minecraft.getInstance()
            .setScreen(new DungeonShopScreen());
    }
}
