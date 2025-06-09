package vcmsa.projects.budgetbuddymanager

import android.app.Application
import vcmsa.projects.budgetbuddymanager.data.BudgetDatabase
import vcmsa.projects.budgetbuddymanager.repository.*

class BudgetBuddyApp : Application() {
    val database by lazy { BudgetDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val expenseRepository by lazy { ExpenseRepository(database.expenseDao()) }
    val budgetGoalRepository by lazy { BudgetGoalRepository(database.budgetGoalDao()) }
}
