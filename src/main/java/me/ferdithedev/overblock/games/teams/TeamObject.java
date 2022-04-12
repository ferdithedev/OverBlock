package me.ferdithedev.overblock.games.teams;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("Team")
public class TeamObject implements ConfigurationSerializable {

    private final String name;
    private final ChatColor color;

    public TeamObject(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public static TeamObject deserialize(Map<String, Object> args) {
        String name = "";
        ChatColor color = ChatColor.WHITE;

        if(args.containsKey("name")) {
            name = (String) args.get("name");
        }

        if(args.containsKey("color")) {
            color = ChatColor.valueOf((String) args.get("color"));
        }

        return new TeamObject(name, color);
    }

    @Override
    public Map<String, Object> serialize() {
        LinkedHashMap result = new LinkedHashMap();
        result.put("name", name);
        result.put("color", color.name());
        return result;
    }

}
