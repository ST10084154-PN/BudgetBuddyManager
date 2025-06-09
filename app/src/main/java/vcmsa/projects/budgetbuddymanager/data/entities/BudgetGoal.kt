package vcmsa.projects.budgetbuddymanager.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_goals")
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userOwnerId: Long,
    val month: Int, // 1-12
    val year: Int,
    val minAmount: Double,
    val maxAmount: Double
)
