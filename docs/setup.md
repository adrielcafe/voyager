# Setup

1.  Add Maven Central to your repositories if needed

    ```kotlin
    repositories {
        mavenCentral()
    }
    ```
2. Add the desired dependencies to your module's `build.gradle` file

=== "Dependencies"

    ```kotlin
    dependencies {
        val voyagerVersion = "1.1.0-beta02"
        
        // Multiplatform
        
        // Navigator
        implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
        
        // Screen Model
        implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
        
        // BottomSheetNavigator
        implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
        
        // TabNavigator
        implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
        
        // Transitions
        implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
        
        // Koin integration
        implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
        
        // Android
        
        // Hilt integration
        implementation("cafe.adriel.voyager:voyager-hilt:$voyagerVersion")
        
        // LiveData integration
        implementation("cafe.adriel.voyager:voyager-livedata:$voyagerVersion")
        
        // Desktop + Android
        
        // Kodein integration
        implementation("cafe.adriel.voyager:voyager-kodein:$voyagerVersion")
        
        // RxJava integration
        implementation("cafe.adriel.voyager:voyager-rxjava:$voyagerVersion")
    }
    ```

=== "Version Catalog"

    ```toml
    [versions]
    voyager = "1.1.0-beta02"
    
    [libraries]
    voyager-navigator = { module = "cafe.adriel.voyager:voyager-navigator", version.ref = "voyager" }
    voyager-screenModel = { module = "cafe.adriel.voyager:voyager-screenmodel", version.ref = "voyager" }
    voyager-bottomSheetNavigator = { module = "cafe.adriel.voyager:voyager-bottom-sheet-navigator", version.ref = "voyager" }
    voyager-tabNavigator = { module = "cafe.adriel.voyager:voyager-tab-navigator", version.ref = "voyager" }
    voyager-transitions = { module = "cafe.adriel.voyager:voyager-transitions", version.ref = "voyager" }
    voyager-koin = { module = "cafe.adriel.voyager:voyager-koin", version.ref = "voyager" }
    voyager-hilt = { module = "cafe.adriel.voyager:voyager-hilt", version.ref = "voyager" }
    voyager-kodein = { module = "cafe.adriel.voyager:voyager-kodein", version.ref = "voyager" }
    voyager-rxjava = { module = "cafe.adriel.voyager:voyager-rxjava", version.ref = "voyager" }
    ```

!!! note "Current version [here](https://github.com/adrielcafe/voyager/releases)."

### Platform compatibility

**Multiplatform targets**: Android, iOS, Desktop, Mac Native, Web.

|                                |      Android       |      Desktop       |   Multiplatform    |
|:------------------------------:|:------------------:|:------------------:|:------------------:|
|       voyager-navigator        | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|      voyager-screenModel       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| voyager-bottom-sheet-navigator | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|     voyager-tab-navigator      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|      voyager-transitions       | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|          voyager-koin          | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|         voyager-kodein         | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|     voyager-lifecycle-kmp      | :white_check_mark: | :white_check_mark: | :white_check_mark: |
|          voyager-hilt          | :white_check_mark: |                    |                    |
|         voyager-rxjava         | :white_check_mark: | :white_check_mark: |                    |
|        voyager-livedata        | :white_check_mark: |                    |                    |
