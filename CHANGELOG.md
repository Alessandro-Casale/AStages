## [2.3.2-test] - 15-07-2026


New Features

- Update AItemTagRestriction class to use `TagKey<Item>` instead of `ResourceLocation` . ([#191](https://github.com/Alessandro-Casale/AStages/pull/191))
- Introduce entity type handling to show an error message if the wrong id is typed. ([#190](https://github.com/Alessandro-Casale/AStages/pull/190))
- Enhance KubeJS integration with player and server stage management methods. ([#192](https://github.com/Alessandro-Casale/AStages/pull/192))
- Integrate LootJS support for loot modification processing. ([#193](https://github.com/Alessandro-Casale/AStages/pull/193))
- Add living drops check for enhanced loot control, flag must be enabled in common config. ([#198](https://github.com/Alessandro-Casale/AStages/pull/198))

Bug Fixes

- Refactor stage retrieval logic in AClientHolder to be in line with AHolder implementation. ([#189](https://github.com/Alessandro-Casale/AStages/pull/189))
- Add ignoredBiomes method for AMobRestriction, restricted mobs can spawn with different equipment. ([#185](https://github.com/Alessandro-Casale/AStages/pull/185))
- Invert stage check logic in AScreenManager for server and player restrictions. ([#195](https://github.com/Alessandro-Casale/AStages/pull/195))
- Update AHolder and AStagesSuggestions to use ServerPlayer collection for command suggestions. ([#196](https://github.com/Alessandro-Casale/AStages/pull/196))
- Allow event to proceed when equipment restrictions are met. ([#209](https://github.com/Alessandro-Casale/AStages/pull/209))
- Implement AItemTag integration with new formatter and assignment manager, fix suggestion provider (1.20.X only). ([#212](https://github.com/Alessandro-Casale/AStages/pull/212))

API Changes

- Change item restriction collections from List to Set for improved performance and semantics. ([#197](https://github.com/Alessandro-Casale/AStages/pull/197))
- Rename reload methods to onReloadStarted and onReloadFinished for clarity. ([#214](https://github.com/Alessandro-Casale/AStages/pull/214))
- Simplify enum definitions and update StreamCodec usage for better clarity (1.21.X only). ([#215](https://github.com/Alessandro-Casale/AStages/pull/215))


