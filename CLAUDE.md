# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

# Project Overview

**Churros** is an Android-first household management app for families, couples, and roommates. Tagline: "Like chores, but sweeter."

**Current Phase:** Design system tokens implemented (`core/design/`). All features are clean scaffolds awaiting implementation.

**Key Documentation:**
- `docs/churros_tech_stack.md` — Full architecture, technology decisions, development workflow
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
- Primary: Honey `#E9B44C`
- Dark: Espresso `#4E342E`
- Background: `#FFF9F1` (warm cream, never pure white/gray)

**Typography:** Manrope (supports Ukrainian)

**Spacing:** 8dp system (4, 8, 16, 24, 32, 48)

**Principles:** Warm, spacious, soft (rounded corners everywhere), minimal, never corporate.

See `shared/src/commonMain/kotlin/com/therxmv/churros/core/design/` for complete specs (`Color.kt`, `Theme.kt`, `Type.kt`, `Shape.kt`, `Spacing.kt`).

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
> **Note:** `androidApp/src/screenshotTest/ChurrosScreenshots.kt` currently references components that are not yet implemented. Before running screenshot tests, ensure all referenced composables exist or update the file to match what is implemented.

iOS: open `iosApp/iosApp.xcodeproj` in Xcode and run from there.

---

# GitHub Issues

All project tickets are GitHub Issues at `therxmv/Churros`.

## Creating an Issue (BA Automation)

When the user asks you to create a ticket:

1. Infer `type`, `epic`, `priority` from the description and context in `docs/churros_tech_stack.md`.
2. Draft an issue body using the template below.
3. Create the issue, assigning it to the appropriate milestone:
   ```bash
   gh issue create --title "Title" --body "..." --label "type:feature,priority:medium,epic:chores" --milestone "Phase 1 — Foundation"
   ```
4. Confirm the issue number and URL.

**Always apply at least a `type:` and `priority:` label.**

## Issue Body Template

```
## Description

## Acceptance Criteria

- [ ]

## Design Notes

## Technical Notes

## Dependencies

Blocked by: none
Related: none
```

## Labels

| Category | Values |
|----------|--------|
| `type:` | `type:feature`, `type:screen`, `type:infra`, `type:design`, `type:bug`, `type:research`, `type:doc`, `type:chore` |
| `priority:` | `priority:critical`, `priority:high`, `priority:medium`, `priority:low` |
| `epic:` | `epic:auth`, `epic:chores`, `epic:shopping`, `epic:notes`, `epic:calendar`, `epic:family`, `epic:settings`, `epic:infra` |

## Type Reference

| Type | Use for |
|------|---------|
| `type:feature` | New user-facing capability |
| `type:screen` | Full screen or flow design/implementation |
| `type:infra` | Project setup, CI/CD, tooling |
| `type:design` | Design tokens, components, visual specs |
| `type:bug` | Defects (once code exists) |
| `type:research` | Spikes, investigations, decisions |
| `type:doc` | Documentation updates |
| `type:chore` | Refactor, cleanup, dependency updates |

## Useful Commands

```bash
gh issue list                                    # view open issues
gh issue view <number>                           # view issue details
gh issue create --title "..." --body "..." --label "..."
gh issue edit <number> --add-label "..." --remove-label "..."
gh issue close <number>                          # mark done
gh issue comment <number> --body "..."           # add comment
```

---

# Development Workflow

## BA → Developer Pipeline

When the user wants to implement a feature (any phrasing with implementation intent):

### Step 1 — BA creates issue
Create the GitHub issue per the GitHub Issues section. Note the issue number.

### Step 2 — Spawn developer agent
Read `.claude/developer_agent_template.md`. Replace `{{TICKET_CONTENT}}` with the output of `gh issue view <number>`. Use the result as the Agent tool prompt (general-purpose, foreground).

### Step 3 — Developer agent runs
The sub-agent asks clarifying questions directly to the user via AskUserQuestion, then implements. Returns an implementation summary.

### Step 4 — Wrap up
Post the implementation summary as a comment on the issue (`gh issue comment <number> --body "..."`). Close the issue when merged.

## Trigger Detection

| User intent | Action |
|-------------|--------|
| "create a ticket for X" | BA only — create issue, no implementation |
| "create ticket and implement X" / "I want a feature that X" | Full pipeline |
| "implement issue #NNN" | Developer only — `gh issue view NNN`, skip BA step |
