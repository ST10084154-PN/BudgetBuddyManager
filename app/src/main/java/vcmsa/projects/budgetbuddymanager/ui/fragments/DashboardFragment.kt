package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import vcmsa.projects.budgetbuddymanager.databinding.FragmentDashboardBinding
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.repository.FirebaseBudgetGoalRepository
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import vcmsa.projects.budgetbuddymanager.repository.FirebaseExpenseRepository
import vcmsa.projects.budgetbuddymanager.viewmodel.BudgetGoalViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.BudgetGoalViewModelFactory
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModelFactory
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModelFactory
import java.util.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private var categoryList: List<Category> = emptyList()
    private var spendingTotal: Double = 0.0
    private var minGoal: Double = 0.0
    private var maxGoal: Double = 0.0

    private val expenseViewModel: ExpenseViewModel by lazy {
        ViewModelProvider(
            this,
            ExpenseViewModelFactory(FirebaseExpenseRepository())
        ).get(ExpenseViewModel::class.java)
    }
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(
            this,
            CategoryViewModelFactory(FirebaseCategoryRepository())
        ).get(CategoryViewModel::class.java)
    }
    private val budgetGoalViewModel: BudgetGoalViewModel by lazy {
        ViewModelProvider(
            this,
            BudgetGoalViewModelFactory(FirebaseBudgetGoalRepository())
        ).get(BudgetGoalViewModel::class.java)
    }

    companion object {
        private const val USER_ID = "user_id"
        fun newInstance(userId: String) = DashboardFragment().apply {
            arguments = Bundle().apply { putString(USER_ID, userId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        val monthLabel = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: "This Month"
        binding.txtCurrentMonth.text = "Month: $monthLabel $year"

        categoryViewModel.getCategoriesForUser(userId)
        categoryViewModel.categories.observe(viewLifecycleOwner) { cats -> categoryList = cats }

        budgetGoalViewModel.getGoalForMonth(userId, month, year)
        budgetGoalViewModel.goal.observe(viewLifecycleOwner) { goal ->
            minGoal = goal?.minAmount ?: 0.0
            maxGoal = goal?.maxAmount ?: 0.0
        }

        val firstDay = cal.apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0) }.timeInMillis
        val lastDay = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59) }.timeInMillis

        expenseViewModel.getExpensesByDateRange(userId, firstDay, lastDay)
        expenseViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            spendingTotal = expenses.sumOf { it.amount }
            val pct = if (maxGoal > 0) (spendingTotal / maxGoal * 100).toInt().coerceAtMost(100) else 0
            binding.progressBudget.progress = pct
            binding.txtBudgetProgress.text = "You spent R$spendingTotal / R$maxGoal"

            val donutEntries = listOf(
                PieEntry(spendingTotal.toFloat(), "Spent"),
                PieEntry((maxGoal - spendingTotal).toFloat().coerceAtLeast(0f), "Remaining")
            )
            val donutDataSet = PieDataSet(donutEntries, "")
            donutDataSet.colors = listOf(Color.rgb(33,150,243), Color.LTGRAY)
            binding.donutChart.data = PieData(donutDataSet)
            binding.donutChart.description.isEnabled = false
            binding.donutChart.legend.isEnabled = false
            binding.donutChart.invalidate()

            if (spendingTotal > maxGoal) {
                binding.txtOverspending.text = "You are OVER your budget!"
            } else {
                binding.txtOverspending.text = ""
            }

            showBadges(spendingTotal, minGoal, maxGoal)
        }

        // Dark mode toggle
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    private fun showBadges(total: Double, min: Double, max: Double) {
        binding.badgeContainer.removeAllViews()
        if (max > 0 && total <= max) addBadge("Within Budget", Color.GREEN)
        if (min > 0 && total < min) addBadge("Super Saver", Color.BLUE)
        if (total == 0.0) addBadge("No Spend!", Color.MAGENTA)
    }

    private fun addBadge(text: String, color: Int) {
        val badge = TextView(requireContext()).apply {
            this.text = text
            setTextColor(color)
            setPadding(16, 8, 16, 8)
            setBackgroundResource(android.R.drawable.alert_light_frame)
        }
        binding.badgeContainer.addView(badge)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
