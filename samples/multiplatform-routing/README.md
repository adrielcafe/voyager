# multiplatform sample

### Running iOS
- IPhone: `./gradlew :samples:multiplatform-routing:iosDeployIPhone8Debug`
- IPad: `./gradlew :samples:multiplatform-routing:iosDeployIPadDebug`

### Running MacOS Native app (Desktop using Kotlin Native)
```shell
./gradlew :samples:multiplatform-routing:runNativeDebug
```

### Running JVM Native app (Desktop)
```shell
./gradlew :samples:multiplatform-routing:run
```

### Running Web Compose Canvas
```shell
./gradlew :samples:multiplatform-routing:jsBrowserDevelopmentRun
```

### Building Android App
```shell
./gradlew :samples:multiplatform-routing:assembleDebug
```

If you want to run Android sample in the emulator, you can open the project and run the application configuration `samples.multiplatform-routing` on Android Studio.
