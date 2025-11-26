const admin = require('firebase-admin');
require('dotenv').config();

// Instructions for Service Account:
// 1. Go to Firebase Console > Project Settings > Service Accounts.
// 2. Generate new private key -> downloads 'serviceAccountKey.json'.
// 3. For local dev: Save it in the 'backend' folder as 'serviceAccountKey.json'.
// 4. For Render: You can construct the credential object from env vars.

let serviceAccount;

try {
  if (process.env.FIREBASE_SERVICE_ACCOUNT) {
     // If passing the entire JSON as a string env var (useful for Render)
     serviceAccount = JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT);
  } else {
     // Fallback to local file
     serviceAccount = require('./serviceAccountKey.json');
  }
} catch (error) {
  console.error("Service Account not found. Please ensure serviceAccountKey.json exists or FIREBASE_SERVICE_ACCOUNT env var is set.");
  serviceAccount = {}; 
}

if (Object.keys(serviceAccount).length > 0) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://live-in-project-dc7bd-default-rtdb.firebaseio.com"
  });
  console.log("Firebase Admin Initialized");
} else {
  console.log("Firebase Admin NOT Initialized (Missing Credentials)");
}

const db = admin.database();

module.exports = { db, admin };
