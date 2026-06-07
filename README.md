# Virtual Power Button

Virtual Power Button is an Android application that emulates power-button functionality for devices with a faulty or broken physical power button. It provides quick access to locking the screen and to the system power menu through Quick Settings tiles and app shortcuts.

## Features

- Lock the screen on demand
- Open the system power menu (power off, restart options)
- Two Quick Settings tiles — one to lock the screen, one to open the power menu
- App shortcuts: long-press the app icon for "Lock Screen" and "Power Menu"
- Haptic feedback for actions
- Material Design 3 UI with dynamic color (Material You) and light/dark theme support
- Localized into 10 languages (English, Russian, German, French, Spanish, Italian, Portuguese, Korean, Japanese, Simplified Chinese)

## Requirements

- Android 10 or higher (API level 29+)
- Accessibility Service permission (required to lock the screen and open the power menu)

## Setup Instructions

1. Launch the app.
2. Grant the Accessibility Service permission when prompted:
    - The app redirects you to the system Accessibility settings.
    - Find "Virtual Power Button" in the list and enable it.
3. Trigger actions from either:
    - the **Quick Settings tiles** (add them from the Quick Settings editor), or
    - a **long-press on the app icon**, then choose "Lock Screen" or "Power Menu".

## How It Works

The app uses Android's Accessibility Service API to provide power-button functionality:
- Screen locking is implemented using `GLOBAL_ACTION_LOCK_SCREEN`
- The power menu is opened using `GLOBAL_ACTION_POWER_DIALOG`

Entry points — the Quick Settings tiles and the app shortcuts — route to the accessibility service, which performs the global action. App shortcuts are declared statically in `app/src/main/res/xml/shortcuts.xml` on the launcher activity.

## Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/arttttt/VirtualPowerButton.git
```

2. Open the project in Android Studio.

3. Build a debug APK:
```bash
./gradlew assembleDebug
```

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- ViewModel
- Kotlin Flow (StateFlow)
- Android Accessibility Service
- Quick Settings Tiles & static App Shortcuts

## Architecture

The app follows the MVVM pattern:
- `ViewModel` holds the screen state
- `StateFlow` for state management
- Jetpack Compose for the declarative UI

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or suggestions, please open an issue in the GitHub repository.

---
Made with ❤️ by arttttt
