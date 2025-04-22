@file:OptIn(ExperimentalFoundationApi::class)

package com.ssk.dashboard.presentation.dashboard.components

import SpendLessBlack
import SpendLessGreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.utils.InstantFormatter
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.dashboard.presentation.dashboard.DashboardState.AmountSettings
import com.ssk.dashboard.presentation.dashboard.utils.AmountFormatter
import java.time.Instant
import kotlin.math.roundToInt

@Composable
fun LatestTransactionView(
    modifier: Modifier = Modifier,
    transactions: Map<Instant, List<Transaction>>,
    amountSettings: AmountSettings,
    onShowAllClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .width(LocalConfiguration.current.screenWidthDp.dp)
            .fillMaxHeight(),
        contentColor = MaterialTheme.colorScheme.onSurface,
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        if (transactions.isEmpty()) {
            EmptyTransactionView()
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
            ) {
                TransactionHeader(
                    onShowAllClick = onShowAllClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    transactions.entries.forEach { (instant, transactions) ->
                        stickyHeader {
                            Text(
                                text = InstantFormatter.formatToRelativeDay(instant),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.70f
                                    )
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                            )
                        }
                        items(
                            items = transactions,
                            key = { transaction -> transaction.id }
                        ) { transaction ->
                            TransactionItemView(
                                transaction = transaction,
                                amountSettings = amountSettings
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemView(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    amountSettings: AmountSettings
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val density = LocalDensity.current

    val animateSurfaceColor by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.surfaceContainerLowest else MaterialTheme.colorScheme.background,
        label = "surfaceColorAnimation"
    )

    var noteStartPadding by remember {
        mutableStateOf(0.dp)
    }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val dragState = remember {
        val actionOffset = with(density) { 100.dp.toPx() }
        AnchoredDraggableState(
            initialValue = SwipeToReveal.Resting,
            anchors = DraggableAnchors {
                SwipeToReveal.Edit at -actionOffset
                SwipeToReveal.Resting at 0f
                SwipeToReveal.Delete at actionOffset
            },
            positionalThreshold = { distance ->
               distance * 0.5f
            },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    transaction.note?.let {
                        if (it.isNotEmpty()) {
                            isExpanded = !isExpanded
                        }
                    }
                }
                .anchoredDraggable(
                    state = dragState,
                    orientation = androidx.compose.foundation.gestures.Orientation.Horizontal,
                )
                .offset {
                    IntOffset(
                        x = dragState.requireOffset().roundToInt(),
                        y = 0
                    )
                },
            shape = RoundedCornerShape(16.dp),
            color = animateSurfaceColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier.animateContentSize()
            ) {
                val elementPadding = 4.dp
                val horizontalPadding = 8.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(elementPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
                ) {
                    val formattedAmount = AmountFormatter.formatUserInput(
                        amount = transaction.amount,
                        amountSettings = amountSettings,
                        enableTwoDecimal = true
                    )
                    val currencySymbol = amountSettings.currency.symbol

                    TransactionIcon(
                        transaction = transaction,
                        modifier = Modifier
                            .onSizeChanged {
                                noteStartPadding = with(density) {
                                    (it.width + elementPadding.roundToPx() + horizontalPadding.roundToPx()).toDp()
                                }
                            }
                    )

                    TransactionInfo(transaction = transaction)

                    Text(
                        text = when (transaction.transactionType) {
                            TransactionType.INCOME -> "$currencySymbol$formattedAmount"
                            else -> when (amountSettings.expensesFormat) {
                                ExpensesFormatUi.MINUS -> "-$currencySymbol$formattedAmount"
                                ExpensesFormatUi.BRACKETS -> "($currencySymbol$formattedAmount)"
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        color = when (transaction.transactionType) {
                            TransactionType.INCOME -> SpendLessGreen
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }

                if (isExpanded) {
                    transaction.note?.let { note ->
                        Text(
                            modifier = Modifier
                                .padding(
                                    start = noteStartPadding,
                                    end = 4.dp,
                                    bottom = 10.dp,
                                    top = 2.dp
                                ),
                            text = note,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Read Action
            AnimatedVisibility(
                visible = dragState.currentValue == SwipeToReveal.Delete,
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Read",
                        tint = SpendLessBlack
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(
                visible = dragState.currentValue == SwipeToReveal.Edit,
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Read",
                        tint = SpendLessBlack
                    )
                }
            }
        }
    }
}

enum class SwipeToReveal {
    Edit,
    Resting,
    Delete
}