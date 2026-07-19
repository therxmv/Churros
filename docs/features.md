# Churros — Feature Catalogue

Derived from design mockups in `docs/Design/`.

---

## Onboarding

3 screens. First-launch carousel introducing the app's value props (shared chores, task tracking, family space). Ends with Create Account / Sign In CTAs.

---

## Auth

5 screens.

- **Sign In** — email/password login with Google and Apple SSO options.
- **Sign Up** — account creation with name, email, password, and ToS agreement.
- **Forgot Password** — request a password reset link via email.
- **Verify Email** — enter a 6-digit OTP sent to the user's email.
- **Set New Password** — set a new password with strength requirements (length, number, special character).

---

## Home

1 screen. Dashboard showing a personalised family greeting, personal chore progress, family-wide goal progress, today's upcoming chores, and a recent activity feed of all household actions.

---

## Chores

2 screens.

- **Chores List** — all household chores filterable by assignee and date (Today / Tomorrow / Done). Each chore shows category, assignee, due time, and reward points.
- **Create Chore** — form to create a chore with title, category, assignee, due date/time, repeat schedule, priority, and reward points.

---

## Family

5 screens.

- **Family Dashboard** — household completion progress, motivational summary, and a list of all members with their individual progress.
- **Manage Family** — overview of the household with quick access to Household Profile and Permissions, plus a full member list.
- **Add New Member** — invite someone by email and assign them a role (Parent / Kid / Caregiver).
- **Permissions** — role-based permission toggles for kids: whether chores require parent approval, whether kids can self-assign chores, and whether kids can claim rewards directly.
- **Household Profile** — edit the family name, home address, and family photo.

---

## Settings & Notifications

2 screens.

- **Settings** — edit profile (name, avatar), view current family and role, toggle dark mode, change language, manage notification preferences, sign out.
- **Notifications** — feed of household activity grouped into Recent and Earlier. Reward requests are actionable (Approve / Decline); other types (chore assignment, completion, shopping list edits, daily goal) are informational.
