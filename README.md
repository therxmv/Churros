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
```

## Documentation

- [`docs/churros_idea.md`](docs/churros_idea.md) — Product vision and feature specs
- [`docs/churros_design_system.md`](docs/churros_design_system.md) — Colors, typography, spacing
- [`docs/churros_tech_stack.md`](docs/churros_tech_stack.md) — Architecture decisions

## Versions

| Tool | Version |
|------|---------|
| Kotlin | 2.4.0 |
| Compose Multiplatform | 1.11.1 |
| AGP | 9.0.1 |
| compileSdk / targetSdk | 36 |
| minSdk | 24 |
| JDK | 21 |
