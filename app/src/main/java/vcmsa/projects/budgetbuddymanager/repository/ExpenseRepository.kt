package vcmsa.projects.budgetbuddymanager.repository

import vcmsa.projects.budgetbuddymanager.data.dao.ExpenseDao
import vcmsa.projects.budgetbuddymanager.data.entities.Expense

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun getExpensesByDateRange(userId: Long, startMillis: Long, endMillis: Long) =
        expenseDao.getExpensesByDateRange(userId, startMillis, endMillis)

    suspend fun getTotalByCategory(userId: Long, categoryId: Long, startMillis: Long, endMillis: Long) =
        expenseDao.getTotalByCategory(userId, categoryId, startMillis, endMillis) ?: 0.0
}
