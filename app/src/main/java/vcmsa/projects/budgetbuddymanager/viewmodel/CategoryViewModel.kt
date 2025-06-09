package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: FirebaseCategoryRepository) : ViewModel() {
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    fun getCategoriesForUser(userId: String) {
        viewModelScope.launch {
            _categories.value = repository.getCategoriesForUser(userId)
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
            getCategoriesForUser(category.userId)
        }
    }
}
