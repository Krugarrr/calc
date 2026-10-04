package ru.krugarrr.calculator

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun click(vararg keys: String) {
        for (key in keys) {
            rule.onNode(hasText(key) and hasClickAction()).performClick()
        }
    }

    @Test
    fun calculatesWithButtons() {
        click("1", "2", "+", "3", "CE", "4", "5", "DEL", "±", "=")
        rule.onNodeWithTag("result").assertTextEquals("8")
        click("/", "0", "=")
        rule.onNodeWithTag("result").assertTextEquals("Infinity")
    }

    @Test
    fun survivesRecreation() {
        click("2", "+", "3", "*")
        rule.activityRule.scenario.recreate()
        click("4", "=")
        rule.onNodeWithTag("result").assertTextEquals("20")
        rule.activityRule.scenario.recreate()
        rule.onNodeWithTag("result").assertTextEquals("20")
    }
}
