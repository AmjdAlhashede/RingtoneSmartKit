# RingtoneSmartKit

A Kotlin library for easy ringtone management on Android, supporting multiple ringtone sources and providing a simplified API that does not require passing Context or Activity.


## Installation

Add the following dependency to your `build.gradle` or `build.gradle.kts` file:

<details>
<summary>Gradle Groovy (build.gradle)</summary>

```groovy
implementation "io.github.amjdalhashede:ringtone-smart-kit:1.0.2-alpha"
```

</details>

<details>
<summary>Gradle Kotlin DSL (build.gradle.kts)</summary>

```kotlin
implementation("io.github.amjdalhashede:ringtone-smart-kit:1.0.2-alpha")
```

</details>



## Features

- Simplified API for setting system and contact ringtones without needing to pass Context or Activity.
- Supports multiple ringtone sources, including assets, local storage (URI), and remote URLs.
- Easy assignment of ringtones to specific contacts via different contact identifiers (ID, URI, phone number).
- Asynchronous operations with success and error callbacks for better control.
- Supports setting different types of system ringtones: call, notification, and alarm.
- Fully written in Kotlin with idiomatic usage and extension-friendly design.
