const { db } = require('../firebaseConfig');

const ROOT_NODE = 'ExpenseTrackerApp'; // Root table for the entire app

exports.addExpense = async (req, res) => {
  try {
    const { amount, description, date, category, type } = req.body;
    const userId = req.user.uid; // From auth middleware

    // Expenses stored under: ExpenseTrackerApp -> expenses
    const expensesRef = db.ref(ROOT_NODE).child('expenses');
    const newExpenseRef = expensesRef.push();
    
    const newExpense = {
      id: newExpenseRef.key,
      userId,
      amount,
      description,
      date: date || new Date().toISOString(),
      category,
      type: type || 'Expense', // Default to Expense if not provided
      createdAt: new Date().toISOString()
    };

    await newExpenseRef.set(newExpense);
    
    res.status(201).json(newExpense);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.getExpenses = async (req, res) => {
  try {
    const userId = req.user.uid;
    const { category, date } = req.query;

    const expensesRef = db.ref(ROOT_NODE).child('expenses');
    const snapshot = await expensesRef.orderByChild('userId').equalTo(userId).once('value');

    let expenses = [];
    if (snapshot.exists()) {
      const data = snapshot.val();
      expenses = Object.values(data);
    }

    // Client-side filtering
    if (category) {
      expenses = expenses.filter(exp => exp.category === category);
    }
    
    if (date) {
      expenses = expenses.filter(exp => exp.date === date);
    }

    res.json(expenses);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.getExpenseById = async (req, res) => {
    try {
        const { id } = req.params;
        const userId = req.user.uid;

        const expenseRef = db.ref(ROOT_NODE).child('expenses').child(id);
        const snapshot = await expenseRef.once('value');

        if (!snapshot.exists()) {
            return res.status(404).json({ error: 'Expense not found' });
        }

        const expenseData = snapshot.val();

        if (expenseData.userId !== userId) {
            return res.status(403).json({ error: 'Unauthorized to access this expense' });
        }

        res.json(expenseData);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.updateExpense = async (req, res) => {
  try {
    const { id } = req.params;
    const updateData = req.body;
    const userId = req.user.uid;

    const expenseRef = db.ref(ROOT_NODE).child('expenses').child(id);
    const snapshot = await expenseRef.once('value');

    if (!snapshot.exists()) {
      return res.status(404).json({ error: 'Expense not found' });
    }

    const expenseData = snapshot.val();

    if (expenseData.userId !== userId) {
      return res.status(403).json({ error: 'Unauthorized to edit this expense' });
    }

    await expenseRef.update(updateData);
    res.json({ id, ...expenseData, ...updateData });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.deleteExpense = async (req, res) => {
  try {
    const { id } = req.params;
    const userId = req.user.uid;

    const expenseRef = db.ref(ROOT_NODE).child('expenses').child(id);
    const snapshot = await expenseRef.once('value');

    if (!snapshot.exists()) {
      return res.status(404).json({ error: 'Expense not found' });
    }

    const expenseData = snapshot.val();

    if (expenseData.userId !== userId) {
      return res.status(403).json({ error: 'Unauthorized to delete this expense' });
    }

    await expenseRef.remove();
    res.json({ message: 'Expense deleted successfully' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
