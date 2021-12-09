package com.robertx22.infinite_dungeons.main;

import com.robertx22.infinite_dungeons.block.ControlBlockEntity;
import com.robertx22.infinite_dungeons.components.MobIDCap;
import com.robertx22.infinite_dungeons.components.PlayerIDCap;
import com.robertx22.infinite_dungeons.components.data.BuyHistoryData;
import com.robertx22.infinite_dungeons.database.db_types.DungeonDifficulty;
import com.robertx22.infinite_dungeons.database.db_types.group.DungeonGroup;
import com.robertx22.infinite_dungeons.database.db_types.layout.DungeonLayout;
import com.robertx22.infinite_dungeons.exile_events.IDExileEvents;
import com.robertx22.infinite_dungeons.exile_events.TryStartDungeonEvent;
import com.robertx22.infinite_dungeons.item.DungeonKeyItem;
import com.robertx22.infinite_dungeons.item.KeyData;
import com.robertx22.infinite_dungeons.util.ClientOnly;
import com.robertx22.library_of_exile.main.ForgeEvents;
import com.robertx22.library_of_exile.utils.Watch;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DungeonEvents {

    public static void startDungeon(ItemStack key, PlayerEntity player, DungeonLayout layout, DungeonDifficulty difficulty) {

        try {
            World world = player.level;

            if (!world.isClientSide) {

                TryStartDungeonEvent event = new TryStartDungeonEvent(player, layout, difficulty);
                IDExileEvents.TRY_START_DUNGEON.callEvents(event);

                if (!event.canStart) {
                    return;
                }

                if (world.dimensionType()
                    .hasCeiling()) {
                    player.displayClientMessage(new StringTextComponent("Can't do this in a dimension with a ceiling."), false);
                    return;
                }

                Watch watch = new Watch();

                KeyData data = DungeonKeyItem.getData(key);

                if (data == null) {
                    return;
                }

                TemplateManager templatemanager = ((ServerWorld) world).getStructureManager();

                Watch load = new Watch();

                Template template = templatemanager.get(new ResourceLocation(layout.structure_file));
                load.print("load took");
                BlockPos pos = new ChunkPos(player.blockPosition()).getWorldPosition();

                pos = new BlockPos(pos.getX(), world.getMaxBuildHeight() - template.getSize()
                    .getY() - 1, pos.getZ());

                MutableBoundingBox box = template.getBoundingBox(new PlacementSettings(), pos);

                for (int x = box.x0; x < box.x1; x++) {
                    for (int y = box.y0; y < box.y1; y++) {
                        for (int z = box.z0; z < box.z1; z++) {
                            if (!world.getBlockState(new BlockPos(x, y, z))
                                .isAir()) {
                                player.displayClientMessage(new StringTextComponent("Sky space has to be completely empty above you."), false);
                                return;
                            }
                        }
                    }
                }

                PlacementSettings pset = new PlacementSettings();
                pset.setKnownShape(true);

                key.shrink(1);

                //  StructureHelper.spawnStructure(layout.structure_file, pos, Rotation.NONE, 0, (ServerWorld) world, false);
                //world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);

                template.placeInWorld((IServerWorld) world, pos, pset, world.random);

                watch.print("putting structure");

                Watch after = new Watch();

                ControlBlockEntity control = null;

                List<BlockPos> chestpos = new ArrayList<>();
                BlockPos tpsurfacepos = player.blockPosition();

                List<PlayerEntity> players = new ArrayList<>();

                for (int x = box.x0; x <= box.x1; x++) {
                    for (int y = box.y0; y <= box.y1; y++) {
                        for (int z = box.z0; z <= box.z1; z++) {
                            BlockPos p = new BlockPos(x, y, z);
                            BlockState state = world.getBlockState(p);
                            if (state.getBlock() == Placeholders.PLAYER_SPAWN) {

                                players.add(player);

                                player.level.getEntities(player, player.getBoundingBox()
                                        .inflate(20))
                                    .forEach(e -> {
                                        if (e instanceof PlayerEntity) {
                                            players.add((PlayerEntity) e);
                                        }
                                    });

                                for (PlayerEntity e : players) {
                                    e.teleportTo(p.getX(), p.getY() + 1, p.getZ());
                                }

                                world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);

                            } else if (state
                                .getBlock() == Placeholders.CONTROL_BLOCK) {
                                world.setBlock(p, DungeonBlocks.CONTROL_BLOCK.get()
                                    .defaultBlockState(), 1);
                                control = (ControlBlockEntity) world.getBlockEntity(p);
                            } else if (state
                                .getBlock() == Placeholders.REWARD_CHEST) {
                                chestpos.add(p);
                                world.setBlock(p, Blocks.AIR.defaultBlockState(), 1);
                            }
                        }
                    }
                }
                if (control == null) {
                    throw new Exception("Control block wasn't found!");
                }

                control.data.chests_pos = chestpos;
                control.data.tp_to_surface_pos = tpsurfacepos;
                control.data.dungeon_layout = layout.GUID();

                control.data.dungeon_boundary_0 = new BlockPos(box.x0, box.y0, box.z0);
                control.data.dungeon_boundary_1 = new BlockPos(box.x1, box.y1, box.z1);

                control.data.difficulty = difficulty.GUID();

                for (PlayerEntity e : players) {
                    PlayerIDCap cap = PlayerIDCap.get(e);
                    cap.data.control_block_pos = control.getBlockPos();
                    cap.data.current_dun_uuid = control.data.dungeon_uuid;
                    cap.data.buy_history = new BuyHistoryData();
                    cap.syncToClient(e);
                }

                after.print("finding blocks ");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void init() {

        ForgeEvents.registerForgeEvent(LivingDamageEvent.class, new Consumer<LivingDamageEvent>() {
            @Override
            public void accept(LivingDamageEvent event) {

                try {
                    // disable environmental damage for dungeon mobs
                    if (event.getEntity() instanceof LivingEntity) {
                        if (event.getEntity() instanceof PlayerEntity == false) {
                            MobIDCap.get(event.getEntityLiving())
                                .getControlBlock()
                                .ifPresent(x -> {
                                    if (event.getSource()
                                        .getDirectEntity() == null) {
                                        event.setAmount(0);
                                        event.setCanceled(true);
                                    }
                                });
                        }
                    }

                    Entity entity = event.getSource()
                        .getDirectEntity();

                    if (entity instanceof LivingEntity) {
                        LivingEntity en = (LivingEntity) entity;

                        if (en instanceof PlayerEntity == false) {

                            MobIDCap cap = MobIDCap.get(en);

                            cap.getControlBlock()
                                .ifPresent(x -> {
                                    event.setAmount(event.getAmount() * x.data.getDifficulty().mob_dmg_multi);
                                });
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, EventPriority.HIGHEST);

        ForgeEvents.registerForgeEvent(TickEvent.PlayerTickEvent.class, new Consumer<TickEvent.PlayerTickEvent>() {
            @Override
            public void accept(TickEvent.PlayerTickEvent event) {
                try {

                    if (event.player.tickCount % 10 != 0) {
                        return;
                    }

                    if (!event.player.level.isClientSide) {
                        ServerPlayerEntity player = (ServerPlayerEntity) event.player;

                        GameType current = player.gameMode.getGameModeForPlayer();

                        if (PlayerIDCap.get(player)
                            .isInDungeon()) {
                            if (current == GameType.SURVIVAL) {
                                PlayerIDCap.get(player).data.orig_gamemode = current.getName();
                                player.setGameMode(GameType.ADVENTURE);
                            }
                        } else {
                            if (!PlayerIDCap.get(player).data.orig_gamemode.isEmpty()) {
                                GameType mode = GameType.byName(PlayerIDCap.get(player).data.orig_gamemode);
                                player.setGameMode(mode);
                                PlayerIDCap.get(player).data.orig_gamemode = "";
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        ForgeEvents.registerForgeEvent(LivingDeathEvent.class, new Consumer<LivingDeathEvent>() {
            @Override
            public void accept(LivingDeathEvent event) {
                if (event.getEntityLiving() instanceof PlayerEntity == false) {

                }
            }
        });

        ForgeEvents.registerForgeEvent(PlayerInteractEvent.EntityInteract.class, new Consumer<PlayerInteractEvent.EntityInteract>() {
            @Override
            public void accept(PlayerInteractEvent.EntityInteract event) {
                if (event.getTarget() instanceof VillagerEntity) {
                    PlayerIDCap cap = PlayerIDCap.get(event.getPlayer());
                    cap.getControlBlock()
                        .ifPresent(x -> {
                            if (x.data.cleared) {
                                if (event.getWorld().isClientSide) {
                                    ClientOnly.openDungeonShopScreen();
                                }
                                event.setResult(Event.Result.DENY);
                                event.setCanceled(true);
                            }
                        });
                }
            }

        });

        ForgeEvents.registerForgeEvent(PlayerInteractEvent.RightClickItem.class, new Consumer<PlayerInteractEvent.RightClickItem>() {
            @Override
            public void accept(PlayerInteractEvent.RightClickItem event) {

                try {
                    if (event.getEntity().level.isClientSide) {

                        if (event.getEntity() instanceof PlayerEntity == false) {
                            return;
                        }
                        ItemStack key = event.getItemStack();

                        DungeonGroup group = DungeonKeyItem.getDungeonGroup(key.getItem());

                        if (group != null) {
                            ClientOnly.openPickDungeonScreen();
                        }

                        /*
                        DungeonLayout layout = DungeonsDB.DungeonLayouts()
                            .getFilterWrapped(x -> x.dungeon_group.equals(group.GUID()))
                            .random();

                         */

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
