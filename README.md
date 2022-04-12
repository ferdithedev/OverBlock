# OverBlock

More or less fun Minecraft minigame including fighting with special items and an API for adding them.

## Important!
- This plugin is still under heavy development so some features aren't working as expected!

## How to use

### Setting up

1. You have to restart your server one time after you loaded the plugin the first time so less bugs can occur!
2. You will see that in the datafolder of the plugin a subfolder called `gameMaps/` is created. This is the folder where your maps a stored at. To add a map just put it into the folder. To register the arena you have to set some settings in the `arenas.yml` file which also should be created automatically:

![grafik](https://user-images.githubusercontent.com/69450649/163047792-4f04596e-6756-41c7-b716-f9741d048b43.png)

You can use this example as a guide.
|Name        |Function                                                                   |
|------------|---------------------------------------------------------------------------|
|world       |It's the exact name of the world directory you put into the gameMaps folder|
|spawnpoints |A list out of spawnpoints which consist of a team name and the spawn cords |
|name        |The name of the map which will be displayed ingame                         |
|builder     |(optional) the name of the creator of the map                              |
|x1,y1,z1    |The first coordinate of the cuboid where the arena is in                   |
|x2,y2,z2    |The second coordinate of the cuboid where the arena is in                  |
