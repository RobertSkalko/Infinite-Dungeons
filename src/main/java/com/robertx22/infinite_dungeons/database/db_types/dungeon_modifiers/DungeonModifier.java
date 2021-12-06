package com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.db_init.RegistryTypes;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import com.robertx22.library_of_exile.registry.ExileRegistry;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class DungeonModifier implements ExileRegistry<DungeonModifier> {

    public static DungeonModifier SERIALIZER = new DungeonModifier("") {
        @Override
        public TextComponent announcement() {
            return null;
        }

        @Override
        public ModifierType getModifierType() {
            return null;
        }

        @Override
        public void onControlBlockTick(ControlBlockEntity en) {
        }

        @Override
        public void onMobSpawn(LivingEntity en) {
        }
    };

    public int weight = 1000;
    public String id = "";

    public int order = 0;

    public boolean canDungeonGetThis(ControlBlockEntity be) {
        return true;
    }

    public TextComponent announcement() {
        return new TranslationTextComponent(MainID.MODID + ".modifier." + this.id);
    }

    public abstract ModifierType getModifierType();

    public abstract void onControlBlockTick(ControlBlockEntity en);

    public abstract void onMobSpawn(LivingEntity en);

    public void onMobDeath(ExileEvents.OnMobDeath event) {

    }

    public static List<ItemStack> getAllGearOn(LivingEntity en) {

        List<ItemStack> list = new ArrayList<>();
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            list.add(en.getItemBySlot(slot));
        }
        return list;

    }

    public DungeonModifier(String id) {
        this.id = id;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return RegistryTypes.DUNGEON_MODIFIER;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return weight;
    }

}

