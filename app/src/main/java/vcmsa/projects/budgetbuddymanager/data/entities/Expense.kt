package vcmsa.projects.budgetbuddymanager.data.entities

data class Expense(
    val id: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val startDateTime: Long = 0L,
    val endDateTime: Long = 0L,
    val categoryId: String = "",
    val userId: String = "",
    val photoUri: String? = null
)
