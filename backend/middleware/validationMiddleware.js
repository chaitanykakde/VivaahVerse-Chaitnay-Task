exports.validateExpense = (req, res, next) => {
  const { amount, description, category, date } = req.body;

  if (amount === undefined || typeof amount !== 'number' || amount <= 0) {
    return res.status(400).json({ error: 'Amount must be a positive number.' });
  }

  if (!description || typeof description !== 'string') {
    return res.status(400).json({ error: 'Description is required.' });
  }

  if (!category || typeof category !== 'string') {
    return res.status(400).json({ error: 'Category is required.' });
  }
  
  // Optional: Validate date format
  if (date && isNaN(Date.parse(date))) {
      return res.status(400).json({ error: 'Invalid date format.' });
  }

  next();
};

