package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.User
import vcmsa.projects.budgetbuddymanager.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            _loginResult.value = if (user != null && user.password == password) user else null
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val user = User(username = username, password = password)
            repository.insertUser(user)
        }
    }
}
