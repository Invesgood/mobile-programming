package com.example.myapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut),
        label = "alpha"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.75f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 600, easing = EaseInOut),
        label = "textAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ornate crest
            Box(
                modifier = Modifier
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
                    .size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawNoblesseCrest(this)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // NOBLESSE
            Text(
                text = "NOBLESSE",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.sp,
                color = Gold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // — PARFUM —
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(1.dp)
                        .background(Gold)
                )
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(1.dp)
                        .background(Gold)
                        .padding(horizontal = 2.dp)
                )
                Text(
                    text = "  PARFUM  ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 5.sp,
                    color = Gold
                )
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(1.dp)
                        .background(Gold)
                )
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(1.dp)
                        .background(Gold)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // EST. 1928
            Text(
                text = "EST. 1928",
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 3.sp,
                color = GoldDark,
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

fun drawNoblesseCrest(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val goldColor = Color(0xFFC9A84C)
        val goldDark = Color(0xFF8B6914)
        val goldLight = Color(0xFFE8C97A)

        // ── Outer decorative circle ──
        drawCircle(
            color = goldColor.copy(alpha = 0.25f),
            radius = size.width * 0.44f,
            center = Offset(cx, cy),
            style = Stroke(width = 1.5f)
        )
        drawCircle(
            color = goldColor.copy(alpha = 0.12f),
            radius = size.width * 0.46f,
            center = Offset(cx, cy),
            style = Stroke(width = 0.8f)
        )

        // ── Laurel wreaths (left & right) ──
        drawLaurelWreath(cx, cy, size.width, goldColor, true)
        drawLaurelWreath(cx, cy, size.width, goldColor, false)

        // ── Crown on top ──
        drawCrown(cx, cy - size.height * 0.30f, size.width * 0.18f, goldColor, goldLight)

        // ── Fleur-de-lis top center ──
        drawFleurDeLis(cx, cy - size.height * 0.16f, size.width * 0.055f, goldColor)

        // ── Fleur-de-lis left & right ──
        drawFleurDeLis(cx - size.width * 0.22f, cy - size.height * 0.04f, size.width * 0.04f, goldColor.copy(0.8f))
        drawFleurDeLis(cx + size.width * 0.22f, cy - size.height * 0.04f, size.width * 0.04f, goldColor.copy(0.8f))

        // ── NP Monogram letters ──
        drawMonogramN(cx - size.width * 0.08f, cy, size.width * 0.13f, goldColor, goldDark)
        drawMonogramP(cx + size.width * 0.06f, cy, size.width * 0.12f, goldColor, goldDark)

        // ── Decorative bottom line ──
        val lineY = cy + size.height * 0.22f
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, goldColor, goldColor, Color.Transparent),
                startX = cx - size.width * 0.38f,
                endX = cx + size.width * 0.38f
            ),
            start = Offset(cx - size.width * 0.38f, lineY),
            end = Offset(cx + size.width * 0.38f, lineY),
            strokeWidth = 1.5f
        )
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, goldColor.copy(0.5f), goldColor.copy(0.5f), Color.Transparent),
                startX = cx - size.width * 0.30f,
                endX = cx + size.width * 0.30f
            ),
            start = Offset(cx - size.width * 0.30f, lineY + 4f),
            end = Offset(cx + size.width * 0.30f, lineY + 4f),
            strokeWidth = 0.8f
        )

        // ── Small diamond ornament bottom center ──
        val ds = size.width * 0.025f
        val diamondPath = Path().apply {
            moveTo(cx, lineY - ds * 1.4f)
            lineTo(cx + ds, lineY)
            lineTo(cx, lineY + ds * 1.4f)
            lineTo(cx - ds, lineY)
            close()
        }
        drawPath(diamondPath, goldColor.copy(0.8f))
    }
}

fun DrawScope.drawLaurelWreath(cx: Float, cy: Float, w: Float, color: Color, isLeft: Boolean) {
    val sign = if (isLeft) -1f else 1f
    val leafCount = 7
    val startAngle = if (isLeft) (150.0) else (30.0)
    val endAngle = if (isLeft) (260.0) else (280.0 + 360.0 - 260.0)

    repeat(leafCount) { i ->
        val t = i.toFloat() / (leafCount - 1)
        val angle = Math.toRadians(if (isLeft) startAngle + t * (endAngle - startAngle) else (startAngle - t * 110.0))
        val r = w * 0.38f
        val leafX = cx + r * cos(angle).toFloat()
        val leafY = cy + r * sin(angle).toFloat()

        val leafAngle = angle + Math.PI / 2 * sign
        val lw = w * 0.038f
        val lh = w * 0.07f

        val leafPath = Path().apply {
            moveTo(leafX.toFloat(), leafY.toFloat())
            cubicTo(
                (leafX + lw * cos(leafAngle - 0.6)).toFloat(),
                (leafY + lw * sin(leafAngle - 0.6)).toFloat(),
                (leafX + lh * cos(leafAngle) + lw * cos(leafAngle - 0.6)).toFloat(),
                (leafY + lh * sin(leafAngle) + lw * sin(leafAngle - 0.6)).toFloat(),
                (leafX + lh * cos(leafAngle)).toFloat(),
                (leafY + lh * sin(leafAngle)).toFloat()
            )
            cubicTo(
                (leafX + lh * cos(leafAngle) - lw * cos(leafAngle - 0.6)).toFloat(),
                (leafY + lh * sin(leafAngle) - lw * sin(leafAngle - 0.6)).toFloat(),
                (leafX - lw * cos(leafAngle - 0.6)).toFloat(),
                (leafY - lw * sin(leafAngle - 0.6)).toFloat(),
                leafX.toFloat(), leafY.toFloat()
            )
            close()
        }
        drawPath(leafPath, color.copy(alpha = 0.7f + i * 0.02f))
        drawPath(leafPath, color.copy(alpha = 0.4f), style = Stroke(0.8f))
    }
}

fun DrawScope.drawCrown(cx: Float, cy: Float, size: Float, color: Color, colorLight: Color) {
    val path = Path().apply {
        // base
        moveTo(cx - size, cy + size * 0.5f)
        lineTo(cx + size, cy + size * 0.5f)
        lineTo(cx + size, cy)
        // right spike
        lineTo(cx + size * 0.65f, cy - size * 0.5f)
        lineTo(cx + size * 0.42f, cy - size * 0.1f)
        // center spike
        lineTo(cx + size * 0.2f, cy - size * 0.85f)
        lineTo(cx, cy - size * 0.55f)
        lineTo(cx - size * 0.2f, cy - size * 0.85f)
        lineTo(cx - size * 0.42f, cy - size * 0.1f)
        // left spike
        lineTo(cx - size * 0.65f, cy - size * 0.5f)
        lineTo(cx - size, cy)
        close()
    }
    drawPath(
        path, brush = Brush.verticalGradient(
            listOf(colorLight, color, Color(0xFF8B6914)),
            startY = cy - size,
            endY = cy + size * 0.5f
        )
    )
    drawPath(path, color.copy(0.9f), style = Stroke(1.2f))

    // crown jewels dots
    listOf(-0.5f, 0f, 0.5f).forEach { dx ->
        drawCircle(colorLight, radius = size * 0.06f, center = Offset(cx + dx * size, cy + size * 0.2f))
    }
}

fun DrawScope.drawFleurDeLis(cx: Float, cy: Float, size: Float, color: Color) {
    // center petal up
    val path = Path().apply {
        moveTo(cx, cy - size * 2f)
        cubicTo(cx + size, cy - size, cx + size * 0.8f, cy, cx, cy + size * 0.3f)
        cubicTo(cx - size * 0.8f, cy, cx - size, cy - size, cx, cy - size * 2f)
        close()
    }
    drawPath(path, color.copy(0.85f))

    // left petal
    val leftPath = Path().apply {
        moveTo(cx, cy)
        cubicTo(cx - size * 0.5f, cy - size * 0.5f, cx - size * 1.8f, cy - size * 0.3f, cx - size * 1.5f, cy + size * 0.5f)
        cubicTo(cx - size * 1.2f, cy + size, cx - size * 0.3f, cy + size * 0.3f, cx, cy)
        close()
    }
    drawPath(leftPath, color.copy(0.75f))

    // right petal
    val rightPath = Path().apply {
        moveTo(cx, cy)
        cubicTo(cx + size * 0.5f, cy - size * 0.5f, cx + size * 1.8f, cy - size * 0.3f, cx + size * 1.5f, cy + size * 0.5f)
        cubicTo(cx + size * 1.2f, cy + size, cx + size * 0.3f, cy + size * 0.3f, cx, cy)
        close()
    }
    drawPath(rightPath, color.copy(0.75f))

    // base bar
    drawLine(color.copy(0.8f), Offset(cx - size * 1.2f, cy + size * 0.6f), Offset(cx + size * 1.2f, cy + size * 0.6f), 2f)
}

fun DrawScope.drawMonogramN(cx: Float, cy: Float, size: Float, color: Color, colorDark: Color) {
    val top = cy - size * 0.6f
    val bottom = cy + size * 0.6f
    val left = cx - size * 0.35f
    val right = cx + size * 0.35f
    val stroke = size * 0.18f

    val path = Path().apply {
        moveTo(left, bottom)
        lineTo(left, top)
        lineTo(right, bottom)
        lineTo(right, top)
    }
    drawPath(
        path,
        brush = Brush.verticalGradient(listOf(color, colorDark), startY = top, endY = bottom),
        style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

fun DrawScope.drawMonogramP(cx: Float, cy: Float, size: Float, color: Color, colorDark: Color) {
    val top = cy - size * 0.6f
    val bottom = cy + size * 0.6f
    val left = cx - size * 0.2f
    val right = cx + size * 0.3f
    val mid = cy - size * 0.05f
    val stroke = size * 0.18f

    // vertical stroke
    drawLine(
        brush = Brush.verticalGradient(listOf(color, colorDark), startY = top, endY = bottom),
        start = Offset(left, top),
        end = Offset(left, bottom),
        strokeWidth = stroke,
        cap = StrokeCap.Round
    )
    // bowl
    val bowlPath = Path().apply {
        moveTo(left, top)
        cubicTo(right * 1.1f, top, right * 1.1f, mid, left, mid)
    }
    drawPath(
        bowlPath,
        brush = Brush.horizontalGradient(listOf(color, colorDark), startX = left, endX = right),
        style = Stroke(width = stroke, cap = StrokeCap.Round)
    )
}
