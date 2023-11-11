package me.ferdithedev.overblock.games.arena;

import me.ferdithedev.overblock.games.teams.Team;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@SerializableAs("Arena")
public record Arena(String worldName, List<Spawnpoint> spawnpoints, String name, String creator,
                    double[] cuboidArenaCords) implements ConfigurationSerializable {

    public Spawnpoint getSpawnpoint(Team team) {
        String name = team.getName();
        for (Spawnpoint s : spawnpoints) {
            if (s.getTeamName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public static Arena deserialize(Map<String, Object> args) {
        String world = "";
        ArrayList<Spawnpoint> spawnpoints = null;
        String name = null;
        String builder = null;
        double x1 = 0;
        double y1 = 0;
        double z1 = 0;
        double x2 = 0;
        double y2 = 0;
        double z2 = 0;

        if (args.containsKey("world")) {
            world = (String) args.get("world");
        }

        if (args.containsKey("spawnpoints")) {
            spawnpoints = (ArrayList<Spawnpoint>) args.get("spawnpoints");
        }

        if (args.containsKey("name")) {
            name = (String) args.get("name");
        }

        if (args.containsKey("builder")) {
            builder = (String) args.get("builder");
        }

        if (args.containsKey("x1")) {
            x1 = (int) args.get("x1");
        }

        if (args.containsKey("y1")) {
            y1 = (int) args.get("y1");
        }

        if (args.containsKey("z1")) {
            z1 = (int) args.get("z1");
        }

        if (args.containsKey("x2")) {
            x2 = (int) args.get("x2");
        }

        if (args.containsKey("y2")) {
            y2 = (int) args.get("y2");
        }

        if (args.containsKey("z2")) {
            z2 = (int) args.get("z2");
        }

        double[] cuboidArenaCords = new double[]{x1, y1, z1, x2, y2, z2};

        return new Arena(world, spawnpoints, name, builder, cuboidArenaCords);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("world", worldName);
        result.put("spawnpoints", spawnpoints);
        result.put("name", name);
        result.put("builder", creator);

        result.put("x1", cuboidArenaCords[0]);
        result.put("y1", cuboidArenaCords[1]);
        result.put("z1", cuboidArenaCords[2]);
        result.put("x2", cuboidArenaCords[3]);
        result.put("y2", cuboidArenaCords[4]);
        result.put("z2", cuboidArenaCords[5]);
        return result;
    }

}
