# BudgetBuddyManager

**Track your expenses. Stay on budget. Visualize your success.**  
BudgetBuddyManager is a personal finance tracker built with Kotlin for Android. It helps users log expenses, set budget goals, and visualize spending — all backed by Firebase for real-time data access.

---

## 🚀 Features

### 🔐 Authentication
- Secure login & registration with Firebase Authentication
- User-specific data isolation via Firebase UID

### 🧾 Expense Management
- Add expenses with:
  - Amount, description, category
  - Start & end date/time
  - Optional photo (camera or gallery)
- Create and manage custom categories

### 💸 Budget Tracking
- Set monthly **minimum and maximum** budget limits
- Receive alerts when you're nearing limits

### 📅 Smart Filtering
- View expenses filtered by a selected date range
- See summaries by category within that period

### 📈 Visual Reports
- **Bar and pie charts** showing spending by category
- Budget min/max goals overlaid for comparison (via MPAndroidChart)

### 📊 Progress Dashboard
- See visual progress on monthly budgeting
- Track spending with color-coded indicators
- Stay aware of category-wise over/under spending

### ✨ Original Add-on Features
1. **Savings Goal Tracker** – Set monthly savings goals and track actual savings vs. target.
2. **Dark Mode Toggle** – Switch between light and dark themes with persistent settings.

---

## 🛠️ Tech Stack

- **Kotlin** with Android SDK
- **RoomDB** (Part 2) → **Firebase Firestore** (Part 3)
- **Firebase Authentication** and **Cloud Firestore**
- **MPAndroidChart** for visualizations
- **Jetpack ViewModel**, **LiveData**, **ViewBinding**
- **Glide** or **Coil** for image handling
- **GitHub Actions** for CI/CD automation

---

## 🧪 Testing & CI/CD

### ✅ Unit Tests
- Budget calculations and category summary logic

### ✅ UI Tests
- Login flow and expense submission validation

### ✅ GitHub Actions
CI Workflow: `.github/workflows/build.yml`
```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build with Gradle
      run: ./gradlew build
    - name: Run Unit Tests
      run: ./gradlew testDebugUnitTest
