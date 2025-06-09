package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.insertImage
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.budgetbuddymanager.data.entities.Category
import vcmsa.projects.budgetbuddymanager.data.entities.Expense
import vcmsa.projects.budgetbuddymanager.databinding.FragmentAddExpenseBinding
import vcmsa.projects.budgetbuddymanager.repository.FirebaseCategoryRepository
import vcmsa.projects.budgetbuddymanager.repository.FirebaseExpenseRepository
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.CategoryViewModelFactory
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.ExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : Fragment() {
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!

    private lateinit var userId: String

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(this, CategoryViewModelFactory(FirebaseCategoryRepository()))
            .get(CategoryViewModel::class.java)
    }
    private val expenseViewModel: ExpenseViewModel by lazy {
        ViewModelProvider(this, ExpenseViewModelFactory(FirebaseExpenseRepository()))
            .get(ExpenseViewModel::class.java)
    }

    private var categoryList: List<Category> = emptyList()
    private var selectedCategoryId: String? = null
    private var photoUri: String? = null

    companion object {
        private const val USER_ID = "user_id"
        fun newInstance(userId: String) = AddExpenseFragment().apply {
            arguments = Bundle().apply { putString(USER_ID, userId) }
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
            binding.imgReceipt.setImageURI(uri)
        }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val uri = saveImageToGallery(bitmap)
            if (uri != null) {
                photoUri = uri.toString()
                binding.imgReceipt.setImageURI(uri)
            }
        }
    }

    private fun saveImageToGallery(bitmap: android.graphics.Bitmap): Uri? {
        val resolver = requireActivity().contentResolver
        val imageUri = insertImage(
            /* cr = */ resolver, /* source = */
            bitmap, /* title = */
            "BudgetBuddyReceipt", /* description = */
            "Receipt Photo"
        )
        return imageUri?.toUri()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryViewModel.getCategoriesForUser(userId)
        categoryViewModel.categories.observe(viewLifecycleOwner) { cats ->
            categoryList = cats
            val names = cats.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedCategoryId = if (categoryList.isNotEmpty()) categoryList[position].id else null
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { selectedCategoryId = null }
        }

        binding.btnAttachPhoto.setOnClickListener {
            val options = arrayOf("Camera", "Gallery")
            AlertDialog.Builder(requireContext())
                .setTitle("Attach Photo")
                .setItems(options) { _, which ->
                    if (which == 0) takePhotoLauncher.launch(null)
                    else pickImageLauncher.launch("image/*")
                }.show()
        }

        binding.btnSaveExpense.setOnClickListener {
            val amountText = binding.editAmount.text.toString()
            val desc = binding.editDescription.text.toString()
            val startDate = binding.editStartDate.text.toString()
            val endDate = binding.editEndDate.text.toString()

            if (amountText.isBlank() || desc.isBlank() || startDate.isBlank() || endDate.isBlank() || selectedCategoryId == null) {
                Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val startMillis = sdf.parse(startDate)?.time ?: 0L
            val endMillis = sdf.parse(endDate)?.time ?: 0L
            if (endMillis < startMillis) {
                Toast.makeText(context, "End date must be after start date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            expenseViewModel.addExpense(
                Expense(
                    amount = amount,
                    description = desc,
                    startDateTime = startMillis,
                    endDateTime = endMillis,
                    categoryId = selectedCategoryId!!,
                    userId = userId,
                    photoUri = photoUri
                )
            )
            Toast.makeText(context, "Expense added!", Toast.LENGTH_SHORT).show()
            binding.editAmount.text?.clear()
            binding.editDescription.text?.clear()
            binding.editStartDate.text?.clear()
            binding.editEndDate.text?.clear()
            binding.imgReceipt.setImageDrawable(null)
        }

        binding.btnToList.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(requireActivity().findViewById<View>(vcmsa.projects.budgetbuddymanager.R.id.fragmentContainer).id,
                    ExpenseListFragment.newInstance(userId))
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
