package com.example.myapplication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myapplication.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

data class OnboardingPage(
    val title: String,
    val description: String,
    val iconType: Int,
    val bgTopColor: Color,
    val bgBottomColor: Color,
    val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Discover Your Scent",
        description = "Temukan ratusan koleksi parfum premium dari seluruh dunia. Dari floral, woody, hingga oriental.",
        iconType = 0,
        bgTopColor = Color(0xFFD4C5B2),
        bgBottomColor = Color(0xFF8B3A3A),
        imageRes = R.drawable.onboarding1
    ),
    OnboardingPage(
        title = "Order For Delivery",
        description = "Pesan parfum favoritmu dengan mudah, dikirim langsung ke pintu rumahmu dalam kondisi sempurna.",
        iconType = 1,
        bgTopColor = Color(0xFF3A3A3A),
        bgBottomColor = Color(0xFF1A1A1A),
        imageRes = R.drawable.onboarding2
    ),
    OnboardingPage(
        title = "Find Your Signature",
        description = "Setiap orang punya aroma khasnya sendiri. Temukan parfum yang mencerminkan kepribadianmu.",
        iconType = 2,
        bgTopColor = Color(0xFFD4DEC8),
        bgBottomColor = Color(0xFFA8B89A),
        imageRes = R.drawable.onboarding3
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }
    val page = onboardingPages[currentPage]

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Photo area (top) ──
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                if (targetState > initialState)
                    (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
                else
                    (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
            },
            label = "photo",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.57f)
        ) { idx ->
            val p = onboardingPages[idx]
            Image(
                painter = painterResource(id = p.imageRes),
                contentDescription = p.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ── Skip button (top-right over photo) ──
        if (currentPage < onboardingPages.size - 1) {
            Text(
                text = "Skip >",
                color = AccentRed,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(end = 20.dp, top = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { currentPage = onboardingPages.size - 1 }
            )
        }

        // ── White card (bottom) ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 28.dp)
                .padding(top = 28.dp, bottom = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    if (targetState > initialState)
                        (slideInHorizontally { it / 2 } + fadeIn()) togetherWith (slideOutHorizontally { -it / 2 } + fadeOut())
                    else
                        (slideInHorizontally { -it / 2 } + fadeIn()) togetherWith (slideOutHorizontally { it / 2 } + fadeOut())
                },
                label = "cardContent",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { idx ->
                val p = onboardingPages[idx]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Icon
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            when (p.iconType) {
                                0 -> drawPerfumeBottleIcon(this)
                                1 -> drawBoxIcon(this)
                                2 -> drawBulbIcon(this)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = p.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = AccentRed,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    Text(
                        text = p.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = TextDark,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dot indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = index == currentPage
                    val dotWidth by animateDpAsState(
                        targetValue = if (isSelected) 28.dp else 16.dp,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "dotWidth"
                    )
                    Box(
                        modifier = Modifier
                            .width(dotWidth)
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) AccentRed else Color(0xFFCCCCCC))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { currentPage = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action button
            Button(
                onClick = {
                    if (currentPage < onboardingPages.size - 1) currentPage++
                    else onFinished()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkButton)
            ) {
                Text(
                    text = if (currentPage < onboardingPages.size - 1) "NEXT" else "GET STARTED",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
            }
        }
    }
}

private fun drawPerfumeBottleIcon(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val color = Color(0xFF1A1A1A)

        // body
        val bL = cx - size.width * 0.28f
        val bR = cx + size.width * 0.28f
        val bT = cy - size.height * 0.15f
        val bB = cy + size.height * 0.42f
        val bodyPath = Path().apply {
            moveTo(bL + 8f, bT)
            lineTo(bR - 8f, bT)
            cubicTo(bR, bT, bR, bT + 8f, bR, bT + 8f)
            lineTo(bR, bB - 8f)
            cubicTo(bR, bB, bR - 8f, bB, bR - 8f, bB)
            lineTo(bL + 8f, bB)
            cubicTo(bL, bB, bL, bB - 8f, bL, bB - 8f)
            lineTo(bL, bT + 8f)
            cubicTo(bL, bT, bL + 8f, bT, bL + 8f, bT)
            close()
        }
        drawPath(bodyPath, color, style = Stroke(width = 3f, cap = StrokeCap.Round))

        // neck
        val nL = cx - size.width * 0.1f
        val nR = cx + size.width * 0.1f
        val nT = cy - size.height * 0.32f
        drawLine(color, Offset(nL, nT), Offset(nL, bT), 3f)
        drawLine(color, Offset(nR, nT), Offset(nR, bT), 3f)
        drawLine(color, Offset(nL, nT), Offset(nR, nT), 3f)

        // cap
        val capL = cx - size.width * 0.14f
        val capR = cx + size.width * 0.14f
        val capT = cy - size.height * 0.46f
        drawRect(color, Offset(capL, capT), androidx.compose.ui.geometry.Size(capR - capL, nT - capT + 2f), style = Stroke(3f))

        // spray nozzle
        drawLine(color, Offset(nR, cy - size.height * 0.38f), Offset(cx + size.width * 0.38f, cy - size.height * 0.38f), 3f)
        drawCircle(color, radius = 4f, center = Offset(cx + size.width * 0.38f, cy - size.height * 0.38f))
    }
}

private fun drawBoxIcon(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val color = Color(0xFF1A1A1A)
        val s = size.width * 0.35f

        // Main box body
        drawRect(color, Offset(cx - s, cy - s * 0.5f), androidx.compose.ui.geometry.Size(s * 2f, s * 1.5f), style = Stroke(3f, cap = StrokeCap.Round))

        // Box lid left flap
        val lidPath = Path().apply {
            moveTo(cx - s, cy - s * 0.5f)
            lineTo(cx - s + s * 0.3f, cy - s * 1.1f)
            lineTo(cx, cy - s * 0.9f)
            lineTo(cx, cy - s * 0.5f)
        }
        drawPath(lidPath, color, style = Stroke(3f, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Box lid right flap
        val lidRPath = Path().apply {
            moveTo(cx + s, cy - s * 0.5f)
            lineTo(cx + s - s * 0.3f, cy - s * 1.1f)
            lineTo(cx, cy - s * 0.9f)
            lineTo(cx, cy - s * 0.5f)
        }
        drawPath(lidRPath, color, style = Stroke(3f, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Center line on box
        drawLine(color, Offset(cx, cy - s * 0.5f), Offset(cx, cy + s), 2f)
    }
}

private fun drawBulbIcon(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.28f
        val yellow = Color(0xFFFFD700)
        val yellowDark = Color(0xFFE6A800)

        // glow
        drawCircle(yellow.copy(alpha = 0.15f), radius = r * 1.5f, center = Offset(cx, cy - r * 0.1f))

        // bulb circle
        drawCircle(yellow, radius = r, center = Offset(cx, cy - r * 0.1f))
        drawCircle(yellowDark, radius = r, center = Offset(cx, cy - r * 0.1f), style = Stroke(2.5f))

        // base
        val baseT = cy + r * 0.85f
        val baseW = r * 0.65f
        drawLine(yellowDark, Offset(cx - baseW, baseT), Offset(cx + baseW, baseT), 3f)
        drawLine(yellowDark, Offset(cx - baseW * 0.8f, baseT + 7f), Offset(cx + baseW * 0.8f, baseT + 7f), 3f)
        drawLine(yellowDark, Offset(cx - baseW * 0.5f, baseT + 14f), Offset(cx + baseW * 0.5f, baseT + 14f), 3f)

        // filament lines
        drawLine(yellowDark.copy(0.6f), Offset(cx - r * 0.15f, cy - r * 0.15f), Offset(cx - r * 0.15f, cy + r * 0.5f), 2f)
        drawLine(yellowDark.copy(0.6f), Offset(cx + r * 0.15f, cy - r * 0.15f), Offset(cx + r * 0.15f, cy + r * 0.5f), 2f)
        drawLine(yellowDark.copy(0.6f), Offset(cx - r * 0.15f, cy + r * 0.3f), Offset(cx + r * 0.15f, cy + r * 0.3f), 2f)

        // rays
        val rayCount = 8
        repeat(rayCount) { i ->
            val angle = (i * 2 * Math.PI / rayCount).toFloat()
            val rStart = r * 1.15f
            val rEnd = r * 1.5f
            drawLine(
                yellow.copy(0.5f),
                Offset(cx + rStart * cos(angle), cy - r * 0.1f + rStart * sin(angle)),
                Offset(cx + rEnd * cos(angle), cy - r * 0.1f + rEnd * sin(angle)),
                2f, cap = StrokeCap.Round
            )
        }
    }
}
