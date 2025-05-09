🏞️ Pexels
=============

🎯 **An Android application for searching image from Pexels**

🎥 Demo (Running on Emulator due to lack of real devices)
--------------
<img src="assets/pexels_demo.gif" height="400" />

📁 Project Structure
--------------------

```
📦 pexels/
├── 📂 app/                 # Main application module
├── 📂 core/                # Core utilities and shared logic
│      └── 📂 common/       # Common module shared between modules
│      └── 📂 data/         # Data module which provides repositories, paging sources
│      └── 📂 database/     # Database module
│      └── 📂 domain/       # Domain module which provides use cases
│      └── 📂 model/        # Model module
│      └── 📂 network/      # Network module
│      └── 📂 testing/      # Testing module
├── 📂 features/            # Features module
│      └── 📂 recent/       # Recently viewed photos list feature
│      └── 📂 search/       # Image searching feature
└── 📂 gradle/              # Gradle wrapper and build scripts

```

🔧 Prerequisites
--------------

- 🧑‍💻 **Android Studio**: [Meerkat](https://developer.android.com/studio)
- 🛠️ **Android Gradle Plugin**: 8.8.2
- ⚙️ **Gradle**: 8.11.1
- 🔑 **Pexels API key**: You will have to create a Pexels account to get an API key.

✨ Key Features
--------------

- 🔍 **Image Search & Detail View** — Browse and explore high-quality images with a smooth detail
  screen and hero transitions.Allowing to pinch-to-zoom as well.
- 🕘 **Recently Viewed Photos** — Automatically tracks and displays the photos you've recently
  viewed.
- ⚡ **Real-time Search** — Get instant search results as you type, optimized for smooth UX.
- 🧠 **Smart Caching** — Efficient memory usage with LRU-like caching for recently viewed photos.
- 🗂️ **Modular Architecture** — Clean separation between feature and data modules using Navigation
  Component.

🏗️ How to Build
--------------

1. Clone the repository:
   ```bash
   git clone https://github.com/ptv1811/pexels.git
   ```

2. Create a `secrets.properties` file and fill in these options:
    ```
    PEXELS_API_KEY="<Your Pexels API key>"
    BACKEND_URL="https://api.pexels.com/v1/"
    ```

3. Sync the build project as usual.

📦APK File
--------------

In case you can't build the project, here is
the [Link](https://drive.google.com/drive/folders/1pJDnJhmilbcbeNv4MVovF74m14VzFk7q?usp=drive_link)
to the APK file.

🛠️ Tech Stack
--------------

- 🧑‍💻 **Language**: ![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-blue?logo=kotlin&logoColor=white)

- 🏗️ **Architecture**: Clean architecture - Single Activity - MVVM Design pattern - Multi-moduled

- 📦 **Libraries**:

    - **Navigation**: [Navigation Component](https://developer.android.com/guide/navigation)

    - **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)

    - **Networking**: [Retrofit](https://square.github.io/retrofit/)

    - **Database**: [Room](https://developer.android.com/jetpack/androidx/releases/room)

    - **Asynchronous Programming
      **: [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

    - **Reactive Streams**: [Flow](https://kotlinlang.org/docs/flow.html)

    - **Pagingination
      **: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

    - **Unit testing
      **: [JUnit](https://junit.org/junit4/), [Mockito](https://site.mockito.org/), [Turbine](https://code.cash.app/flow-testing-with-turbine),
      etc

🧠 Decision Making
-------------
This section outlines the rationale behind key architectural and UX decisions made during the
development of this application.

- 🏗️ **Architecture**:

  **Clean Architecture + MVVM** \
  Ensures clear separation of concerns across layers: UI, Domain, and Data. Each layer has a
  well-defined responsibility, improving testability and maintainability.

  **Modularization** \
  Even though the app requirements are simple, the project is modularized to support scalability.
  This allows different features to be developed in isolation without conflicts, making the codebase
  future-proof and team-friendly.

  **Single Activity with Navigation Component** \
  The app uses a single-activity architecture, with each feature represented by a Fragment. This
  centralizes navigation and simplifies lifecycle handling across screens.

- 🎨 **UI/UX Decision**:

  **Curated Photos as Initial State** \
  To avoid an empty state when the app launches, a curated list of trending photos is displayed
  before the user performs any search.

  **Real-Time Image Search** \
  As the user types, search results update in real time, creating an engaging and dynamic
  experience. Input is debounced to prevent excessive network requests.

  **Shared Element Transition** \
  A hero animation is implemented when navigating from the search results to the detail screen,
  enhancing the visual polish and continuity of the user experience.

  **Recently Viewed Photos Instead of Search History** \
  Due to real-time search, it's difficult to pinpoint when a user has finalized a search query.
  Instead of saving raw queries—which may include incomplete or accidental ones—the app tracks
  recently viewed photos. This provides more meaningful history and improves usability without
  cluttering the UI.

- 📦 **Third-Party Libraries**

  **PhotoView for Pinch-to-Zoom** \
  Rather than reinventing the wheel, I used
  the [PhotoView](https://github.com/GetStream/photoview-android) library to quickly and reliably
  add pinch-to-zoom functionality for the photo details screen. This was chosen due to time
  constraints and the library’s stability, saving development time while maintaining quality.

🚀 Future Improvements
---------------
Some planned or possible enhancements:

**Support Build variants** \
Due to time constraint, I don't have enough time to support build variance but it will be a good
improvements in the future.

**Implement Pinch-to-zoom from scratch** \
Replace the PhotoView library with a fully custom gesture-based zooming mechanism to gain more
control over animations, behavior, and integration with the app's UI/UX flow.

**User Favorites** \
Allow users to mark and store favorite photos for quicker access in the future.

**Support for Dark Theme**  
Provide full support for dark mode following Material guidelines.
