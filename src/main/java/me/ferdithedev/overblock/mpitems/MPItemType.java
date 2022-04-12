package me.ferdithedev.overblock.mpitems;

public enum MPItemType {

    TURRET("turret"),
    TRAP("trap"),
    WEAPON("weapon"),
    TOOL("tool");

    MPItemType(String s) {
        this.s = s;
    }

    private final String s;

    public String string() {return s;}

}
