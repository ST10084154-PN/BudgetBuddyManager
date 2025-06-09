package vcmsa.projects.budgetbuddymanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import vcmsa.projects.budgetbuddymanager.databinding.FragmentLoginBinding
import vcmsa.projects.budgetbuddymanager.viewmodel.UserViewModel
import vcmsa.projects.budgetbuddymanager.viewmodel.UserViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by lazy {
        val app = requireActivity().application as vcmsa.projects.budgetbuddymanager.BudgetBuddyApp
        androidx.lifecycle.ViewModelProvider(
            this,
            UserViewModelFactory(app.userRepository)
        )[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userViewModel.login(username, password)
        }
        binding.btnRegister.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userViewModel.register(username, password)
            Toast.makeText(context, "Registration complete. Please log in.", Toast.LENGTH_SHORT).show()
        }

        userViewModel.loginResult.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                parentFragmentManager.beginTransaction()
                    .replace(requireActivity().findViewById<View>(vcmsa.projects.budgetbuddymanager.R.id.fragmentContainer).id,
                        CategoryFragment.newInstance(user.id))
                    .commit()
            } else {
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

