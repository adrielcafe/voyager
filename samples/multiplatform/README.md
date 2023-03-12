# multiplatform sample

### Running iOS
- IPhone: `./gradlew :samples:multiplatform:iosDeployIPhone8Debug`
- IPad: `./gradlew :samples:multiplatform:iosDeployIPadDebug`

### Running MacOS Native app (Desktop using Kotlin Native)
```shell
./gradlew :samples:multiplatform:runNativeDebug
```

### Running JVM Native app (Desktop)
```shell
./gradlew :samples:multiplatform:run
```

### Building Android App
```shell
./gradlew :samples:multiplatform:assembleDebug
```

If you want to run Android sample in the emulator, you can open the project and run the application configuration `samples.multiplatform` on Android Studio.
