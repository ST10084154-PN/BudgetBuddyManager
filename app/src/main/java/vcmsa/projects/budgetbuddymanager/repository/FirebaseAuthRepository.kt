package vcmsa.projects.budgetbuddymanager.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun register(email: String, password: String): String? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid
    }

    suspend fun login(email: String, password: String): String? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun logout() = auth.signOut()
}
