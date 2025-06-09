package vcmsa.projects.budgetbuddymanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vcmsa.projects.budgetbuddymanager.data.entities.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category): Long

    @Query("SELECT * FROM categories WHERE userOwnerId = :userId")
    suspend fun getCategoriesForUser(userId: Long): List<Category>
}
