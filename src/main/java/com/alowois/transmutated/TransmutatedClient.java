package com.alowois.transmutated;

import com.alowois.transmutated.block.entity.ModBlockEntities;
import com.alowois.transmutated.block.entity.TransmutationEncasedShaftRenderer;
import com.alowois.transmutated.compat.ponder.TransmutatedPonderPlugin;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Handles client-side initialization for the Transmutated mod.
 * This class is only loaded on the client and is safe to access client-only code.
 */
@Mod(value = Transmutated.MODID, dist = Dist.CLIENT)
public class TransmutatedClient {
    /**
     * Constructor for the client-side mod class.
     * Registers client-specific event listeners and configuration screen factories.
     *
     * @param container    The mod container.
     * @param modEventBus  The mod-specific event bus.
     */
    public TransmutatedClient(ModContainer container, IEventBus modEventBus) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerRenderers);
    }

    /**
     * Fired during client setup.
     * Used for registering plugins like Ponder and other client-side logic.
     *
     * @param event The client setup event.
     */
    private void onClientSetup(FMLClientSetupEvent event) {
        Transmutated.LOGGER.info("HELLO FROM CLIENT SETUP");
        Transmutated.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        PonderIndex.addPlugin(new TransmutatedPonderPlugin());
    }

    /**
     * Registers block entity renderers.
     *
     * @param event The register renderers event.
     */
    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ENCASED_TRANSMUTATION_SHAFT.get(), TransmutationEncasedShaftRenderer::new);
    }
}
