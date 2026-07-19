# Design Inconsistencies Audit

Findings across all mockup screens: Auth, Chores, Family, Home, Onboarding, Profile/Settings.
Each entry includes the observed inconsistency and the agreed expected result.

---

## 1. FAB (Floating Action Button) — Shape

| Screen | Shape |
|--------|-------|
| Home (light + dark) | Perfect circle |
| Chores list | Rounded rectangle (squircle) |

**Expected result:** All FABs use a **rounded rectangle (squircle)** shape consistently.

---

## 2. Primary Button — Arrow Suffix

| Button | Has Arrow |
|--------|-----------|
| Sign In | No |
| Create Account | No |
| Reset Password | No |
| Verify Code | No |
| **Update Password** | **Yes ("Update Password →")** |
| Add to Family | No |
| Save Changes | No |

**Expected result:** Trailing `→` is used **only on "Next" / forward-navigation buttons** (e.g. `Next →` in onboarding). All other action buttons have no arrow.

---

## 3. Save Action — Placement and Style

| Screen | Save Pattern |
|--------|-------------|
| Settings / Profile | Small rectangular `SAVE` button in the top-right of the header bar |
| Permissions | Full-width orange `Save Changes` button at the bottom of the screen |

**Expected result:** Save action is always a **small button in the top-right header**. Full-width bottom buttons are reserved for primary conversion actions (e.g. Create Account, Add to Family).

---

## 4. Back Navigation — Text vs Icon

| Screen | Back Navigation |
|--------|----------------|
| Forgot Password | "← Back" — icon + word |
| Verify Email | "← Back" — icon + word |
| All other sub-screens | Arrow icon only |

**Expected result:** All back buttons are **icon only** (arrow, no label text).

---

## 5. Auth Screen Header Structure

Each auth screen has a different layout for the top navigation area:

| Screen | Top Left | Top Center | Top Right |
|--------|----------|------------|-----------|
| Sign In | *(nothing)* | *(nothing)* | *(nothing)* |
| Create Account | *(nothing)* | *(nothing)* | *(nothing)* |
| Forgot Password | "← Back" | *(nothing)* | "Skip" |
| Verify Email | "← Back" | "Churros" (centered title) | *(nothing)* |
| Set New Password | Back icon | *(nothing)* | *(nothing)* |

**Expected result:** All auth sub-screens use **back icon only** (no center title, no "Skip"). The "Churros" center title on Verify Email and the "Skip" on Forgot Password should be removed.

---

## 6. Onboarding Progress Dots

| Screen | Dots |
|--------|------|
| Slide 1 | 3 dots, active appears larger |
| Slide 2 | 3 dots, slight size variation |
| Slide 3 | No dots |

**Expected result:** **All slides including the last** show progress dots. Active dot is consistently larger/filled orange; inactive dots are smaller gray. Dot size must be the same across Slide 1 and Slide 2.

---

## 7. Onboarding — Bottom Navigation Element

| Screen | Bottom CTA |
|--------|-----------|
| Slide 1 | *(no bottom CTA)* |
| Slide 2 | `Step →` |
| Slide 3 | `Create Account` + `Sign In` |

**Expected result:** Slides 1 and 2 both have a bottom `Next →` button. Slide 3 shows `Create Account` (primary) + `Sign In` (secondary). The label "Step" is replaced with **"Next"**.

---

## 8. Onboarding — Skip Button

| Screen | Skip |
|--------|------|
| Slide 1 | Top-right text link |
| Slide 2 | Missing |
| Slide 3 | N/A (final screen) |

**Expected result:** `Skip` is visible on **all slides except the last**. Same placement: top-right text link.

---

## 9. Chore Completion Indicator (Checkbox)

| Screen | Checked State |
|--------|--------------|
| Home (light + dark) | Orange filled circle with white checkmark |
| Chores list | Not shown in mockup |

**Expected result:** Checked state is always an **orange filled circle with a white checkmark**. Unchecked is an empty circle outline.

---

## 10. Chore Category Tabs vs Other Segmented Controls

| Location | Active Tab Style |
|----------|-----------------|
| Chores list header (Today / Tomorrow / Done) | Underline / text highlight |
| Create Chore – Category (Kitchen / Garden / Cleaning) | Orange filled background pill |

**Expected result:** All segmented tab selectors use the **filled pill (orange background)** style.

---

## 11. Family Member Role Badge Colors

| Role | Badge Color |
|------|------------|
| Primary Parent | Salmon / pink-red |
| Parent | Green |
| Kid | Blue-green / teal |

**Expected result:** Colors are derived from the **design system palette** (brand colors defined in `core/design/Color.kt`). The exact mapping should be defined there (e.g. Honey for Parent, a secondary accent for Kid, etc.) rather than using ad-hoc colors.

---

## 12. Avatar Rendering — Fallback

| Context | Current |
|---------|---------|
| All contexts when no photo set | Undefined / inconsistent |

**Expected result:** When no photo is available, display the user's **initials on a colored background**. Color can be derived from the name. Applied consistently in all avatar sizes throughout the app.

---

## 13. Notification Action Buttons (Approve / Decline)

Currently: small inline pair, not aligned to any button size standard.

**Expected result:** The two buttons are placed **side by side and together span the full card width** (each button takes 50% of card width). Styled as filled (Approve) and outlined (Decline), using the standard button component with a compact height variant.

---

## 14. Bottom Navigation — Tab Count

| Screen | Tabs visible |
|--------|-------------|
| Home | 3: Home, Chores, Family |
| Chores | 3: Home, Chores, Family |
| Family | Appears to show 3–4 icons |

**Expected result:** Bottom nav has exactly **3 tabs: Home, Chores, Family**. Profile/Settings is accessed via a gear icon or avatar within a screen, not a nav tab.

---

## 15. Icon Container Shape — Chore Icons vs Avatars

| Element | Current Shape |
|---------|--------------|
| Chore category / type icons | Rounded square (squircle) |
| User avatars (all sizes) | Circle |
| Activity / notification avatars | Circle |

**Expected result:** Shape rule is **circles for people (avatars), squircles for everything else** (chore icons, category icons, feature icons). This is consistent with the existing mockups and is now the explicit standard.

---

## 16. Primary Button — Corner Radius

Some primary buttons across screens are fully rounded (pill shape — radius = 50% of height), while others use a moderate rounded corner (squircle-like radius).

| Screen | Button | Shape |
|--------|--------|-------|
| Auth (Sign In, Create Account, etc.) | Primary CTA | Fully rounded pill |
| Onboarding | Create Account, Next | Fully rounded pill |
| Chores | Save Chore | Appears more squircle |
| Family | Add to Family, Save Changes | Appears more squircle |

**Expected result:** All primary buttons use a **squircle corner radius** (consistent moderate rounding, not a full pill). Fully rounded pill buttons should be updated to match.

---

## 17. Text and Icon Color on Orange Background

Some screens show dark text or icons placed on the primary orange/honey background (e.g. button labels, active tab text, FAB icon), while others use white. No explicit rule is documented.

**Expected result:** Any text, icon, or graphic element placed on an **orange/honey background must use white**. This applies to:
- Primary button labels
- FAB icon
- Active filled-pill tab label
- Any badge or chip with an orange fill

---

## 18. Screen Background Color

Some screens use a pure white (`#FFFFFF`) or neutral gray background instead of the brand warm cream.

**Expected result:** All screen backgrounds use the brand **warm cream `#FFF9F1`** (never pure white or gray). This applies to every screen, bottom sheet, and card surface unless a specific design token overrides it.

---

## Summary Table

| # | Area | Severity | Expected Result |
|---|------|----------|-----------------|
| 1 | FAB shape | High | Rounded rectangle everywhere |
| 2 | Button arrow | Medium | Arrow only on "Next" navigation buttons |
| 3 | Save action placement | High | Top-right header button |
| 4 | Back nav label | Medium | Icon only, no text |
| 5 | Auth header structure | Medium | Back icon only, no center title |
| 6 | Onboarding dots | Medium | Dots on all slides, consistent size |
| 7 | Onboarding "Step →" label | Medium | Rename to "Next →", add to Slide 1 |
| 8 | Skip button missing on Slide 2 | Low | Show Skip on all non-final slides |
| 9 | Checkbox checked state | Medium | Orange filled circle + white checkmark |
| 10 | Tab selector style | High | Filled pill everywhere |
| 11 | Role badge colors | Low | Map to design system palette |
| 12 | Avatar fallback | Low | Initials on colored background |
| 13 | Notification Approve/Decline size | Medium | Side-by-side, full card width |
| 14 | Bottom nav tab count | High | 3 tabs only (Home, Chores, Family) |
| 15 | Icon container shapes | Low | Circles for avatars, squircles for icons |
| 16 | Primary button corner radius | High | Squircle, not full pill |
| 17 | Text/icon color on orange background | High | Always white on orange |
| 18 | Screen background color | High | Warm cream `#FFF9F1`, never white/gray |
