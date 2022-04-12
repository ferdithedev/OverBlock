package me.ferdithedev.overblock.games.arena;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("Spawnpoint")
public class Spawnpoint implements ConfigurationSerializable {

    private final String teamName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Spawnpoint(String teamName, double x, double y, double z, float yaw, float pitch) {
        this.teamName = teamName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double[] getCord() {
        return new double[]{x,y,z};
    }

    public float[] getLooking() {
        return new float[]{yaw,pitch};
    }

    public String getTeamName() {
        return teamName;
    }

    public static Spawnpoint deserialize(Map<String, Object> args) {
        String teamName = "";
        double x = 0,y = 0,z = 0;
        float yaw = 0, pitch = 0;

        if(args.containsKey("teamName")) {
            teamName = (String) args.get("teamName");
        }

        if(args.containsKey("x")) {
            x = (double) args.get("x");
        }

        if(args.containsKey("y")) {
            y = (double) args.get("y");
        }

        if(args.containsKey("z")) {
            z = (double) args.get("z");
        }

        if(args.containsKey("yaw")) {
            yaw = (float) ((double) args.get("yaw"));
        }

        if(args.containsKey("pitch")) {
            pitch = (float) ((double) args.get("pitch"));
        }

        return new Spawnpoint(teamName, x, y, z, yaw, pitch);
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap result = new LinkedHashMap();
        result.put("teamName", teamName);
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        result.put("yaw", yaw);
        result.put("pitch", pitch);
        return result;
    }
}
