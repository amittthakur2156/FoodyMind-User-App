# 🍔 FoodyMind – Online Food Ordering Application

<p align="center">
  <img src="foodymindSmaal.png" alt="FoodyMind Logo" width="220"/>
</p>

<p align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)
![Firebase](https://img.shields.io/badge/Firebase-Backend-orange?style=for-the-badge&logo=firebase)
![Android](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)

</p>

---

# 📱 About The Project

**FoodyMind** is an Android-based **Online Food Ordering Application** developed using **Kotlin** and **MVVM Architecture**.

The application enables users to browse food items, search meals, add products to cart, place orders, manage profiles, and view order history. All data is managed using **Firebase Authentication**, **Firebase Realtime Database**, and **Firebase Storage**.

The project is designed with a clean architecture and scalable code structure, making it suitable for both **academic projects** and **professional portfolios**.

---

# ✨ Features

## 👤 User Authentication

- Email & Password Login
- User Registration
- Google Sign-In
- Forgot Password

---

## 🍽 Food Ordering

- Browse Food Menu
- Popular Items
- View Food Details
- Search Food
- Add to Cart

---

## 🛒 Cart Management

- Increase Quantity
- Decrease Quantity
- Delete Item
- Smart Add to Cart
- Total Amount Calculation

---

## 📦 Order System

- Checkout
- Place Order
- Order Confirmation
- Order History
- Buy Again Feature

---

## 👤 User Profile

- Update Profile
- Save Information
- Logout

---

# 🏗 Architecture

The project follows **MVVM (Model-View-ViewModel)** Architecture.

```
                View
        (Activity / Fragment)
                    │
                    │
                    ▼
              ViewModel
                    │
                    │
                    ▼
               Repository
                    │
                    │
                    ▼
      Firebase Realtime Database
                    │
                    │
        ┌───────────┴───────────┐
        │                       │
        ▼                       ▼
Firebase Authentication   Firebase Storage
```

---

# 🔥 Firebase Services Used

## ✅ Firebase Authentication

- Email Login
- Password Login
- Google Sign-In

---

## ✅ Firebase Realtime Database

- User Data
- Menu Items
- Cart Data
- Orders
- Order History

---

## ✅ Firebase Storage

- Food Images
- Image Management

---

# 🛠 Tech Stack

| Technology | Used |
|------------|------|
| Language | Kotlin |
| Architecture | MVVM |
| IDE | Android Studio |
| Database | Firebase Realtime Database |
| Authentication | Firebase Authentication |
| Storage | Firebase Storage |
| UI | XML |
| Image Loading | Glide |
| Version Control | Git |
| Repository | GitHub |

---

# 📂 Project Structure

```
com.example.foodymind

│

├── Adapter

├── Fragment

├── Model

├── Repository

├── ViewModel

│

├── LoginActivity

├── MainActivity

├── DetailActivity

├── PayOutActivity

│

└── Firebase Services
```

---

# 📦 Libraries & Dependencies

```gradle
implementation "com.google.firebase:firebase-auth"

implementation "com.google.firebase:firebase-database"

implementation "com.google.firebase:firebase-storage"

implementation "com.github.bumptech.glide:glide"

implementation "com.google.android.gms:play-services-auth"

implementation "androidx.recyclerview:recyclerview"

implementation "androidx.lifecycle:lifecycle-viewmodel-ktx"

implementation "androidx.lifecycle:lifecycle-livedata-ktx"

implementation "androidx.navigation:navigation-fragment-ktx"

implementation "androidx.navigation:navigation-ui-ktx"
```

---

# ⭐ Special Functionalities

- ✅ MVVM Architecture
- ✅ Firebase Integration
- ✅ Google Authentication
- ✅ Dynamic Food Menu
- ✅ Smart Cart System
- ✅ Quantity Management
- ✅ Real-Time Order Management
- ✅ Buy Again Feature
- ✅ Profile Management
---

# 📸 Application Screenshots

## 🚀 Splash Screen

![Splash Screen](screenshot/splash.jpeg)

---

## 👋 Welcome Screen

![Welcome Screen](screenshot/Welcome%20page.jpeg)

---

## 🔐 Login Screen

![Login](screenshot/SignIn.jpeg)

---

## 🔑 Google Sign-In

![Google SignIn](screenshot/google%20signIn.jpeg)

---

## 📝 Sign Up Screen

![SignUp](screenshot/SignUp.jpeg)

---

## 🏠 Home Screen

![Home](screenshot/home.jpeg)

---

## 🍽 Menu Screen

![Menu](screenshot/menu.jpeg)

---

## 🔍 Search Screen

![Search](screenshot/Search.jpeg)

---

## 🛒 Cart Screen

![Cart](screenshot/cart.jpeg)

---

## 💳 Checkout Screen

![Checkout](screenshot/checkout.jpeg)

---

## 🎉 Order Placed

![Order Placed](screenshot/confrats.jpeg)

---

## 📜 Order History

![History](screenshot/history.jpeg)

---

## 👤 Profile Screen

![Profile](screenshot/profile.jpeg)

---

## 🏗 MVVM Architecture

![Architecture](screenshot/Structure.png)

---

# 🚀 Getting Started

## Clone Repository

```bash
git clone https://github.com/amittthakur2156/FoodyMind-User-App.git
```

## Open Project

- Open Android Studio
- Click **Open**
- Select the project folder
- Sync Gradle

---

# 🔥 Firebase Setup

1. Create a Firebase Project
2. Enable Authentication
3. Enable Realtime Database
4. Enable Firebase Storage
5. Download `google-services.json`
6. Place it inside:

```
app/
    google-services.json
```

---

# ▶️ Run the Project

- Connect Android Device or Emulator
- Click **Run ▶**
- Enjoy the App

---

# 📂 Repository

**GitHub Repository**

https://github.com/amittthakur2156/FoodyMind-User-App

---

# 👨‍💻 Developer

## Amit Thakur

Android Developer

- Kotlin
- Firebase
- MVVM Architecture
- Android Studio

---

# 🌟 Future Improvements

- ❤️ Online Payment Gateway
- ❤️ Live Order Tracking
- ❤️ Push Notifications
- ❤️ Dark Mode
- ❤️ Coupons & Offers
- ❤️ Ratings & Reviews
- ❤️ Favorite Foods
- ❤️ AI Food Recommendation

---

# ⭐ Support

If you like this project,

⭐ Star the repository

🍴 Fork the repository

🛠 Contribute to the project

---

# 📜 License

This project is developed for learning, academic, and portfolio purposes.

---

<p align="center">

## 🍔 FoodyMind

### GOOD FOOD, QUICK FOR YOU ❤️

**Made with ❤️ using Kotlin, Firebase & MVVM**

</p>
- ✅ Firebase Storage Images
- ✅ Clean & Scalable Project Structure
