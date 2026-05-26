package com.alowois.transmutated.item;

import com.alowois.transmutated.Transmutated;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Utility class for registering mod items.
 * Uses DeferredRegister to handle item registration.
 */
public class ModItems {
    /**
     * Deferred register for items.
     */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Transmutated.MODID);

    /**
     * Matter item, used in transmutation recipes.
     */
    public static final DeferredItem<Item> MATTER = ITEMS.register("matter",
            () -> new Item(new Item.Properties()));

    /**
     * Registers the item deferred register to the mod event bus.
     *
     * @param eventBus The mod event bus.
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
