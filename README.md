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
implementation "io.github.amjdalhashede:ringtone-smart-kit:1.0.2-alpha"
```

### Gradle Kotlin DSL (build.gradle.kts)

```kotlin
implementation("io.github.amjdalhashede:ringtone-smart-kit:1.0.2-alpha")
```

## Required Permissions

| Feature                          | Required Permissions                                                                                      | Notes                                                                                   |
|---------------------------------|----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| **Set System Ringtone**          | - `android.permission.WRITE_SETTINGS`<br>- `android.permission.READ_EXTERNAL_STORAGE` (for files on storage, API ≤ 32)<br>- `android.permission.WRITE_EXTERNAL_STORAGE` (API ≤ 32 only) | `WRITE_SETTINGS` permission is mandatory to change system ringtone.<br>Storage permissions needed only on older Android versions (API 32 and below). |
| **Set Contact Ringtone**         | - `android.permission.READ_EXTERNAL_STORAGE` (if ringtone from storage, API ≤ 32)<br>- `android.permission.WRITE_EXTERNAL_STORAGE` (API ≤ 32 only) | Storage permissions only on older devices.<br>Contacts permissions NOT required in this setup. |
| **Interactive Contact Picker**   | No extra permissions needed if contacts permissions are not declared.                                     | Contact picker might not work fully without contact permissions.                        |

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



## Usage

### 1. Setting a system ringtone

```kotlin
RingtoneHelper.setSystemRingtone(
    source = RingtoneSource.FromAssets("ringtones/my_ringtone.mp3")
).onSuccess {
    println("System ringtone set successfully")
}.onFailure { error ->
    println("Failed to set system ringtone: ${error.message ?: "Unknown error"}")
}.onDone {
    println("Operation completed")
}
```

---

### 2. Setting a ringtone for a specific contact

```kotlin
val contact = ContactIdentifier.ByPhone("+1234567890")

RingtoneHelper.setContactRingtone(
    source = RingtoneSource.FromAssets("ringtones/my_ringtone.mp3"),
    contact = contact
).onSuccess { contactInfo ->
    println("Contact ringtone set successfully for: ${contactInfo?.displayName}")
}.onFailure { error ->
    println("Failed to set contact ringtone: ${error.message ?: "Unknown error"}")
}.onDone {
    println("Operation completed")
}

```

---

### 3. Applying ringtone to any target (system or contact)

```kotlin
val target = RingtoneTarget.Contact(ContactIdentifier.ById(42))

RingtoneHelper.applyToTarget(
    source = RingtoneSource.FromAssets("another_ringtone.mp3"),
    target = target
).onSuccess {
    println("Ringtone applied successfully")
}.onFailure { error ->
    println("Error applying ringtone: ${error.message ?: "Unknown error"}")
}.onDone {
    println("Operation completed")
}

```

---

### 4. Applying ringtone from local storage

```kotlin
val source = RingtoneSource.FromStorage(Uri.parse("content://media/internal/audio/media/10"))

RingtoneHelper.setSystemRingtone(
    source = source,
    type = RingtoneType.ALARM
).onSuccess {
    println("Alarm ringtone set successfully")
}.onFailure {
    println("Failed to set ringtone from storage")
}.onDone {
    println("Operation completed")
}

```

---

### 5. Setting ringtone via interactive contact picker

```kotlin
RingtoneHelper.setContactRingtone(
    source = RingtoneSource.FromAssets("ringtones/my_ringtone.mp3"),
    contact = ContactIdentifier.Interactive
).onSuccess { info ->
    println("Contact ringtone set for: ${info?.displayName}")
}.onFailure { error ->
    println("Failed to set contact ringtone: ${error.message ?: "Unknown error"}")
}.onDone {
    println("Operation completed")
}

```

---

### Callback Behavior Comparison

| Callback    | Triggered when...                                    | Contact Ringtone Specifics                        |
|-------------|------------------------------------------------------|---------------------------------------------------|
| `onSuccess` | Ringtone is applied successfully                     | Returns `ContactInfo?` containing contact details |
| `onError`   | An error occurs while applying the ringtone          | Returns the thrown `Throwable`                    |
| `onDone`    | Operation completes (whether it succeeded or failed) | Can be used to dismiss loaders, etc.              |

## Sources available for ringtones

```kotlin
sealed class RingtoneSource {
    data class FromAssets(val filePath: String, val outputDirPath: String = "") : RingtoneSource()
    data class FromStorage(val uri: Uri) : RingtoneSource()
    // Note: FromUrl will be available in the future
    // data class FromUrl(val url: String) : RingtoneSource()
}
```

---

## Future feature: Setting ringtone from URL

The `FromUrl` source will be supported in a future release:

```kotlin
// This will be available soon
// RingtoneSource.FromUrl("https://example.com/ringtone.mp3")
```

## Contributing

Contributions are welcome and appreciated!  
If you find a bug, have a feature request, or want to improve the code/documentation, feel free to:

- Open an issue
- Create a pull request
- Suggest enhancements

Please make sure your changes follow the current code style and include proper documentation or
tests when applicable.

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
🐙 [GitHub Packages](https://github.com/AmjdAlhashede/RingtoneSmartKitProject/packages): Published to
the GitHub repository packages as an alternative.

- 💻 **GitHub Repository**: Source code, examples, and documentation are available here:
  [https://github.com/AmjdAlhashede/RingtoneSmartKitProject](https://github.com/AmjdAlhashede/RingtoneSmartKitProject)

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




