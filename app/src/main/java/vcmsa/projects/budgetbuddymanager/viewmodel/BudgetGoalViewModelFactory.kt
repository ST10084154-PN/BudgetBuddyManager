package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgetbuddymanager.repository.BudgetGoalRepository

class BudgetGoalViewModelFactory(private val repository: BudgetGoalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetGoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetGoalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
