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

## Palette

### UI theme (`Koehler.theme.json` `colors` block)

| Name | Hex | Used for |
|---|---|---|
| `koehlerBg` | `#101010` | Primary background — panels, popups, menus, editor gutter |
| `koehlerInputBg` | `#1A1A1C` | Input fields, text areas, editor panes, doc popup content |
| `koehlerHover` | `#2A2A2C` | Hover states, selection highlight, matched-brace bg |
| `koehlerTrack` | `#0E0E10` | Scrollbar track |
| `koehlerFg` | `#D8D8D8` | Primary foreground text |

Use palette names everywhere in the JSON — no raw hex literals outside the `colors` block.

### Editor accent colors (`Koehler.xml` attributes)

| Hue | Hex | Vim koehler role | Token types |
|---|---|---|---|
| Blue | `#6b83c7` | Comment (`#80a0ff`) | Comments, doc comments, annotations/metadata |
| Pink | `#c97ac9` | Special/PreProc (`#ff80ff`) | Fields, methods, function calls, attributes, `this`/`super` |
| Cyan | `#7ac5c5` | Identifier (`#40ffff`) | (inherited via `DEFAULT_INSTANCE_FIELD` in some contexts) |
| Yellow | `#d7c758` | Statement/keyword (`#ffff60`) | Keywords, labels |
| White | `#d8d8d8` | Normal text | Classes, interfaces, locals, parameters, identifiers |
| Pink-red | `#d89393` | String/Constant (`#ffa0a0`) | Strings, numbers, constants |
| Orange | `#d18b3d` | Operator/punctuation (`Orange`) | Braces, brackets, operators, dots, commas |

When adding tokens: pick the Vim hue family for the role, use the dimmed IntelliJ hex above. Don't introduce new hues.

## Conventions

- **Editor scheme edits go in `Koehler.xml` only.** There is no separate `.icls` at the repo root — it was a duplicate and was removed. Don't reintroduce it.
- **UI vs editor split:** scrollbars, tool window background, tree/panel colors live in `Koehler.theme.json`. Syntax token colors, caret, gutter, line numbers, selection live in `Koehler.xml`.
- **`parent_scheme` is `Darcula`**, not `Default`. Inheriting from `Default` causes bright color bleed for unmapped tokens.
- **Plugin version** is set in `gradle.properties` (`pluginVersion`), not in `plugin.xml`.
- **`since-build` / `until-build`** are set in `gradle.properties` (`pluginSinceBuild`, `pluginUntilBuild`). Bump `pluginUntilBuild` when validating against a newer platform.
- **`EditorScrollBar.*` keys don't exist** in the platform — editor scrollbar is controlled by the same `ScrollBar.*` keys as all other scrollbars. Don't add `EditorScrollBar` blocks.
- **Quick Documentation popup background** is `DOCUMENTATION_COLOR` in `Koehler.xml <colors>`, not `Popup.background` in the JSON. Darcula's default for this key is a medium gray — always override it explicitly.

## Testing changes

1. `./gradlew buildPlugin` — produces `build/distributions/koehler-theme-<version>.zip`
2. In IntelliJ: **Settings → Plugins → ⚙ → Install Plugin from Disk** → select the zip → restart
3. **`Koehler.xml` changes (editor scheme) won't apply until you toggle the color scheme**: Settings → Editor → Color Scheme → switch to any other scheme → switch back to Koehler. This is an IntelliJ cache behavior, not a bug.
4. Use **Help → Find Action → "LaF Defaults"** to inspect any UI component's color key live — hover a UI element while the dialog is open to see its key name.

## Color philosophy

The Vim `koehler` source uses vivid GUI colors (`#80a0ff`, `#ffa0a0`, `#40ffff`, `#ffff60`, `#60ff60`, `#ff80ff`, `Orange`). This plugin keeps the same hue mapping but tones each accent down ~30–40% saturation/value (e.g. `#80a0ff` → `#6b83c7`, `#ffff60` → `#d7c758`). When adding new token mappings, follow the same desaturation rule — pick the Vim role's hue family, then dim it to match neighbors. Don't introduce new hue families that aren't present in Vim koehler.

## Reference

- [Theme Structure](https://plugins.jetbrains.com/docs/intellij/theme-structure.html) — JSON skeleton, `plugin.xml` wiring, `themeProvider` declaration
- [Platform Theme Colors](https://plugins.jetbrains.com/docs/intellij/platform-theme-colors.html) — all UI color keys, LaF Defaults dialog for live inspection
- [Themes - Editor Schemes and Background Images](https://plugins.jetbrains.com/docs/intellij/themes-extras.html) — linking editor scheme from theme JSON, XML structure basics
- [Color Scheme Management](https://plugins.jetbrains.com/docs/intellij/color-scheme-management.html) — editor scheme XML attribute format, inheritance model, extension points

## Don't

- Don't add unrelated UI tweaks (fonts, icons, line spacing) — this is a *theme*, not a UI overhaul.
- Don't commit `build/`, `.gradle/`, `.intellijPlatform/`, or `*.zip` (already in `.gitignore`).
- Don't bump `<idea-version>` in `plugin.xml` — that field is intentionally absent so Gradle owns it.
