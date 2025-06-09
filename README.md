# BudgetBuddyManager

> **A full-featured Android app for tracking expenses, setting budgets, and building healthy financial habits, built with Kotlin, MVVM, Firebase Firestore, and MPAndroidChart.**

---

## 📱 Overview

BudgetBuddyManager is a personal budgeting app that helps users manage expenses, set flexible budget goals, view spending analytics, and unlock rewards for healthy financial behavior. All your data is securely synced in the cloud with Firebase Authentication and Firestore.


---

## 🚀 Features

* **Register and login** with email & password (Firebase Auth)
* **Real-time cloud sync** using Firebase Firestore (user data is 100% private)
* **Add expenses** with:

  * Amount, description, category, date/time range, and optional photo/receipt
* **Create custom categories**
* **Set monthly budget goals** (min and max limits)
* **View all expenses** and filter by date range
* **See totals by category** in each period
* **Spending analytics** with interactive charts (MPAndroidChart)
* **Progress dashboard** with pie/donut charts and progress bars
* **Badges and rewards** for staying on track (unique feature)
* **Dark mode toggle** for comfortable viewing (unique feature)
* **Professional UI**, smooth navigation, and cloud-first design

---

## 🛠️ Setup Instructions

### 1. **Firebase Project Setup**

* [Create a new Firebase project](https://console.firebase.google.com)
* Enable **Authentication** → "Email/Password" sign-in
* Enable **Firestore Database** (production mode recommended)
* Download your `google-services.json` file and place it in `/app` directory

### 2. **Firestore Security Rules**

Place this into Firestore "Rules" in the Firebase Console:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /categories/{doc} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
    match /expenses/{doc} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
    match /budgetGoals/{doc} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
  }
}
```

### 3. **Dependencies**

* **Firebase:**

  * `firebase-auth-ktx`
  * `firebase-firestore-ktx`
* **Charts:**

  * `com.github.PhilJay:MPAndroidChart:v3.1.0`
* **AndroidX, ViewBinding, ConstraintLayout, etc.**

Add to your `build.gradle.kts`:

```kotlin
implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
```

### 4. **Clone, Sync, and Run**

* Open in Android Studio
* Sync Gradle
* Build & run on an emulator or device

---

## 🧭 Screens & Navigation

* **Login/Register Screen**
* **Category Management**
* **Add Expense**
* **Expense List & Filtering**
* **Budget Goal Setting**
* **Charts/Analytics (Pie/Bar Chart)**
* **Progress Dashboard (with donut chart and badges)**
* **Settings (dark mode toggle)**

---

## 📊 Graph & Dashboard Usage

* **Pie Chart**: Visualize spending per category for any date range. Access via "Show Graph" button.
* **Dashboard**: See monthly progress towards your budget goal, overspending warnings, and unlock badges for saving.

---

## ✨ Unique Features

### 1. **Badges/Rewards System**

Earn badges for:

* Staying within your max budget
* Saving more than your min goal
* Not spending at all during a month

### 2. **Dark Mode Toggle**

* Instantly switch between light and dark themes from the dashboard.

---

## 🧪 Testing & CI

### **Unit & UI Testing**

* Sample unit test: `ExampleUnitTest.kt`
* Sample UI test: `ExampleInstrumentedTest.kt`
* Run with:

  ```shell
  ./gradlew test
  ./gradlew connectedAndroidTest
  ```

### **GitHub Actions**

* App builds, tests, and uploads artifacts automatically via `.github/workflows/build.yml`

---

## 📁 Assets

* **App icon:** In `/mipmap-*`
* **Receipt icon:** In `/drawable`

---

## 🙌 Credits

* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
* [Firebase for Android](https://firebase.google.com/docs/android/setup)
* Designed and developed by **\[ST10084154/Preshen Naidoo]**.

---

## 📄 License

This project is released under the MIT License.

---

**BudgetBuddyManager helps you spend smarter, stay on track, and earn rewards for healthy money habits.**

---
