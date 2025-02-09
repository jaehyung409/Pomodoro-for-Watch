### 🎯🎯🎯 Pomodoro Timer for Wear OS 🎯🎯🎯

## 📌 Overview 📌
This is a **standalone Pomodoro Timer app** designed specifically for **Wear OS** smartwatches. The app follows the Pomodoro Technique, alternating between **Focus Mode** and **Break Mode (Short & Long)** to enhance productivity by structuring work and rest periods effectively.

## 🔧 Requirements 🔧
- **Minimum API Level:** 34 (Android 14.0 - UpsideDownCake)
- **Compatibility:** Wear OS smartwatches

## 🚀 Key Features 🚀
### 🎨 Focus & Break Modes 🎨
- Displays a **Pomodoro timer** with distinct visual cues:
  - **Red for Focus Mode**
  - **Green for Break Mode**
- In **foreground mode**, the timer dynamically shrinks in a Pomodoro format while running.
- In **background mode**, only alarms are maintained to optimize performance.
- Provides **haptic feedback** at key moments:
  - **Short vibration** at the halfway point.
  - **Double vibration** when the timer ends.

### 🔄 Cycle Management 🔄
- Implements a **4-cycle system:**
  - **Short Break** occurs after the **1st, 2nd, and 3rd Focus sessions**.
  - **Long Break** follows the **4th Focus session**.
- Allows users to **adjust durations** for **Focus, Short Break, and Long Break** (from **1 to 59 minutes**).
- Supports **auto-start mode**, enabling seamless transitions between Focus and Break periods without manual input.

## ⚙️ In-Progress Features ⚙️
- **Code Comments & Logic Explanation:** Enhancing documentation for improved readability and maintainability.
- **Database Integration:** Storing and tracking total focus time per day.
- **Customizable Cycle Count:** Allowing users to adjust the Long Break interval.
- **UI Enhancements:** Refining the user interface for a smoother experience.

## ⚠️ Usage Notes ⚠️
- This app is developed for **personal use**. Due to limited experience in **Wear OS development**, optimizations are not guaranteed.
- To **improve usability**, it is recommended to **increase screen timeout duration** in watch settings.
- Installation requires **wireless debugging** via **Android Studio**, as the app is still under development.
- Future updates will be announced, but there is no fixed release schedule.

## 📥 Installation 📥
As the app is **not available on Google Play**, installation requires manual setup:
1. **Enable Developer Mode** on your Wear OS device.
2. **Enable ADB over Wi-Fi** and connect your watch to Android Studio.
3. **Manually install the APK** via ADB commands or through Android Studio.

Stay tuned for further updates!

## 📜 License 📜
This project is licensed under the **Creative Commons Attribution-NonCommercial (CC BY-NC) License**. This allows for free use, modification, and distribution for **non-commercial purposes only**. For more details, refer to the official [CC BY-NC License documentation](https://creativecommons.org/licenses/by-nc/4.0/).

