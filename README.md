![Cover Image](docs/assets/ringtone_smart_kit_cover_image.png)
# Android Set Ringtone - Kotlin Library

![API Level](https://img.shields.io/badge/API_Level-22%2B-blue.svg?style=flat-square)
![Maven Central](https://img.shields.io/maven-central/v/io.github.amjdalhashede/ringtone-smart-kit?style=flat-square&label=latest%20version&color=blue)

Easily set and customize ringtones, notification sounds, and alarms programmatically on Android using Kotlin.  
Supports setting ringtones globally or per contact from assets or local files (URIs) — all without boilerplate code.

Perfect for Android developers who need a simple, flexible ringtone manager and setter library written entirely in Kotlin.

## Features

- Simplified API for setting system and contact ringtones without needing to pass Context or
  Activity.
- Supports multiple ringtone sources, including assets, local storage (URI), and remote URLs.
- Easy assignment of ringtones to specific contacts via different contact identifiers (ID, URI,
  phone number).
- Asynchronous operations with success and error callbacks for better control.
- Supports setting different types of system ringtones: call, notification, and alarm.
- Fully written in Kotlin with idiomatic usage and extension-friendly design.

## Installation

Add the following dependency to your `build.gradle` or `build.gradle.kts` file:

### Gradle Groovy (build.gradle)

```groovy
implementation "io.github.amjdalhashede:ringtone-smart-kit:1.0.5"
```

### Gradle Kotlin DSL (build.gradle.kts)

```kotlin
implementation("io.github.amjdalhashede:ringtone-smart-kit:1.0.5")
```

## Required Permissions

| Feature                    | Required Permissions                                                                                      | Notes                                                                                   |
|----------------------------|----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| **Set System Ringtone**    | - `android.permission.WRITE_SETTINGS`<br>- `android.permission.READ_EXTERNAL_STORAGE` (API ≤ 32)<br>- `android.permission.WRITE_EXTERNAL_STORAGE` (API ≤ 32) | Required to change system ringtone and access local files (on older Android versions). |
| **Set Contact Ringtone**   | - `android.permission.READ_CONTACTS`<br>- `android.permission.WRITE_CONTACTS`<br>- `android.permission.READ_EXTERNAL_STORAGE` (API ≤ 32)<br>- `android.permission.WRITE_EXTERNAL_STORAGE` (API ≤ 32) | Required to update contact ringtones and access files from storage.                    |
| **Interactive Contact Picker** | - `android.permission.READ_CONTACTS`<br>- `android.permission.WRITE_CONTACTS`                          | Required for selecting and modifying contact info interactively.                        |

---

### Add these permissions in your AndroidManifest.xml (copy one line at a time):

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" android:maxSdkVersion="32" />
``` 

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="32" tools:ignore="ScopedStorage" />
```

```xml
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
```

```xml
 <uses-permission android:name="android.permission.READ_CONTACTS" /> 
```
 
```xml
 <uses-permission android:name="android.permission.WRITE_CONTACTS" />
```

## 🎯 Usage
 
Use one of these methods to set ringtones:

### 🎧 Set a system ringtone

```kotlin
RingtoneHelper.setSystemRingtone(
  source = RingtoneSource.FromAssets("ringtones/my_ringtone.mp3"),
  target = SystemTarget.Call // or Notification, Alarm
).onSuccess {
  println("✅ System ringtone set successfully")
}.onFailure { error ->
  println("❌ Error setting system ringtone: ${error.message}")
}.onDone {
  println("Operation completed")
}.launch()

```

> ℹ️ **Important:** `onSuccess` for system ringtones is `onSuccess { }` — it takes no arguments.

### 📱 Set a ringtone for a specific contact

```kotlin
RingtoneHelper.setContactRingtone(
  source = RingtoneSource.FromStorage(Uri.parse("content://media/internal/audio/media/10")),
  target = ContactTarget.ByPhone("+1234567890")
).onSuccess { contact ->
  println("✅ Ringtone set for contact: ${contact.displayName}")
}.onFailure { error ->
  println("❌ Error setting contact ringtone: ${error.message}")
}.onDone {
  println("Operation completed")
}.launch()

```

> ℹ️ **Important:** `onSuccess(block: (ContactInfo) -> Unit)` returns contact details for contact ringtones only.

---

## 🎵 RingtoneSource

You can load ringtones from different types of sources:

```kotlin
// From the app's assets directory:
RingtoneSource.FromAssets(filePath = "ringtones/my_ringtone.mp3")   

// From device storage:
RingtoneSource.FromStorage(uri = "content://media/internal/audio/media/10".toUri()))
 
```

> ⚠️ **About asset file paths** You can use any of these path styles for assets:
>
> ```kotlin
> RingtoneSource.FromAssets(filePath = "ringtones/my_ringtone.mp3")
> RingtoneSource.FromAssets(filePath = "assets/ringtones/my_ringtone.mp3")
> RingtoneSource.FromAssets(filePath = "file:///android_assets/ringtones/my_ringtone.mp3")
> ```
>
> Internally, `RingtoneHelper` will normalize these paths, so feel free to use whichever style you prefer. ✅ Recommendation: Use `ringtones/my_ringtone.mp3` for a concise path.

---

## 🎷 RingtoneTarget

### For system ringtones:

```kotlin
SystemTarget.Call
SystemTarget.Notification
SystemTarget.Alarm
```

### For contact ringtones:

```kotlin
ContactTarget.Interactive
ContactTarget.ByPhone("+1234567890")
ContactTarget.ById(42L)
ContactTarget.ByUri(contactUri)
```

---

## 💡 Callbacks

| Callback    | Triggered when...                                                                                                     |
| ----------- | --------------------------------------------------------------------------------------------------------------------- |
| `onSuccess` | Ringtone is successfully set. Either provides `ContactInfo` if a contact target or no arguments for system ringtones. |
| `onFailure` | An error occurs. Provides a `Throwable` for debugging.                                                                |
| `onDone`    | Operation finishes (either success or failure).                                                                       |

---
 
## Contact

If you have suggestions, questions, or feedback, feel free to reach out:

- Open an issue on [GitHub](https://github.com/AmjdAlhashede/RingtoneSmartKitProject/issues)
- Contact me directly via [GitHub Profile](https://github.com/AmjdAlhashede)
- Or send an email to: **amjdalhashede@gmail.com**

## Availability

This library is available via:

📦 [Maven Central](https://central.sonatype.com/artifact/io.github.amjdalhashede/ringtone-smart-kit):
Easy integration using Gradle/Maven.  
🐙 [GitHub Packages](https://github.com/AmjdAlhashede/RingtoneSmartKit/packages): Published to
the GitHub repository packages as an alternative.

- 💻 **GitHub Repository**: Source code, examples, and documentation are available here:
  [https://github.com/AmjdAlhashede/RingtoneSmartKit](https://github.com/AmjdAlhashede/RingtoneSmartKit)


💭 **Enjoy!**\
Feel free to contribute or open issues if you have suggestions or encounter problems. Happy coding! 🎵

## License

Copyright 2025 Amjd Alhashede

Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at:

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.




