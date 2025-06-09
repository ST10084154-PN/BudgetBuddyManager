package vcmsa.projects.budgetbuddymanager.repository

import com.google.firebase.firestore.FirebaseFirestore
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal
import kotlinx.coroutines.tasks.await

class FirebaseBudgetGoalRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("budgetGoals")

    suspend fun setBudgetGoal(goal: BudgetGoal): String {
        val query = collection.whereEqualTo("userId", goal.userId)
            .whereEqualTo("month", goal.month)
            .whereEqualTo("year", goal.year)
            .get().await()
        val docRef = if (query.isEmpty) collection.document() else query.documents[0].reference
        val goalWithId = goal.copy(id = docRef.id)
        docRef.set(goalWithId).await()
        return docRef.id
    }

    suspend fun getBudgetGoalForMonth(userId: String, month: Int, year: Int): BudgetGoal? {
        val query = collection.whereEqualTo("userId", userId)
            .whereEqualTo("month", month)
            .whereEqualTo("year", year)
            .get().await()
        return query.documents.firstOrNull()?.toObject(BudgetGoal::class.java)
    }
}
