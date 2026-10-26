# FireworkIndicator port for Minecraft 1.21.11

This is a source-level compatibility port based on the uploaded `FireworkIndicator.jar`'s behavior and assets. The original jar itself has **not** been relabeled as compatible, because its Minecraft mixin bytecode must be recompiled against 1.21.11.

## Build
Requires JDK 21 and Gradle. Run `gradle build` from this directory with internet access so Gradle can fetch Fabric/Minecraft dependencies. The built mod jar will be in `build/libs/`.

## Install
Use Minecraft Java 1.21.11 with Fabric Loader and Fabric API. Copy the built jar into the instance's `mods` folder.

## Behavior
Uses the original jar's estimated durations for rocket flight levels 1, 2, and 3 (1170 ms, 1550 ms, 2220 ms) and draws the firework icon overlay near the crosshair. This is an estimate, not an exact server-side boost timer.

## Testing note
This project has been prepared as source but not compiled or run in Minecraft in this environment. Verify the Gradle/Fabric API versions and test in a separate SKLauncher profile before use.
