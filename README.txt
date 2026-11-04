AMONG US HOST MENU — JAVA + KOTLIN ANDROID PROJECT

This is an Android Gradle project configured for modern Android IDEs that support
Gradle 8.7, Android Gradle Plugin 8.6.1, and JDK 17. It supports Java source and
Kotlin source files. The current app implementation is Java; Kotlin support is
enabled in the build configuration.

BUILD REQUIREMENTS
- Android IDE with Gradle project import support (for example, AndroidIDE)
- JDK 17
- Android SDK Platform 35 and matching build tools
- Internet access on first build so Gradle can download its plugins/dependencies

BUILD STEPS
1. Extract the ZIP.
2. In the IDE, choose Open Existing Project / Import Gradle Project.
3. Select the extracted AmongUsHostMenu_JavaKotlin folder (not the ZIP).
4. Allow Gradle sync and SDK downloads.
5. Build > Assemble/Build APK.
6. Find the debug APK under app/build/outputs/apk/debug/.

NOTE
- No Gradle wrapper JAR is bundled; use the IDE's installed Gradle or let the IDE
  generate/use its wrapper. The wrapper properties specify Gradle 8.7.
- This is only a launcher and floating UI prototype. It does not modify Among Us,
  detect actual host status, reveal roles, force roles, change cooldowns, teleport,
  noclip, or kick players. The menu entries explain that they need an authorized
  private-game integration.
- Android overlay permission must be granted by the user in system settings.
