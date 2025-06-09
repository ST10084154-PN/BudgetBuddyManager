package vcmsa.projects.budgetbuddymanager

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import vcmsa.projects.budgetbuddymanager.ui.activities.MainActivity

class LoginInstrumentedTest {
    @Test
    fun loginScreen_hasButtonsAndFields() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.editUsername)).check(matches(isDisplayed()))
        onView(withId(R.id.editPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(withText("Login")))
        onView(withId(R.id.btnRegister)).check(matches(withText("Register")))
    }
}
