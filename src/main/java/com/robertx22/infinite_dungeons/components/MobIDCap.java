package com.robertx22.infinite_dungeons.components;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.components.data.DungeonMobData;
import com.robertx22.library_of_exile.components.forge.BaseProvider;
import com.robertx22.library_of_exile.components.forge.BaseStorage;
import com.robertx22.library_of_exile.components.forge.ICommonCap;
import com.robertx22.library_of_exile.main.Ref;
import com.robertx22.library_of_exile.utils.LoadSave;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class MobIDCap implements ICommonCap {

    public static final ResourceLocation RESOURCE = new ResourceLocation(Ref.MODID, "data");

    @CapabilityInject(MobIDCap.class)
    public static final Capability<MobIDCap> Data = null;

    public static MobIDCap get(LivingEntity entity) {
        return entity.getCapability(Data)
            .orElse(new MobIDCap(entity));
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onEntityConstruct(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof LivingEntity) {
                event.addCapability(RESOURCE, new MobIDCap.Provider((LivingEntity) event.getObject()));
            }
        }

    }

    public static class Provider extends BaseProvider<MobIDCap, LivingEntity> {
        public Provider(LivingEntity owner) {
            super(owner);
        }

        @Override
        public MobIDCap newDefaultImpl(LivingEntity owner) {
            return new MobIDCap(owner);
        }

        @Override
        public Capability<MobIDCap> dataInstance() {
            return Data;
        }
    }

    public static class Storage implements BaseStorage<MobIDCap> {

    }

    public DungeonMobData data = new DungeonMobData();

    public LivingEntity owner;

    public boolean isDungeonMob() {
        return !this.data.dungeon_uuid.isEmpty();
    }

    public Optional<ControlBlockEntity> getControlBlock() {

        try {
            return Optional.of((ControlBlockEntity) this.owner.level.getBlockEntity(data.control_block_pos));
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return Optional.empty();

    }

    public MobIDCap(LivingEntity en) {
        this.owner = en;
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
            this.data = LoadSave.Load(DungeonMobData.class, new DungeonMobData(), nbt, LOC);
            if (data == null) {
                data = new DungeonMobData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

