# Project Modernization & Migration Plan (XML to Jetpack Compose)

This plan outlines the full refactoring of the `tmdb-explorer` project from a hybrid Fragment/XML architecture to a modern, 100% Jetpack Compose architecture using the latest Android libraries and best practices.

## User Review Required

> [!IMPORTANT]
> This is a destructive migration for the UI layer. Once completed, all XML layouts, ViewBinding, and Fragments will be removed.
>
> Key decisions:
> 1. **Coil over Glide**: Migration to Coil for image loading (Compose-first).
> 2. **Kotlin Serialization**: Replacing Moshi for better integration with Type-Safe Navigation.
> 3. **Single Activity**: The app will move towards a true Single Activity architecture with a Compose `NavHost`.
> 4. **KSP**: Moving from `kapt` to `ksp` for faster builds.

## Proposed Changes

### Phase 0: Plan Persistence [DONE]
#### Task:
1.  **[DONE] [Plan.md](file:///G:/ComposeProjects/TMDB2/Plan.md)**: Save the full approved implementation plan to the project root for easy reference.

---

### Phase 1: Infrastructure & Foundation [DONE]
Set up the tooling and libraries required for the modern stack.

#### Tasks:
1.  **[DONE] [libs.versions.toml](file:///G:/ComposeProjects/TMDB2/gradle/libs.versions.toml)**: Add KSP, Kotlin Serialization, Coil, and Compose Navigation versions/libraries.
2.  **[DONE] Root Build Configuration**: Add KSP and Serialization plugins to the project-level `build.gradle.kts`.
3.  **[DONE] Module Build Configuration**: Apply plugins and update dependencies in `:app`, `:base`, `:shared`, and feature modules.
4.  **[DONE] Edge-to-Edge Support**: Call `enableEdgeToEdge()` in `MainActivity` and prepare for inset handling.

---

### Phase 2: Core Layer Migration (`:base` & `:shared`)
Refactor the foundational layers to support the new architecture.

#### Tasks:
1.  **[DONE] Network Layer Migration**: Replace Moshi with Kotlin Serialization in `:base network classes (e.g., `ResponseAdapter`, `BaseResponse`).
2.  **[DONE] Material 3 Theme**: Implement a centralized Compose Theme (Color, Type, Shape) in `:base`.
3.  **[DONE] Base ViewModels**: Refactor `BaseViewModel` and `BaseOpsViewModel` to support Compose-native state collection and lifecycle awareness.
4.  **[DONE] Shared Models**: Annotate all models in `:shared` and features with `@Serializable`.
5.  **[DONE] Common UI Components**: Migrate shared views (Gallery, SearchBar, Adapters) to reusable Composables in `:shared`.

---

### Phase 2.1: Network Layer Stability [DONE]
Fix the Retrofit `CallAdapter` crash by properly configuring Moshi for Kotlin.

#### Tasks:
1.  **[DONE] [libs.versions.toml](file:///G:/ComposeProjects/TMDB2/gradle/libs.versions.toml)**: Add `moshi-kotlin` and `moshi-kotlin-codegen`.
2.  **[DONE] [:base build.gradle.kts](file:///G:/ComposeProjects/TMDB2/base/build.gradle.kts)**: Add Moshi Kotlin dependencies.
3.  **[DONE] [AppModule.kt](file:///G:/ComposeProjects/TMDB2/base/src/main/java/com/saiful/base/di/AppModule.kt)**: Configure Moshi with `KotlinJsonAdapterFactory`.
4.  **[DONE] Network Models**: Add `@JsonClass(generateAdapter = true)` to `GenericError` and `MoviesResponse`.

---

### Phase 3: Feature Migration (`:movie`, `:tvshows`, `:person`)
Iteratively migrate each feature module to Compose.

#### Tasks:
1.  **[DONE] Movie Module Migration**:
    *   Implement `MovieDashboardScreen`, `MovieDetailsScreen`, and `MovieListScreen` in Compose.
    *   Update `DashboardVM` to expose `StateFlow`.
    *   [TODO in Phase 4] Remove `MovieDashboardFragment` and related XMLs.
2.  **[DONE] TV Shows Module Migration**:
    *   Implement `TVShowsScreen`, `TVShowDetailsScreen` in Compose.
    *   Remove legacy Fragments and XMLs.
3.  **Person Module Migration**:
    *   Implement `PersonDashboardScreen`, `PersonDetailsScreen` in Compose.
    *   Remove legacy Fragments and XMLs.

---

### Phase 4: Integration & Cleanup (`:app`)
Finalize the app structure and remove legacy code.

#### Tasks:
1.  **Type-Safe Navigation**: Define the app's route structure using Kotlin Serialization objects.
2.  **Main Navigation Host**: Implement `NavHost` in `MainActivity` to orchestrate screen transitions.
3.  **Bottom Navigation**: Create a Compose-based `BottomNavigationBar`.
4.  **Final Cleanup**: Remove all unused XML files, Fragments, ViewBinding, and legacy dependencies (Glide, Moshi, SafeArgs).

---

## Verification Plan

### Automated Tests
- Run existing unit tests (refactored for serialization changes).
- Create new Compose UI tests for critical flows (Dashboard, Search).
- `gradlew test` to ensure no regressions in business logic.

### Manual Verification
- Verify Edge-to-Edge rendering on status and navigation bars.
- Test navigation flows and backstack handling.
- Verify image loading and caching with Coil.
- Test dark/light mode switching.
