# ImpressMap

ImpressMap is an Android application for location-based discussions about neighbourhood improvement. Authenticated users select residential addresses, explore their areas on Google Maps, create location-bound posts, and discuss proposals in nested comment threads.

[Watch the application demo](impressmap-demo.mp4)

## User flows

- Sign up and sign in with Firebase Authentication.
- Select up to five residential addresses and display their areas on the map.
- Activate one of the selected addresses and create map markers with posts attached to that area.
- Browse posts associated with a marker.
- Comment on posts and reply to other comments to build discussion threads.

## Technology

- Java 8 and Android SDK 33
- Google Maps SDK for Android
- Firebase Authentication and Firebase Realtime Database
- AndroidX Navigation
- ViewModel and LiveData
- View Binding and Data Binding

## Project structure

The application separates Android UI components, map adapters, data models, and Firebase access classes. ViewModel and LiveData keep screen state across the main navigation flows, while repository-like classes encapsulate reads and writes to Firebase Realtime Database.

## Running locally

Requirements:

- Android Studio with Android SDK 33
- JDK 11 for the Android Gradle Plugin
- a Google Maps API key
- a Firebase Android application with Authentication and Realtime Database enabled

Clone the repository and add the Maps key to the root `local.properties` file:

```properties
MAPS_API_KEY=your_google_maps_api_key
```

For an independent Firebase environment, download its `google-services.json` file and replace `app/google-services.json`. The repository contains the historical client configuration used during development, but availability and access policies of the original backend are not guaranteed.

Build the debug APK on Windows:

```powershell
gradlew.bat assembleDebug
```

On Linux or macOS, the preserved Gradle wrapper can be launched through the shell:

```bash
bash ./gradlew assembleDebug
```

## Project background

ImpressMap was created as a graduation project for Samsung IT School in 2023. It received a first-degree diploma at the regional stage and a score of 9.5 in the second round of the national project competition.

- [Regional results](https://lyceum.nstu.ru/samsung-it-school/vypusknoj-it-shkoly-samsung-2023)
- [Second-round results](https://www.innovationcampus.ru/wp-content/uploads/2023/06/2tour2023.pdf)
