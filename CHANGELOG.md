## [2.0.0] - 07-04-2026


New Features

- Offline stages (will be polished in next releases).
- Add stage warning if a stage is not linked to any restriction/method. (Can be disabled via config flag).
- New temporary stages.
- Add test mode, related methods and config flag.
- New simple restrictions location can be configured.
- Implement tick method for temporary stages. ([#65](https://github.com/Alessandro-Casale/AStages/pull/65))
- New methods exposed for both Stages and TemporaryStages. ([#75](https://github.com/Alessandro-Casale/AStages/pull/75))
- Implement method `ignoreItems` for Recipe Mod Restrictions. ([#79](https://github.com/Alessandro-Casale/AStages/pull/79))
- New Preset system, that replaces old Config system, now supports all restrictions/stages and every method, not only attributes. ([#93](https://github.com/Alessandro-Casale/AStages/pull/93))

Bug Fixes

- Solve bug when reloading scripts.
- Solve bug which prevents restrictions from working as expected (client and server side).
- Solve bug which prevents world from loading correctly if a custom stage will be added without setting a custom stack. ([#67](https://github.com/Alessandro-Casale/AStages/pull/67))
- Solve disconnection from servers when client info command is run. ([#68](https://github.com/Alessandro-Casale/AStages/pull/68))
- Solve a bug which keeps server crashing at startup. ([#73](https://github.com/Alessandro-Casale/AStages/pull/73))
- Critical server crash caused by invalid registry setup. ([#74](https://github.com/Alessandro-Casale/AStages/pull/74))
- Solve server crash if null player is used to instantiate a holder. ([#89](https://github.com/Alessandro-Casale/AStages/pull/89))
- Solve Jade doesn't show correct item name and solve inventory not being updated when player logged in. ([#92](https://github.com/Alessandro-Casale/AStages/pull/92))
- Solve issue which prevents correct tooltip if JEI was not installed. ([#95](https://github.com/Alessandro-Casale/AStages/pull/95))
- Solve issue regarding command auto completion. ([#96](https://github.com/Alessandro-Casale/AStages/pull/96))

API Changes

- Annotations cleanup.
- Expose new utility methods.
- Adopt new Java IO file system.
- Unify event post/listener system over different loaders. ([#61](https://github.com/Alessandro-Casale/AStages/pull/61))
- New method for adding stages via java code, new tests. ([#66](https://github.com/Alessandro-Casale/AStages/pull/66))
- Moved stage managers and instances to attribute system like restrictions. ([#70](https://github.com/Alessandro-Casale/AStages/pull/70))
- Plugin integration for `Stage` and `TemporaryStage` classes. ([#76](https://github.com/Alessandro-Casale/AStages/pull/76))
- Attributes can now be added to all restrictions and stages, both client and server. ([#77](https://github.com/Alessandro-Casale/AStages/pull/77))
- Add `attachClientAttributes` and `attachClientStageAttributes` methods to AStagesPlugin class. ([#78](https://github.com/Alessandro-Casale/AStages/pull/78))
- Divide Permanent and Temporary stages on the client. ([#80](https://github.com/Alessandro-Casale/AStages/pull/80))
- Refactor nearly all packages, breaking changes with legacy addons. ([#88](https://github.com/Alessandro-Casale/AStages/pull/88))
- Refactor reload system in order to be JS independent. ([#90](https://github.com/Alessandro-Casale/AStages/pull/90))
- Hide AHolder dev message under ENABLE_DEV_LOGS common config flag, change flag default value to false. ([#94](https://github.com/Alessandro-Casale/AStages/pull/94))
- Nullable stream codec builder. (1.21.X Only).
- Add tests for smithing table. ([#97](https://github.com/Alessandro-Casale/AStages/pull/97))


