package my.tech.calculator.ui.screens.home.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.tech.calculator.R
import my.tech.calculator.ui.screens.home.models.ExpressionItem
import my.tech.calculator.ui.screens.home.models.HomeViewState
import my.tech.calculator.ui.theme.MyTechCalculatorTheme
import my.tech.calculator.ui.theme.components.JetRoundedButtonDefaults
import my.tech.calculator.ui.theme.components.JetRoundedButton

@Composable
fun HomeViewInit(
    viewState: HomeViewState,
    onChangeTheme: (Boolean) -> Unit,
    onChangeExpression: (ExpressionItem) -> Unit,
    onClearExpression: () -> Unit,
    onRemoveLastSymbol: () -> Unit,
    onCalculateExpression: () -> Unit
) {

    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Поле для выражения
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 120.dp else 208.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(start = 32.dp, end = 64.dp, top = 32.dp, bottom = 16.dp)
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = viewState.displayExpression.toString(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (viewState.expressionResult.isEmpty()) "0" else "=${viewState.expressionResult}",
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        // Адаптивная компоновка кнопок
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            val elementMargin = 8.dp

            // Размер кнопки адаптируется под ориентацию
            val buttonSize = if (isLandscape) {
                // В ландшафте используем высоту контейнера для расчета
                (this.maxHeight - elementMargin * 5) / 4
            } else {
                // В портрете используем ширину
                if (this.maxWidth > 400.dp) {
                    this.maxWidth / 5 - elementMargin
                } else {
                    64.dp
                }
            }

            if (isLandscape) {
                // ЛАНДШАФТНАЯ ОРИЕНТАЦИЯ: 4 строки × 6 столбцов
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(elementMargin)
                ) {
                    // Строка 1: C, (, ), ×, ÷, ⌫ - ТЕМНО-СЕРЫЕ КНОПКИ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "C",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onClearExpression.invoke() }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "(",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.LeftBracket) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = ")",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.RightBracket) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "×",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationMul) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "÷",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationDiv) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            iconId = R.drawable.ic_backspace,
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onRemoveLastSymbol.invoke() }
                        )
                    }

                    // Строка 2: √, %, X², +, -, = - СВЕТЛО-СЕРЫЕ КНОПКИ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "√",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationSqrt) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "%",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationPercent) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = buildAnnotatedString {
                                append("x")
                                withStyle(
                                    SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                ) {
                                    append("2")
                                }
                            },
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationSqr) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "+",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationPlus) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "-",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationMinus) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "=",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onCalculateExpression.invoke() }
                        )
                    }

                    // Строка 3: 1, 2, 3, 4, 5, 6 - СВЕТЛО-СЕРЫЕ КНОПКИ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "1",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value1) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "2",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value2) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "3",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value3) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "4",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value4) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "5",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value5) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "6",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value6) }
                        )
                    }

                    // Строка 4: 7, 8, 9, 0, ., x^y - СВЕТЛО-СЕРЫЕ КНОПКИ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "7",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value7) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "8",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value8) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "9",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value9) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = "0",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value0) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = ".",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.ValuePoint) }
                        )
                        JetRoundedButton(
                            modifier = Modifier
                                .size(buttonSize)
                                .weight(1f),
                            text = buildAnnotatedString {
                                append("x")
                                withStyle(
                                    SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                ) {
                                    append("y")
                                }
                            },
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationSqr) }
                        )
                    }
                }
            } else {
                // ПОРТРЕТНАЯ ОРИЕНТАЦИЯ: 6 строк × 4 столбца (оригинальная компоновка)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Столбец 1: C, √, 1, 4, 7, .
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "C",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onClearExpression.invoke() }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "√",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationSqrt) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "1",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value1) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "4",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value4) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "7",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value7) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = ".",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.ValuePoint) }
                        )
                    }

                    // Столбец 2: (, %, 2, 5, 8, 0
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "(",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.LeftBracket) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "%",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationPercent) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "2",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value2) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "5",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value5) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "8",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value8) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "0",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value0) }
                        )
                    }

                    // Столбец 3: ), x^y, 3, 6, 9, ⌫
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = ")",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.RightBracket) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = buildAnnotatedString {
                                append("x")
                                withStyle(
                                    SpanStyle(
                                        baselineShift = BaselineShift.Superscript,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                ) {
                                    append("y")
                                }
                            },
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationSqr) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "3",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value3) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "6",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value6) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "9",
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.Value9) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            iconId = R.drawable.ic_backspace,
                            buttonColors = JetRoundedButtonDefaults.numberButtonColors(),
                            onClick = { onRemoveLastSymbol.invoke() }
                        )
                    }

                    // Столбец 4: ×, ÷, +, -, =
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(elementMargin)
                    ) {
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "×",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationMul) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "÷",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationDiv) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "+",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationPlus) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize),
                            text = "-",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onChangeExpression.invoke(ExpressionItem.OperationMinus) }
                        )
                        JetRoundedButton(
                            modifier = Modifier.size(buttonSize, buttonSize * 2 + elementMargin),
                            text = "=",
                            buttonColors = JetRoundedButtonDefaults.operationButtonColors(),
                            onClick = { onCalculateExpression.invoke() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ShowPreview() {
    MyTechCalculatorTheme {
        HomeViewInit(viewState = HomeViewState(), {}, {}, {}, {}, {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShowPreview2() {
    MyTechCalculatorTheme {
        HomeViewInit(viewState = HomeViewState(), {}, {}, {}, {}, {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, widthDp = 640, heightDp = 360)
@Composable
private fun ShowPreviewLandscape() {
    MyTechCalculatorTheme {
        HomeViewInit(viewState = HomeViewState(), {}, {}, {}, {}, {})
    }
}