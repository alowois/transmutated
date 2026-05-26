package com.alowois.transmutated.compat.ponder.scenes;
 
import com.alowois.transmutated.block.ModBlocks;
import com.alowois.transmutated.block.entity.TransmutationEncasedShaftBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
 
public class TransmutationScenes {
    public static void transmutation(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("transmutation", Component.translatable("transmutated.ponder.transmutation.header").getString());
        scene.configureBasePlate(0, 0, 5);
        scene.scaleSceneView(0.9f);
        scene.setSceneOffsetY(-1);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
 
        BlockPos center = new BlockPos(2, 1, 2);
 
        // Shaft setup
        for (int x = 0; x < 5; x++) {
            if (x == 2) continue;
            scene.world().setBlock(new BlockPos(x, 1, 2), AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X), false);
        }
        scene.world().setBlock(center, AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X), false);
 
        scene.world().showSection(util.select().fromTo(0, 1, 2, 4, 1, 2), Direction.DOWN);
        scene.idle(10);
 
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_1").getString())
                .attachKeyFrame()
                .pointAt(util.vector().topOf(center))
                .placeNearTarget();
 
        scene.idle(20);
        scene.world().setBlock(center, ModBlocks.ENCASED_TRANSMUTATION_SHAFT.get().defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X), true);
        scene.idle(50);
 
        scene.world().setKineticSpeed(util.select().everywhere(), 128f);
        scene.effects().rotationSpeedIndicator(center);
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_2").getString())
                .attachKeyFrame()
                .pointAt(util.vector().topOf(center))
                .placeNearTarget();
        scene.idle(70);
 
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_3").getString())
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(center))
                .placeNearTarget();
        scene.idle(70);
 
        Vec3 filterSlot = util.vector().of(2.5, 1.5, 2.0);
        scene.overlay().showFilterSlotInput(filterSlot, Direction.NORTH, 60);
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_4").getString())
                .attachKeyFrame()
                .pointAt(filterSlot)
                .placeNearTarget();
        scene.idle(20);
        scene.overlay().showControls(filterSlot, Pointing.DOWN, 30).withItem(new ItemStack(Items.NETHERRACK));
        scene.idle(10);
        scene.world().modifyBlockEntity(center, TransmutationEncasedShaftBlockEntity.class, be -> {
            be.filtering.setFilter(Direction.NORTH, new ItemStack(Items.NETHERRACK));
        });
        scene.idle(40);
 
        BlockPos depotPos = new BlockPos(2, 1, 1);
        scene.world().setBlock(depotPos, AllBlocks.DEPOT.getDefaultState(), true);
        scene.world().showSection(util.select().position(depotPos), Direction.SOUTH);
        scene.idle(10);
 
        ItemStack stone = new ItemStack(Items.STONE);
        scene.world().createItemOnBeltLike(depotPos, Direction.UP, stone);
        scene.idle(20);
 
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_5").getString())
                .attachKeyFrame()
                .pointAt(util.vector().topOf(depotPos))
                .placeNearTarget();
 
        scene.idle(20);
        scene.world().modifyBlock(center, s -> s.setValue(BlockStateProperties.POWERED, true), false);
        scene.effects().indicateSuccess(center);
        scene.world().removeItemsFromBelt(depotPos);
        scene.world().createItemOnBeltLike(depotPos, Direction.UP, new ItemStack(Items.NETHERRACK));
        scene.idle(5);
        scene.world().modifyBlock(center, s -> s.setValue(BlockStateProperties.POWERED, false), false);
 
        scene.idle(50);
 
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_6").getString())
                .attachKeyFrame()
                .pointAt(util.vector().topOf(center))
                .placeNearTarget();
 
        for (int i = 0; i < 3; i++) {
            scene.idle(20);
            scene.world().modifyBlock(center, s -> s.setValue(BlockStateProperties.POWERED, true), false);
            scene.effects().indicateRedstone(center);
            scene.idle(5);
            scene.world().modifyBlock(center, s -> s.setValue(BlockStateProperties.POWERED, false), false);
        }
 
        scene.idle(40);
 
        scene.overlay().showText(60)
                .text(Component.translatable("transmutated.ponder.transmutation.text_7").getString())
                .attachKeyFrame()
                .placeNearTarget();
 
        scene.idle(60);
        scene.markAsFinished();
    }
}
