package com.team12.myopensecret

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.view.get
import androidx.core.view.size
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
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
class ViewEntryActivityTest {

    private lateinit var stringToBetypedTitle: String
    private lateinit var stringToBetypedNotes: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        onView(withId(R.id.add_entry_button)).perform(click())
        stringToBetypedTitle = "randomText"
        stringToBetypedNotes = "a real long randomText to verify everything works fine"
    }

    @Test
    fun viewNewEntry() {
        onView(withId(R.id.title_field))
                .perform(typeText(stringToBetypedTitle), closeSoftKeyboard())
        onView(withId(R.id.notes_field))
                .perform(typeText(stringToBetypedNotes), closeSoftKeyboard())
        onView(withId(R.id.accept_entry)).perform(click())
        val journals = activityRule.activity.entryList
        journals.get(0).id = View.generateViewId()
        onView(withId(journals.get(0).id)).perform(click())
    }

    @Test
    fun viewAndDeleteNewEntry() {
        onView(withId(R.id.title_field))
                .perform(typeText(stringToBetypedTitle), closeSoftKeyboard())
        onView(withId(R.id.notes_field))
                .perform(typeText(stringToBetypedNotes), closeSoftKeyboard())
        onView(withId(R.id.accept_entry)).perform(click())
        val journals = activityRule.activity.entryList
        journals.get(0).id = View.generateViewId()
        onView(withId(journals.get(0).id)).perform(click())
        onView(withId(R.id.delete_entry)).perform(click())
        onView(withText(android.R.string.yes)).perform(click())
    }
}