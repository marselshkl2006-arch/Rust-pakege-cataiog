# crates-catalog

Kotlin Multiplatform + Compose Multiplatform catalog of Rust crates (data model and API: crates.io).
Navigation 3 (JetBrains), Ktor, kotlinx.serialization, Compose resources (RU + EN).

## Requirements
- JDK 17+ (the Gradle wrapper downloads Gradle 9.6.1 itself)
- Internet access on first build (dependencies, Node/Yarn for the Wasm target)
- Python 3 for `tools/check-strings.py`

## Run
```
./gradlew :desktopApp:run                      # Desktop (main target)
./gradlew :webApp:wasmJsBrowserDevelopmentRun  # Web (Wasm), opens in the browser
```
On Windows use `gradlew.bat`.

## Check
```
./gradlew :composeApp:jvmTest      # unit tests (API with MockEngine, mock data, list filtering)
python tools/check-strings.py      # EN/RU key parity
```

## Layout
- `composeApp` - shared code (`commonMain`: app, model, data, navigation, state, theme, ui, composeResources)
- `desktopApp` - JVM entry point
- `webApp` - Wasm entry point

## Mock <-> network
The top-bar button "Mock data / crates.io" flips `AppState.useMocks`; `CratesRepository` reads the flag on every
call, so the UI is identical for both sources. Default: mocks. On Web the real API may be blocked by CORS.

## Not in v1 (by design)
No persistence (theme/language/data source live in memory), no width-adaptive layouts, no deep links / address bar
integration, catalog data is not translated.

## Targets
Only Desktop (JVM) and Web (Wasm) are configured. Android and iOS are not included: they need the Android SDK /
Xcode and cannot be build-checked on a Desktop-first setup.
