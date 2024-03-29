![OverBlock](https://user-images.githubusercontent.com/69450649/219955387-7ee2aeff-52af-40e2-81d4-ccd5ad60ee5f.png)

# OverBlock

More or less fun Minecraft minigame including fighting with special items and an API for adding them.

## Important!
- This plugin is still under heavy development so some features aren't working as expected!

## Gameplay

For a description of the gameplay take a look at the [website](https://ferdithedev.github.io/OverBlock/gameplay.html)!

## How to use

### Setting up

1. You have to restart your server one time after you loaded the plugin the first time so less bugs can occur!
2. You will see that in the datafolder of the plugin a subfolder called `gameMaps/` is created. This is the folder where your maps are stored at. To add a map just put it into the folder. To register the arena you have to set some settings in the `arenas.yml` file which also should be created automatically:

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

3. If you want you can edit the `teams.yml` file. Note that you don't have to have 4 teams.

4. As the last step you want to edit the `config.yml` which is also created in the datafolder of the plugin.

|Name              |Function                                                                                         |
|------------------|-------------------------------------------------------------------------------------------------|
|lobby             |The exact name of the world which is used as the lobby                                           |
|playersperteam    |The amount of players which can fit into one team                                                |
|roundtime         |The time in minutes a round lasts when nobody wins                                               |
|Spawnlocation     |The location where players spawn when they join on the server (will be switchable in near future)|
|Lobbyspawnlocation|The location where players will be teleported to when joining the lobby                          |

5. To start a game just teleport with at least 2 players in the lobby world and wait.

### Adding items

#### Creating the item

First you have to create a new class which is extending the OBItem class.

```java
public class MagicWand extends OBItem {

    public MagicWand(JavaPlugin plugin) {
        super(plugin, Material.STICK, "Magic Wand", 40, OBItemType.WEAPON, OBItemRarity.SPECIAL, "§7Make some magic stuff (or staff???)");
    }

    @Override
    public void function(Player player) {
        //you can do way more here and probably damage other players because it's a weapon
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    @Override
    public void click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(this.noCooldown(e.getPlayer())) {
                function(e.getPlayer());
            } else {
                OBItemManager.cooldownMessage(e.getPlayer());
            }
        }
    }
}
```

The arguments of a OBItem are the following:

|Argument             |Description                                                                                                               |
|---------------------|--------------------------------------------------------------------------------------------------------------------------|
|plugin   (JavaPlugin)|The plugin you are writing in                                                                                             | 
|material   (Material)|The material your item is supposed to be                                                                                  |
|name         (String)|The display name the item will have                                                                                       |
|cooldown       (long)|The cooldown of the item                                                                                                  |
|type     (OBItemType)|The type of the item (TURRET, TRAP, WEAPON, TOOL)                                                                         |
|rarity  (OBItemRariy)|The rarity of the item which affects the coloring and possibility of getting one (COMMON, UNIQUE, EPIC, ULTIMATE, SPECIAL)|
|lore (String varargs)|Lore of the item                                                                                                          |

#### Registering the item

First, you have to create an ItemPackage which contains your new items.
```java
ItemPackage magicWands = new ItemPackage(plugin, "magic_wands","§e§kM§r§d§lMagic Wands§r§e§kM§r", Material.STICK, "§eAdding some magic wands");
```
than you add your items to the package
```java
magicWands.addItem(new MagicWand(ExamplePlugin.getInstance()));
```
finally, you register the ItemPackage
```java
OBAPI.registerItemPackage(magicWands);
```

Now you can use your items ingame :-)

### Items UI

With the `/items` command you can open a UI where all registered ItemPackages are listed. You can click on them and see all items of them and disable them if you want.

![packages](https://user-images.githubusercontent.com/69450649/163058454-f23519a6-a9c2-4706-9706-89885d6a369f.png)

![items](https://user-images.githubusercontent.com/69450649/163058465-d82892e8-36b5-4529-87ff-b2a2e08234ad.png)

## Commands

|Command                    |Function                       |
|---------------------------|-------------------------------|
|/getobitem                 |Get a registered OBItem        |
|/skip                      |Skips the lobby waiting time   |
|/getrandomitem <luck (int)>|Get a random item              |
|/reloaditems               |Reload items.yml               |
|/items                     |Opens the item ui              |
|/spawnbox                  |Spawns a itembox underneath you|

