package com.example.pricechecker

import android.service.autofill.Validators.not
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.Is
import org.hamcrest.core.IsNot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)
    @Test
    fun testActivity_inView() {

        onView(withId(R.id.activity_register))
            .check(matches(isDisplayed()))

        // Notice this does not effect the next test
        activityRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun testVisibility_title_nextButton() {
        onView(withId(R.id.register))
            .check(matches(isDisplayed()))
    }
    @Test
    fun test_short_username_input(){
        onView(withId(R.id.username)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.email)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.confirm_password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.register)).perform(click())
        onView(withId(R.id.username)).check(
            matches(
                hasErrorText("Please enter a valid username")
            )
        )

    }
    @Test
    fun test_short_password_input(){
        onView(withId(R.id.username)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.email)).perform(
            typeText("meme12wqw@gmail.com"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.confirm_password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.register)).perform(click())
        onView(withId(R.id.password)).check(
            matches(
                hasErrorText("The given password is invalid. [Password should be at least 6 characters]")
            )
        )

    }

    @Test
    fun test_invalid_email_address_input(){
        onView(withId(R.id.username)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.email)).perform(
            typeText("meme12wqwgmail.com"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.confirm_password)).perform(
            typeText("meme"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.register)).perform(click())
        onView(withId(R.id.email)).check(
            matches(
                hasErrorText("Please enter valid email")
            )
        )
    }
    @Test
    fun email_address_already_exists(){
        onView(withId(R.id.username)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.email)).perform(
            typeText("meme12@gmail.com"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.confirm_password)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.register)).perform(click())

        onView(withText("The email address is already in use by another account.")).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }
    @Test
    fun passwords_dont_match(){
        onView(withId(R.id.username)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.email)).perform(
            typeText("meme1233233233232@gmail.com"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            typeText("meme121"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.confirm_password)).perform(
            typeText("meme12"), ViewActions.closeSoftKeyboard()
        )
        onView(withId(R.id.register)).perform(click())

        onView(withId(R.id.password)).check(
            matches(
                hasErrorText("Entered passwords don't match")
            )
        )
    }


}