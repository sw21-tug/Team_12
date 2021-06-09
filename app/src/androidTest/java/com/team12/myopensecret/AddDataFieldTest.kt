package com.team12.myopensecret

import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddDataFieldTest {
    @get:Rule
    var activityRule: ActivityTestRule<NewDataFieldEntry>
            = ActivityTestRule(NewDataFieldEntry::class.java, true, false)
    private lateinit var stringToBetyped: String
    @Before
    fun initValidString() {
        stringToBetyped = "randomText"
        activityRule.launchActivity(null);
    }

    @Test
    fun typingDataFieldNameTest() {
        onView(withId(R.id.data_field_name))
                .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.data_field_name))
                .check(matches(withText(stringToBetyped)))
    }

    @Test
    fun typingNotEnoughDataFieldTest() {
        stringToBetyped = "oof"
        onView(withId(R.id.data_field_name))
                .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.accept_entry)).perform(click())

        onView(allOf(withId(R.id.data_field_name), isDisplayed())).check(matches(not(hasNoErrorText())));
    }

    @Test
    fun typingTooMuchDataFieldTest() {
        stringToBetyped = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy"
        onView(withId(R.id.data_field_name))
                .perform(typeText(stringToBetyped), closeSoftKeyboard())

        onView(withId(R.id.accept_entry)).perform(click())

        onView(allOf(withId(R.id.data_field_name), isDisplayed())).check(matches(not(hasNoErrorText())));
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