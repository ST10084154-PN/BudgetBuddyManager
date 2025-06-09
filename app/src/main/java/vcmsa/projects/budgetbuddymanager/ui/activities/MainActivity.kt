package vcmsa.projects.budgetbuddymanager.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.budgetbuddymanager.databinding.ActivityMainBinding
import vcmsa.projects.budgetbuddymanager.ui.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, LoginFragment())
                .commit()
        }
    }
}
