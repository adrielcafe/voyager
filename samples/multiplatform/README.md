# multiplatform sample

### Running iOS
- Open Xcode project with: `open samples/multiplatform-iosApp/iosApp.xcodeproj`
- Run/Build in Xcode

### Running MacOS Native app (Desktop using Kotlin Native)
```shell
./gradlew :samples:multiplatform:runNativeDebug
```

### Running JVM Native app (Desktop)
```shell
./gradlew :samples:multiplatform:run
```

### Running Web Compose Canvas
```shell
./gradlew :samples:multiplatform:jsBrowserDevelopmentRun
```

### Building Android App
```shell
./gradlew :samples:multiplatform:assembleDebug
```

If you want to run Android sample in the emulator, you can open the project and run the application configuration `samples.multiplatform` on Android Studio.
