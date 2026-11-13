
# Online Kabin Registration

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)
[![Database](https://img.shields.io/badge/Database-Room%20%2F%20SQLite-blue.svg)](https://developer.android.com/training/data-storage/room)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-brightgreen.svg)](https://developer.android.com/about/versions/nougat)


A simple, offline Android application for online Kabin (marriage) registration.
Users can create an account, log in, submit a Kabin registration form, and track
the status of their application — all stored in a local Room (SQLite) database.
No Firebase, server, or internet connection is required at runtime.

---

## Table of Contents

- [Features](#features)
- [How It Works](#how-it-works)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing Checklist](#testing-checklist)
- [Security](#security)
- [Limitations & Future Improvements](#limitations--future-improvements)
- [Contributing](#contributing)


---

## Features

- **User registration** with name, email, phone, and password
- **Secure login** using the same email and password
- **One email, one profile** — duplicate registration with the same email is blocked
- **Home screen** with the current application status and quick navigation
- **My Profile** — shows the user's details and Kabin application status
  (`Pending` or `Accepted`)
- **Kabin Registration form** — groom, bride, NID numbers, marriage date,
  denmohor, address, and two witnesses
- **One application per user** — a second application from the same account
  is not allowed
- **Persistent login session** — the app remembers the logged-in user
- **Fully offline** — data is stored locally with Room (SQLite)

## How It Works

```text
Register → Login → Home → Kabin Registration Form → Submit → My Profile
                                      │                         │
                                      │                         ├─ Pending
                                      │                         └─ Accepted
                                      └─ Saved in Room database
```

### 1. Registration

1. The user opens the app and goes to the registration screen.
2. They enter their full name, email, phone number, and password
   (minimum 6 characters).
3. The app validates the input: required fields, a valid email format,
   matching password confirmation, and a minimum password length.
4. It checks whether the email is already registered. If it is, registration
   is rejected with the message
   *"This email is already registered."*
5. Otherwise, the password is hashed with SHA-256 and the user is saved in
   the `users` table.

> **Note:** Registration does **not** log the user in automatically.
> After successful registration, the user must log in separately.

### 2. Login

1. The user enters their registered email and password.
2. The app looks up the user by email (`UserDao.getByEmail()`).
3. It hashes the entered password and compares it with the stored hash.
4. On a match, the user's ID is saved in `SharedPreferences`
   (`SessionManager`) and the Home screen opens.
5. On a mismatch, the app shows *"Wrong email or password."*

If a user is already logged in, the app opens the Home screen directly.

### 3. Kabin Registration

1. From Home, the user opens the **Kabin Registration** form.
2. They fill in all required fields:
   groom name, bride name, groom NID, bride NID, marriage date,
   denmohor, address, witness 1, and witness 2.
3. On submit, the app first checks whether the user has already applied.
4. If not, the application is saved in the `kabin_applications` table with
   `status = "PENDING"`.
5. The user is taken to **My Profile**, where the application and its
   status are displayed.

### 4. Application Status

| Status | Meaning |
| ------ | ------- |
| `PENDING` | The application has been submitted and is awaiting verification. |
| `ACCEPTED` | The application has been verified and accepted. |
| — | No application has been submitted yet. |

For demonstration purposes, the Profile screen includes a
*"Demo: Accept as Admin"* button. Tapping it changes a `PENDING`
application to `ACCEPTED`. In a real system, this action would be
performed by an administrator, not by the applicant.

## Tech Stack

| Component | Technology / Version |
| --------- | -------------------- |
| Language | Java 17 |
| Platform | Android (Gradle Plugin 8.2.2) |
| UI | XML Layouts, Material Components 1.11.0 |
| Database | Room 2.6.1 (SQLite) |
| Session | SharedPreferences |
| Password hashing | SHA-256 |
| Compile SDK | 34 |
| Target SDK | 34 |
| Min SDK | 24 (Android 7.0) |

## Project Structure

```text
KabinRegistrationJava/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/kabinregistration/
│       │   ├── AppDatabase.java        # Room database, DAOs, background executor
│       │   ├── User.java               # users table (entity)
│       │   ├── KabinApplication.java   # kabin_applications table (entity)
│       │   ├── LoginActivity.java      # Login screen logic
│       │   ├── RegisterActivity.java   # Registration screen logic
│       │   ├── HomeActivity.java       # Home screen logic
│       │   ├── ProfileActivity.java    # My Profile + application status
│       │   ├── KabinFormActivity.java  # Kabin Registration form logic
│       │   ├── SessionManager.java     # Login session (SharedPreferences)
│       │   └── PasswordUtil.java       # SHA-256 password hashing
│       └── res/
│           ├── layout/                 # activity_login, activity_register,
│           │                           # activity_home, activity_profile,
│           │                           # activity_kabin_form
│           ├── values/                 # colors.xml, strings.xml, themes.xml
│           ├── drawable/               # App icon foreground
│           └── mipmap-anydpi-v26/      # Adaptive launcher icon
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## Database Schema

The database is named `kabin_registration.db` and is created locally by Room.

### `users`

| Column | Type | Constraint |
| ------ | ---- | ---------- |
| `id` | INTEGER | Primary key, auto-increment |
| `fullName` | TEXT | Not null |
| `email` | TEXT | **UNIQUE** — one email, one profile |
| `passwordHash` | TEXT | SHA-256 hash, never the plain password |
| `phone` | TEXT | Optional |
| `createdAt` | INTEGER | Registration timestamp (milliseconds) |

### `kabin_applications`

| Column | Type | Constraint |
| ------ | ---- | ---------- |
| `id` | INTEGER | Primary key, auto-increment |
| `userId` | INTEGER | **UNIQUE**, foreign key → `users(id)`, `CASCADE` on delete |
| `groomName` | TEXT | Not null |
| `brideName` | TEXT | Not null |
| `groomNid` | TEXT | Not null |
| `brideNid` | TEXT | Not null |
| `marriageDate` | TEXT | Not null |
| `denmohor` | TEXT | Not null |
| `address` | TEXT | Not null |
| `witness1` | TEXT | Not null |
| `witness2` | TEXT | Not null |
| `status` | TEXT | `"PENDING"` or `"ACCEPTED"` (default: `"PENDING"`) |
| `submittedAt` | INTEGER | Submission timestamp (milliseconds) |

All database operations run on a background thread
(`AppDatabase.databaseWriteExecutor`) and update the UI with
`runOnUiThread()`, so the main thread is never blocked.

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version)
- JDK 17
- Android SDK with API 34 installed
- An Android emulator (API 24+) or a physical Android device

### Installation

1. **Clone or download this project.**

   ```bash
   git clone <repository-url>
   ```

   Or download the ZIP, then extract it.

2. **Open the project in Android Studio.**
   - Select **File → Open**
   - Choose the extracted `KabinRegistrationJava` folder

3. **Wait for Gradle Sync to finish.**
   The first sync downloads the required libraries, so an internet
   connection is needed for this step only.

4. **Run the app.**
   - Select an emulator or a connected device
   - Click **Run ▶** (or press `Shift + F10`)

## Usage

1. **Register** — create an account with your name, email, and password
   (at least 6 characters).
2. **Log in** — use the same email and password.
3. **Open Kabin Registration** from the Home screen.
4. **Fill in the form** and tap **Submit**.
5. **Open My Profile** to see your details and the application status
   (`Pending`).
6. **Demo only:** tap *"Demo: Accept as Admin"* on the Profile screen
   to see the `Accepted` state.

## Testing Checklist

- [ ] Register with a new email — should succeed and return to Login
- [ ] Register again with the same email — should be rejected
- [ ] Register with a password shorter than 6 characters — should be rejected
- [ ] Register with mismatched passwords — should be rejected
- [ ] Log in with the correct email and password — should open Home
- [ ] Log in with the wrong password — should show an error
- [ ] Close and reopen the app while logged in — should open Home directly
- [ ] Submit the Kabin form with an empty field — should be rejected
- [ ] Submit a complete Kabin form — should save and show `Pending` in Profile
- [ ] Try to submit a second application — should be blocked
- [ ] Tap the demo Accept button — status should change to `Accepted`
- [ ] Log out — should return to the Login screen

## Security

- Passwords are **never stored in plain text** — only their SHA-256 hash
  is saved in the database.
- Email uniqueness is enforced at the database level (`UNIQUE` index),
  not only in the UI.
- The login session stores only the user's ID in `SharedPreferences` —
  no password is kept there.
- Database access is performed off the main thread to keep the UI responsive.

## Limitations & Future Improvements

**Current limitations:**

- The database is local only — data does not sync between devices.
- Password hashing uses SHA-256 without a salt.
- There is no password reset or "forgot password" feature.
- Application approval is a demo button; there is no separate admin panel.

**Possible improvements:**

- Add an admin panel for reviewing and approving applications
- Add password reset and profile editing
- Migrate to a cloud backend (e.g. Firebase) for multi-device sync
- Add salted password hashing (e.g. BCrypt)
- Add form validation for NID format and date picking

## Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m "Add your feature"`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request



---

#
*Built with Java and Room — a simple, offline Kabin registration app for Android.*
