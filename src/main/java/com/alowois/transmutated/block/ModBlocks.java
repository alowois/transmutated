package com.alowois.transmutated.block;

import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.item.ModItems;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Utility class for registering mod blocks.
 * Handles the registration of blocks and their corresponding items using DeferredRegister.
 */
public class ModBlocks {
    /**
     * Deferred register for blocks.
     */
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Transmutated.MODID);

    /**
     * The basic transmutation casing, used as a casing for shafts.
     */
    public static final DeferredBlock<CasingBlock> TRANSMUTATION_CASING = registerBlock("transmutation_casing",
            () -> new CasingBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(3f)
            ));

    /**
     * The encased transmutation shaft block, which performs the actual transmutation.
     */
    public static final DeferredBlock<TransmutationEncasedShaftBlock> ENCASED_TRANSMUTATION_SHAFT = registerBlock("encased_transmutation_shaft",
            () -> new TransmutationEncasedShaftBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(3f)
                    .noOcclusion(),
                    TRANSMUTATION_CASING::get
            ));

    /**
     * Helper method to register a block and its associated BlockItem.
     *
     * @param name  The registry name of the block.
     * @param block A supplier for the block instance.
     * @param <T>   The block class type.
     * @return The registered block object.
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    /**
     * Helper method to register a BlockItem for a registered block.
     *
     * @param name  The registry name of the block item.
     * @param block The registered block object.
     * @param <T>   The block class type.
     */
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * Registers the block deferred register to the mod event bus.
     *
     * @param eventBus The mod event bus.
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
