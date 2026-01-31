# Second Brain Android App

A native Kotlin Android app for browsing and searching your Second Brain knowledge base.

## Features

- 📱 Native Android experience with Material 3 design
- 🔍 Full-text search across all documents
- 📁 Browse by folder (Concepts, Journals, Projects)
- 📝 Markdown rendering with syntax highlighting
- 🏷️ Tag-based organization
- 🌙 Dark mode support
- 💉 Dependency injection with Hilt
- 🌐 Retrofit for API communication

## Tech Stack

- **Language:** Kotlin 1.9
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM with Repository pattern
- **DI:** Hilt (Dagger)
- **Networking:** Retrofit + OkHttp
- **Markdown:** Markwon

## Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Kotlin 1.9+

### Running the app

1. Open the project in Android Studio
2. Start the Second Brain Rails server:
   ```bash
   cd ../second-brain
   bin/rails server -b 0.0.0.0
   ```
3. Run the app on an emulator or device

### API Configuration

The app connects to `https://siathinks.ngrok.app/api/v1/` (persistent ngrok tunnel to the Second Brain server).

## Project Structure

```
app/src/main/java/com/sweepsatlas/secondbrain/
├── data/
│   ├── api/           # Retrofit API interface
│   ├── model/         # Data classes
│   └── repository/    # Repository pattern
├── di/                # Hilt modules
├── ui/
│   ├── components/    # Reusable Compose components
│   ├── screens/       # Full-screen composables
│   ├── theme/         # Material theme
│   └── viewmodel/     # ViewModels
├── MainActivity.kt
└── SecondBrainApp.kt
```

## API Endpoints

The Rails backend provides these endpoints:

| Endpoint | Description |
|----------|-------------|
| `GET /api/v1/documents` | List all documents |
| `GET /api/v1/documents/folders` | Documents grouped by folder |
| `GET /api/v1/documents/:folder/:slug` | Get single document with content |
| `GET /api/v1/documents/search?q=query` | Search documents |

## Building

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing config)
./gradlew assembleRelease
```

## License

Private - Sweeps Atlas
