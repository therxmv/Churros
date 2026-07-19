# Churros

> "Like chores, but sweeter."

Android-first household management app for families, couples, and roommates. Built with Kotlin Multiplatform + Compose Multiplatform.

## Features (planned)

- **Chores** — recurring tasks, assignments, priorities
- **Shopping Lists** — real-time collaborative, multi-store
- **Notes** — digital sticky notes with color labels
- **Family** — member management, roles, invites

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.4.0 |
| Multiplatform | Kotlin Multiplatform |
| UI | Compose Multiplatform 1.11.1 |
| Architecture | Clean Architecture + MVI |
| DI | Koin |
| Backend | Supabase (Auth, DB, Realtime, Storage) |
| Local cache | Room (KMP) |
| Networking | Ktor |
| Push / Analytics | Firebase |

## Project Structure

```
androidApp/     — Android application entry point
iosApp/         — Xcode project (iOS entry point)
shared/         — KMP shared module (common + platform-specific code)
  src/
    commonMain/ — shared Kotlin code and Compose UI
    androidMain/— Android-specific implementations
    iosMain/    — iOS-specific implementations
docs/           — Product vision, design system, tech stack specs
```

## Prerequisites

- JDK 21 (Amazon Corretto recommended)
- Android Studio Meerkat or later
- Xcode 16+ (for iOS builds)

## Building

```bash
# Android debug APK
./gradlew :androidApp:assembleDebug

# iOS — open in Xcode
open iosApp/iosApp.xcodeproj
```

## Testing

```bash
# Shared module — Android host tests
./gradlew :shared:testAndroidHostTest

# Shared module — iOS simulator tests
./gradlew :shared:iosSimulatorArm64Test

# Screenshot tests (golden image regression)
./gradlew :androidApp:updateDebugScreenshotTest   # regenerate / update goldens
./gradlew :androidApp:validateDebugScreenshotTest # CI check — fails on visual diff
```

## Documentation

- [`docs/churros_tech_stack.md`](docs/churros_tech_stack.md) — Architecture decisions and tech stack
- [`docs/Design/`](docs/Design/) — Screen mockups (Auth, Chores, Family, Home, Onboarding, Profile)

## Versions

| Tool | Version |
|------|---------|
| Kotlin | 2.4.0 |
| Compose Multiplatform | 1.11.1 |
| AGP | 9.2.1 |
| compileSdk / targetSdk | 37 |
| minSdk | 24 |
| JDK | 21 |
