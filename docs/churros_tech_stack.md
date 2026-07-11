# Churros — Technology Stack

> **Mission:** Build a modern, Android-first household organizer using Kotlin Multiplatform, with a native Android experience as the primary target and a polished iOS application built from the same shared codebase.

---

# High-Level Architecture

```
                         +----------------------+
                         | Android (Primary)    |
                         | Compose Multiplatform|
                         +----------+-----------+
                                    |
                          Shared UI & Business Logic
                                    |
                        Kotlin Multiplatform (KMP)
                                    |
           +------------------------+------------------------+
           |                                                 |
 Android App (Reference)                         iOS App (Secondary)
           |                                                 |
           +------------------------+------------------------+
                                    |
                             Repository Layer
                                    |
                       Ktor + Supabase Kotlin SDK
                                    |
                 +------------------+------------------+
                 |                                     |
            Supabase                           Firebase
   PostgreSQL/Auth/Realtime/etc.      FCM + Analytics + Crashlytics
```

---

# Platform Strategy

## Android First

Android is the primary development platform.

Every feature is:

- Designed for Android first
- Implemented on Android first
- Tested on Android first
- Considered complete only after Android implementation is finished

Android acts as the **reference implementation** for the project.

---

## iOS

The iOS application shares:

- Business logic through Kotlin Multiplatform
- UI through Compose Multiplatform

Whenever possible, the exact same code should be reused.

During active Android feature development, some iOS-specific issues or visual inconsistencies may temporarily remain unresolved.

Development priority is:

1. Finish Android implementation
2. Keep iOS compiling whenever possible
3. Fix critical cross-platform issues
4. Complete iOS stabilization after Android MVP

A dedicated iOS polish phase will include:

- fixing platform-specific bugs
- improving performance
- refining animations
- adapting interactions to iOS expectations
- implementing missing native integrations

This approach maximizes development speed while preserving a shared codebase.

---

# Client

## Language

- Kotlin 2.x

---

## UI

- Compose Multiplatform
- Shared UI across Android and iOS
- Android-first implementation
- Navigation 3 (Nav3)
- Custom Churros Design System

---

# Architecture

Clean Architecture with an MVI-inspired presentation layer.

```
Presentation
│
├── Screens
├── Components
├── ViewModels
├── UI State
├── UI Events
└── UI Effects
        │
        ▼
Use Cases
        │
        ▼
Repositories
        │
        ▼
Remote / Local Data Sources
```

---

## Presentation Pattern

Each screen consists of:

- Immutable UI State
- UI Events
- UI Effects
- ViewModel
- Reducer-like state updates

Flow:

```
User Action
      │
      ▼
UI Event
      │
      ▼
ViewModel
      │
      ├── Update UI State
      ├── Execute Use Case
      └── Emit UI Effect
```

---

### UI State

Contains:

- Screen content
- Loading state
- Error state
- Dialog visibility
- Selection state

---

### UI Events

Examples:

- OnTaskClicked
- OnTaskCompleted
- OnRefresh
- OnCreateTask
- OnDeleteConfirmed

---

### UI Effects

One-time events.

Examples:

- Navigate
- Show Snackbar
- Show Toast
- Open Dialog
- Share Intent

---

# Dependency Injection

## Koin

Used for:

- ViewModels
- Repositories
- Use Cases
- Services
- Configuration
- Platform modules

---

# Networking

## Ktor Client

Responsibilities:

- HTTP requests
- Authentication
- Logging
- Serialization
- Request interceptors
- Error mapping

---

# Backend

## Supabase

Services used:

- PostgreSQL
- Authentication
- Realtime
- Storage
- Edge Functions

Supabase is the **single source of truth**.

---

# Authentication

Supabase Auth

Providers:

- Google
- Apple
- Email / Password

Future:

- Magic Links

---

# Database

## Remote

PostgreSQL (Supabase)

---

## Local

Room (Kotlin Multiplatform)

Purpose:

- Temporary caching
- Faster screen rendering
- Offline resilience
- State restoration

Application strategy:

**Online First**

The server always remains the source of truth.

---

# Realtime

Supabase Realtime

Used for:

- Chores
- Shopping lists
- Notes
- Calendar events
- Family member updates

Purpose:

Synchronize all connected devices instantly.

---

# Push Notifications

Firebase Cloud Messaging (FCM)

Examples:

- Task assigned
- Shopping item added
- Reminder
- Family invitation
- Upcoming event

Flow:

```
User Action
      │
      ▼
Supabase
      │
      ▼
Edge Function
      │
      ▼
Firebase Cloud Messaging
      │
      ▼
Recipient Devices
```

---

# Serialization

kotlinx.serialization

Used for:

- DTOs
- API models
- Database models
- Preferences

---

# Images

Coil

Used for:

- Family avatars
- Attachments
- Image caching

---

# Navigation

Navigation 3 (Nav3)

Responsibilities:

- Typed destinations
- Bottom navigation
- Nested graphs
- Deep links (future)

---

# Design System

Custom Churros Design System

Not based on Material Design.

Inspired by:

- Scandinavian interiors
- Sticky notes
- Family fridge
- Warm household atmosphere

---

# Logging

Kermit

Used for:

- Debug logging
- Network logging
- Feature diagnostics
- Error reporting

---

# Analytics

Firebase Analytics

Track:

- Screen views
- Feature adoption
- User engagement
- Funnels

---

# Crash Reporting

Firebase Crashlytics

Track:

- Crashes
- ANRs
- Non-fatal exceptions

---

# Project Structure

```
shared/
│
├── core/
│   ├── common/
│   ├── design/
│   ├── navigation/
│   ├── database/
│   ├── network/
│   ├── auth/
│   ├── logger/
│   ├── util/
│   └── resources/
│
├── domain/
│
├── data/
│
├── feature/
│   ├── home/
│   ├── chores/
│   ├── shopping/
│   ├── notes/
│   ├── calendar/
│   ├── family/
│   └── settings/
│
└── shared/

androidApp/

iosApp/
```

---

# Feature Module Structure

```
feature/
└── chores/
    ├── data/
    ├── domain/
    ├── presentation/
    │   ├── components/
    │   ├── screen/
    │   ├── model/
    │   ├── events/
    │   ├── effects/
    │   ├── state/
    │   └── viewmodel/
    └── di/
```

Every feature follows the same structure.

---

# Core Principles

- Android-first development
- Kotlin Multiplatform for shared business logic
- Compose Multiplatform for shared UI
- Android is the reference implementation
- Shared code whenever practical
- Platform-specific implementations only when necessary
- Feature-based modularization
- Clean Architecture
- MVI-inspired presentation layer
- Online-first architecture
- Supabase as the single source of truth
- Realtime synchronization
- Strong type safety
- High testability
- Scalable feature modules
- Minimal unnecessary dependencies

---

# Technology Summary

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| Multiplatform | Kotlin Multiplatform |
| UI | Compose Multiplatform |
| Architecture | Clean Architecture + MVI-inspired MVVM |
| Dependency Injection | Koin |
| Networking | Ktor Client |
| Backend | Supabase |
| Remote Database | PostgreSQL |
| Local Database | Room (KMP) |
| Authentication | Supabase Auth |
| Realtime | Supabase Realtime |
| Storage | Supabase Storage |
| Server Logic | Supabase Edge Functions |
| Serialization | kotlinx.serialization |
| Navigation | Navigation 3 |
| Image Loading | Coil |
| Logging | Kermit |
| Analytics | Firebase Analytics |
| Crash Reporting | Firebase Crashlytics |
| Push Notifications | Firebase Cloud Messaging |
| Design System | Custom Churros Design System |

---

# Development Workflow

For every new feature:

1. Design UI
2. Implement shared models
3. Implement domain layer
4. Implement repositories
5. Implement Android UI
6. Verify shared code compatibility
7. Test on Android
8. Fix critical cross-platform issues if needed
9. Continue with the next feature

After Android MVP:

1. Dedicated iOS stabilization
2. UI polish
3. Native integrations
4. Performance optimization
5. Platform-specific improvements

---

# Vision

Build **Churros** as a warm, modern household companion powered by a shared Kotlin Multiplatform codebase.

Android drives product development and serves as the reference implementation.

Compose Multiplatform enables sharing both UI and business logic with iOS, while still allowing platform-specific implementations where necessary.

The primary goal is to deliver an exceptional Android experience first, then use the shared architecture to efficiently produce a polished, native-feeling iOS application with minimal duplicated code.