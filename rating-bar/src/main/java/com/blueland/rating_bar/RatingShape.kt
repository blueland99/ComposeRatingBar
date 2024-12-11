package com.blueland.rating_bar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 단일 도형을 그리는 Composable 함수입니다.
 * 주어진 파라미터에 따라 별, 원, 사각형, 하트 등을 그릴 수 있습니다.
 *
 * This is a composable function to draw a single shape. It can draw different shapes like a star, circle, square, or heart based on the given parameters.
 *
 * @param modifier 이 컴포저블에 적용할 Modifier (기본값: Modifier)
 * @param filled 도형이 완전히 채워졌는지 여부
 * @param partialFillPercentage 도형의 채워진 비율 (부분적으로 채워진 도형에 사용)
 * @param size 도형의 크기 (기본값: 24.dp)
 * @param selectColor 채워진 부분의 색상
 * @param unselectColor 채워지지 않은 부분의 색상
 * @param shapeType 그릴 도형의 종류 (ShapeType enum에 정의된 값)
 * @param borderColor 테두리 색상 (기본값: 회색)
 * @param borderWidth 테두리 두께 (기본값: 2.dp)
 *
 * @param modifier Modifier to be applied to the composable (Default: Modifier)
 * @param filled Whether the shape is completely filled or not
 * @param partialFillPercentage The percentage of the shape that is filled (Used for partially filled shapes)
 * @param size The size of the shape (Default: 24.dp)
 * @param selectColor Color for the filled portion of the shape
 * @param unselectColor Color for the unfilled portion of the shape
 * @param shapeType The type of shape to be drawn (defined in ShapeType enum)
 * @param borderColor The color of the border (Default value: gray)
 * @param borderWidth The width of the border (Default value: 2.dp)
 */
@Composable
fun RatingShape(
    modifier: Modifier = Modifier,
    filled: Boolean,
    partialFillPercentage: Double,
    size: Dp = 24.dp,
    selectColor: Color,
    unselectColor: Color,
    shapeType: ShapeType,
    borderColor: Color = Color.Gray,
    borderWidth: Dp = 2.dp
) {
    val density = LocalDensity.current.density
    val borderWidthPx = if (borderWidth > 0.dp) borderWidth.value * density else 0f

    val adjustedSize = size - (borderWidth * 2)
    val side = adjustedSize.value * density
    val radius = (adjustedSize.value * density) / 2

    Canvas(modifier = modifier.size(size)) {
        when (shapeType) {
            ShapeType.Star -> {
                val midX = size.toPx() / 2
                val midY = size.toPx() / 2
                val outerRadius = side / 2
                val innerRadius = outerRadius * 0.5f
                val angleOffset = -18f
                val path = Path().apply {
                    for (i in 0..4) {
                        val outerAngle = Math.toRadians(angleOffset + i * 72.0)
                        val innerAngle = Math.toRadians(angleOffset + i * 72.0 + 36.0)

                        if (i == 0) {
                            moveTo(
                                midX + outerRadius * kotlin.math.cos(outerAngle).toFloat(),
                                midY + outerRadius * kotlin.math.sin(outerAngle).toFloat()
                            )
                        } else {
                            lineTo(
                                midX + outerRadius * kotlin.math.cos(outerAngle).toFloat(),
                                midY + outerRadius * kotlin.math.sin(outerAngle).toFloat()
                            )
                        }

                        lineTo(
                            midX + innerRadius * kotlin.math.cos(innerAngle).toFloat(),
                            midY + innerRadius * kotlin.math.sin(innerAngle).toFloat()
                        )
                    }
                    close()
                }

                if (borderWidthPx > 0f) {
                    drawPath(path, color = borderColor, style = androidx.compose.ui.graphics.drawscope.Stroke(borderWidthPx))
                }

                drawShapePath(path, filled, partialFillPercentage, adjustedSize, selectColor, unselectColor)
            }

            ShapeType.Circle -> {
                if (borderWidthPx > 0f) {
                    drawCircle(color = borderColor, radius = radius, style = androidx.compose.ui.graphics.drawscope.Stroke(borderWidthPx))
                }

                if (filled) {
                    drawCircle(selectColor, radius)
                } else if (partialFillPercentage > 0) {
                    val clipAmount = (partialFillPercentage * adjustedSize.toPx()).toFloat()
                    clipRect(right = clipAmount) {
                        drawCircle(selectColor, radius)
                    }
                    clipRect(left = clipAmount) {
                        drawCircle(unselectColor, radius)
                    }
                } else {
                    drawCircle(unselectColor, radius)
                }
            }

            ShapeType.Square -> {
                if (borderWidthPx > 0f) {
                    drawRect(
                        color = Color.Transparent,
                        size = androidx.compose.ui.geometry.Size(side, side),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(0f)
                    )
                }

                if (filled) {
                    drawRect(selectColor)
                } else if (partialFillPercentage > 0) {
                    val clipAmount = (partialFillPercentage * side).toFloat()

                    clipRect(right = clipAmount) {
                        drawRect(selectColor)
                    }

                    clipRect(left = clipAmount) {
                        drawRect(unselectColor)
                    }
                } else {
                    drawRect(unselectColor)
                }
            }

            ShapeType.Heart -> {
                val path = Path().apply {
                    val width = adjustedSize.toPx()
                    val height = adjustedSize.toPx()

                    moveTo(width / 2, height * 0.9f)

                    cubicTo(
                        -width * 0.5f, height * 0.4f,
                        width * 0.25f, -height * 0.3f,
                        width / 2, height * 0.3f
                    )

                    cubicTo(
                        width * 0.75f, -height * 0.3f,
                        width * 1.5f, height * 0.4f,
                        width / 2, height * 0.9f
                    )

                    close()
                }

                if (borderWidthPx > 0f) {
                    drawPath(path, color = borderColor, style = androidx.compose.ui.graphics.drawscope.Stroke(borderWidthPx))
                }

                if (filled) {
                    drawPath(path, selectColor)
                } else if (partialFillPercentage > 0) {
                    val totalWidth = adjustedSize.toPx()

                    val clipAmount = (partialFillPercentage * totalWidth).toFloat()

                    clipRect(right = clipAmount) {
                        drawPath(path, selectColor)
                    }

                    clipRect(left = clipAmount) {
                        drawPath(path, unselectColor)
                    }
                } else {
                    drawPath(path, unselectColor)
                }
            }
        }
    }
}

/**
 * 도형을 그리는 함수입니다. 채워진 상태에 따라 도형을 그리거나, 부분적으로 채워진 상태를 그립니다.
 *
 * This function draws the shape based on the fill status. It will either fill the entire shape or fill a partial portion of it.
 *
 * @param path 그릴 도형의 경로 (Path)
 * @param filled 도형이 완전히 채워졌는지 여부
 * @param partialFillPercentage 도형의 채워진 비율 (부분적으로 채워진 도형에 사용)
 * @param size 도형의 크기
 * @param selectColor 채워진 부분의 색상
 * @param unselectColor 채워지지 않은 부분의 색상
 *
 * @param path The path of the shape to be drawn (Path)
 * @param filled Whether the shape is completely filled
 * @param partialFillPercentage The percentage of the shape that is filled (Used for partially filled shapes)
 * @param size The size of the shape
 * @param selectColor Color for the filled portion of the shape
 * @param unselectColor Color for the unfilled portion of the shape
 */
private fun DrawScope.drawShapePath(
    path: Path,
    filled: Boolean,
    partialFillPercentage: Double,
    size: Dp,
    selectColor: Color,
    unselectColor: Color
) {
    if (filled) {
        drawPath(path, selectColor)
    } else if (partialFillPercentage > 0) {
        val clipAmount = (partialFillPercentage * size.toPx()).toFloat()
        clipRect(right = clipAmount) {
            drawPath(path, selectColor)
        }
        clipRect(left = clipAmount) {
            drawPath(path, unselectColor)
        }
    } else {
        drawPath(path, unselectColor)
    }
}