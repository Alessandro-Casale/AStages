# AStages Documentation

## Overview

AStages introduces a robust system for creating and managing player progression stages. Designed for modpack developers and server administrators, this mod allows you to control player access to items, mods, and dimensions based on their current stage in the game.

### Key Features:
* **Player Stage System**: Define custom stages for players and manage their progression.
* **Item Restrictions**: Lock or unlock specific items based on the player's stage.
* **Mod Restrictions**: Control access to mods, ensuring they can only be used at certain stages.
* **Dimension Control**: Restrict access to dimensions until players reach the required stage.
* **Screen Restrictions**: Disable opening block entity menus until players reach the appropriate stage.
* **Ore Restrictions**: Replace block texture with another one based on the player's progression.
* **Server-Side Management**: All scripts run server-side to prevent manipulation and ensure a smooth multiplayer experience.
* **Easy Configuration**: Easily set up stages and restrictions using configuration files.

### How It Works:
* The mod loads all restrictions during server startup.
* The class `AStages` contains all utility methods for managing stages and restrictions.
* Each restriction is initialized with an ID, a stage, and the item, mod, or dimension to block.
* Modify restrictions dynamically by using the `AStages#getRestrictionById` method and adjust its properties as needed.

### Ideal For (please, provide credits):
* **Modpack Creators**: Structure progression for modpacks and enhance gameplay experiences.
* **Server Admins**: Create complex, stage-based servers with controlled access to game content.

### Installation:
1. Download and install the mod from this page.
2. Drop the `.jar` file into your `mods` folder.
3. Configure the stages and restrictions with KubeJS (`.json` file configuration coming soon).

Create the perfect progression system for your players with ease!

---

## Core Concept

AStages uses a simple but powerful concept of "stages" - labels assigned to players that determine what content they can access. As players progress through your modpack, they unlock new stages, gradually gaining access to more content.

```
Player → Unlocks Stage → Gains Access to Content
```

**Example Progression Flow:**
```
Start → Mining Stage → Tech Stage → Nether Stage → End Stage
```

---

## Commands

AStages provides intuitive commands for managing player stages:

| Command | Description |
|---------|-------------|
| `/astages add <player> <stage>` | Add a stage to a player |
| `/astages remove <player> <stage>` | Remove a stage from a player |
| `/astages check <player> <stage>` | Check if a player has a stage |
| `/astages list <player>` | List all stages a player has |
| `/astages clear <player>` | Remove all stages from a player |

---

## KubeJS Integration

AStages seamlessly integrates with KubeJS, allowing you to create complex progression systems with simple scripts.

### Restricting Content

#### Recipe Restrictions

Control which recipes players can use based on their stages.

```javascript
// Restrict individual recipes
event.astages.recipes.restrict('minecraft:diamond_pickaxe', 'mining_stage')

// Restrict all recipes from a mod
event.astages.recipes.restrictMod('botania', 'botania_stage')

// Restrict recipes by category
event.astages.recipes.restrictCategory('minecraft:stonecutting', 'masonry_stage')
```

#### Item Restrictions

Control which items players can use or interact with.

```javascript
// Restrict individual items
event.astages.items.restrict('minecraft:ender_pearl', 'ender_stage')

// Restrict all items from a mod
event.astages.items.restrictMod('thermal', 'thermal_stage')

// Restrict items by tag
event.astages.items.restrictTag('forge:tools/diamond', 'diamond_tools_stage')
```

#### Block/Ore Restrictions

Control which blocks players can mine or interact with.

```javascript
// Basic ore restriction
event.astages.ores.restrict('minecraft:diamond_ore', 'diamond_mining')

// Replace appearance (shows as stone until unlocked)
event.astages.ores.restrict('minecraft:diamond_ore', 'diamond_mining', 'minecraft:stone')

// Restrict blocks by tag
event.astages.ores.restrictTag('forge:ores/gold', 'gold_mining_stage')
```

#### Enchantment Restrictions

Control which enchantments players can apply or use.

```javascript
// Restrict individual enchantments
event.astages.enchantments.restrict('minecraft:fortune', 'fortune_stage')

// Restrict multiple enchantments to the same stage
event.astages.enchantments.restrict('minecraft:protection', 'advanced_enchants')
event.astages.enchantments.restrict('minecraft:sharpness', 'advanced_enchants')
```

#### Food Restrictions

Control which foods players can eat.

```javascript
// Restrict food items
event.astages.foods.restrict('minecraft:golden_apple', 'magical_food')

// Restrict food by tag
event.astages.foods.restrictTag('forge:foods/meat', 'meat_stage')
```

#### Structure Restrictions

Control which structures players can see or interact with.

```javascript
// Restrict structures
event.astages.structures.restrict('minecraft:fortress', 'nether_exploration')

// Restrict all structures from a mod
event.astages.structures.restrictMod('repurposed_structures', 'exploration_stage')
```

---

### Managing Stages

AStages provides intuitive methods for managing player stages through KubeJS.

#### Adding Stages

```javascript
// Basic stage addition
event.player.stages.add('stage_name')

// On first login
onEvent('player.logged_in', event => {
  if (!event.player.stages.has('starter')) {
    event.player.stages.add('starter')
    event.player.tell('Starter stage unlocked!')
  }
})

// On advancement
onEvent('player.advancement', event => {
  if (event.advancement.id == 'minecraft:story/enter_the_nether') {
    event.player.stages.add('nether_stage')
    event.player.tell('Nether stage unlocked!')
  }
})
```

#### Checking Stages

```javascript
// Check if player has a stage
if (event.player.stages.has('stage_name')) {
  // Do something when player has the stage
  event.player.tell('You have access to this content!')
}
```

#### Removing Stages

```javascript
// Remove a stage
event.player.stages.remove('stage_name')

// Remove a stage on death (for hardcore progression)
onEvent('player.death', event => {
  if (event.player.stages.has('end_access')) {
    event.player.stages.remove('end_access')
    event.player.tell('You have lost your End access!')
  }
})
```

#### Listing Stages

```javascript
// Get all player stages
const stages = event.player.stages.getAll()
event.player.tell('Your stages: ' + stages.join(', '))
```

---

## Example Implementation: Tech Progression

Here's a complete example of how to implement a tech progression system:

```javascript
// Define stage restrictions
onEvent('server.datapack.high_priority', event => {
  // TIER 1: BASIC TECH
  event.astages.recipes.restrict('minecraft:piston', 'basic_engineering')
  event.astages.recipes.restrict('minecraft:hopper', 'basic_engineering')
  event.astages.items.restrict('minecraft:comparator', 'basic_engineering')
  
  // TIER 2: ADVANCED TECH
  event.astages.recipes.restrictMod('thermal', 'advanced_engineering')
  event.astages.items.restrictMod('thermal', 'advanced_engineering')
  
  // TIER 3: EXPERT TECH
  event.astages.recipes.restrictMod('mekanism', 'expert_engineering')
  event.astages.items.restrictMod('mekanism', 'expert_engineering')
})

// Grant stages based on player actions
onEvent('item.crafted', event => {
  // Unlock basic engineering when crafting redstone
  if (event.item.id == 'minecraft:redstone' && !event.player.stages.has('basic_engineering')) {
    event.player.stages.add('basic_engineering')
    event.player.tell('Basic Engineering unlocked!')
  }
  
  // Unlock advanced engineering when crafting machine frame
  if (event.item.id == 'thermal:machine_frame' && !event.player.stages.has('advanced_engineering')) {
    event.player.stages.add('advanced_engineering')
    event.player.tell('Advanced Engineering unlocked!')
  }
  
  // Unlock expert engineering when crafting advanced components
  if (event.item.id == 'thermal:energy_cell' && 
      event.player.stages.has('advanced_engineering') && 
      !event.player.stages.has('expert_engineering')) {
    event.player.stages.add('expert_engineering')
    event.player.tell('Expert Engineering unlocked!')
  }
})
```

---

## Example Implementation: Dimension Progression

Control access to different dimensions with stages:

```javascript
// Restrict access to dimensions
onEvent('server.datapack.high_priority', event => {
  // Restrict nether portal blocks
  event.astages.ores.restrict('minecraft:nether_portal', 'nether_access')
  
  // Restrict end portal frames and eyes of ender
  event.astages.ores.restrict('minecraft:end_portal', 'end_access')
  event.astages.ores.restrict('minecraft:end_portal_frame', 'end_access')
  event.astages.items.restrict('minecraft:ender_eye', 'end_access')
})

// Grant dimension access through boss kills
onEvent('entity.death', event => {
  if (!event.entity.player && event.source.player) {
    // When player kills a wither, grant nether access
    if (event.entity.type == 'minecraft:wither') {
      event.source.player.stages.add('nether_access')
      event.server.runCommand(`title ${event.source.player.name} title "Nether Access Granted!"`)
    }
    
    // When player kills an Elder Guardian, grant end access
    if (event.entity.type == 'minecraft:elder_guardian') {
      event.source.player.stages.add('end_access')
      event.server.runCommand(`title ${event.source.player.name} title "End Access Granted!"`)
    }
  }
})
```

---

## Best Practices

For the best experience with AStages, follow these guidelines:

- **Start Simple**: Begin with a few key stages before expanding
- **Clear Progression**: Make it obvious to players how to advance
- **Balance**: Don't lock too much content behind difficult stages
- **Feedback**: Always inform players when they unlock new stages
- **Documentation**: Create an in-game guide for your stage system
- **Testing**: Play through your pack to ensure smooth progression
- **Performance**: Group related restrictions under the same stage name

---

## Compatibility

AStages works seamlessly with:

- FTB Quests / Better Quests
- Game Stages
- KubeJS
- Most major content mods
- CraftTweaker (for additional recipe control)

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Stage not being granted | Check event trigger conditions and spelling |
| Content not being restricted | Verify resource IDs using `/kubejs hand` |
| Players bypass restrictions | Ensure server-side scripts are loading correctly |
| Script errors | Check logs for typos or syntax errors |
| Performance issues | Group restrictions under fewer stages |

For additional support, join our Discord or open an issue on GitHub.

---

## Credits

When using AStages in your modpack, please provide credit to the mod author as requested.
