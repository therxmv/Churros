# Churros — Project Roadmap

---

## Phase 1 — Foundation

**Goal:** Working app with core screens using local state only. No backend, no real auth.

### Scope

- KMP project setup and build configuration
- Churros Design System implementation (tokens, typography, components)
- Auth screens (UI only — local/mock state)
- Home screen
- Chores screen — create, complete, delete (local state)
- Shopping List screen — add, check off, delete items (local state)
- Notes screen — create, edit, delete sticky notes (local state)
- Navigation between all screens

### Out of Scope

- Any backend or network calls
- Real authentication
- Multi-user / family features
- Data persistence across app restarts

---

## Phase 2 — Backend & Family

**Goal:** Real data, real users, collaborative household management.

### Scope

- Supabase integration (auth, PostgreSQL, Realtime, Storage)
- Real authentication (email/password, social login)
- Household creation and management
- Family member invites and roles
- Chore assignments to household members
- Real-time sync for shopping lists and chores
- Local caching with Room (offline support)

### Out of Scope

- Calendar and reminders
- Push notifications

---

## Phase 3 — Advanced Features

**Goal:** Full-featured household app with scheduling and notifications.

### Scope

- Calendar screen — household events, birthdays, bills
- Reminders and due dates for chores
- Recurring tasks (daily, weekly, custom schedules)
- Push notifications via Firebase Cloud Messaging
- Firebase Analytics and Crashlytics integration
- Home screen widgets (Android)

---

## Status

| Phase | Status |
|-------|--------|
| Phase 1 — Foundation | In Progress |
| Phase 2 — Backend & Family | Not Started |
| Phase 3 — Advanced Features | Not Started |
