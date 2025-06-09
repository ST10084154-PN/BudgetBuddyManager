package vcmsa.projects.budgetbuddymanager.repository

import vcmsa.projects.budgetbuddymanager.data.dao.BudgetGoalDao
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal

class BudgetGoalRepository(private val goalDao: BudgetGoalDao) {
    suspend fun upsertBudgetGoal(goal: BudgetGoal) = goalDao.upsertBudgetGoal(goal)
    suspend fun getGoalForMonth(userId: Long, month: Int, year: Int) = goalDao.getGoalForMonth(userId, month, year)
}
