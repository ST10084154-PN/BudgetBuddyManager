package vcmsa.projects.budgetbuddymanager.repository

import com.google.firebase.firestore.FirebaseFirestore
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import kotlinx.coroutines.tasks.await

class FirebaseCategoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("categories")

    suspend fun addCategory(category: Category): String {
        val docRef = collection.document()
        val catWithId = category.copy(id = docRef.id)
        docRef.set(catWithId).await()
        return docRef.id
    }

    suspend fun getCategoriesForUser(userId: String): List<Category> {
        val snap = collection.whereEqualTo("userId", userId).get().await()
        return snap.documents.mapNotNull { it.toObject(Category::class.java) }
    }
}
