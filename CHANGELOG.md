## [2.3.0] - 05-07-2026


New Features

- Refactor suggestion providers for player/server stages commands. ([#150](https://github.com/Alessandro-Casale/AStages/pull/150))
- Rename method `disableSpawning` in `disableOverallSpawning` for AMobRestriction. ([#152](https://github.com/Alessandro-Casale/AStages/pull/152))
- Refactor all config flags, both client and common sides. ([#168](https://github.com/Alessandro-Casale/AStages/pull/168))
- Custom messages for stage can be shown in player action bar. ([#170](https://github.com/Alessandro-Casale/AStages/pull/170))
- Change signature for stage methods in KubeJS. ([#171](https://github.com/Alessandro-Casale/AStages/pull/171))

Bug Fixes

- Commands accept string with spaces (must be quoted with single or double quotes). ([#149](https://github.com/Alessandro-Casale/AStages/pull/149))
- Fix in-game logo error. ([#147](https://github.com/Alessandro-Casale/AStages/pull/147))
- Hide original item tooltip while waiting for synchronization. ([#156](https://github.com/Alessandro-Casale/AStages/pull/156))
- Fix ALootRestriction bugs. ([#172](https://github.com/Alessandro-Casale/AStages/pull/172))
- Solve command suggestion not being correct when player or server only stages are involved. ([#174](https://github.com/Alessandro-Casale/AStages/pull/174))
- Update stage alert logic in order to use default settings when no attribute is changed. ([#175](https://github.com/Alessandro-Casale/AStages/pull/175))
- Fix unknown messages for stage checks and warnings in commands. ([#176](https://github.com/Alessandro-Casale/AStages/pull/176))
- Fix validation checks for player and server stages. ([#177](https://github.com/Alessandro-Casale/AStages/pull/177))

API Changes

- Rename parameters in AMobRestriction. ([#155](https://github.com/Alessandro-Casale/AStages/pull/155))
- Refactor all stage alert system for titles, subtitles, chat messages and action bar messages. ([#169](https://github.com/Alessandro-Casale/AStages/pull/169))
- Server and player only stages are synced between server and client. ([#173](https://github.com/Alessandro-Casale/AStages/pull/173))


