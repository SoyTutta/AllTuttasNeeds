# All Tutta's Needs

All Tutta's Needs is a modular NeoForge mod for Minecraft 1.21.1 that combines
Tutta's Doors, Tutta's Beds and Tutta's Delights in one configurable project.

## Modules

- **Tutta's Doors** adds discreet, normal, indiscreet, transit, pet, sliding
  and secret bookshelf doors across vanilla and supported wood families.
- **Tutta's Beds** adds bed frames, mattresses, covers, blankets, connected
  beds, bunk beds and different comfort tiers.
- **Tutta's Delights** expands Farmer's Delight with meals, feasts, popsicles,
  potions, loot additions and decorative food presentation.

Each module can be disabled separately in the startup configuration. Disabling
a module also leaves the corresponding vanilla and modded content unchanged.
Tutta's Delights additionally exposes thematic content groups for suckling pig
dishes, ancient foods, potatoes, creepers, slime, frozen treats, frogs, extra
meals, breaded meals, sandwich portions and undead foods. All enabled
groups share the single Tutta's Delights creative tab.
Most content sets and gameplay mechanics have their own options for modpack
authors. Disabling the consistent door set restores the original door recipes;
transit and pet conversions then use the original door as their ingredient.

The configuration is loaded at startup because module and content switches can
change which registry entries exist. Client and server must therefore use the
same `alltuttasneeds-startup.toml` when playing together.

## Compatibility

Door compatibility is available for Vanilla Backport, No Man's Land, New
World, Abundant Atmosphere, Caverns & Chasms, Upgrade Aquatic, Atmospheric,
Environmental, Autumnity, Windswept, Spawn, Nature's Spirit, Biomes O' Plenty,
Enderscape, Arts & Crafts, My Nether's Delight, Architect's Palette, Malum, Create,
BlockBox and Woodworks.

Farmer's Delight adds straw and canvas mattress materials and canvas covers.
It is also the base mod required for Tutta's Delights: when Farmer's Delight is
not installed, the entire Delights module remains inactive. My Nether's Delight,
Brewin' and Chewin', Miner's Delight and Spawn add optional Delights content
through internal compatibility paths. Their entries follow the related content groups
automatically and do not require separate compatibility switches. Every
compatibility remains optional for the container mod.

## Data packs

Bed cover and blanket ingredients are data-driven. They can be replaced or
extended through `data/<namespace>/bed_covers` and
`data/<namespace>/bed_blankets`; suffixes must remain unique across loaded data
packs. Door recipes also carry NeoForge conditions for their module, content set
and optional owner mod. Delights recipes and loot modifiers carry the Delights
module condition, plus owner-mod conditions where required, so disabled or
unavailable content is not loaded.

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
