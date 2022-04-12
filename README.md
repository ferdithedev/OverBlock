![MP2](https://user-images.githubusercontent.com/69450649/163052938-5a72d694-4533-4056-8669-3cce519abfb7.png)

# OverBlock

More or less fun Minecraft minigame including fighting with special items and an API for adding them.

## Important!
- This plugin is still under heavy development so some features aren't working as expected!

## How to use

### Setting up

1. You have to restart your server one time after you loaded the plugin the first time so less bugs can occur!
2. You will see that in the datafolder of the plugin a subfolder called `gameMaps/` is created. This is the folder where your maps a stored at. To add a map just put it into the folder. To register the arena you have to set some settings in the `arenas.yml` file which also should be created automatically:

![grafik](https://user-images.githubusercontent.com/69450649/163048980-fa189453-5450-4eef-822e-13e755d9e86b.png)

You can use this example as a guide.
|Name        |Function                                                                   |
|------------|---------------------------------------------------------------------------|
|world       |It's the exact name of the world directory you put into the gameMaps folder|
|spawnpoints |A list out of spawnpoints which consist of a team name and the spawn cords |
|name        |The name of the map which will be displayed ingame                         |
|builder     |(optional) the name of the creator of the map                              |
|x1,y1,z1    |The first coordinate of the cuboid where the arena is in                   |
|x2,y2,z2    |The second coordinate of the cuboid where the arena is in                  |

3. As the last step you want to edit the `config.yml` which is also created in the datafolder of the plugin.

|Name              |Function                                                                                         |
|------------------|-------------------------------------------------------------------------------------------------|
|lobby             |The exact name of the world which is used as the lobby                                           |
|playersperteam    |The amount of players which can fit into one team                                                |
|roundtime         |The time in minutes a round lasts when nobody wins                                               |
|Spawnlocation     |The location where players spawn when they join on the server (will be switchable in near future)|
|Lobbyspawnlocation|The location where players will be teleported to when joining the lobby                          |

4. To start a game just teleport with at least 2 players in the lobby world and wait.

### Adding items

First you have to create a new class with is extending the MPItem class.

```java
public class MagicWand extends MPItem {

    public MagicWand(JavaPlugin plugin) {
        super(plugin, Material.STICK, "Magic Wand", 40, MPItemType.WEAPON, MPItemRarity.SPECIAL, "§7Make some magic stuff (or staff???)");
    }

    @Override
    public void function(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    @Override
    public void click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(this.noCooldown(e.getPlayer())) {
                function(e.getPlayer());
            } else {
                MPItemManager.cooldownMessage(e.getPlayer());
            }
        }
    }
}
```

The arguments of a MPItem are the following:

|Argument             |Description                                                                                                               |
|---------------------|--------------------------------------------------------------------------------------------------------------------------|
|plugin   (JavaPlugin)|The plugin you are writing in                                                                                             | 
|material   (Material)|The material your item is supposed to be                                                                                  |
|name         (String)|The display name the item will have                                                                                       |
|cooldown       (long)|The cooldown of the item                                                                                                  |
|type     (MPItemType)|The type of the item (TURRET, TRAP, WEAPON, TOOL)                                                                         |
|rarity  (MPItemRariy)|The rarity of the item which affects the coloring and possibility of getting one (COMMON, UNIQUE, EPIC, ULTIMATE, SPECIAL)|
|lore (String varargs)|Lore of the item                                                                                                          |
