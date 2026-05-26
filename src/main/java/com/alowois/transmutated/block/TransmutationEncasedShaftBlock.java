package com.alowois.transmutated.block;

import com.alowois.transmutated.block.entity.ModBlockEntities;
import com.alowois.transmutated.block.entity.TransmutationEncasedShaftBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

/**
 * Block class for the Transmutation Encased Shaft.
 * Extends Create's EncasedShaftBlock and adds transmutation functionality,
 * including redstone emission and filter interaction.
 */
public class TransmutationEncasedShaftBlock extends EncasedShaftBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    /**
     * Constructor for TransmutationEncasedShaftBlock.
     *
     * @param properties Block properties.
     * @param casing     Supplier for the casing block.
     */
    public TransmutationEncasedShaftBlock(Properties properties, Supplier<Block> casing) {
        super(properties, casing);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return (Class<KineticBlockEntity>) (Class<?>) TransmutationEncasedShaftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TransmutationEncasedShaftBlockEntity> getBlockEntityType() {
        return ModBlockEntities.ENCASED_TRANSMUTATION_SHAFT.get();
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() || !player.mayBuild())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TransmutationEncasedShaftBlockEntity shaftBe) {
            if (shaftBe.filtering.canShortInteract(stack) && shaftBe.filtering.setFilter(hit.getDirection(), stack))
                return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown() || !player.mayBuild())
            return InteractionResult.PASS;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TransmutationEncasedShaftBlockEntity shaftBe) {
            shaftBe.filtering.onShortInteract(player, InteractionHand.MAIN_HAND, hit.getDirection(), hit);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
