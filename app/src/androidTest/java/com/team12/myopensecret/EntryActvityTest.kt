package com.team12.myopensecret

import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class EntryActvityTest {

    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        onView(withId(R.id.add_entry_button)).perform(click())
        stringToBetyped = "randomText"
    }

    @Test
    fun typingTitleTest() {
        onView(withId(R.id.title_field))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.title_field))
            .check(matches(withText(stringToBetyped)))
    }

    @Test
    fun typingNotesTest() {
        onView(withId(R.id.notes_field))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.notes_field))
            .check(matches(withText(stringToBetyped)))
    }

    @Test
    fun typingNotEnoughTitleTest() {
        stringToBetyped = "oof"
        onView(withId(R.id.title_field))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.accept_entry)).perform(click())

        onView(allOf(withId(R.id.title_field), isDisplayed())).check(matches(not(hasNoErrorText())));
    }

    @Test
    fun typingNotEnoughNotesTest() {
        stringToBetyped = "oof"
        onView(withId(R.id.title_field))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.accept_entry)).perform(click())

        onView(allOf(withId(R.id.notes_field), isDisplayed())).check(matches(not(hasNoErrorText())));
    }

    @Test
    fun typingTooMuchTitle() {
        stringToBetyped = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"
        onView(withId(R.id.title_field))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.accept_entry)).perform(click())

        onView(allOf(withId(R.id.title_field), isDisplayed())).check(matches(not(hasNoErrorText())));

    }
    fun hasNoErrorText(): Matcher<View?>? {
        return object : BoundedMatcher<View?, EditText>(EditText::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has no error text: ")
            }

            override fun matchesSafely(view: EditText): Boolean {
                return view.error == null
            }
        }
    }
}