package vcmsa.projects.budgetbuddymanager.viewmodel

import androidx.lifecycle.*
import vcmsa.projects.budgetbuddymanager.data.entities.Expense
import vcmsa.projects.budgetbuddymanager.repository.FirebaseExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: FirebaseExpenseRepository) : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    fun getExpensesByDateRange(userId: String, startMillis: Long, endMillis: Long) {
        viewModelScope.launch {
            _expenses.value = repository.getExpensesForUserInRange(userId, startMillis, endMillis)
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
            // Optionally refresh here


        }
    }
    fun isAmountValid(amount: Double): Boolean = amount > 0

}


