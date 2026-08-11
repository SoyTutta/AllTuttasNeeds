All Tutta's Needs 3.2.0

General:
Changes:

- Split the config into startup, client and common files. Old values are migrated automatically.
- All Tutta's Needs is now required on both the client and server.
- Reorganized all texture paths. Resource packs using the old paths will need to update them.

Fixes:

- Fixed generated recipes, tags and loot referencing disabled or missing compatibility content.

Tutta's Doors:
Changes:

- Transit and Pet Doors now handle entity contact more efficiently.

Tutta's Beds:
Changes:

- Added optional standalone blanket items for every color. They are disabled by default because they are still being tested.
- Blanket items can be placed directly on beds, exchanged for another blanket and combined with a bare bed in crafting.
- The blanket items still have no recipes of their own.
- Beds can now exchange mattresses. You get the old bare mattress back, but any cover it had is lost forever. Sorry not sorry.
- Basic covers can now replace other basic covers. Covers are disposable, so the old one is not returned.
- Covered beds and mattresses drop their bare version unless broken with Silk Touch.
- Blanketed beds return their specific blanket item when it exists. Otherwise they drop as the complete bed.
- Added support for multiple items applying the same basic cover.
- Added configurable bed tiers for special beds from other mods.
- Bed tiers can now have multiple wake-up effects and decide whether nearby monsters matter.
- Bed tooltips and sleep duration now follow the server's settings instead of the client's local common config.
- Removed the old Optimized Block Entities workaround because my beds no longer use unnecessary bed block entities.

Fixes:

- Fixed beds disappearing when changing covers, blankets, mattresses or frames.
- Fixed beds being replaceable while someone was sleeping in them. They now use Minecraft's normal occupied-bed message.
- Fixed leather and wool being accepted as blanket items when right-clicking a bed.
- Fixed blanket items existing when their matching bed variants did not.
- Fixed optional blanket items having missing inventory models and names when enabled.
- Fixed blanket drops giving the wrong material or ignoring Silk Touch when the foot of the bed was broken.
- Fixed beds dropping their contents twice when broken from the foot.
- Fixed ambiguous cover and blanket ingredients silently choosing whichever result they felt like.
- Fixed mismatched bed halves accepting each other after external block changes.
- Fixed connected and bunk bed hitboxes.
- Fixed respawning from bunk beds made with Bed Frames.
- Fixed excluded bed tiers still affecting gameplay and tooltips.
- Fixed server tier synchronization ignoring special beds without an associated item.
- Fixed Tutta beds being registered incompletely as villager homes.
- Fixed very long wake-up effects eventually exploding into math nonsense.

Tutta's Delights:
Changes:

- Configurable cheese recipes now follow the server's settings instead of the client's local common config.
- Slime Cubes now replace slime ball drops 25% of the time instead of always.

Compatibility:

- Updated compatibility requirements for Malum, My Nether's Delight, No Man's Land, Spawn, Vanilla Backport, Miner's Delight and Brewin' and Chewin'.


All Tutta's Needs 3.1.1

- soy boba nomas


All Tutta's Needs 3.1.0

- Fixed a bunch of spelling errors.
- My Doors now inherit their original door's name, so renaming the original renames mine too.
- Added Windswept compatibility with the matching wood set.
- Rebalanced a ton of food values.
- Creeper food can now explode depending on the dish.
- Eating too much slime food makes you throw up slimes (they steal both hunger and your active effects)
- Added Woodworks Saw recipes for my doors.


All Tutta's Needs 3.0.0

- Fixed a few bugs. Oh, and there's a new Needs module (added a ton of new food)


All Tutta's Needs 2.1.0

fix things I didn't notice


All Tutta's Needs 2.0.0
Changes:

- Added startup config system.
- Doors and Beds can now be enabled independently.
- Added content toggles for most doors and beds.
- Added configurable sleep tiers.
- Added configurable wake-up effects.
- Added configurable respawn rules.
- Added configurable villager bed usage.
- Added configurable automatic door behavior.

New Mechanics:

- Connected Beds.
- Bunk Beds.
- Bed Frames.
- Loose Mattresses.
- Bed Covers & Blankets.
- Deluxe Beds.
- Bed recoloring.
- Improved automatic Transit & Pet Doors.

Tutta's Doors 1.6.2

They moved three textures, I fixed three doors.


Tutta's Doors 1.6.1

I'm boluda


Tutta's Doors 1.6

More compats and minor details… Let me sleep aghhhhhhhhhhhhhhhhhhhhhhhhhh


Tutta's Doors 1.5.2

fir bookshelf recipes (New World)


Tutta's Doors 1.5.1

- secret doorsssssss
- render bug fix


All Tutta's Needs 1.5

Changes:
- Bars Sliding doors!
- Fixed all doors recipes and loot tables.

Door Set Compatibility:

- No Man's Land
- Blockbox
- New World
- My Nether's Delight
- Enderscape
- Arts and Crafts
- Nature's Spirit
- Biomes O' Plenty
- Malum
- Create


Tutta's Doors 1.0.0

-Pain-
