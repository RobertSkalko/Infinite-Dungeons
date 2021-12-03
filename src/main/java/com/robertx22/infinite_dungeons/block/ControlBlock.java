package com.robertx22.infinite_dungeons.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ControlBlock extends Block implements ITileEntityProvider {

    public ControlBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
            .noOcclusion()
            .lightLevel(x -> 15));
    }

    @Override
    @Deprecated
    public List<ItemStack> getDrops(BlockState blockstate, LootContext.Builder context) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        return items;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new ControlBlockEntity();
    }
}
