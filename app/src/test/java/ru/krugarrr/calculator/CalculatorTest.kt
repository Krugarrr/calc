package ru.krugarrr.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorTest {
    fun calc(vararg keys: String): String {
        val c = Calculator()
        for (key in keys) {
            c.press(key)
        }
        return c.getResult()
    }

    @Test
    fun simpleOperations() {
        assertEquals("13", calc("6", "+", "7", "="))
        assertEquals("42", calc("6", "*", "7", "="))
        assertEquals("15", calc("1", "7", "-", "2", "="))
        assertEquals("32", calc("6", "4", "/", "2", "="))
    }

    @Test
    fun operationsGoInOrder() {
        assertEquals("20", calc("2", "+", "3", "*", "4", "="))
        assertEquals("2", calc("8", "/", "2", "/", "2", "="))
    }

    @Test
    fun fractions() {
        assertEquals("0.3", calc("0", ".", "1", "+", "0", ".", "2", "="))
        assertEquals("0.25", calc(".", "2", ".", "5", "="))
        assertEquals("192.168019", calc("1", "9", "2", ".", "1", "6", "8", ".", "0", ".", "1", "9", "="))
    }

    @Test
    fun divisionByZero() {
        assertEquals("Infinity", calc("1", "/", "0", "="))
        assertEquals("-Infinity", calc("-", "1", "/", "0", "="))
        assertEquals("NaN", calc("0", "/", "0", "="))
        assertEquals("1", calc("0", "/", "0", "=", "1"))
    }

    @Test
    fun negativeNumbers() {
        assertEquals("-3", calc("-", "5", "+", "2", "="))
        assertEquals("-15", calc("5", "*", "-", "3", "="))
        assertEquals("8", calc("5", "-", "-", "3", "="))
        assertEquals("-3", calc("1", ".", "5", "±", "*", "2", "="))
    }

    @Test
    fun editing() {
        assertEquals("17", calc("1", "2", "+", "3", "4", "CE", "5", "="))
        assertEquals("24", calc("1", "2", "+", "*", "2", "="))
        assertEquals("-3", calc("3", "4", "DEL", "±"))
        assertEquals("0", calc("3", "±", "DEL"))
        assertEquals("0", calc("1", "+", "2", "=", "C"))
    }

    @Test
    fun continueAfterResult() {
        assertEquals("20", calc("2", "+", "3", "=", "*", "4", "="))
        assertEquals("7", calc("2", "+", "3", "=", "7"))
        assertEquals("5", calc("2", "+", "3", "=", "="))
    }

    @Test
    fun bigNumbers() {
        assertEquals("1e+20", calc("1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "*", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "="))
    }
}
