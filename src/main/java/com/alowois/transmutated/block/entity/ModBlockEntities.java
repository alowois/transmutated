package com.alowois.transmutated.block.entity;

import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.block.ModBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Utility class for registering mod block entities.
 * Uses DeferredRegister to handle the registration of BlockEntityType.
 */
public class ModBlockEntities {
    /**
     * Deferred register for block entity types.
     */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Transmutated.MODID);

    /**
     * The block entity type for the Encased Transmutation Shaft.
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TransmutationEncasedShaftBlockEntity>> ENCASED_TRANSMUTATION_SHAFT =
            BLOCK_ENTITIES.register("encased_transmutation_shaft",
                    () -> BlockEntityType.Builder.of((pos, state) -> new TransmutationEncasedShaftBlockEntity(ModBlockEntities.ENCASED_TRANSMUTATION_SHAFT.get(), pos, state), ModBlocks.ENCASED_TRANSMUTATION_SHAFT.get()).build(null));

    /**
     * Registers the block entity deferred register to the mod event bus.
     *
     * @param eventBus The mod event bus.
     */
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
