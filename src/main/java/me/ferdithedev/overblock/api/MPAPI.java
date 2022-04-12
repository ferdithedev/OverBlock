package me.ferdithedev.overblock.api;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.mpitems.ItemPackage;
import me.ferdithedev.overblock.mpitems.MPItemManager;

/**
 * API of the OverBlock plugin
 * @version 1.0
 */
public class MPAPI {

    private static final MPItemManager manager = OverBlock.getMPItemManager();

    /**
     * Register an ItemPackage which contains MPItems
     * @param itemPackage ItemPackage which is registered
     * @see me.ferdithedev.overblock.mpitems.MPItem
     * @see ItemPackage
     */
    public static void registerItemPackage(ItemPackage itemPackage) {
        manager.registerItemPackage(itemPackage);
    }

    /**
     * Remove an ItemPackage from the registered items list
     * @param itemPackage ItemPackage which is unregistered
     * @see me.ferdithedev.overblock.mpitems.MPItem
     * @see ItemPackage
     */
    public static void unregisterItemPackage(ItemPackage itemPackage) {manager.unregisterItemPackage(itemPackage);}

}
