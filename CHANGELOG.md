## [2.5.0] - 02-08-2026


New Features

- Add config options for recipe viewer integration and asynchronous cache building. ([#230](https://github.com/Alessandro-Casale/AStages/pull/230))
- Implement EMI integration for item, fluid and recipes. ([#232](https://github.com/Alessandro-Casale/AStages/pull/232))
- Implement REI integration for item, fluid and recipes. ([#236](https://github.com/Alessandro-Casale/AStages/pull/236))

Bug Fixes

- Fix mod restriction evaluation logic. ([#234](https://github.com/Alessandro-Casale/AStages/pull/234))
- Fix remove_all action and command. ([#237](https://github.com/Alessandro-Casale/AStages/pull/237))
- Finally solved "AStages is forgetting Player Data". ([#239](https://github.com/Alessandro-Casale/AStages/pull/239))
- Handle corrupted legacy simple_restrictions.json. ([#240](https://github.com/Alessandro-Casale/AStages/pull/240))
- Improve player validation in block breaking and harvesting events. ([#253](https://github.com/Alessandro-Casale/AStages/pull/253))
- Add player validation in entity interaction event. ([#254](https://github.com/Alessandro-Casale/AStages/pull/254))
- Add player validation in player attack event. ([#255](https://github.com/Alessandro-Casale/AStages/pull/255))

API Changes

- Refactor reload handling to use ClientReloadPhase for improved clarity and consistency. ([#217](https://github.com/Alessandro-Casale/AStages/pull/217))
- Enhance client reload handling with new phases and improved model management. ([#223](https://github.com/Alessandro-Casale/AStages/pull/223))
- Introduce RecipeViewerManager and enhance JeiItemStagesPlugin with caching functionality. ([#224](https://github.com/Alessandro-Casale/AStages/pull/224))
- Replace HashMap with ConcurrentHashMap for thread-safe caching in OfflinePlayerStage. ([#238](https://github.com/Alessandro-Casale/AStages/pull/238))


