# LWJGL_GameEngine

Small 3D game / engine sandbox built with [LWJGL](https://www.lwjgl.org/) and OpenGL.

Bundled natives are for **Linux (x86_64)**. The project is set up as an **Eclipse** Java project (no Maven/Gradle build for the game itself).

## Requirements

- **JDK 17** (or newer)
- **Linux x86_64** with a working OpenGL driver
- **Eclipse IDE for Java Developers** (or another IDE that can import Eclipse projects)
- Sibling projects in the **same Eclipse workspace** (see below)

## Companion projects

This repo alone is not enough to compile or run. Eclipse expects these projects **next to** `LWJGL_GameEngine` (same parent folder / same workspace), with these exact names:

| Eclipse project name | Purpose | Where to get it |
| --- | --- | --- |
| `LWJGL_GameEngineResource` | Models, shaders, textures, audio, `ResourceLoader` | [LaurinTrp/LWJGL_GameEngineResource](https://github.com/LaurinTrp/LWJGL_GameEngineResource) |
| `LWJGUI` | UI toolkit used by `Engine_Main` | Local copy of [orange451/LWJGUI](https://github.com/orange451/LWJGUI) (or your fork), imported as project `LWJGUI` |
| `GLM` | Java GLM math (`glm.vec`, `glm.mat`, …) | Source tree imported as project `GLM` (e.g. from [kotlin-graphics/glm](https://github.com/kotlin-graphics/glm) / your local GLM project) |

Example layout after cloning:

```text
workspace/
  LWJGL_GameEngine/           ← this repo
  LWJGL_GameEngineResource/
  LWJGUI/
  GLM/
```

JARs under `LWJGL_GameEngine/lib/` (LWJGL, Assimp, JOML, Commons IO) are already included in this repository.

## Setup (Eclipse)

1. Clone this repository and the companion projects into one folder.
2. Open Eclipse and create/open a workspace that can see that folder.
3. **File → Import → Existing Projects into Workspace**.
4. Select the parent folder and import:
   - `LWJGL_GameEngine`
   - `LWJGL_GameEngineResource`
   - `LWJGUI`
   - `GLM`
5. Make sure the project JRE is **Java 17**.
6. Let Eclipse rebuild. Fix any missing project references if a sibling was imported under a different name (names must match the table above).

If `LWJGUI` needs its own LWJGL jars, use the ones under `LWJGUI/lib/` (already present in a full checkout of that project).

## Run the game

1. Open `src/main/java/gui/Engine_Main.java`.
2. Run it as a **Java Application**.
3. Main class: `main.java.gui.Engine_Main`

Optional JVM argument (used in the existing run config):

```text
-XX:ErrorFile=crashLogs/hs_err_pid%p.log
```

On first run, LWJGL extracts native libraries under `/tmp/lwjgl<username>/…`.

## Controls / window

A GLFW window opens via LWJGUI. Default size is **800×800**. Further input handling lives in `KeyHandler` / `MouseInputs` under `src/main/java/utils/Inputs/`.

## Troubleshooting

### `UnsatisfiedLinkError` on `libassimp.so`

Assimp’s native library depends on **Draco** (`libdraco.so.8`). This project’s `AssimpWrapper` must load Draco **before** Assimp. Rebuild after pulling so that class is up to date.

Also ensure `lib/assimp/lwjgl-assimp-natives-linux.jar` is on the classpath (it is listed in `.classpath`).

### Missing models / shaders / textures

That content lives in `LWJGL_GameEngineResource`. If that project is not imported, or `ResourceLoader` cannot find files, asset loading will fail at startup.

### OpenAL / sound errors

OpenAL Java bindings are on the classpath (`lib/lwjgl/lwjgl-openal.jar`). If natives fail to load, add `lib/lwjgl/natives_linux/lwjgl-openal-natives-linux.jar` to the project classpath (same as the other Linux native jars).

### Wrong OS

Linux natives are what this checkout is wired for. Windows jars exist under `lib/lwjgl/natives_windows/`, but `.classpath` currently points at the Linux set—swap those entries if you run on Windows.

## Libraries

- LWJGL 3.3.1 (core / GLFW / OpenGL / STB / OpenAL / NanoVG) with Assimp natives from a newer snapshot
- JOML
- Apache Commons IO
- LWJGUI (sibling project)
- Java GLM (sibling project)
