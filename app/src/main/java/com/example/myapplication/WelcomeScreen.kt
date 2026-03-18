package com.example.myapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = EaseInOut),
        label = "alpha"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 700, delayMillis = 400, easing = EaseInOut),
        label = "buttonAlpha"
    )

    LaunchedEffect(Unit) { startAnimation = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.splashscreen),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Temukan aroma yang\nmencerminkan dirimu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Koleksi parfum eksklusif pilihan untuk setiap momen",
                fontSize = 13.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // LOGIN button (solid)
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .alpha(buttonAlpha),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkButton)
            ) {
                Text(
                    text = "LOGIN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = Gold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SIGN UP button (outline)
            OutlinedButton(
                onClick = onNavigateToSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .alpha(buttonAlpha),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Gold),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Gold
                )
            ) {
                Text(
                    text = "SIGN UP",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = GoldDark
                )
            }
        }
    }
}
