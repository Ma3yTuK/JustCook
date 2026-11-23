# JustCook 🍳

**JustCook** is a modern, modular Android application for discovering, searching, creating, and sharing culinary recipes. Built entirely with **Kotlin** and **Jetpack Compose**, it follows Modern Android Development (MAD) practices and clean architecture principles.

---

## 🌟 Key Features

- **📖 Recipe Catalogue & Feed**: Explore featured recipe collections, trending dishes, and top culinary creators.
- **🔍 Advanced Search & Filtering**:
  - Full-text search with local suggestion history backed by Room.
  - Filter recipes by categories, lifestyle diets (vegan, keto, etc.), ingredients, calorie count, preparation time, and user ratings.
  - Separate search results for recipes and creators/users.
- **🍲 Interactive Recipe Detail**:
  - Dynamic ingredient scaling based on serving size or target portion weight.
  - Automatic measurement unit conversions.
  - Step-by-step cooking instructions.
  - User ratings and interactive review submission with bottom sheet support.
- **⭐ Favorites & Bookmarks**: Save favorite recipes locally for quick access.
- **👤 User Profile & Moderation**:
  - Authentication support (Backend JWT Bearer token & Google Sign-In integration).
  - User profile management and personal recipe collections.
  - Recipe moderation workflow for content approval.
- **🎨 Fluid UI & Shared Element Transitions**: Uses Jetpack Compose Material 3 with expressive spring physics animations and `SharedTransitionLayout`.

---

## 🏗️ Architecture & Module Structure

The project is structured into modular Gradle sub-projects promoting separation of concerns and build scalability:

```
JustCook/
├── app/                  # Application module (Main Activity, top-level Navigation & DI Providers)
├── components/           # Reusable UI component library & Material 3 JustCookTheme
├── data/                 # Data layer (Data Models, Room DB, Retrofit API Services, Auth Repositories)
├── feature/
│   ├── catalogue/        # Recipe Feed, Recipe Details, User Profiles & Collections
│   ├── favorite/         # Favorite / Saved Recipes screen
│   ├── profile/          # User Profile, Sign-In, Recipe Creation & Moderation
│   └── search/           # Search screen, Filters, and Suggestions
└── utils/                # Utility functions and common helpers
```

---

## 🛠️ Tech Stack & Libraries

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3 (`androidx.compose.material3`)
- **Navigation**: [Jetpack Compose Navigation](https://developer.android.com/guide/navigation/navigation-with-compose)
- **Local Storage**: [Room Database](https://developer.android.com/training/data-storage/room) for search suggestions & local caching
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) + [OkHttp 3](https://square.github.io/okhttp/) with custom Authorization Header Interceptor
- **JSON Parsing**: [Gson](https://github.com/google/gson)
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- **Asynchrony**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Shared Transitions**: Compose `SharedTransitionLayout` with custom spring animation specs

---

## 📋 Requirements

- **Android Studio**: Ladybug (2024.2.1) or newer recommended
- **JDK**: Version 11 or 17
- **Min SDK**: API level 34 (Android 14)
- **Target SDK**: API level 35 (Android 15)

---

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/JustCook.git
   cd JustCook
   ```

2. **Configure Backend URL**:
   Ensure the API backend endpoint is configured in `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="backend_url">https://your-backend-api.com/</string>
   ```

3. **Build & Run**:
   Open the project in Android Studio, select the `app` run configuration, and deploy to an emulator or physical device running Android 14+ (API 34+). Alternatively, build via Gradle CLI:
   ```bash
   ./gradlew assembleDebug
   ```
