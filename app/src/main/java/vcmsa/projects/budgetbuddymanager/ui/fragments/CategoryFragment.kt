package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.databinding.FragmentCategoryBinding
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModelFactory

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String
    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this, CategoryViewModelFactory(FirebaseCategoryRepository()))
            .get(CategoryViewModel::class.java)
    }

    companion object {
        private const val USER_ID = "user_id"
        fun newInstance(userId: String) = CategoryFragment().apply {
            arguments = Bundle().apply { putString(USER_ID, userId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryViewModel.getCategoriesForUser(userId)

        binding.btnAddCategory.setOnClickListener {
            val name = binding.editCategoryName.text.toString()
            if (name.isBlank()) {
                Toast.makeText(context, "Category name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            categoryViewModel.addCategory(Category(name = name, userId = userId))
            binding.editCategoryName.text?.clear()
        }

        categoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            binding.txtCategories.text = categories.joinToString("\n") { it.name }
        }

        binding.btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(requireActivity().findViewById<View>(vcmsa.projects.budgetbuddymanager.R.id.fragmentContainer).id,
                    AddExpenseFragment.newInstance(userId))
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
