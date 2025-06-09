package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal
import vcmsa.projects.budgetbuddymanager.repository.BudgetGoalRepository
import kotlinx.coroutines.launch

class BudgetGoalViewModel(private val repository: BudgetGoalRepository) : ViewModel() {
    private val _goal = MutableLiveData<BudgetGoal?>()
    val goal: LiveData<BudgetGoal?> get() = _goal

    fun getGoalForMonth(userId: Long, month: Int, year: Int) {
        viewModelScope.launch {
            _goal.value = repository.getGoalForMonth(userId, month, year)
        }
    }

    fun upsertBudgetGoal(goal: BudgetGoal) {
        viewModelScope.launch {
            repository.upsertBudgetGoal(goal)
            getGoalForMonth(goal.userOwnerId, goal.month, goal.year) // refresh
        }
    }
}
