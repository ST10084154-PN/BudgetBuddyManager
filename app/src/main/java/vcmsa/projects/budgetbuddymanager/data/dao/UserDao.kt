package vcmsa.projects.budgetbuddymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vcmsa.projects.budgetbuddymanager.data.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}
