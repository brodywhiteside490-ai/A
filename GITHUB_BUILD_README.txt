GITHUB APK BUILD SETUP

This folder includes a GitHub Actions workflow that builds a debug APK on GitHub's
hosted Linux runner and uploads the APK as a workflow artifact.

I cannot create a new GitHub repository from the connected GitHub tools available
in this chat. To use this workflow:
1. Create a new empty GitHub repository in your GitHub account.
2. Upload the CONTENTS of this extracted folder to the repository (keep the
   .github/workflows/build.yml file and its folders).
3. Commit to the main branch, or open Actions and run "Build Android APK".
4. Open the completed workflow run and download the
   "AmongUsHostMenu-debug-apk" artifact.

The APK is a launcher/floating-menu UI prototype only. It does not alter Among Us
or manipulate public multiplayer matches.
