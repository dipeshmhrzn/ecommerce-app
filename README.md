# E-commerce App

A sleek and modern **E-commerce application** built using **Kotlin** and **Jetpack Compose** for Android. The app now includes secure authentication, product browsing, search, sorting, filtering, wishlist, cart management, and user profile features. It integrates **Firebase**, **Firestore**, **Room**, **DataStore**, and **Supabase** to deliver a complete shopping experience.

## Features

### Authentication
- **User Registration** using Firebase Authentication.
- **User Login** with proper error handling.
- **Google Sign-In** support.
- **Password Reset** via email.
- **Logout** functionality.
- **Authentication State Management** to persist login sessions.
- **Form Validation** for both login and registration.


### Product Features
- **Product Fetching** from a remote API.
- **Search** products.
- **Sort** products (price).
- **Filter** products by price range and rating.
- **Product Details** screen.

### Wishlist
- Add/remove products from wishlist.
- Wishlist stored locally using **Room Database**.

### Cart
- Add/remove items from cart.
- Cart stored using **DataStore** for persistence.

### Profile
- User data stored in **Firestore** after registration.
- Select profile image from gallery.
- Upload profile picture to **Supabase Storage**.
- Profile image URL stored in Firestore.
- Profile screen displays user info and photo.


## Tech Stack

- **Programming Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Authentication**: Firebase Authentication
- **Database**: Firestore, Room, DataStore
- **Storage**: Supabase
- **API**: Ktor (Product fetching)

## App Screenshots

![Screenshot 1](./images/screenshot1.png)

![Screenshot 2](./images/screenshot2.png)

![Screenshot 3](./images/screenshot3.png)


## Future Improvements

- Payment Gateway Integration
- Order History & Tracking
- Push Notifications

---

## Setup Steps

1. Clone this repository:

   ```bash
   git clone https://github.com/dipeshmhrzn/ecommerce-app.git

2. Open the project in **Android Studio**.
3. Add your Firebase configuration:
   - Download your **google-services.json** file from the Firebase Console.
   - Place it in the **app/** directory of your project.
4. Enable Google Sign-In in Firebase:
   - Firebase Console → Authentication → Sign-in methods → Google.
5. Add your Supabase configuration (URL + ANON key) in the project.
6. Sync the project with Gradle.
7. Build and run the app on an emulator or Android device.
