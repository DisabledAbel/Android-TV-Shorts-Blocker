# ShortsBlocker TV 🚫📺

Automatically block YouTube Shorts on Android TV with an on/off toggle switch.

![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-Android%20TV-blue)

## Features

- ✅ **Auto-detect & Block**: Instantly exits YouTube Shorts when detected
- ✅ **On/Off Switch**: Toggle blocking without disabling Accessibility Service
- ✅ **Android TV Optimized**: Works with remote control, Leanback UI
- ✅ **Background Service**: Runs automatically once enabled
- ✅ **No Root Required**: Uses Android Accessibility Services

## How It Works

The app uses Android's Accessibility Service to monitor the YouTube TV app. When it detects Shorts UI elements (reels, shorts player, specific view IDs), it automatically simulates a "Back" button press to exit the Short, returning you to regular videos.

**Note**: The Accessibility Service must remain enabled in system settings, but the in-app switch lets you toggle the blocking behavior on/off instantly.

## Installation

### Method 1: Direct Download (Recommended)
Download the latest APK from the [Releases](../../releases) section (if published) or from the Actions artifacts.

### Method 2: Build with GitHub Actions (Free)
1. Fork this repository
2. Go to **Actions** tab → **Build APK** → **Run workflow**
3. Wait 3-4 minutes for the build to complete
4. Download the `ShortsBlocker-APK-v2` artifact
5. Extract the ZIP to get `app-debug.apk`

### Method 3: Install on Android TV

**Option A: Send Files to TV**
1. Install "Send Files to TV" on your iPad/Android phone and Android TV
2. Send the APK file to your TV
3. Open file manager on TV, install the APK

**Option B: Downloader App**
1. Upload APK to Google Drive/Dropbox and get a direct link
2. On Android TV, open **Downloader** app
3. Enter the download URL
4. Install when prompted

**Option C: ADB (Advanced)**
```bash
adb install app-debug.apk
```

## Setup

### First Time Setup
1. Open the **ShortsBlocker** app on your TV
2. You'll be redirected to **Accessibility Settings** (required)
3. Find **"ShortsBlocker"** in the list → Toggle **ON**
4. Return to the ShortsBlocker app

### Using the Toggle Switch
- Open the ShortsBlocker app from your app drawer
- Use the **Switch** to toggle:
  - 🟢 **ON** (Green): Blocks Shorts automatically
  - ⚫ **OFF** (Gray): Allows Shorts to play normally

**Note**: Your preference is saved automatically. The setting persists after reboots.

## Troubleshooting

**"App not installed"**
- Enable "Unknown Sources" in Settings → Security → Unknown Sources
- Or enable for your specific file manager app

**"Service not appearing in Accessibility"**
- Reboot your Android TV after installing
- Some Fire TV devices may require ADB to enable: `adb shell settings put secure enabled_accessibility_services com.example.shortsblocker/com.example.shortsblocker.ShortsBlockerService`

**Shorts not being blocked**
- Make sure the switch is ON in the app
- Verify Accessibility Service is enabled in system settings
- Different YouTube TV versions may have different UI elements - check the logs or submit an issue

**App crashes or doesn't open**
- Uninstall and reinstall the APK
- Clear app data in Settings → Apps → ShortsBlocker

## Technical Details

- **Min SDK**: 21 (Android 5.0+)
- **Target SDK**: 34 (Android 14)
- **Permissions**: `BIND_ACCESSIBILITY_SERVICE` (required for detecting UI)
- **Storage**: Uses SharedPreferences to save on/off state (~1KB)

## Privacy

This app does not collect any data. It operates entirely locally on your device. The Accessibility Service is used solely to detect YouTube Shorts UI elements and does not transmit any information.

## Building Locally

If you prefer to build locally instead of using GitHub Actions:

1. Clone this repository
2. Open in Android Studio (or use command line Gradle)
3. Sync project with Gradle files
4. Build → Build APK(s)

## License

MIT License - Feel free to modify and distribute.

---

**Disclaimer**: This app is not affiliated with YouTube or Google. Use at your own risk. YouTube's UI changes may affect functionality.
```
