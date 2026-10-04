package ru.krugarrr.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.krugarrr.calculator.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { testTagsAsResourceId = true }
                ) { innerPadding ->
                    CalculatorScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

val buttons = listOf(
    listOf("C", "CE", "DEL", "/"),
    listOf("7", "8", "9", "*"),
    listOf("4", "5", "6", "-"),
    listOf("1", "2", "3", "+"),
    listOf("±", "0", ".", "=")
)

@Composable
fun CalculatorScreen(modifier: Modifier) {
    var prefix by rememberSaveable { mutableStateOf("") }
    var input by rememberSaveable { mutableStateOf("0") }
    var finished by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val calc = Calculator()
    calc.prefix = prefix
    calc.input = input
    calc.finished = finished
    val result = calc.getResult()

    fun onButton(key: String) {
        calc.press(key)
        prefix = calc.prefix
        input = calc.input
        finished = calc.finished
    }

    fun copy() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("result", result))
        Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        Row(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Display(prefix, result, { copy() }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(24.dp))
            Keyboard({ onButton(it) }, Modifier.weight(1f))
        }
    } else {
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Display(prefix, result, { copy() }, Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))
            Keyboard({ onButton(it) }, Modifier.weight(1.4f))
        }
    }
}

@Composable
fun Display(expression: String, result: String, onCopy: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(text = expression, fontSize = 20.sp)
        Text(
            text = result,
            modifier = Modifier.testTag("result"),
            fontSize = if (result.length > 12) 28.sp else 42.sp
        )
        TextButton(onClick = onCopy) {
            Text(text = stringResource(R.string.copy))
        }
    }
}

@Composable
fun Keyboard(onButton: (String) -> Unit, modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        for (row in buttons) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                for (key in row) {
                    Button(
                        onClick = { onButton(key) },
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = key, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}
