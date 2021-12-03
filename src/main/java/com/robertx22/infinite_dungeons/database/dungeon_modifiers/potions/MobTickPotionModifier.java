package com.robertx22.infinite_dungeons.database.dungeon_modifiers.potions;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.DungeonModifier;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.ModifierType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class MobTickPotionModifier extends DungeonModifier {

    public Effect effect;
    public int amplifier;
    public int rank;

    public MobTickPotionModifier(String id, int rank, Effect effect, int amplifier) {
        super(id);
        this.effect = effect;
        this.amplifier = amplifier;
        this.rank = rank;
    }

    @Override
    public ModifierType getModifierType() {
        return ModifierType.mobTickPotion(rank);
    }

    @Override
    public void onControlBlockTick(ControlBlockEntity en) {
        if (en.data.ticks % 20 == 0) {
            en.getAllDungeonMobsAlive()
                .forEach(x -> {
                    x.addEffect(new EffectInstance(effect, 20 * 5, amplifier));
                });
        }
    }

    @Override
    public void onMobSpawn(LivingEntity en) {

    }

}
