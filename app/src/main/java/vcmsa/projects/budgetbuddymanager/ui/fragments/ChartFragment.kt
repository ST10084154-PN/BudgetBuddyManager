package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import vcmsa.projects.budgetbuddymanager.databinding.FragmentChartBinding
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import vcmsa.projects.budgetbuddymanager.repository.FirebaseExpenseRepository
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModelFactory
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private var categoryList: List<Category> = emptyList()

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

    companion object {
        private const val USER_ID = "user_id"
        private const val START = "startMillis"
        private const val END = "endMillis"
        fun newInstance(userId: String, startMillis: Long, endMillis: Long) = ChartFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
                putLong(START, startMillis)
                putLong(END, endMillis)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startMillis = arguments?.getLong("startMillis") ?: 0L
        val endMillis = arguments?.getLong("endMillis") ?: System.currentTimeMillis()

        categoryViewModel.getCategoriesForUser(userId)
        categoryViewModel.categories.observe(viewLifecycleOwner) { cats ->
            categoryList = cats
            expenseViewModel.getExpensesByDateRange(userId, startMillis, endMillis)
        }

        expenseViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            val totalsByCategory = mutableMapOf<String, Double>()
            expenses.forEach { exp ->
                totalsByCategory[exp.categoryId] = (totalsByCategory[exp.categoryId] ?: 0.0) + exp.amount
            }
            val pieEntries = totalsByCategory.map { (catId, sum) ->
                val catName = categoryList.find { it.id == catId }?.name ?: "Other"
                PieEntry(sum.toFloat(), catName)
            }
            if (pieEntries.isNotEmpty()) {
                val dataSet = PieDataSet(pieEntries, "Spending per Category")
                dataSet.colors = listOf(
                    Color.rgb(33,150,243), Color.rgb(255,193,7), Color.rgb(76,175,80),
                    Color.rgb(244,67,54), Color.rgb(156,39,176), Color.rgb(255,87,34)
                )
                val data = PieData(dataSet)
                data.setValueTextSize(16f)
                binding.pieChart.data = data
                binding.pieChart.invalidate()
                binding.pieChart.description.isEnabled = false
                binding.pieChart.legend.isEnabled = true
            } else {
                binding.pieChart.clear()
            }
        }

        binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
