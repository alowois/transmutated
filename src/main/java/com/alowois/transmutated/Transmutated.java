package com.alowois.transmutated;

import com.alowois.transmutated.block.ModBlocks;
import com.alowois.transmutated.block.entity.ModBlockEntities;
import com.alowois.transmutated.item.ModItems;
import com.alowois.transmutated.recipe.ModRecipeTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * The main mod class for Transmutated.
 * This class handles mod initialization, registration of items, blocks, and block entities,
 * and sets up common mod configurations.
 */
@Mod(Transmutated.MODID)
public class Transmutated {
    /**
     * The unique identifier for the mod.
     */
    public static final String MODID = "transmutated";

    /**
     * Logger for the mod, used for debug and info messages.
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the main mod class.
     * Registers components to the mod event bus and initializes configuration.
     *
     * @param modEventBus  The mod-specific event bus.
     * @param modContainer The container for this mod.
     */
    public Transmutated(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipeTypes.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /**
     * Handles common setup tasks that occur after all mods have been initialized.
     * Includes registering block variants and initializing recipe types.
     *
     * @param event The common setup event.
     */
    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EncasingRegistry.addVariant(AllBlocks.SHAFT.get(), ModBlocks.ENCASED_TRANSMUTATION_SHAFT.get());
        });
    }

    /**
     * Populates creative mode tabs with mod items and blocks.
     *
     * @param event The creative mode tab content event.
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.MATTER);
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.TRANSMUTATION_CASING);
        }
    }

    /**
     * Fired when the server is starting.
     *
     * @param event The server starting event.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
