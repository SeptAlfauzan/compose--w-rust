# Simple Android Jetpack Compose /w Rust code bridge
This project is aim to bridge Rust code in Android. For more explanation you can watch this [youtube video](https://www.youtube.com/watch?v=cUKcrfdFRqk&pp=ygUQa290bGluIGNvbmYgcnVzdA%3D%3D)

### Video Benchmark
[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/KOQHbbq7WQs/0.jpg)](https://www.youtube.com/watch?v=KOQHbbq7WQs)
 
### How to run this project
Before to run this project make sure in your machine is already installed Rust, then you can follow these steps

1. Install NDK and CMake in your Android Studio by going to Settings > Language & Frameworks > Android SDK > SDK Tools.
2. Run these commands to enables cross-compile Rust code for Android devices with ARM64 and x84 64bit processors.
    ```shell
   rustup target add aarch64-linux-android
   rustup target add x86_64-linux-android
   ```
3. Run this command and finally you can run the project
    ```
   ./gradlew clean; ./gradlew build
   ```