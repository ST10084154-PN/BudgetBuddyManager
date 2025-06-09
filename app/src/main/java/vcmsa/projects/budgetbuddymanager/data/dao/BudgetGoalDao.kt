package vcmsa.projects.budgetbuddymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal

@Dao
interface BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBudgetGoal(goal: BudgetGoal): Long

    @Query("""
        SELECT * FROM budget_goals
        WHERE userOwnerId = :userId AND month = :month AND year = :year
        LIMIT 1
    """)
    suspend fun getGoalForMonth(userId: Long, month: Int, year: Int): BudgetGoal?
}
