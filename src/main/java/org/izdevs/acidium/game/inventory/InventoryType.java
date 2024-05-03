package org.izdevs.acidium.game.inventory;

/**
 * primary_inventory types indicator
 */
public enum InventoryType {
    /**
     * basic primary_inventory: that stores item forever and has no effects to player's nbt except for its storage
     */
    Inventory,
    /**
     * electron slot makes occupied items orbit around its owner
     */
    Electron,
    /**
     * internal primary_inventory that supports the implementation of crafting
     */
    _Crafting,
    /**
     * @deprecated
     * graphics rendering implementation
     */
    @Deprecated
    _Graphics,

    /**
     * armour slot makes occupied items give the effect to its owner
     * while taking damage itself instead of its owner
     */
    Armour
}
