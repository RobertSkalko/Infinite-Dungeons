package com.robertx22.infinite_dungeons.block;

import com.robertx22.infinite_dungeons.components.MobIDCap;
import com.robertx22.infinite_dungeons.components.PlayerIDCap;
import com.robertx22.infinite_dungeons.database.CompletitionScore;
import com.robertx22.infinite_dungeons.database.DungeonsDB;
import com.robertx22.infinite_dungeons.database.db_types.DungeonDifficulty;
import com.robertx22.infinite_dungeons.database.db_types.dungeon_modifiers.DungeonModifier;
import com.robertx22.infinite_dungeons.database.db_types.layout.DungeonSpawn;
import com.robertx22.infinite_dungeons.main.DungeonBlockEntities;
import com.robertx22.library_of_exile.utils.LoadSave;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControlBlockEntity extends TileEntity implements ITickableTileEntity {

    public ControlBlockEntity(TileEntityType<?> type) {
        super(type);
    }

    public ControlBlockData data = new ControlBlockData();

    public ControlBlockEntity() {
        super(DungeonBlockEntities.CONTROL_BLOCK.get());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return new SUpdateTileEntityPacket(getBlockPos(), 0, nbt);
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net,
                             net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
        this.load(getBlockState(), pkt.getTag());
    }

    @Override
    public void tick() {
        try {

            if (level.isClientSide) {
                return;
            }

            if (data != null) {

                data.ticks++;

                if (data.ticks % 40 == 0) {

                    for (PlayerEntity p : getAllPlayers()) {
                        ServerPlayerEntity sp = (ServerPlayerEntity) p;
                        PlayerIDCap.get(sp).data.buy_history.shop_list = this.data.getDungeonLayout().shop_list;
                        PlayerIDCap.get(sp)
                            .syncToClient(sp);
                        sp.connection.send(getUpdatePacket());
                    }
                }

                if (getAllPlayers().isEmpty()) {
                    data.no_players_nearby_ticks++;

                    if (data.no_players_nearby_ticks > 20 * 5) {
                        this.destroyDungeon();
                        return;
                    }
                }

                if (data.cleared) {
                    data.ticks_until_destruction--;

                    if (data.ticks_until_destruction % 100 == 0 || (data.ticks_until_destruction % 20 == 0 && data.ticks_until_destruction < 100)) {
                        int sec = data.ticks_until_destruction / 20;
                        sendMessage(new StringTextComponent(sec + "s until Self Destruction"));
                    }
                    if (data.ticks_until_destruction < 1) {
                        this.destroyDungeon();

                    }
                    return;
                }

                if (getAllDungeonMobsAlive()
                    .size() != 0) {
                    data.no_mobs_alive_ticks = 0;
                } else {
                    data.no_mobs_alive_ticks++;

                }

                data.getDungeonLayout()
                    .getDungeonType()
                    .onControlBlockTick(this);

                for (DungeonModifier x : data.getModifiers()) {
                    x.onControlBlockTick(this);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.sendMessage(new StringTextComponent("Error in Dungeon Control Block code, destroying dungeon :("));
            this.destroyDungeon();
            this.setRemoved();
        }

    }

    public void spawnMob() {

        List<DungeonSpawn> list = data.getDungeonLayout().spawns.stream()
            .filter(x -> x.canSpawn(data))
            .collect(Collectors.toList());

        DungeonSpawn spawn = RandomUtils.weightedRandom(list);

        EntityType type = spawn.getEntityType();

        BlockPos pos = getRandomPosition(type);

        LivingEntity en = (LivingEntity) type.create(level);
        en.setPos(pos.getX(), pos.getY(), pos.getZ());
        // todo flag as dungeon mob

        if (en instanceof MobEntity) {
            MobEntity mob = (MobEntity) en;
            mob.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(pos), SpawnReason.STRUCTURE, null, null);
        }

        for (DungeonModifier x : data.getModifiers()) {
            x.onMobSpawn(en);
        }

        MobIDCap.get(en).data.dungeon_uuid = this.data.dungeon_uuid;
        MobIDCap.get(en).data.control_block_pos = getBlockPos();

        for (String mod : spawn.modifiers) {
            try {
                DungeonModifier m = DungeonsDB.DungeonModifiers()
                    .get(mod);
                m.onMobSpawn(en);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        data.mobs_spawned++;
        level.addFreshEntity(en);

        applyDifficultyMods(en);

    }

    private void applyDifficultyMods(LivingEntity en) {

        DungeonDifficulty diff = data.getDifficulty();

        AttributeModifier m2 = new AttributeModifier(
            UUID.fromString("72738c54-5357-11ec-bf63-0242ac130002"),
            Attributes.MAX_HEALTH.getDescriptionId(),
            diff.mob_hp_multi - 1F,
            AttributeModifier.Operation.MULTIPLY_BASE);

        en.getAttribute(Attributes.MAX_HEALTH)
            .addPermanentModifier(m2);

        en.heal(500000);

    }

    public void destroyDungeon() {

        try {
            for (PlayerEntity x : getAllPlayers()) {
                x.addEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 30));
            }

            this.getAllDungeonMobsAlive()
                .forEach(x -> x.kill());

            BlockPos.betweenClosed(data.dungeon_boundary_1, data.dungeon_boundary_0)
                .forEach(x -> {
                    if (level.getBlockEntity(x) != null) {
                        this.level.destroyBlock(x, true); // todo idk if i should keep or not
                    } else {
                        this.level.setBlockAndUpdate(x, Blocks.AIR.defaultBlockState());
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LivingEntity> getAllDungeonMobsAlive() {
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, this.getRenderBoundingBox()
            .inflate(40));
        list.removeIf(x -> !isInsideDungeon(x.blockPosition()));
        list.removeIf(x -> x instanceof PlayerEntity);
        // todo only mobs marked as dungeon spawns
        return list;
    }

    public Boolean isInsideDungeon(BlockPos pos) {
        return pos.getX() >= data.dungeon_boundary_0.getX() &&
            pos.getY() >= data.dungeon_boundary_0.getY() &&
            pos.getZ() >= data.dungeon_boundary_0.getZ() &&
            pos.getX() <= data.dungeon_boundary_1.getX() &&
            pos.getY() <= data.dungeon_boundary_1.getY() &&
            pos.getZ() <= data.dungeon_boundary_1.getZ();
    }

    public List<PlayerEntity> getAllPlayers() {
        List<PlayerEntity> list = level.players()
            .stream()
            .filter(x -> {
                return isInsideDungeon(x.blockPosition());
            })
            .collect(Collectors.toList());
        return list;
    }

    public void spawnRewardsAndFinish(CompletitionScore score) {

        this.data.cleared = true;

        BlockPos pos = data.chests_pos.get(0);
        if (data.getDungeonLayout().score_configs.containsKey(score)) {
            spawnChestWithLoot(pos, data.getDungeonLayout().score_configs.get(score)
                .getLootTable());

            VillagerEntity trader = EntityType.VILLAGER.create(level);
            trader.setPos(pos.getX() + 1, pos.getY(), pos.getZ());
            level.addFreshEntity(trader);
        }

        finishMessage(score);

    }

    public static String STAR = "\u272B";

    public void finishMessage(CompletitionScore score) {

        ITextComponent text = new StringTextComponent("Dungeon Cleared.").withStyle(TextFormatting.GREEN);

        if (score == CompletitionScore.FAIL) {
            text = new StringTextComponent("Dungeon Failed.").withStyle(TextFormatting.RED);
        }

        sendTitleMessage(text);

        if (score != CompletitionScore.FAIL) {
            String stars = "";

            for (int i = 0; i <= score.tier; i++) {
                stars += STAR;
            }
            ITextComponent scoretxt = new StringTextComponent("Score: " + stars).withStyle(score.format);
            sendTitleMessage(scoretxt, STitlePacket.Type.SUBTITLE);
        }
    }

    public void spawnChestWithLoot(BlockPos pos, ResourceLocation loottable) {

        level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 0);
        ChestTileEntity chest = (ChestTileEntity) level.getBlockEntity(pos);
        chest.setLootTable(loottable, level.random.nextLong());

    }

    public static boolean canPlaceMob(World world, EntityType type, BlockPos p) {
        AxisAlignedBB box = type.getAABB(p.getX(), p.getY(), p.getZ());
        boolean can = world.noCollision(box);
        return can;
    }

    public BlockPos getRandomPosition() {
        return getRandomPosition(null);
    }

    public BlockPos getRandomPosition(EntityType type) {

        BlockPos start = data.dungeon_boundary_0;
        BlockPos end = data.dungeon_boundary_1;

        BlockPos pos = null;

        while (pos == null) {

            BlockPos trypos = new BlockPos(
                RandomRange(start.getX(), end.getX(), level.random),
                RandomRange(start.getY(), end.getY(), level.random),
                RandomRange(start.getZ(), end.getZ(), level.random)
            );
            if (type == null) {
                if (level.getBlockState(trypos)
                    .isAir()) {
                    pos = trypos;
                }
            } else {
                if (canPlaceMob(level, type, trypos)) {
                    pos = trypos;
                }
            }
        }
        return pos;
    }

    public static int RandomRange(int min, int max, Random rand) {
        try {

            int reamin = Math.min(min, max);
            int realmax = Math.max(min, max);

            int result = rand.nextInt(realmax - reamin + 1);
            return MathHelper.clamp(result + reamin, reamin, realmax);
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0;
        }
    }

    public void sendTitleMessage(ITextComponent txt) {
        sendTitleMessage(txt, STitlePacket.Type.TITLE);
    }

    public void sendTitleMessage(ITextComponent txt, STitlePacket.Type type) {
        getAllPlayers().forEach(x -> {
            ServerPlayerEntity sp = (ServerPlayerEntity) x;
            sp.connection.send(new STitlePacket(type, txt));
        });
    }

    public void sendMessage(ITextComponent txt) {
        getAllPlayers().forEach(x -> {
            x.displayClientMessage(txt, false);
        });
    }

    public boolean hasNearbyPlayers() {
        return !getAllPlayers().isEmpty();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        try {
            this.data = LoadSave.Load(ControlBlockData.class, new ControlBlockData(), nbt, "dungeon");
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        try {
            LoadSave.Save(data, nbt, "dungeon");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.save(nbt);
    }

}
