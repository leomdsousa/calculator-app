package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import java.util.ArrayList
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    private var text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.text)
    }

    fun onDigit(view: View) {
        if(text?.text.toString() == "0") {
            text?.text = null
        }

        text?.append((view as Button)?.text)
    }

    fun onOperator(view: View) {
        var value = text?.text.toString()

        if(value.startsWith('-')) {
            value = value.substring(1)
        }

        if(value?.endsWith(".") == false
            && !this.isOperatorAdded(value)
          ) {
            if(this.endsWithOperator(text?.text.toString()!!)) {
                this.onDeleteLast(text as View)
            }

            text?.append((view as Button)?.text)
        }
    }

    fun onClear(view: View) {
        text?.text = null
    }

    fun onDeleteLast(view: View) {
        if(!text?.text.isNullOrBlank()) {
            text?.text = text?.text?.dropLast(1)
        }
    }

    fun onDecimal(view: View) {
        if(!text?.text.isNullOrBlank()
           && !text?.text?.endsWith(".")!!
           && text?.text?.count { x -> x == '.' }!! <= 1) {
            text?.append((view as Button)?.text)
        }
    }

    fun onResult(view: View) {
        text?.text.let {
            if(this.isOperatorAdded(it.toString())
               && !this.endsWithOperator(it.toString())) {
                var value = text?.text.toString()
                var preffix = ""

                if(value.startsWith('-')) {
                    value = value.substring(1)
                    preffix = "-"
                }

                try {
                    val operator = this.findOperator(value);

                    val splittedValue = value!!.split(operator)

                    var one = splittedValue[0]
                    var two = splittedValue[1]

                    var result = ""

                    if(!preffix.isNullOrBlank()) {
                        one = preffix + one
                    }

                    when (operator) {
                        '/' -> {
                            result = (one.toDouble() / two.toDouble()).toString()
                        }
                        '*' -> {
                            result = (one.toDouble() * two.toDouble()).toString()
                        }
                        '-' -> {
                            result = (one.toDouble() - two.toDouble()).toString()
                        }
                        '+' -> {
                            result = (one.toDouble() + two.toDouble()).toString()
                        }
                    }

                    result = this.removeZeroAfterDot(result)
                    text?.text = result
                } catch (e: ArithmeticException) {
                    Toast.makeText(this,"An arithmetic exception ocurred", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun onModule(view: View) {
        if(!text?.text?.isNullOrBlank()!! && text?.text.toString().isDigitsOnly()) {
            val module = (text?.text.toString().toDouble() % 2).toString()
            text?.text = this.removeZeroAfterDot(module)
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if(value.startsWith("-")) {
            value.count { x -> this.isOperator(x) } > 1
        } else {
            value.contains("/")
            || value.contains("*")
            || value.contains("-")
            || value.contains("+")
        }
    }

    private fun isOperator(value: Char): Boolean {
        return value == '/'
            || value == '-'
            || value == '*'
            || value == '+'
    }

    private fun endsWithOperator(value: String): Boolean {
        return value.endsWith("/")
            || value.endsWith("*")
            || value.endsWith("-")
            || value.endsWith("+")
    }

    private fun findOperator(value: String): Char {
        return value.first { x -> this.isOperator(x) }
    }

    private fun removeZeroAfterDot(result: String): String {
        var value = result

        if(result.contains(".0"))
            value = result.substring(0, result.length - 2)

        return value
    }
}