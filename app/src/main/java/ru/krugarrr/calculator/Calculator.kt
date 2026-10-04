package ru.krugarrr.calculator

import java.util.Locale

class Calculator {
    var prefix = ""
    var input = "0"
    var finished = false

    fun getResult(): String {
        if (input != "") return input
        if (prefix != "") return prefix.split(" ")[0]
        return "0"
    }

    fun press(key: String) {
        when (key) {
            "C" -> clear()
            "CE" -> {
                if (finished) clear()
                input = "0"
            }
            "DEL" -> deleteLast()
            "±" -> changeSign()
            "." -> addDot()
            "+", "-", "*", "/" -> addOperator(key)
            "=" -> equalsPressed()
            else -> addDigit(key)
        }
    }

    fun clear() {
        prefix = ""
        input = "0"
        finished = false
    }

    fun isSpecial(): Boolean {
        return input.contains("e") || input.contains("NaN") || input.contains("Infinity")
    }

    fun addDigit(digit: String) {
        if (finished) clear()
        if (isSpecial()) input = "0"
        if (input == "0") {
            input = digit
        } else if (input == "-0") {
            input = "-" + digit
        } else if (input.length < 18) {
            input += digit
        }
    }

    fun addDot() {
        if (finished) clear()
        if (isSpecial()) input = "0"
        if (input.contains(".")) return
        if (input == "" || input == "-") {
            input += "0."
        } else if (input.length < 18) {
            input += "."
        }
    }

    fun deleteLast() {
        if (finished) {
            prefix = ""
            finished = false
        }
        if (isSpecial() || input.length <= 1) {
            input = "0"
        } else {
            input = input.substring(0, input.length - 1)
        }
        if (input == "-") input = "0"
    }

    fun changeSign() {
        if (finished) {
            prefix = ""
            finished = false
        }
        if (input.startsWith("-")) {
            input = input.substring(1)
        } else {
            input = "-" + input
        }
        if (input == "" && prefix == "") input = "0"
    }

    fun addOperator(op: String) {
        if (finished) {
            prefix = ""
            finished = false
        }
        if (op == "-" && (input == "" || (prefix == "" && input == "0"))) {
            input = "-"
        } else if (input == "-" && prefix == "") {
            input = "0"
        } else if (input == "" || input == "-") {
            input = ""
            prefix = prefix.substring(0, prefix.length - 2) + op + " "
        } else {
            prefix = calculate() + " " + op + " "
            input = ""
        }
    }

    fun equalsPressed() {
        if (finished || input == "" || input == "-") return
        val res = calculate()
        prefix = prefix + input + " ="
        input = res
        finished = true
    }

    fun calculate(): String {
        val b = input.toDouble()
        if (prefix == "") return format(b)
        val parts = prefix.split(" ")
        val a = parts[0].toDouble()
        val op = parts[1]
        var res = 0.0
        if (op == "+") res = a + b
        if (op == "-") res = a - b
        if (op == "*") res = a * b
        if (op == "/") res = a / b
        return format(res)
    }

    fun format(x: Double): String {
        if (x.isNaN() || x.isInfinite()) return x.toString()
        if (x == 0.0) return "0"
        var s = String.format(Locale.US, "%.15g", x)
        var exp = ""
        if (s.contains("e")) {
            exp = s.substring(s.indexOf("e"))
            s = s.substring(0, s.indexOf("e"))
        }
        if (s.contains(".")) {
            while (s.endsWith("0")) s = s.substring(0, s.length - 1)
            if (s.endsWith(".")) s = s.substring(0, s.length - 1)
        }
        return s + exp
    }
}
