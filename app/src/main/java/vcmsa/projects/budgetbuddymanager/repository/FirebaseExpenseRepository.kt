package vcmsa.projects.budgetbuddymanager.repository

import com.google.firebase.firestore.FirebaseFirestore
import vcmsa.projects.budgetbuddymanager.data.entities.Expense
import kotlinx.coroutines.tasks.await

class FirebaseExpenseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("expenses")

    suspend fun addExpense(expense: Expense): String {
        val docRef = collection.document()
        val expenseWithId = expense.copy(id = docRef.id)
        docRef.set(expenseWithId).await()
        return docRef.id
    }

    suspend fun getExpensesForUserInRange(userId: String, startMillis: Long, endMillis: Long): List<Expense> {
        val snap = collection
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("startDateTime", startMillis)
            .whereLessThanOrEqualTo("endDateTime", endMillis)
            .get().await()
        return snap.documents.mapNotNull { it.toObject(Expense::class.java) }
    }
}
