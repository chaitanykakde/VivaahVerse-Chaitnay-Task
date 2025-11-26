const jwt = require('jsonwebtoken');
const { db } = require('../firebaseConfig');

const JWT_SECRET = process.env.JWT_SECRET || 'super_secret_key_change_me';
const ROOT_NODE = 'ExpenseTrackerApp'; // Root table for the entire app

exports.signup = async (req, res) => {
  try {
    const { name, email, password } = req.body;
    
    if (!email || !password) {
      return res.status(400).json({ error: 'Email and password required' });
    }

    // Check if user exists: Root -> users -> query by email
    const usersRef = db.ref(ROOT_NODE).child('users');
    const snapshot = await usersRef.orderByChild('email').equalTo(email).once('value');

    if (snapshot.exists()) {
      return res.status(400).json({ error: 'User already exists' });
    }

    const newUserRef = usersRef.push();
    const newUser = {
      id: newUserRef.key,
      name: name || 'User', // Store name
      email,
      password, // WARNING: Storing plain text password for demo only. Use bcrypt!
      createdAt: new Date().toISOString()
    };
    
    await newUserRef.set(newUser);

    const token = jwt.sign({ uid: newUser.id, email }, JWT_SECRET, { expiresIn: '1h' });
    res.status(201).json({ token, userId: newUser.id, name: newUser.name });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.login = async (req, res) => {
  try {
    const { email, password } = req.body;
    
    const usersRef = db.ref(ROOT_NODE).child('users');
    const snapshot = await usersRef.orderByChild('email').equalTo(email).once('value');

    if (!snapshot.exists()) {
      return res.status(400).json({ error: 'Invalid credentials' });
    }

    // RTDB returns an object of keys, we need to find the one matching password
    const users = snapshot.val();
    const userId = Object.keys(users)[0];
    const userData = users[userId];

    if (userData.password !== password) {
      return res.status(400).json({ error: 'Invalid credentials' });
    }

    const token = jwt.sign({ uid: userData.id, email: userData.email }, JWT_SECRET, { expiresIn: '1h' });
    res.json({ token, userId: userData.id, name: userData.name });

  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
