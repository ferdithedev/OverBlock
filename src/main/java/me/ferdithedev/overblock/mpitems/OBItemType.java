package me.ferdithedev.overblock.mpitems;

public enum OBItemType {

    TURRET("turret"),
    TRAP("trap"),
    WEAPON("weapon"),
    TOOL("tool");

    OBItemType(String s) {
        this.s = s;
    }

    private final String s;

    public String string() {return s;}

}
