package me.ferdithedev.overblock.fm;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.channels.Channels;
import java.util.logging.Level;

public class Config {

    private FileConfiguration config = null;

    private final boolean inPlugin;

    private final String subfolder;
    private final String filename;

    private final JavaPlugin plugin;

    private File file;

    public Config(JavaPlugin plugin, String filename, boolean internalFile, String... subfolder) {
        this.plugin = plugin;
        this.filename = filename;
        this.subfolder = subfolder.length > 0 ? "plugins/" + plugin.getName() + "/" + subfolder[0] : "plugins/" + plugin.getName();
        this.inPlugin = internalFile;
        if (this.inPlugin) {
            try {
                File outFile = new File(this.subfolder, filename);
                outFile.getParentFile().mkdirs();

                if (!outFile.exists()) {
                    try (InputStream fileInputStream = plugin.getResource(filename); FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                        if (fileInputStream != null) {
                            fileOutputStream.getChannel().transferFrom(Channels.newChannel(fileInputStream), 0, Integer.MAX_VALUE);
                        }
                    } catch (FileNotFoundException e) {
                        plugin.getLogger().log(Level.WARNING, "Failed to create file " + filename, e);
                    }
                }
            } catch(Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to create file " + filename, e);
            }
        }
        get().options().copyDefaults(true);
    }

    public FileConfiguration get() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public void save() {
        if(config == null) {
            reload();
        }
        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        file = new File(subfolder, filename);
        config = YamlConfiguration.loadConfiguration(file);
        if (inPlugin) {
            InputStream dataStream = plugin.getResource(filename);
            if (dataStream != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(dataStream)));
            }
        }
    }

}
