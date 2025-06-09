package vcmsa.projects.budgetbuddymanager.data.entities

data class BudgetGoal(
    val id: String = "",
    val userId: String = "",
    val month: Int = 1,
    val year: Int = 2024,
    val minAmount: Double = 0.0,
    val maxAmount: Double = 0.0
)
