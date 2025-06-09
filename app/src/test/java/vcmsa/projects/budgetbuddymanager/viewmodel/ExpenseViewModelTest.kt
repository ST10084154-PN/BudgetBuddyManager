package vcmsa.projects.budgetbuddymanager.viewmodel

import org.junit.Assert.*
import org.junit.Test

class ExpenseViewModelTest {
    @Test
    fun `expense amount should be positive`() {
        val vm = ExpenseViewModel(/* pass a fake/mock repo or use real one with test DB/firestore emulator */)
        val result = vm.isAmountValid(100.0)
        assertTrue(result)

        val negativeResult = vm.isAmountValid(-20.0)
        assertFalse(negativeResult)

        val zeroResult = vm.isAmountValid(0.0)
        assertFalse(zeroResult)
    }

    private fun ExpenseViewModel(): ExpenseViewModel {

        return TODO("Provide the return value")
    }

    fun isAmountValid(amount: Double): Boolean = amount > 0

}
