package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal
import vcmsa.projects.budgetbuddymanager.repository.FirebaseBudgetGoalRepository
import kotlinx.coroutines.launch

class BudgetGoalViewModel(private val repository: FirebaseBudgetGoalRepository) : ViewModel() {
    private val _goal = MutableLiveData<BudgetGoal?>()
    val goal: LiveData<BudgetGoal?> get() = _goal

    fun getGoalForMonth(userId: String, month: Int, year: Int) {
        viewModelScope.launch {
            _goal.value = repository.getBudgetGoalForMonth(userId, month, year)
        }
    }

    fun setBudgetGoal(goal: BudgetGoal) {
        viewModelScope.launch {
            repository.setBudgetGoal(goal)
            getGoalForMonth(goal.userId, goal.month, goal.year)
        }
    }
}
