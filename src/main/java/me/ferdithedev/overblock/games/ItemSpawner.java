package me.ferdithedev.overblock.games;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.obitems.OBItemRarity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class ItemSpawner {

    private static final Random random = new Random();

    private final List<Item> existingItems = new ArrayList<>();

    private static final String silverUrl = "http://textures.minecraft.net/texture/2bdd62f25f4a49cc42e054a3f212c3e0092138299172d7d8f3d438214ca972ac";
    private static final String purpleUrl = "http://textures.minecraft.net/texture/27df3991d918f959248fcf84f17bb652d12b7f86291d52227c25e929f2e4b4df";
    private static final String redUrl = "http://textures.minecraft.net/texture/8045743910f23cecff1008ab6adf797d8e6297b2a43c70df572f2e8245697237";

    private static final ItemStack silver = getSkull(silverUrl);
    private static final ItemStack purple = getSkull(purpleUrl);
    private static final ItemStack red = getSkull(redUrl);

    static {
        NamespacedKey boxkey = new NamespacedKey(OverBlock.getInstance(), "boxtype");

        ItemMeta silverMeta = silver.getItemMeta();
        if (silverMeta != null) {
            silverMeta.getPersistentDataContainer().set(boxkey, PersistentDataType.INTEGER, 1);
            silver.setItemMeta(silverMeta);
        }

        ItemMeta purpleMeta = purple.getItemMeta();
        if (purpleMeta != null) {
            purpleMeta.getPersistentDataContainer().set(boxkey, PersistentDataType.INTEGER, 2);
            purple.setItemMeta(purpleMeta);
        }

        ItemMeta redMeta = red.getItemMeta();
        if (redMeta != null) {
            redMeta.getPersistentDataContainer().set(boxkey, PersistentDataType.INTEGER, 3);
            red.setItemMeta(redMeta);
        }
    }

    public static void spawnItem(int randomInt, Location loc) {
        while (loc.getBlock().getType() != Material.AIR) {
            loc.add(0,1,0);
        }
        int i = random.nextInt(100);
        if(randomInt > 0) i = randomInt;
        ItemStack itemStack;
        if(i < 80) {
            itemStack = silver;
        } else if (i > 80 && i < 98) {
            itemStack = purple;
        } else {
            itemStack = red;
        }

        if(loc.getWorld() != null) {
            loc.getWorld().dropItem(loc,itemStack);
        }
    }

    public static OBItem getItem(int luck) {
        OBItemRarity rarity = rarityBasedOnLuck(luck, null);
        if(rarity==null)return null;
        return OverBlock.getItemManager().getRandomOBItemByRarity(rarity);
    }

    private static OBItemRarity rarityBasedOnLuck(int luck, List<OBItemRarity> notPossible) {
        int i = random.nextInt(50);
        i -= luck;
        List<OBItemRarity> rarities = new ArrayList<>(Arrays.asList(OBItemRarity.values()));
        if(notPossible != null) rarities.removeAll(notPossible);
        if(rarities.isEmpty()){
            OverBlock.getInstance().getLogger().log(Level.WARNING,"WARNING! There are no items enabled in 'items.yml'");
            return null;
        }
        Collections.reverse(rarities);
        OBItemRarity selected = null;
        for(OBItemRarity rarity : rarities) {
            if(i < rarity.getChance()) {
                selected = rarity;
                break;
            }
        }

        if(selected != null && OverBlock.getItemManager().getRandomOBItemByRarity(selected) != null) {
            return selected;
        } else {
            if(notPossible == null) notPossible = new ArrayList<>();
            notPossible.add(selected);
            return rarityBasedOnLuck(luck,notPossible);
        }
    }

    public static ItemStack getSkull(String url) {
        ItemStack skull= new ItemStack(Material.PLAYER_HEAD);

        if (url == null || url.isEmpty())
            return skull;

        ItemMeta skullMeta = skull.getItemMeta();

        if(skullMeta == null) return skull;

        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
        PlayerTextures texture = profile.getTextures();
        try {
            texture.setSkin(new URL(url));
        } catch (MalformedURLException e) {
            OverBlock.printWarn("Can't resolve texture URL: " + url);
            return skull;
        }
        profile.setTextures(texture);
        ((SkullMeta) skullMeta).setOwnerProfile(profile);

        skull.setItemMeta(skullMeta);
        return skull;
    }

    public ItemStack getSilver() {
        return silver;
    }

    public ItemStack getPurple() {
        return purple;
    }

    public ItemStack getRed() {
        return red;
    }
}
