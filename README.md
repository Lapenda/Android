# Lab Exercises - Android Calculator

This repository contains my lab exercises for Android development at university.

## Lab 1: Fuel Consumption Calculator

### Description
- An application that calculates fuel consumption based on entered kilometers and liters.
- Developed in Kotlin using Android Studio.

### Features
- Language support: English, German, Croatian.
- Three themes: Light, Dark, Teal.
- Uses View Binding for connecting UI elements.

### How to Run
1. Clone the repository: `git clone https://github.com/Lapenda/Android.git`
2. Open the project in Android Studio.
3. Sync Gradle and run on an emulator or device.

### Project Structure
- `Labos1/`: Contains the Android project files.
  - `app/src/main/java/`: Kotlin source code (MainActivity.kt, NextActivity.kt).
  - `app/src/main/res/`: Resources (layouts, strings, drawables).
  - `app/build.gradle.kts`: App-level Gradle configuration.

### Requirements
- Android Studio (latest version recommended).
- Android SDK.
- Kotlin plugin.

### Notes
- The app calculates consumption per 100 km and displays the result with two decimal places.
- Themes can be changed via a Spinner in the main activity.