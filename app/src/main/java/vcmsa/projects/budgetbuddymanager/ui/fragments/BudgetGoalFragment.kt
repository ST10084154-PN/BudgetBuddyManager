package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import vcmsa.projects.budgetbuddymanager.databinding.FragmentBudgetGoalBinding
import vcmsa.projects.budgetbuddymanager.data.entities.BudgetGoal
import vcmsa.projects.budgetbuddymanager.viewmodel.BudgetGoalViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.BudgetGoalViewModelFactory
import java.util.*

class BudgetGoalFragment : Fragment() {
    private var _binding: FragmentBudgetGoalBinding? = null
    private val binding get() = _binding!!

    private val budgetGoalViewModel: BudgetGoalViewModel by lazy {
        val app = requireActivity().application as vcmsa.projects.budgetbuddymanager.BudgetBuddyApp
        androidx.lifecycle.ViewModelProvider(
            this,
            BudgetGoalViewModelFactory(app.budgetGoalRepository)
        )[BudgetGoalViewModel::class.java]
    }
    private var userId: Long = 0L

    companion object {
        private const val USER_ID = "user_id"
        fun newInstance(userId: Long) = BudgetGoalFragment().apply {
            arguments = Bundle().apply { putLong(USER_ID, userId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong(USER_ID) ?: 0L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        budgetGoalViewModel.getGoalForMonth(userId, month, year)
        budgetGoalViewModel.goal.observe(viewLifecycleOwner) { goal ->
            binding.seekMin.progress = goal?.minAmount?.toInt() ?: 0
            binding.seekMax.progress = goal?.maxAmount?.toInt() ?: 0
            binding.txtCurrentGoal.text = if (goal != null)
                "Current Goal: Min R${goal.minAmount} - Max R${goal.maxAmount}"
            else
                "No budget set for this month"
        }

        binding.seekMin.max = 5000
        binding.seekMax.max = 20000

        binding.seekMin.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.txtMinValue.text = "Min: R$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekMax.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.txtMaxValue.text = "Max: R$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnSaveBudget.setOnClickListener {
            val minAmount = binding.seekMin.progress.toDouble()
            val maxAmount = binding.seekMax.progress.toDouble()
            if (minAmount > maxAmount) {
                Toast.makeText(context, "Min cannot exceed Max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val goal = BudgetGoal(
                userOwnerId = userId,
                month = month,
                year = year,
                minAmount = minAmount,
                maxAmount = maxAmount
            )
            budgetGoalViewModel.upsertBudgetGoal(goal)
            Toast.makeText(context, "Budget goal saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
