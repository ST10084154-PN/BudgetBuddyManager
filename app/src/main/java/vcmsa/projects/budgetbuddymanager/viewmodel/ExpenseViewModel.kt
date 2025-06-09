package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.Expense
import vcmsa.projects.budgetbuddymanager.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    fun getExpensesByDateRange(userId: Long, startMillis: Long, endMillis: Long) {
        viewModelScope.launch {
            _expenses.value = repository.getExpensesByDateRange(userId, startMillis, endMillis)
        }
    }

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
            // Optionally refresh list if you want live updates here
        }
    }
}

