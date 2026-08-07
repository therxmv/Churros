# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

# Project Overview

**Churros** is an Android-first household management app for families, couples, and roommates. Tagline: "Like chores, but sweeter."

**Current Phase:** Navigation graph wired (Phase 3 complete). Design system + 13 components implemented. All feature data/domain layers implemented. Presentation layers (screens/ViewModels) are next — feature components exist for Chores, Family, Notifications.

**Key Documentation:**
- `docs/churros_tech_stack.md` — Full architecture, technology decisions, development workflow
- `docs/features.md` — Complete feature catalogue derived from design mockups (screens, flows, UI elements)
- `docs/Design/` — Screen mockups: Auth, Chores, Family, Home, Onboarding, Profile

Read these docs when working on product decisions, UI implementation, or architecture.

---

# Architecture Quick Reference

**Stack:** Kotlin Multiplatform + Compose Multiplatform + Supabase + Firebase

**Pattern:** Clean Architecture with MVI-inspired presentation
- UI State (immutable) + UI Events (user actions) + UI Effects (one-time events)
- Flow: User Action → Event → ViewModel → (Update State / Execute Use Case / Emit Effect)

**Platform Priority:** Android first (reference implementation), then iOS stabilization

**Feature Structure:**
```
feature/[name]/
├── data/          (repositories, DTOs)
├── domain/        (use cases, models)
├── presentation/  (screens, components, viewmodels, state/events/effects)
└── di/            (Koin modules)
```

**Data Strategy:** Online-first. Supabase is source of truth. Room for local caching.

---

# Design System Essentials

**Brand Colors:**
- Primary: Honey `#F5A623`
- Dark: Espresso `#4E342E`
- Background: `#FFF9F1` (warm cream, never pure white/gray)

**Typography:** Manrope (supports Ukrainian)

**Spacing:** 8dp system (4, 8, 16, 24, 32, 48)

**Principles:** Warm, spacious, soft (rounded corners everywhere), minimal, never corporate.

See `shared/src/commonMain/kotlin/com/therxmv/churros/core/design/` for complete specs (`Color.kt`, `Theme.kt`, `Type.kt`, `Shape.kt`, `Spacing.kt`).

---

# Code Exploration (codebase-memory-mcp)

The repo is indexed via [codebase-memory-mcp](https://github.com/DeusData/codebase-memory-mcp). Always use these tools **first** for any code exploration — they are faster and more precise than grep/glob.

| Tool | When to use |
|---|---|
| `search_graph` | Find functions, classes, routes by name or natural language |
| `get_code_snippet` | Read exact source for a known qualified name |
| `trace_path` | Follow call chains (callers/callees, data flow, cross-service) |
| `get_architecture` | Project structure, clusters, hotspots, layers |
| `search_code` | Graph-augmented grep for text patterns |
| `query_graph` | Complex Cypher queries for multi-hop patterns |

Re-index after large changes: `mcp__codebase-memory-mcp__index_repository` with `repo_path` set to the project root and `name: Churros`.

---

# Development Commands

```bash
# Android debug APK
./gradlew :androidApp:assembleDebug

# Shared module tests
./gradlew :shared:testAndroidHostTest
./gradlew :shared:iosSimulatorArm64Test

# Screenshot tests (golden image regression)
./gradlew :androidApp:updateDebugScreenshotTest   # regenerate / update goldens
./gradlew :androidApp:validateDebugScreenshotTest # CI check — fails on visual diff
```

> **Screenshot tests are golden truth for the UI.** Every `@ChurrosPreview` composable has a committed golden PNG under `androidApp/src/screenshotTestDebug/reference/`. If you intentionally change UI, run `updateDebugScreenshotTest` to update the goldens and commit them alongside the code change. Never delete goldens without regenerating them.
>
> **Note:** When adding a new `ChurrosXxx` component, also register it in `androidApp/src/screenshotTest/ChurrosScreenshots.kt` with a `@PreviewTest` entry and run `updateDebugScreenshotTest` to commit the golden.

iOS: open `iosApp/iosApp.xcodeproj` in Xcode and run from there.

---

# Ticket Creation

All ticket creation — single bugs, features, bulk milestone tickets, any type — goes through the BA specialist agent.

## When the user asks to create any ticket(s)

1. Read `.claude/ba_agent_template.md`
2. Replace `{{REQUEST}}` with the user's exact request
3. Spawn a general-purpose foreground agent with that prompt

## When the user wants to implement a feature

1. `gh issue view <number>` to get ticket content
2. Read `.claude/developer_agent_template.md`
3. Replace `{{TICKET_CONTENT}}` with the issue output
4. Spawn a general-purpose foreground agent with that prompt

After the developer agent returns its summary, post it as a comment: `gh issue comment <number> --body "..."`. Close the issue when merged.

## Trigger Detection

| User intent | Action |
|---|---|
| "create a ticket for X" / "create tickets for phase X" / any ticket creation | BA agent via `ba_agent_template.md` |
| "implement issue #NNN" / "implement X" | Developer agent via `developer_agent_template.md` |
| "create ticket and implement X" | BA agent first → then Developer agent |
