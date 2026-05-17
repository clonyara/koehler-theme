# Koehler Theme — Claude Notes

IntelliJ Platform plugin: a dark UI theme + matching editor color scheme. Inspired by the Vim `koehler` color scheme by Ron Aaron — same hue roles (blue comments, pink strings, cyan identifiers, yellow keywords, green types, orange punctuation, green caret on black), uniformly desaturated for sustained-use readability.

## Layout

- `src/main/resources/META-INF/plugin.xml` — plugin descriptor. Keep it minimal: `name`, `version`, `since-build`, `until-build`, and `change-notes` are patched at build time by the Gradle IntelliJ Platform plugin from `gradle.properties`. Do not duplicate those here.
- `src/main/resources/themes/Koehler.theme.json` — UI theme (scrollbars, panels, tool windows). Points to the editor scheme at `/themes/Koehler.xml`.
- `src/main/resources/themes/Koehler.xml` — editor color scheme (`<scheme>` XML, parent `Default`).
- `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` — Gradle build. Uses `org.jetbrains.intellij.platform` 2.x. Target IDE: `IC` at `platformVersion` in `gradle.properties`. JDK 17.

## Build

```bash
./gradlew buildPlugin                  # → build/distributions/koehler-theme-<version>.zip
./gradlew runIde                       # sandbox IDE with the plugin installed
./gradlew verifyPlugin                 # IntelliJ Plugin Verifier against recommended IDEs
./gradlew bumpVersion -Ppart=patch     # bump pluginVersion in gradle.properties (patch|minor|major)
```

## Conventions

- **Editor scheme edits go in `Koehler.xml` only.** There is no separate `.icls` at the repo root — it was a duplicate and was removed. Don't reintroduce it.
- **UI vs editor split:** scrollbars, tool window background, tree/panel colors live in `Koehler.theme.json`. Syntax token colors, caret, gutter, line numbers, selection live in `Koehler.xml`.
- **Plugin version** is set in `gradle.properties` (`pluginVersion`), not in `plugin.xml`.
- **`since-build` / `until-build`** are set in `gradle.properties` (`pluginSinceBuild`, `pluginUntilBuild`). Bump `pluginUntilBuild` when validating against a newer platform.

## Color philosophy

The Vim `koehler` source uses vivid GUI colors (`#80a0ff`, `#ffa0a0`, `#40ffff`, `#ffff60`, `#60ff60`, `#ff80ff`, `Orange`). This plugin keeps the same hue mapping but tones each accent down ~30–40% saturation/value (e.g. `#80a0ff` → `#6b83c7`, `#ffff60` → `#d7c758`). When adding new token mappings, follow the same desaturation rule — pick the Vim role's hue family, then dim it to match neighbors. Don't introduce new hue families that aren't present in Vim koehler.

## Don't

- Don't add unrelated UI tweaks (fonts, icons, line spacing) — this is a *theme*, not a UI overhaul.
- Don't commit `build/`, `.gradle/`, `.intellijPlatform/`, or `*.zip` (already in `.gitignore`).
- Don't bump `<idea-version>` in `plugin.xml` — that field is intentionally absent so Gradle owns it.
