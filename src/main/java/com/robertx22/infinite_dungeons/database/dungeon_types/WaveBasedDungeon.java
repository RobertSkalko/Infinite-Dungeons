package com.robertx22.infinite_dungeons.database.dungeon_types;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.database.CompletitionScore;
import com.robertx22.infinite_dungeons.database.db_types.DungeonType;
import com.robertx22.infinite_dungeons.database.ids.DungeonTypeIds;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class WaveBasedDungeon extends DungeonType {

    public WaveBasedDungeon() {
        super(DungeonTypeIds.WAVE);
    }

    @Override
    public void onControlBlockTick(ControlBlockEntity en) {

        if (en.data.no_mobs_alive_ticks > en.data.getDungeonLayout().wave_config.between_wave_cooldown_seconds * 20) {
            // start new wave
            en.data.no_mobs_alive_ticks = 0;

            int totalwaves = en.data.getDungeonLayout().wave_config.total_waves;
            int currentwave = en.data.current_wave;

            if (totalwaves > currentwave) {

                en.data.tryAddRandomModifier(en);

                en.data.current_wave++;

                int amount = en.data.getDungeonLayout().wave_config.getMobsToSpawnForWave(en.data.current_wave);

                for (int i = 0; i < amount; i++) {
                    en.spawnMob();
                }

                ITextComponent text = new StringTextComponent("Wave " + currentwave + " / " + totalwaves).withStyle(TextFormatting.YELLOW);
                en.sendTitleMessage(text);

                //  en.sendMessage(new StringTextComponent("Wave " + currentwave + "/" + totalwaves));
            } else {

                CompletitionScore score = en.data.getDungeonLayout()
                    .getScore(en);

                en.spawnRewardsAndFinish(score);

            }

        }

    }

}
