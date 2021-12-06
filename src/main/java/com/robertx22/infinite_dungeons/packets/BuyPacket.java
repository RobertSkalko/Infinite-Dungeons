package com.robertx22.infinite_dungeons.packets;

import com.robertx22.infinite_dungeons.components.PlayerIDCap;
import com.robertx22.infinite_dungeons.database.db_types.layout.ShopEntry;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.main.MyPacket;
import com.robertx22.library_of_exile.packets.ExilePacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class BuyPacket extends MyPacket<BuyPacket> {

    public int index;

    public BuyPacket(int index) {
        this.index = index;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return MainID.id("buy");
    }

    @Override
    public void loadFromData(PacketBuffer buf) {
        this.index = buf.readInt();

    }

    @Override
    public void saveToData(PacketBuffer buf) {
        buf.writeInt(index);
    }

    @Override
    public void onReceived(ExilePacketContext ctx) {

        PlayerEntity player = ctx.getPlayer();

        PlayerIDCap.get(player)
            .getControlBlock()
            .ifPresent(x -> {
                ShopEntry entry = x.data.getDungeonLayout()
                    .getShopList().shop_entries.get(index);
                if (entry.canBuy(x, player)) {
                    entry.buy(player);
                }
            });

    }

    @Override
    public MyPacket newInstance() {
        return new BuyPacket(0);
    }
}
