package vcmsa.projects.budgetbuddymanager.repository

import vcmsa.projects.budgetbuddymanager.data.dao.UserDao
import vcmsa.projects.budgetbuddymanager.data.entities.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
}
