package com.robertx22.infinite_dungeons.exile_events;

import com.robertx22.infinite_dungeons.database.db_types.DungeonDifficulty;
import com.robertx22.library_of_exile.events.base.ExileEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class DungeonDifficultyTooltipEvent extends ExileEvent {

    public PlayerEntity player;
    public DungeonDifficulty difficulty;
    public List<ITextComponent> tooltip;

    public DungeonDifficultyTooltipEvent(PlayerEntity player, DungeonDifficulty difficulty, List<ITextComponent> tooltip) {
        this.player = player;
        this.difficulty = difficulty;
        this.tooltip = tooltip;
    }
}
