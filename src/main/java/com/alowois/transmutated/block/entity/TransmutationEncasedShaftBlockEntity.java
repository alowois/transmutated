package com.alowois.transmutated.block.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.Vec3;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.Config;
import com.alowois.transmutated.block.TransmutationEncasedShaftBlock;
import com.alowois.transmutated.recipe.ModRecipeTypes;
import com.alowois.transmutated.recipe.TransmutationRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Block entity for the Encased Transmutation Shaft.
 * Handles the logic for detecting items, checking recipes, and performing transmutation.
 * Also manages a redstone pulse upon successful transmutation.
 */
public class TransmutationEncasedShaftBlockEntity extends KineticBlockEntity {
    /**
     * Current progress of the transmutation process.
     * Uses float to allow smooth progress with varying rotation speeds.
     */
    private float timer = 0;

    /**
     * Remaining ticks for the redstone pulse.
     */
    private int redstoneTimer = 0;

    /**
     * Behavior handling the filter slot of the block.
     */
    public FilteringBehaviour filtering;

    public TransmutationEncasedShaftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(filtering = new FilteringBehaviour(this, new FilterSlot()).withCallback(this::onFilterChanged));
        filtering.setLabel(Component.translatable("transmutated.gui.filtering_label"));
    }

    /**
     * Resets the transmutation timer when the filter is changed.
     *
     * @param stack The new filter item stack.
     */
    private void onFilterChanged(ItemStack stack) {
        timer = 0;
    }

    /**
     * Inner class defining the position and hit detection for the filter slot.
     */
    private class FilterSlot extends ValueBoxTransform {
        Direction lastSide = Direction.UP;

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            // Ensure the slot is on a face not occupied by the shaft
            if (lastSide.getAxis() == axis) {
                for (Direction d : Direction.values()) {
                    if (d.getAxis() != axis) {
                        lastSide = d;
                        break;
                    }
                }
            }
            Vec3 offset = Vec3.atLowerCornerOf(lastSide.getNormal()).scale(0.501);
            return new Vec3(0.5, 0.5, 0.5).add(offset);
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            if (lastSide.getAxis() == axis) return;

            // Rotate based on which face the slot is on
            if (lastSide == Direction.UP) {
                ms.mulPose(Axis.XP.rotationDegrees(90));
            } else if (lastSide == Direction.DOWN) {
                ms.mulPose(Axis.XP.rotationDegrees(-90));
            } else if (lastSide == Direction.NORTH) {
                // Default is North
            } else if (lastSide == Direction.SOUTH) {
                ms.mulPose(Axis.YP.rotationDegrees(180));
            } else if (lastSide == Direction.EAST) {
                ms.mulPose(Axis.YP.rotationDegrees(270));
            } else if (lastSide == Direction.WEST) {
                ms.mulPose(Axis.YP.rotationDegrees(90));
            }
        }

        @Override
        public float getScale() {
            return 0.5f;
        }

        @Override
        public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
            for (Direction side : Direction.values()) {
                if (side.getAxis() == axis) continue;
                Vec3 offset = Vec3.atLowerCornerOf(side.getNormal()).scale(0.5);
                Vec3 center = new Vec3(0.5, 0.5, 0.5).add(offset);
                // Simple distance check for interaction
                if (localHit.distanceTo(center) < 0.3) {
                    lastSide = side;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public float calculateStressApplied() {
        return (float) Config.TRANSMUTATION_STRESS.get();
    }

    /**
     * Checks if the block entity is currently emitting a redstone signal.
     *
     * @return true if emitting redstone.
     */
    public boolean isEmittingRedstone() {
        return redstoneTimer > 0;
    }

    /**
     * Finds a matching transmutation recipe for the given input and current filter.
     *
     * @param input The item stack to transmutate.
     * @return The matching recipe, or null if none found.
     */
    private TransmutationRecipe getRecipe(ItemStack input) {
        if (level == null || input.isEmpty()) return null;
        List<RecipeHolder<TransmutationRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRANSMUTATION_TYPE.get());
        Transmutated.LOGGER.info("Checking recipes for input: {}, total recipes found: {}", input, recipes.size());
        for (RecipeHolder<TransmutationRecipe> holder : recipes) {
            TransmutationRecipe recipe = holder.value();
            boolean matches = recipe.matches(filtering.getFilter(), filtering::test, input);
            Transmutated.LOGGER.info("Recipe {} matches: {}", holder.id(), matches);
            if (matches) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide)
            return;

        // Manage redstone pulse timer
        if (redstoneTimer > 0) {
            redstoneTimer--;
            if (redstoneTimer == 0) {
                level.setBlock(worldPosition, getBlockState().setValue(TransmutationEncasedShaftBlock.POWERED, false), 3);
                level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            }
        }

        float speed = Math.abs(getSpeed());
        if (speed <= 0.001f) {
            return;
        }

        // Higher speed is even more rewarding.
        // Formula: Progress per tick = (speed * speed) / (256 * 256)
        // This makes the time taken T = target / (speed / 256)^2 = (target * 65536) / speed^2
        // With default target = 8, T = 524288 / speed^2
        double progress = (speed * speed) / 65536.0;
        timer += (float) progress;

        if (timer >= Config.TRANSMUTATION_TIMER.get()) {
            timer = 0;

            // Send redstone pulse regardless of transmutation success
            redstoneTimer = 10; // 0.5s redstone pulse
            level.setBlock(worldPosition, getBlockState().setValue(TransmutationEncasedShaftBlock.POWERED, true), 3);
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());

            performTransmutation();
        }
    }

    /**
     * Executes the transmutation on valid items and depots in range.
     * Checks for recipes and handles item consumption.
     */
    private void performTransmutation() {
        Transmutated.LOGGER.info("Performing transmutation at {}", worldPosition);
        // Collect all potential items and their recipes
        class ItemSource {
            final Object source; // ItemEntity or DepotBlockEntity
            ItemStack stack;
            final TransmutationRecipe recipe;

            ItemSource(Object source, ItemStack stack, TransmutationRecipe recipe) {
                this.source = source;
                this.stack = stack;
                this.recipe = recipe;
            }

            void setStack(ItemStack newStack) {
                this.stack = newStack;
                if (source instanceof ItemEntity ie) {
                    if (newStack.isEmpty()) ie.discard();
                    else ie.setItem(newStack);
                } else if (source instanceof DepotBlockEntity d) {
                    d.setHeldItem(newStack);
                    d.notifyUpdate();
                }
            }
        }

        List<ItemSource> sources = new ArrayList<>();
        Map<TransmutationRecipe, Integer> totalCounts = new HashMap<>();

        // Search for items and depots/belts in a 5x5x5 area
        AABB bounds = new AABB(worldPosition).inflate(2);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, bounds);

        for (ItemEntity ie : items) {
            if (!ie.isAlive()) continue;
            ItemStack stack = ie.getItem();
            TransmutationRecipe recipe = getRecipe(stack);
            if (recipe != null) {
                Transmutated.LOGGER.info("Found item entity source: {} with recipe", stack);
                sources.add(new ItemSource(ie, stack, recipe));
                totalCounts.put(recipe, totalCounts.getOrDefault(recipe, 0) + stack.getCount());
            }
        }

        for (BlockPos pos : BlockPos.betweenClosed(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 2, 2))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof DepotBlockEntity depot) {
                ItemStack stack = depot.getHeldItem();
                TransmutationRecipe recipe = getRecipe(stack);
                if (recipe != null) {
                    Transmutated.LOGGER.info("Found depot source at {} with item: {} and recipe", pos, stack);
                    sources.add(new ItemSource(depot, stack, recipe));
                    totalCounts.put(recipe, totalCounts.getOrDefault(recipe, 0) + stack.getCount());
                }
            }
        }

        // Process each recipe that has enough items
        Transmutated.LOGGER.info("Total recipe counts: {}", totalCounts.size());
        for (Map.Entry<TransmutationRecipe, Integer> entry : totalCounts.entrySet()) {
            TransmutationRecipe recipe = entry.getKey();
            int totalAvailable = entry.getValue();
            int inputReq = 1;
            int totalBatches = totalAvailable / inputReq;

            Transmutated.LOGGER.info("Recipe: {}, Available: {}, Required: {}, Batches: {}", recipe.getResult(), totalAvailable, inputReq, totalBatches);

            if (totalBatches > 0) {
                int toConsume = totalBatches * inputReq;
                int totalProduced = 0;
                int outputPerBatch = 1;

                for (int i = 0; i < totalBatches * outputPerBatch; i++) {
                    if (level.random.nextFloat() < recipe.getSuccessPercentage()) {
                        totalProduced++;
                    }
                }

                // Consume items from sources
                int remainingToConsume = toConsume;
                BlockPos lastPos = worldPosition;

                for (ItemSource source : sources) {
                    if (source.recipe == recipe && remainingToConsume > 0) {
                        int available = source.stack.getCount();
                        int taking = Math.min(available, remainingToConsume);
                        
                        ItemStack newStack = source.stack.copy();
                        newStack.shrink(taking);
                        source.setStack(newStack);
                        
                        remainingToConsume -= taking;
                        if (source.source instanceof ItemEntity ie) lastPos = ie.blockPosition();
                        else if (source.source instanceof DepotBlockEntity d) lastPos = d.getBlockPos();
                    }
                }

                // Spawn results
                if (totalProduced > 0) {
                    ItemStack resultStack = recipe.getResult().copyWithCount(totalProduced);
                    while (!resultStack.isEmpty()) {
                        int count = Math.min(resultStack.getMaxStackSize(), resultStack.getCount());
                        ItemStack split = resultStack.split(count);
                        ItemEntity resultEntity = new ItemEntity(level, 
                            lastPos.getX() + 0.5, lastPos.getY() + 0.5, lastPos.getZ() + 0.5, split);
                        level.addFreshEntity(resultEntity);
                    }
                }
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Timer", timer);
        compound.putInt("RedstoneTimer", redstoneTimer);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        timer = compound.getFloat("Timer");
        redstoneTimer = compound.getInt("RedstoneTimer");
    }
}
