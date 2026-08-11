# All Tutta's Needs

All Tutta's Needs is a modular NeoForge mod for Minecraft 1.21.1 that combines
Tutta's Doors, Tutta's Beds and Tutta's Delights in one configurable project.

## Modules

- **Tutta's Doors** adds discreet, normal, indiscreet, transit, pet, sliding
  and secret bookshelf doors across vanilla and supported wood families.
- **Tutta's Beds** adds bed frames, interchangeable mattresses, covers,
  blankets, optional standalone blanket items, connected beds, bunk beds and
  different comfort tiers.
- **Tutta's Delights** expands Farmer's Delight with meals, feasts, popsicles,
  potions, loot additions and decorative food presentation.

Tutta's Beds has four comfort tiers, each with a different default advantage:

| Tier | Default advantage |
|---|---|
| **Basic** | Takes 10 seconds to complete sleep and keeps the current respawn point, making it useful for temporary rest. |
| **Low** | Takes 7.5 seconds to complete sleep and sets the respawn point. |
| **Normal** | Keeps vanilla's 5-second sleep time, sets the respawn point and grants 5 seconds of Regeneration after waking. |
| **Deluxe** | Takes 2.5 seconds to complete sleep, sets the respawn point, grants 10 seconds of Regeneration and can ignore nearby monsters when starting sleep. |

Vanilla beds and recognized beds from other mods use the Basic tier by default.
The common configuration can change whether each tier sets spawn, its sleep
duration, whether nearby monsters are ignored and the effects applied after
waking. Every wake effect can use a vanilla or modded effect ID and its own
duration. Individual beds or complete mod namespaces can also be reassigned to
another tier or excluded from the tier system. Special beds that do not extend
Minecraft's `BedBlock` can be opted in through `blockOverrides` when their mod
also includes them in the `minecraft:beds` block tag.

Each module can be disabled separately in the startup configuration. Disabling
a module also leaves the corresponding vanilla and modded content unchanged.
Tutta's Delights additionally exposes thematic content groups for suckling pig
dishes, ancient foods, potatoes, creepers, slime, frozen treats, frogs, extra
meals, breaded meals, sandwich portions and undead foods. All enabled
groups share the single Tutta's Delights creative tab.
Most content sets and gameplay mechanics have their own options for modpack
authors. Disabling the consistent door set restores the original door recipes;
transit and pet conversions then use the original door as their ingredient.

Module and content switches are loaded from `alltuttasneeds-startup.toml`
because they can change which registry entries exist; client and server must
use the same file when playing together. Local tooltip preferences for Doors
and Beds are stored in `alltuttasneeds-client.toml`. Automatic door behavior,
bed interaction and tier rules, and the Delights cheese recipe option are
stored globally in `alltuttasneeds-common.toml`. All Tutta's Needs
must be installed on both the client and server; connection negotiation rejects
a missing installation or incompatible network protocol before joining.
The server synchronizes each bed's effective tier, tiered sleep timing and the
active Delights cheese ingredient when joining and after data pack reloads.
Client tooltips, sleep progress and recipe displays therefore follow the
server's common rules without replacing local configuration files.
When updating from the previous single-file configuration or the obsolete
global server file, existing client and common values are migrated automatically
without replacing values already present in either new file.

## Compatibility

Door compatibility is available for Vanilla Backport, No Man's Land, New
World, Abundant Atmosphere, Caverns & Chasms, Upgrade Aquatic, Atmospheric,
Environmental, Autumnity, Windswept, Spawn, Nature's Spirit, Biomes O' Plenty,
Enderscape, Arts & Crafts, My Nether's Delight, Architect's Palette, Malum, Create,
BlockBox and Woodworks.

When Woodworks is installed, its sawmill can produce enabled Tutta's Doors variants
from the matching wood-family tag, convert matching doors and trapdoors between their
available forms, and create secret bookshelf doors from their bookshelves.

Farmer's Delight adds straw and canvas mattress materials and canvas covers.
It is also the base mod required for Tutta's Delights: when Farmer's Delight is
not installed, the entire Delights module remains inactive. My Nether's Delight,
Brewin' and Chewin', Miner's Delight and Spawn add optional Delights content
through internal compatibility paths. Their entries follow the related content groups
automatically and do not require separate compatibility switches. Every
compatibility remains optional for the container mod.

## Data packs

Bed cover and blanket ingredients are data-driven through
`data/<namespace>/bed_covers` and `data/<namespace>/bed_blankets`; suffixes must
remain unique across loaded data packs. A cover can declare one legacy `item`,
an `items` array, or both; every listed item applies that same cover and can be
combined with a bare bed. Blanket `colors` assign one item to each NORMAL color,
while `recipe_items.normal` and `recipe_items.deluxe` can override the single
associated item for either tier. That item applies the blanket directly, works in
the dynamic bare-bed recipe and is returned when the blanket is removed or the
bed is broken without Silk Touch. The optional standalone blanket items provide
the fallback assignment when enabled. Applying another cover replaces and
discards the previous cover. A blanket can be replaced only when its previous
variant has a valid associated item, which is returned to the player. If no
valid associated item exists, the decorated bed drops itself. Existing
multi-material blanket and cover recipes
remain available. Data-driven blanket shortcuts can be disabled per suffix with
`beds.gameplay.interactions.directApplyDisabled`. The bundled blanket data does
not associate raw wool or leather as blanket items; those materials remain part
of the normal multi-item recipes. Runtime assignments and the interaction
configuration are synchronized from the server to connected clients.
Door recipes also carry NeoForge conditions for their module, content set
and optional owner mod. Bed recipes and loot tables carry module, result-item
and owner-mod conditions, while their tag entries are optional, so disabled
materials and absent compatibilities do not leave invalid data references.
Delights recipes and loot modifiers carry the Delights module condition, plus
owner-mod conditions where required, so disabled or unavailable content is not
loaded.

## Development

The project targets Java 21, Minecraft 1.21.1 and NeoForge 21.1.234. Generated
resources live in `src/generated/resources` and are part of the main resource
set.

Guardian, Elder Guardian and Potluck content is currently under development. It
is not registered, generated or configurable in release builds.

Development runs load a selected compatibility profile rather than every
supported mod. The current `runClient` profile includes JEI, Jade, AppleSkin,
several door integrations, the Tutta's Delights ecosystem and their required
libraries. `build.gradle` is the source of truth for this `localRuntime`
selection; the broader compatibility matrix remains available through
`datagenRuntime`.

- `./gradlew.bat compileJava` checks the Java sources.
- `./gradlew.bat runDoorsData` regenerates only Tutta's Doors data.
- `./gradlew.bat runBedsData` regenerates only Tutta's Beds data.
- `./gradlew.bat runDelightsData` regenerates only Tutta's Delights data.
- `./gradlew.bat runData` regenerates the complete data set.
- `./gradlew.bat build` creates the distributable and runs the available checks.

## Issues

Please report bugs through the
[GitHub issue tracker](https://github.com/SoyTutta/AllTuttasNeeds/issues).
