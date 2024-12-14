package com.blueland.rating_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 별점 표시 바를 생성하는 Composable 함수입니다.
 * 주어진 파라미터에 따라 다양한 모양과 스타일로 별점 평가 바를 표시할 수 있습니다.
 *
 * This composable function creates a rating bar to display a rating with various shapes and styles based on the given parameters.
 *
 * @param modifier 이 RatingBar에 적용할 Modifier (기본값: Modifier)
 * @param rating 현재 별점 (0.0에서 `count` 사이의 값)
 * @param onRatingChanged 별점이 변경될 때 호출되는 콜백 함수
 * @param count 총 별 개수 (기본값: 5)
 * @param stepSize 별점 변경 시의 단계 크기 (기본값: 0.1, 최대값: 1.0)
 * @param size 각 별의 크기 (기본값: 28.dp)
 * @param spacing 별들 사이의 간격 (기본값: 8.dp)
 * @param isIndicator 읽기 전용으로 설정할지 여부 (기본값: false)
 * @param isEnabled RatingBar가 활성화된 상태인지 여부 (기본값: true)
 * @param shapeType 그릴 도형의 종류 (기본값: 별)
 * @param selectColor 채워진 부분의 색상 (기본값: 노란색)
 * @param unselectColor 채워지지 않은 부분의 색상 (기본값: 회색)
 * @param borderColor 테두리 색상 (기본값: 검은색)
 * @param borderWidth 테두리 두께 (기본값: 0.dp)
 *
 * @param modifier Modifier to be applied to the RatingBar (Default: Modifier)
 * @param rating The current rating value (between 0.0 and `count`)
 * @param onRatingChanged Callback triggered when the rating changes
 * @param count Total number of stars (Default: 5)
 * @param stepSize The step size for rating changes (Default: 0.1, Maximum: 1.0)
 * @param size Size of each star (Default: 28.dp)
 * @param spacing Spacing between stars (Default: 8.dp)
 * @param isIndicator Whether the rating bar is read-only (Default: false)
 * @param isEnabled Whether the rating bar is enabled (Default: true)
 * @param shapeType The shape of the rating icons (Default: Star)
 * @param selectColor Color of the filled portion of the stars (Default: Yellow)
 * @param unselectColor Color of the unfilled portion of the stars (Default: Gray)
 * @param borderColor The color of the border (Default: Black)
 * @param borderWidth The width of the border (Default: 0.dp)
 *
 * 제한 사항:
 * - `stepSize`는 0보다 커야 하며, 최대값은 1.0입니다. 이 범위를 벗어나면 에러가 발생합니다.
 */
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double,
    onRatingChanged: (Double) -> Unit,
    count: Int = 5,
    stepSize: Double = 0.1,
    size: Dp = 28.dp,
    spacing: Dp = 8.dp,
    isIndicator: Boolean = false,
    isEnabled: Boolean = true,
    shapeType: ShapeType = ShapeType.Star,
    selectColor: Color = Color.Yellow,
    unselectColor: Color = Color.Gray,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 0.dp
) {
    // stepSize 검증
    require(stepSize > 0 && stepSize <= 1.0) {
        "Invalid stepSize: $stepSize. stepSize must be greater than 0 and less than or equal to 1.0"
    }

    val totalWidth = remember(size, spacing, count) {
        (size + spacing) * count - spacing
    }
    val updatedOnRatingChanged by rememberUpdatedState(onRatingChanged)

    Box(
        modifier = modifier
            .width(totalWidth)
            .pointerInput(Unit) {
                if (!isIndicator && isEnabled) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val position = event.changes.firstOrNull()?.position

                            if (position != null) {
                                // Calculate new rating based on drag position
                                val rawRating = (position.x / totalWidth.toPx()) * count
                                val clampedRating = rawRating.coerceIn(0.0f, count.toFloat())

                                // Adjust rating based on step size
                                val finalRating = ((clampedRating / stepSize).roundToInt() * stepSize * 100).toInt() / 100.0
                                updatedOnRatingChanged(finalRating)
                            }

                            event.changes.forEach { change ->
                                if (change.pressed) {
                                    change.consume()
                                }
                            }
                        }
                    }
                }
            }
            .semantics {
                contentDescription = "Rating: $rating out of $count stars"
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            for (i in 1..count) {
                val fullStars = rating.toInt()
                val fractionalPart = rating - fullStars
                val isFilled = i <= fullStars
                val isPartialFilled = i == fullStars + 1 && fractionalPart > 0

                // Calculate the fill percentage for the partial star
                val partialFillPercentage = when {
                    isPartialFilled -> fractionalPart
                    else -> 0.0
                }

                RatingShape(
                    filled = isFilled,
                    partialFillPercentage = partialFillPercentage,
                    size = size,
                    selectColor = selectColor,
                    unselectColor = unselectColor,
                    borderColor = borderColor,
                    borderWidth = borderWidth,
                    shapeType = shapeType
                )
            }
        }
    }
}
