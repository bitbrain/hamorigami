# Hamorigami Developer Guide

This guide is for internal use only and is directed at developers of this game.

## Assets

Please put assets into the following directories:

| Directory        | Purpose           | Naming conventions  |
| ------------- |:-------------:| -----:|
| [assets/font](assets/font) | True Type (.ttf) fonts | fontname.ttf |
| [assets/i18n](assets/i18n) | translation strings | translations_XX.properties where XX is an official [locale code](https://www.viralpatel.net/java-locale-list-tutorial/) |
| [assets/music](assets/music) | music in .ogg format | *.ogg |
| [assets/sound](assets/sound) |sound in .ogg format | *.ogg |
| [assets/particles](assets/particles) | 2D particle effects | name.p |
| [assets/texture](assets/texture) | various textures | *.jpg, *.gif, *.png |
| [assets/game.play](assets/game.play) | The gameplay file | *.play |

## Gameplay file

The gameplay file `assets/game.play` contains instructions in how the story mode is supposed to be played:

```
# this is a comment and it won't get parsed

command arg1 input 1 arg2 input2
command input
```
Each line starts with a so called **command**. Additionally, a command can have zero or more arguments. Each argument has a corresponding input, for example:
```
spawn kodama at tree
```
`spawn` is the command, `kodama` is an input, `at` is an argument and `tree` is another input.

The following section describes various concepts in gameplay files, including syntax and examples:

### Starting a new day

```
day x
````
*`x` is the number of the day*

### Defining cutscenes

Cutscenes are a way to let things happen in the game **outside** of gameplay. The idea is that you can for example spawn spirits, let them talk and move and disappear again.

> **PLEASE NOTE! Cutscenes are currently only supported at the start and at the end of a day (both is optional)**

A cutscene block is defined like this:

```
begin cutscene
# cutscene instructions go here
end cutscene
```
The following explains various cutscene instructions you can use in order to define a cutscene.

#### The player and despawning

The cutscene editor treats kodama slightly different from other entities, whereas the other spirits are referred to as their natural name.

The reason is that spawning a player during a morning cutscene **will not** despawn it at the end of a cutscene. Instead, once the gameplay starts, kodama can be controlled directly by the player. All other entities spawned within cutscenes will automatically disappear once a cutscene is over!

#### Spawning spirits (in a cutscene!)

```
spawn entity at anchorName aligned direction offset x,y
```
*`entity` is the type of spirit to spawn*<br/>
*`anchorName` the anchor to spawn the entity at*<br/>
*`aligned` the direction to align. Can be north,west,east,south,center or a combination of each*<br/>
*`offset` the offset of pixels from the point of alignment*<br/>

The following spirit entities are currently supported:

* ame
* hi
* kodama

Available anchors:

* tree
* floor

Available directions:

* south
* west
* north
* east
* center

#### Examples
```
# spawn kodama on the bottom right of the tree,
# 10 pixels to the right from that position
spawn kodama aligned south,east at tree offset 10,0

# spawn kodama on the top center of the tree
spawn kodama at tree aligned top,center

# spawn kodama at position 100,100
spawn kodama offset 100,100
```

#### Waiting

A very important concept is waiting. This allows certain things to happen before the cutscene continues.

> **PLEASE NOTE! By default, all cutscene steps are played at once, unless you specify waiting!**

```
wait x seconds
```
*`x` is the amount of seconds*

#### Fading in

When a spirit spawns, it directly appears. In order to control a fade effect, do the following right after the spawn instruction:
```
fadeIn entity for x seconds
```
*`entity` is the type of spirit to fade in*<br/>
*`x` the number of seconds to fade in*

#### Movement

Entities can be moved during cutscenes. Currently, it is only possible to move entities relatively to their current position:
```
move entity by x,y for 3 seconds looped
```
*`entity` is the type of spirit to move*<br/>
*`x` the amount of pixels to move left or right (can be negative)*<br/>
*`y` the amount of pixels to move up or down (can be negative)*<br/>
*`looped` is completely optional and lets the entity move back and forth between the old and new position*

#### Speech

Entities can talk:
```
say key on entity
```
*`entity` is the type of spirit that should say something*<br/>
*`key` is the sentence the entity should say. Must be a translation key*<br/>

##### Translations

Define sentences within the `assets/i18n` folder. 

For example within: `assets/i18n/translations.properties`
```
cutscene.day1.kodama.01=This is a dialog
```
and within: `assets/i18n/translations_DE.properties` you could define a German translation:
```
cutscene.day1.kodama.01=Das ist ein Dialog
```
Within the cutscene instruction, you then can automatically let the entity say the translated sentence:
```
kodama says cutscene.day1.kodama.01
```
Depending on the language configured on the operating system of the player, the language gets automatically loaded (English is default).

Please choose a name for the sentence that makes sense and is easy to remember. Some bad examples:
```
# very bad - when does he say this? What if I want to add another sentence for a particular day?
cutscene.ame.01=hey
# oops even worse!
sentence1=you
```
Instead, we should have the concept of specific and general sentences per spirit. For example, there might be things certain spirits always say like certain catchphrases etc. while for story, some things are only said once:
```
# general sentences
cutscene.ame.yawn=Yaaaaawn

# day 1
cutscene.day1.ame.01=Oh hey!
cutscene.day1.ame.02=nice to see you
cutscene.day1.hi.01=wow hey
cutscene.day1.hi.02=mhhh

# day 2
cutscene.day2.ame.01=another day, another life
cutscene.day2.ame.02=why? I wanna be friends with you...
cutscene.day2.hi.01=stop talking to me
cutscene.day2.hi.02=mhhh...
```

##### Multiple sentences
A player can say multiple sentences at once:
```
say cutscene.day4.ame.01 on ame
say cutscene.day4.ame.02 on ame
```
It is quite impossible for someone to say two things at the same time, so those sentences get shown after another.

#### Emotes

Entities can emote:
```
emote availableEmote on entity
```
*`entity` is the type of spirit that should emote something*<br/>
*`availableEmote` the kind of emote an entity should perform*<br/>

Currently supported emotes:

* `smile`
* `confusion`
* `sadness`
* `disagree`
* `agree`

#### Screen shake

You can shake the screen for a given amount of time with a given intensity:
```
shake with intensity a for b seconds
```
*`a` the intensity. Must be greater than 0*<br/>
*`b` the number of seconds to shake the screen*<br/>

#### Toggling states on entities

It is possible to enable or disable certain properties/states on entities, for example to make them do something very specific like a custom animation or effect.

```
# sets the property on the entity
start activity on entity
# removes the property from the entity
stop activity on entity
```
Currently supported:

* `swiping` (kodama only) kodama starts to brush around

#### Resetting an entity

During a cutscene, you might wanna stop any behavior or movement on a certain entity:
```
reset entity
```
*`entity` is the type of spirit that should be resetted*<br/>

### Spawning spirits

Once a day has been defined, it is possible to spawn spirits in various ways.

> **PLEASE NOTE! You cannot define spawning before a morning cutscene or after an evening cutscene!**

The following spirit types are currently supported:

* `ame`
* `hi`
* `kodama`

#### Spawning at a given point in time

```
at x seconds spawn type times amount
```
*`type` is the type of spirit to spawn*<br/>
*`x` is the amount of seconds*
*`amount` is the number of entities*

#### Spawning at a progress of the day

```
at x% spawn type times amount
```
*`type` is the type of spirit to spawn*<br/>
*`x` is the percentage of how much the day has already passed*

#### Spawning at a given chronological rate

```
every x seconds spawn type
```
*`type` is the type of spirit to spawn*<br/>
*`x` is the interval in seconds*

#### Spawning at a given progress rate

```
every x% spawn type
```
*`type` is the type of spirit to spawn*<br/>
*`x` is the percentage of the day as an interval*

#### Examples
```
# spawns 4x hi at the middle of the day
at 50% spawn hi 4 times
# spawns 2x ame every 10 seconds
every 10 seconds spawn ame 2 times
```
