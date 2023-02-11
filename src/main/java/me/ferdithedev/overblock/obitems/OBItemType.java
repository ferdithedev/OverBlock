package me.ferdithedev.overblock.obitems;

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
