package vcmsa.projects.budgetbuddymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vcmsa.projects.budgetbuddymanager.data.entities.Expense

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Query("""
        SELECT * FROM expenses
        WHERE userOwnerId = :userId
          AND startDateTime BETWEEN :startMillis AND :endMillis
        ORDER BY startDateTime DESC
    """)
    suspend fun getExpensesByDateRange(userId: Long, startMillis: Long, endMillis: Long): List<Expense>

    @Query("""
        SELECT SUM(amount) FROM expenses
        WHERE userOwnerId = :userId AND categoryId = :categoryId AND startDateTime BETWEEN :startMillis AND :endMillis
    """)
    suspend fun getTotalByCategory(userId: Long, categoryId: Long, startMillis: Long, endMillis: Long): Double?
}
