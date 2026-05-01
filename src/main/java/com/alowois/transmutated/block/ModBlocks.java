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

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Transmutated.MODID);

    public static final DeferredBlock<CasingBlock> TRANSMUTATION_BLOCK = registerBlock("transmutation_block",
            () -> new CasingBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(3f)
            ));

    public static final DeferredBlock<TransmutationEncasedShaftBlock> ENCASED_TRANSMUTATION_SHAFT = registerBlock("encased_transmutation_shaft",
            () -> new TransmutationEncasedShaftBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(3f)
                    .noOcclusion(),
                    TRANSMUTATION_BLOCK::get
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
