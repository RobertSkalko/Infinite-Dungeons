package com.robertx22.infinite_dungeons.components;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.components.data.PlayerData;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.components.forge.BaseProvider;
import com.robertx22.library_of_exile.components.forge.BaseStorage;
import com.robertx22.library_of_exile.components.forge.IPlayerCap;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class PlayerIDCap implements IPlayerCap {

    public static final ResourceLocation RESOURCE = new ResourceLocation(MainID.MODID, "data");

    @CapabilityInject(PlayerIDCap.class)
    public static final Capability<PlayerIDCap> Data = null;

    public static PlayerIDCap get(PlayerEntity entity) {
        return entity.getCapability(Data)
            .orElse(null);
    }

    @Override
    public String getCapIdForSyncing() {
        return "infinite_dungeons_player_data";
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                event.addCapability(RESOURCE, new Provider((PlayerEntity) event.getObject()));
            }
        }
    }

    public static class Provider extends BaseProvider<PlayerIDCap, PlayerEntity> {
        public Provider(PlayerEntity owner) {
            super(owner);
        }

        @Override
        public PlayerIDCap newDefaultImpl(PlayerEntity owner) {
            return new PlayerIDCap(owner);
        }

        @Override
        public Capability<PlayerIDCap> dataInstance() {
            return Data;
        }
    }

    public static class Storage implements BaseStorage<PlayerIDCap> {

    }

    public PlayerData data = new PlayerData();

    public PlayerEntity owner;

    public PlayerIDCap(PlayerEntity en) {
        this.owner = en;
    }

    public Optional<ControlBlockEntity> getControlBlock() {
        try {
            return Optional.of((ControlBlockEntity) this.owner.level.getBlockEntity(data.control_block_pos));
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean isInDungeon() {

        if (getControlBlock().isPresent()) {
            return getControlBlock().get()
                .isInsideDungeon(owner.blockPosition());
        }

        return false;

    }

    static String LOC = "rep";

    @Override
    public CompoundNBT saveToNBT() {

        CompoundNBT nbt = new CompoundNBT();
        try {

            if (data != null) {
                LoadSave.Save(data, nbt, LOC);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbt;

    }

    @Override
    public void loadFromNBT(CompoundNBT nbt) {
        try {
            this.data = LoadSave.Load(PlayerData.class, new PlayerData(), nbt, LOC);
            if (data == null) {
                data = new PlayerData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
