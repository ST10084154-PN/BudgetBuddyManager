package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.repository.FirebaseAuthRepository
import kotlinx.coroutines.launch

class UserViewModel(private val authRepo: FirebaseAuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> get() = _loginResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val uid = authRepo.register(email, password)
            _loginResult.postValue(uid)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val uid = authRepo.login(email, password)
            _loginResult.postValue(uid)
        }
    }

    fun getCurrentUserId(): String? = authRepo.getCurrentUserId()

    fun logout() = authRepo.logout()
}
