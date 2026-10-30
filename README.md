# ZuGPT — Android Starter

ZuGPT is a black-and-purple Android AI assistant starter project built with Kotlin and Jetpack Compose.

## Current starter features
- Futuristic black + purple interface
- Chat UI and navigation drawer
- Library, Coding Studio, Deep Research, History, and Settings screens
- GitHub Actions workflow to build a debug APK

## Important
This is the **starter interface**, not a fully connected AI yet. Chat replies are currently a placeholder. AI model/API, PDF parsing/indexing, live web search, persistent chat history, and project ZIP generation must be connected in later steps.

## Build APK on GitHub
1. Upload all files in this project to the root of your `ZuGPT` repository.
2. Commit to the `main` branch.
3. Open the repository's **Actions** tab.
4. Select **Build ZuGPT APK** and press **Run workflow** (or let the push trigger it).
5. Open the completed workflow run and download the `ZuGPT-debug-apk` artifact.
6. Extract the downloaded ZIP on your phone and install `app-debug.apk`. Android may ask you to allow installs from that browser/file manager.

## Tech
- Kotlin
- Jetpack Compose / Material 3
- Android Gradle Plugin 8.7.3
- GitHub Actions + Java 17 + Gradle 8.9

## Security
Never commit API keys, passwords, signing keys, or other secrets to a public repository. Add future credentials through GitHub Actions Secrets or a backend secret store.
