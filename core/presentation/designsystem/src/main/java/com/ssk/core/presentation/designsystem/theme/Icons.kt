package com.ssk.core.presentation.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.core.presentation.designsystem.AppColorStyles
import com.ssk.core.presentation.designsystem.R


val RegisterIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_wallet)

val CheckIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.check)

val CrossIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.cross)

val ArrowForward: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.arrow_right)

val ArrowBack: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.arrow_left)

val DownloadIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.download)

val SettingsIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.settings)

val ExitIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_exit)

val PlusIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_add)

val NoteIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_note)

val TrendingDownIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_expense)

val TrendingUpIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_income)

val TickIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_tick)

val LockIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.lock)

val ExpandedIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.arrow_drop_up)

val CollapsedIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.arrow_drop_down)

@Composable
fun ExpenseIconBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppColorStyles.IconBackground(isIncome = false)),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun RepeatIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String(Character.toChars(0x1F504)),
            fontSize = fontSize
        )
    }
}

@Composable
fun IncomeIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp
) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(AppColorStyles.IconBackground(isIncome = true)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = String(Character.toChars(0x1F4B0)),
            fontSize = fontSize
        )
    }
}

@Composable
fun HomeIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F3E0)),
            fontSize = fontSize
        )
    }
}

@Composable
fun FoodIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F355)),
            fontSize = fontSize
        )
    }
}

@Composable
fun EntertainmentIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F4BB)),
            fontSize = fontSize
        )
    }
}

@Composable
fun ClothingIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F454)),
            fontSize = fontSize
        )
    }
}

@Composable
fun HealthIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x2764)),
            fontSize = fontSize
        )
    }
}

@Composable
fun PersonalCareIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F6C1)),
            fontSize = fontSize
        )
    }
}

@Composable
fun TransportationIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.offset(y = (-4).dp),
            text = String(Character.toChars(0x1F697)),
            fontSize = fontSize
        )
    }
}

@Composable
fun EducationIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F393)),
            fontSize = fontSize
        )
    }
}

@Composable
fun SavingIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x1F48E)),
            fontSize = fontSize
        )
    }
}

@Composable
fun OtherIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    ExpenseIconBackground(
        modifier = modifier
    ) {
        Text(
            text = String(Character.toChars(0x2699)),
            fontSize = fontSize
        )
    }
}

@Composable
fun MoneyIcon(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(
        text = String(Character.toChars(0x1F4B8)),
        fontSize = fontSize
    )
}