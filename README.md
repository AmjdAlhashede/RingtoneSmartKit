![Cover Image](docs/assets/ringtone_smart_kit_cover_image.png)
# RingtoneSmartKit

A Kotlin library for easy ringtone management on Android, supporting multiple ringtone sources and
providing a simplified API that does not require passing Context or Activity.

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
implementation "io.github.amjdalhashede:ringtone-smart-kit:1.0.4"
```

### Gradle Kotlin DSL (build.gradle.kts)

```kotlin
implementation("io.github.amjdalhashede:ringtone-smart-kit:1.0.4")
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
    type = RingtoneType.RINGTONE // or ALARM, NOTIFICATION
).onSuccess {
    println("✅ System ringtone set successfully")
}.onFailure { error ->
    println("❌ Error setting system ringtone: ${error.message}")
}.onDone {
    println("Operation completed")
}
```

> ℹ️ **Important:** `onSuccess` for system ringtones is `onSuccess { }` — it takes no arguments.

### 📱 Set a ringtone for a specific contact

```kotlin
RingtoneHelper.setContactRingtone(
    source = RingtoneSource.FromStorage(Uri.parse("content://media/internal/audio/media/10")),
    contact = ContactIdentifier.ByPhone("+1234567890")
).onSuccess { contactInfo ->
    println("✅ Ringtone set for contact: ${contactInfo.displayName}")
}.onFailure { error ->
    println("❌ Error setting contact ringtone: ${error.message}")
}.onDone {
    println("Operation completed")
}
```

> ℹ️ **Important:** `onSuccess(block: (ContactInfo) -> Unit)` returns contact details for contact ringtones only.

---

## 🎵 RingtoneSource

You can load ringtones from different types of sources:

```kotlin
// From the app's assets directory:
RingtoneSource.FromAssets(filePath = "ringtones/my_ringtone.mp3") or RingtoneSource.FromAssets(filePath = "ringtones/my_ringtone.mp3", outputDirPath = filesDir.path)
 

// From device storage:
RingtoneSource.FromStorage(uri = Uri.parse("content://media/internal/audio/media/10"))

// Coming soon — From a remote URL:
RingtoneSource.FromUrl("https://example.com/ringtone.mp3")
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

## 🎧 RingtoneTarget

You can target the contacts :

```kotlin 
// A specific contact by URI:
ContactIdentifier.ByUri(contactUri)

// A specific contact by ID:
ContactIdentifier.ById(42L)

// A specific contact by phone number:
ContactIdentifier.ByPhone("+1234567890")

// Interactive contact picker (select contact at runtime):
ContactIdentifier.Interactive
```

---

## 💡 Callbacks

| Callback    | Triggered when...                                                                                                     |
| ----------- | --------------------------------------------------------------------------------------------------------------------- |
| `onSuccess` | Ringtone is successfully set. Either provides `ContactInfo` if a contact target or no arguments for system ringtones. |
| `onFailure` | An error occurs. Provides a `Throwable` for debugging.                                                                |
| `onDone`    | Operation finishes (either success or failure).                                                                       |

---

## 🚀 Coming Soon

Support for setting ringtones directly from remote URLs:

```kotlin
RingtoneSource.FromUrl("https://example.com/ringtone.mp3")
```
 
 
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




