# Duka — Retail Operating System for Rwanda

A **Kotlin Multiplatform Mobile (KMM)** retail operating system for small shops in Rwanda. Shared business logic runs on both Android and iOS, with native UI on each platform.

## Architecture

```
DukaApp/
├── shared/           # Shared Kotlin module (business logic, models, ViewModels)
│   ├── commonMain/   # Platform-agnostic code
│   │   ├── domain/         # Data models, repository interfaces, pure functions
│   │   ├── analytics/      # FinancialCalculator (revenue, profit, tax)
│   │   ├── intelligence/   # ProductIntelligence (low stock, slow movers)
│   │   ├── notifications/  # NotificationPreferences
│   │   ├── data/           # Repository interfaces, session manager
│   │   ├── viewmodel/      # KMM-ViewModels (same on both platforms)
│   │   └── sqldelight/     # SQLDelight schema (.sq files)
│   ├── androidMain/  # Android-specific (SQLDelight driver, Koin module)
│   └── iosMain/      # iOS-specific (SQLDelight driver, Koin module)
├── androidApp/       # Android app (Jetpack Compose UI)
│   └── src/main/     # Existing Compose screens, Hilt DI, Room (legacy)
└── iosApp/           # iOS app (SwiftUI)
    └── iosApp/       # SwiftUI screens, design system, platform wrappers
```

### What's Shared

| Layer | Contents |
|-------|----------|
| **Domain Models** | Business, Product, Sale, Employee, ChatMessage, Promo, IssueReport, EbmReceipt, User, TaxProfile, Purchase, BudgetGoal, FeedbackReport, ShopRating, Wholesaler, RestockRequest, StockAdjustment, ProductAlert, AppNotification, Expense |
| **Repository Interfaces** | BusinessRepository, ProductRepository, SaleRepository, UserRepository, EmployeeRepository, ChatRepository, PromoRepository, IssueRepository, EbmRepository, TaxRepository, PurchaseRepository, BudgetGoalRepository, FeedbackReportRepository, ShopRatingRepository, WholesalerRepository, RestockRequestRepository, StockAdjustmentRepository, ProductAlertRepository, AppNotificationRepository, ExpenseRepository |
| **Pure Business Logic** | TaxCalculator, CreditScoreCalculator, ReorderQuantityCalculator, MockDistanceCalculator |
| **Analytics** | FinancialCalculator (revenue, profit, tax tiers) |
| **Intelligence** | ProductIntelligence (low stock, slow movers, restock suggestions, sales velocity) |
| **ViewModels** | SessionViewModel (KMM-ViewModel — same Kotlin class on both platforms) |
| **Persistence** | SQLDelight schema (generates both Android SQLite and iOS SQLite drivers) |
| **Session** | Multiplatform Settings (replaces DataStore) |
| **DI** | Koin modules (shared + platform-specific) |

### Platform-Specific

| Layer | Android | iOS |
|-------|---------|-----|
| **UI** | Jetpack Compose | SwiftUI |
| **Navigation** | Navigation Compose | NavigationStack + TabView |
| **Database Driver** | SQLDelight Android Driver | SQLDelight Native Driver |
| **Session Storage** | Multiplatform Settings (AndroidSettings) | Multiplatform Settings (NSUserDefaultsSettings) |
| **Speech Recognition** | Android SpeechRecognizer | SFSpeechRecognizer |
| **Notifications** | NotificationManagerCompat | UNUserNotificationCenter |
| **Password Hashing** | BCrypt (JVM) | CommonCrypto PBKDF2 |
| **Background Jobs** | WorkManager | BackgroundTasks framework |

## Building

### Android
```bash
./gradlew :app:assembleDebug
```

### iOS (Xcode)
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Build the shared framework: `./gradlew :shared:linkDebugFrameworkIosArm64`
3. Run on iOS Simulator (iOS 16+)

## What's Implemented

### Auth & Multi-User
- **Owner Login** — Phone/email + password with BCrypt hashing
- **Company Signup** — Full owner identity + business setup form
- **Employee Login** — Business employee code (validated against Employee table)
- **Client Signup** — Phone/email + password
- **Session Persistence** — Multiplatform Settings-backed session
- **Logout** — Available in More/Settings for all roles
- **Seed Data** — Demo accounts pre-populated

### V1 — Core Ops
- **Owner Dashboard** — Today's income, monthly stats, top products
- **Add Product** — Name, price, stock, category picker
- **Employee Sell** — Live search, tap-to-sold with stock decrement

### V2 — Smart Layer
- **Voice Add Product** — Speech recognizer with regex parser (Kinyarwanda + English)
- **Tax & EBM Status** — Auto-computed tax tier, annual turnover, EBM receipts
- **Chat** — Two-party owner↔employee messaging
- **Promo** — Create promos with discount %, date range
- **Report an Issue** — Category dropdown, saves locally

### V3 — Marketplace + Insight
- **Client Discover** — Search businesses with filter chips
- **Client Store** — View products, buy with mock payment
- **Client Budget** — Weekly budget vs spend progress bar
- **Client Feedback** — Category dropdown, anonymous toggle
- **Credit Readiness** — Credit score from sales history
- **Trending Shops** — Cross-district ranked business list

### V4 — Supply Network
- **Request Restock** — Low-stock products with reorder suggestions
- **Wholesaler Marketplace** — Search wholesalers by specialty
- **Regional Roadmap** — Expansion plans (Live/Planned/Coming Soon)

## What's Mocked

| Feature | Status | Notes |
|---------|--------|-------|
| **EBM Gateway** | Local mock | Fake receipt numbers. `// TODO: replace with real RRA API` |
| **Client payments** | Placeholder | No real payment integration |
| **Distance/ratings** | Locally computed | Mock distance from business name hash |
| **Wholesaler delivery** | Locally seeded | No real supply chain |
| **Lending partners** | Coming soon | Credit score shows "Coming soon" state |
| **Government metrics** | Locally computed | No real government backend |
| **Password hashing** | Platform-specific | BCrypt (Android), PBKDF2 (iOS) |

## Demo Credentials

| Role | Phone | Password | Code |
|------|-------|----------|------|
| **Owner** | `0788000000` | `demo123` | — |
| **Employee** | — | — | `EMP-1234` |
| **Client** | `0788000001` | `demo123` | — |
| **Government Admin** | `0788000002` | `demo123` | — |

## Tech Stack

### Shared (KMM)
- Kotlin Multiplatform
- SQLDelight (database)
- KMM-ViewModel (shared ViewModels)
- Koin (dependency injection)
- Multiplatform Settings (preferences)
- kotlinx.coroutines + Flow
- kotlinx.serialization

### Android
- Jetpack Compose + Material 3
- Room (existing data layer)
- Hilt (Android-specific DI)
- Navigation Compose
- Android SpeechRecognizer
- BCrypt (password hashing)
- DataStore (session persistence)
- WorkManager (background jobs)

### iOS
- SwiftUI
- NavigationStack + TabView
- SFSpeechRecognizer
- UNUserNotificationCenter
- CommonCrypto (password hashing)
- BackgroundTasks framework

## Extension Points

1. **Backend Sync** — Add `RemoteDataSource` alongside `LocalDataSource`
2. **Real EBM Certification** — Replace `MockEbmGateway` with RRA API
3. **LLM Voice Parser** — Replace `RegexVoiceEntryParser` with LLM-based implementation
4. **Real Payments** — Integrate mobile money APIs
5. **Lending Partners** — Connect credit scores to real lending APIs
6. **Multi-Device Sync** — Firebase or CRDT-based sync layer
