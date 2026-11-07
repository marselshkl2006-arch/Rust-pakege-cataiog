# crates-catalog

Kotlin Multiplatform + Compose Multiplatform catalog of Rust crates (data model and API: crates.io).
Navigation 3 (JetBrains), Ktor, kotlinx.serialization, Compose resources (RU + EN).

## Requirements
- Android SDK with platform 37 (Android Studio installs it); point Gradle at it via `ANDROID_HOME`
  or `sdk.dir=/path/to/Android/Sdk` in `local.properties`. Without an SDK the whole build fails, Desktop included.
- JDK 17+ (the Gradle wrapper downloads Gradle 9.6.1 itself)
- Internet access on first build (dependencies, Node/Yarn for the Wasm target)
- Python 3 for `tools/check-strings.py`

## Run
```
./gradlew :desktopApp:run                      # Desktop (main target)
./gradlew :webApp:wasmJsBrowserDevelopmentRun  # Web (Wasm), opens in the browser
./gradlew :androidApp:installDebug             # Android (device/emulator connected)
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
- `androidApp` - Android entry point (`MainActivity`)

## Mock <-> network
The top-bar button "Mock data / crates.io" flips `AppState.useMocks`; `CratesRepository` reads the flag on every
call, so the UI is identical for both sources. Default: mocks. On Web the real API may be blocked by CORS.

## Not in v1 (by design)
No persistence (theme/language/data source live in memory), no width-adaptive layouts, no deep links / address bar
integration, catalog data is not translated.

## Targets
Desktop (JVM), Web (Wasm) and Android. iOS is not included (needs Xcode/macOS).
Android language switching sets the JVM default locale; if Compose resources on your Android version read the
system locale instead, the in-app RU/EN button will not change strings there.
