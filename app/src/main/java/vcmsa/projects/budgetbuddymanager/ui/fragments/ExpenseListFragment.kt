package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.databinding.FragmentExpenseListBinding
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import vcmsa.projects.budgetbuddymanager.repository.FirebaseExpenseRepository
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModelFactory
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListFragment : Fragment() {
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private val expenseViewModel: ExpenseViewModel by lazy {
        ViewModelProvider(this, ExpenseViewModelFactory(FirebaseExpenseRepository()))
            .get(ExpenseViewModel::class.java)
    }
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this, CategoryViewModelFactory(FirebaseCategoryRepository()))
            .get(CategoryViewModel::class.java)
    }

    private var categoryList: List<Category> = emptyList()

    companion object {
        private const val USER_ID = "user_id"
        fun newInstance(userId: String) = ExpenseListFragment().apply {
            arguments = Bundle().apply { putString(USER_ID, userId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryViewModel.getCategoriesForUser(userId)
        categoryViewModel.categories.observe(viewLifecycleOwner) { cats ->
            categoryList = cats
        }

        binding.btnFilter.setOnClickListener {
            val startDate = binding.editFilterStart.text.toString()
            val endDate = binding.editFilterEnd.text.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val startMillis = sdf.parse(startDate)?.time ?: 0L
            val endMillis = sdf.parse(endDate)?.time ?: 0L
            expenseViewModel.getExpensesByDateRange(userId, startMillis, endMillis)
        }

        expenseViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val text = expenses.joinToString("\n") {
                val categoryName = categoryList.find { cat -> cat.id == it.categoryId }?.name ?: "Unknown"
                "${categoryName}: R${it.amount} (${sdf.format(Date(it.startDateTime))})"
            }
            binding.txtExpenses.text = text

            val totals = mutableMapOf<String, Double>()
            expenses.forEach { exp ->
                totals[exp.categoryId] = (totals[exp.categoryId] ?: 0.0) + exp.amount
            }
            val totalPerCat = totals.map { (catId, total) ->
                val catName = categoryList.find { it.id == catId }?.name ?: "Unknown"
                "$catName: R$total"
            }.joinToString("\n")
            binding.txtTotals.text = "Total per category:\n$totalPerCat"
        }

        binding.btnBudget.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(requireActivity().findViewById<View>(vcmsa.projects.budgetbuddymanager.R.id.fragmentContainer).id,
                    BudgetGoalFragment.newInstance(userId))
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
