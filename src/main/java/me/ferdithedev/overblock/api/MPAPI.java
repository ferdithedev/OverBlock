package me.ferdithedev.overblock.api;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.ItemPackage;
import me.ferdithedev.overblock.obitems.OBItemManager;
import me.ferdithedev.overblock.obitems.OBItem;

/**
 * API of the OverBlock plugin
 * @version 1.0
 */
public class MPAPI {

    private static final OBItemManager manager = OverBlock.getOBItemManager();

    /**
     * Register an ItemPackage which contains MPItems
     * @param itemPackage ItemPackage which is registered
     * @see OBItem
     * @see ItemPackage
     */
    public static void registerItemPackage(ItemPackage itemPackage) {
        manager.registerItemPackage(itemPackage);
    }

    /**
     * Remove an ItemPackage from the registered items list
     * @param itemPackage ItemPackage which is unregistered
     * @see OBItem
     * @see ItemPackage
     */
    public static void unregisterItemPackage(ItemPackage itemPackage) {manager.unregisterItemPackage(itemPackage);}

    /**
     * Get the main class of the main plugin
     * @see OverBlock
     */
    public static OverBlock getOverBlock() {return OverBlock.getInstance();}
}
