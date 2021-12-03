package com.robertx22.infinite_dungeons.main;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

public class StructurePlacer {

    public static boolean loadStructure(ServerWorld sw, BlockPos pos, Template template) {
        PlacementSettings pset = new PlacementSettings();
        pset.setKnownShape(true);
        template.placeInWorldChunk(sw, pos, pset, sw.random);
        return true;
    }

}
