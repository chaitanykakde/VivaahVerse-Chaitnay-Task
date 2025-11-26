# Expense Tracker Solution

## Structure
- `backend/`: Node.js + Express API (Firestore)
- `android_app/`: Jetpack Compose Android App (MVVM, Hilt, Retrofit)

## Backend Setup
1. Navigate to `backend/`.
2. Run `npm install`.
3. **Firebase Setup**:
   - Go to Firebase Console -> Project Settings -> Service Accounts.
   - Generate a new private key.
   - Save the file as `serviceAccountKey.json` inside `backend/`.
4. Run `npm start` (or `npm run dev`).
5. The API will run on `http://localhost:3000`.

### Render Deployment
1. Push this repo to GitHub.
2. Create a new Web Service on Render.
3. Root Directory: `backend`.
4. Build Command: `npm install`.
5. Start Command: `node index.js`.
6. **Environment Variables**:
   - `FIREBASE_SERVICE_ACCOUNT`: Paste the content of your `serviceAccountKey.json` here (minified JSON).
   - `JWT_SECRET`: Set a secure secret.

## Android App Setup
1. Open `android_app/` in Android Studio.
2. Sync Gradle.
3. **Config**:
   - Open `di/AppModule.kt`.
   - Update `BASE_URL` to your backend URL.
     - Emulator: `http://10.0.2.2:3000/`
     - Physical Device: `http://<YOUR_PC_IP>:3000/`
     - Render: `https://your-app.onrender.com/`
4. Run the app.

## Features
- Cyberpunk/Hacker Theme (Black & Neon Green).
- JWT Auth (Signup/Login).
- Expense Dashboard with Donut Chart.
- Add/Delete Expenses.
- Category Filter.

# VivaahVerse-Chaitnay-Task
