package me.ferdithedev.overblock.obitems.packagebrowser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.util.invs.InventoryItemCreator;
import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.stream.StreamSupport;

public class PackageBrowser {

    private final List<OnlinePackage> onlinePackageList;
    private final JavaPlugin plugin;

    public PackageBrowser(JavaPlugin plugin) {
        this.plugin = plugin;
        onlinePackageList = collectPackages();
    }

    private List<OnlinePackage> collectPackages() {
        Gson gson = new Gson();
        List<OnlinePackage> list = new ArrayList<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                JsonObject obj =  gson.fromJson(json(new URL("https://raw.githubusercontent.com/ferdithedev/OverBlock-packages/main/packages.json")),JsonObject.class);
                JsonArray packagelist = obj.get("packages").getAsJsonArray();

                StreamSupport.stream(packagelist.spliterator(), true).map(p -> (JsonObject) p).forEach(p -> list.add(new OnlinePackage(
                        p.get("name").getAsString(),
                        p.get("author").getAsString(),
                        p.get("description").getAsString(),
                        p.get("url").getAsString(),
                        plugin
                )));
                updatePackages();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        return list;
    }

    public void updatePackages() {
        List<ItemStack> items = new ArrayList<>();
        for(OnlinePackage onlinePackage : onlinePackageList) {
            items.add(
                    new InventoryItemCreator(new ItemStack(Material.PAPER))
                            .setDisplayName(ChatColor.RESET.toString() + ChatColor.WHITE + ChatColor.BOLD + onlinePackage.name)
                            .setLore(ChatColor.DARK_GRAY + "By: " + onlinePackage.author,"",ChatColor.RESET.toString() + ChatColor.WHITE + onlinePackage.description,"",ChatColor.DARK_GRAY+ChatColor.ITALIC.toString()+"Click to download")
                            .addString("name",onlinePackage.name).get()
            );
        }
        ListInventoryManager.inventories.get("packagebrowser").updateItems(items);
        ListInventoryManager.inventories.get("packagebrowser").update();
    }

    public static String json(URL url) {
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        } catch (IOException ignored) {
            return null;
        }
    }

    public record OnlinePackage(String name, String author, String description, String url, JavaPlugin plugin) {
        public void download(Player downloader) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                AtomicBoolean installed = new AtomicBoolean(false);
                AtomicBoolean finalInstalled = installed;
                OverBlock.getOBItemManager().getItemPackages().stream().map(itemPackage -> new String[]{itemPackage.getName(), itemPackage.getInternalName()}).forEach(names -> {
                    if(names[0].equalsIgnoreCase(name) || names[1].equalsIgnoreCase(name)) finalInstalled.set(false);
                });

                installed = Arrays.stream(OverBlock.getInstance().getServer().getPluginManager().getPlugins()).map(Plugin::getName).anyMatch(s -> s.equalsIgnoreCase(name)) ? new AtomicBoolean(true) : installed;
                installed = new File(plugin.getDataFolder().getParentFile().getAbsolutePath()+"/"+name+".jar").exists() ? new AtomicBoolean(true) : installed;

                if(!installed.get()) {
                    downloader.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "INFO! " + ChatColor.GREEN + "Starting download of package '" + name + "'!");
                    try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());

                         FileOutputStream fileOutputStream = new FileOutputStream(plugin.getDataFolder().getParentFile().getAbsolutePath()+"/"+name+".jar")) {
                        byte[] dataBuffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                        downloader.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "INFO! " + ChatColor.GREEN + "Download of package '" + name + "' complete! Restart the server to load the package!");
                    } catch (IOException ignored) {
                        plugin.getLogger().log(Level.WARNING,"Failed to download package: " + name);
                        downloader.sendMessage("§c§lERROR!§c Download of package '" + name + "' failed!");
                    }
                } else
                    downloader.sendMessage(ChatColor.of("#ff6e0d").toString() + ChatColor.BOLD + "INFO! " + ChatColor.of("#F76300").toString() + "Package '" + name + "' is already installed and won't be downloaded!");

            });

        }
    }

    public List<OnlinePackage> getPackages() {
        return onlinePackageList;
    }

    public OnlinePackage getPackageByName(String name) {
        for(OnlinePackage onlinePackage : onlinePackageList) {
            if(onlinePackage.name.equals(name)) return onlinePackage;
        }
        return null;
    }
}
