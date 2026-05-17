# Koehler Theme

Dark UI theme for IntelliJ-based IDEs with a matching editor color scheme and dark scrollbars.

Inspired by the classic Vim [`koehler`](https://github.com/rodnaph/vim-color-schemes/blob/master/colors/koehler.vim) color scheme by Ron Aaron — black background with blue comments, pink strings, cyan identifiers, yellow keywords, green types, orange punctuation, and a green caret. Accent hues are toned down from Vim's vivid originals for long-session readability.

## Build

```bash
./gradlew buildPlugin
```

The distributable zip is written to `build/distributions/`.

## Run in a sandbox IDE

```bash
./gradlew runIde
```

## Verify against target IDE versions

```bash
./gradlew verifyPlugin
```

## Bump plugin version

```bash
./gradlew bumpVersion -Ppart=patch   # 1.0.0 -> 1.0.1 (default)
./gradlew bumpVersion -Ppart=minor   # 1.0.0 -> 1.1.0
./gradlew bumpVersion -Ppart=major   # 1.0.0 -> 2.0.0
```

Updates `pluginVersion` in `gradle.properties` in place.

## Install locally

In the IDE: `Settings` → `Plugins` → gear icon → `Install Plugin from Disk…` and pick the zip in `build/distributions/`.

## Layout

- `src/main/resources/META-INF/plugin.xml` — plugin descriptor
- `src/main/resources/themes/Koehler.theme.json` — UI theme
- `src/main/resources/themes/Koehler.xml` — editor color scheme
