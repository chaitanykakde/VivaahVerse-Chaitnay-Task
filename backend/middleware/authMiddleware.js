const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'super_secret_key_change_me';

exports.verifyToken = (req, res, next) => {
  // Expect header: "Authorization: Bearer <token>"
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    // For testing purposes, if no token is provided but a 'userid' header exists, 
    // we might allow it in a 'loose' mode, but let's stick to JWT for the requirement.
    // However, the prompt mentioned "or a mocked auth middleware that accepts a userId in headers".
    // Let's support that as a fallback for easier testing if token is missing.
    const mockUserId = req.headers['userid'];
    if (mockUserId) {
        req.user = { uid: mockUserId };
        return next();
    }
    return res.status(401).json({ error: 'Access denied. No token provided.' });
  }

  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    req.user = decoded;
    next();
  } catch (error) {
    res.status(400).json({ error: 'Invalid token.' });
  }
};

