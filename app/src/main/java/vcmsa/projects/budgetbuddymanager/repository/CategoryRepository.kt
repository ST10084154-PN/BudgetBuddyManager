package vcmsa.projects.budgetbuddymanager.repository

import vcmsa.projects.budgetbuddymanager.data.dao.CategoryDao
import vcmsa.projects.budgetbuddymanager.data.entities.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun getCategoriesForUser(userId: Long) = categoryDao.getCategoriesForUser(userId)
}
