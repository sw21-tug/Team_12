package com.team12.myopensecret

import android.provider.ContactsContract
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
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DataBaseTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    private lateinit var dataBaseHelper: DataBaseHelper

    @Before
    fun setUp() {
        dataBaseHelper = DataBaseHelper(activityRule.activity.applicationContext)
    }

    @After
    fun tearDown() {
        dataBaseHelper.onUpgrade(dataBaseHelper.writableDatabase, 0, 1)
    }

    @Test
    fun testInsertDataField() {
        val x = DataFieldData("false", "testName", "0", null, null, -1)
        val id = dataBaseHelper.addDataField(x)
        Assert.assertNotEquals(x.dfId,-1)
        val y = dataBaseHelper.getDataFieldById(id.toInt())
        Assert.assertTrue(equalsDataField(x, y!!))
    }

    private fun equalsDataField(actual: DataFieldData, expected: DataFieldData):Boolean {
        return actual.name == expected.name && actual.alwaysAdd == expected.alwaysAdd &&
                actual.type == expected.type && actual.dfId == expected.dfId
    }

    @Test
    fun testInsertLabels() {
        val x = LabelData("testLabel", "FF43FF", -1)
        val id = dataBaseHelper.addLabelEntry(x)
        Assert.assertNotEquals(x.dbId,-1)
        val y = dataBaseHelper.getLabelById(id.toInt())
        Assert.assertTrue(equalsLabel(x, y!!))
    }

    private fun equalsLabel(actual: LabelData, expected: LabelData):Boolean {
        return actual.name == expected.name && actual.color == expected.color &&
                actual.dbId == expected.dbId
    }

    @Test
    fun testInsertJournal() {
        val x = JournalDataEntry("testTitle", "testDescription", ArrayList(), ArrayList(), -1)
        dataBaseHelper.addJournalEntry(x)
        Assert.assertNotEquals(x.dbId,-1)
        val y = dataBaseHelper.viewJournalEntries()
        Assert.assertTrue(isInList(y, x))
    }

    private fun isInList(hay: List<JournalDataEntry>, needle: JournalDataEntry):Boolean {
        hay.forEach{
            if (equalsJournal(it, needle))
                return true
        }
        return false
    }

    private fun equalsJournal(actual: JournalDataEntry, expected: JournalDataEntry):Boolean {
        return actual.dbId == expected.dbId && actual.description == expected.description &&
                actual.title == expected.title && actual.dbId == expected.dbId
    }
}