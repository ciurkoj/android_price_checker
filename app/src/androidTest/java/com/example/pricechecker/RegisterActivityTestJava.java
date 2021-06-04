package com.example.pricechecker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class RegisterActivityTestJava {
    @Rule
    public ActivityTestRule<RegisterActivity> activityTestRule =
            new ActivityTestRule<>(RegisterActivity.class);
    @Test
    public void changeText_sameActivity() {

        onView(withId(R.id.username)).perform(typeText("meme"),
                closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());

        onView(withId(R.id.username)).check(matches(hasErrorText("Please enter a valid username")));
        onView(withText("email")).
                inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

}
