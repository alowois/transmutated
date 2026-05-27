package com.alowois.transmutated.compat.ponder;

import com.alowois.transmutated.Transmutated;
import com.alowois.transmutated.block.ModBlocks;
import com.alowois.transmutated.compat.ponder.scenes.TransmutationScenes;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredBlock;

public class TransmutatedPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Transmutated.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<DeferredBlock<?>> HELPER = helper.withKeyFunction(DeferredBlock::getId);

        HELPER.forComponents(ModBlocks.TRANSMUTATION_CASING)
                .addStoryBoard("transmutation_casing/transmutation", TransmutationScenes::transmutation, AllCreatePonderTags.KINETIC_APPLIANCES);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<DeferredBlock<?>> HELPER = helper.withKeyFunction(DeferredBlock::getId);

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(ModBlocks.TRANSMUTATION_CASING);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {
    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {
    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {
    }
}