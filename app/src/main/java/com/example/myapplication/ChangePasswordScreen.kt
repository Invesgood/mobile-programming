package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun ChangePasswordScreen(onBack: () -> Unit) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var successMsg by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Top bar ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavBar)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = "←",
                color = Gold,
                fontSize = 22.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onBack() }
            )
            Text(
                text = "GANTI PASSWORD",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ── Content ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Icon lock (Canvas-drawn)
            androidx.compose.foundation.Canvas(modifier = Modifier.size(64.dp)) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val iconColor = Color(0xFF1A1A1A)

                // Shackle (arc top)
                val shackleRect = androidx.compose.ui.geometry.Rect(
                    left = cx - size.width * 0.22f,
                    top = cy - size.height * 0.45f,
                    right = cx + size.width * 0.22f,
                    bottom = cy + size.height * 0.02f
                )
                drawArc(
                    color = iconColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = shackleRect.topLeft,
                    size = shackleRect.size,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = size.width * 0.09f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )

                // Body
                val bodyLeft = cx - size.width * 0.32f
                val bodyTop = cy + size.height * 0.02f
                val bodyRight = cx + size.width * 0.32f
                val bodyBottom = cy + size.height * 0.45f
                drawRoundRect(
                    color = iconColor,
                    topLeft = androidx.compose.ui.geometry.Offset(bodyLeft, bodyTop),
                    size = androidx.compose.ui.geometry.Size(bodyRight - bodyLeft, bodyBottom - bodyTop),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.1f)
                )

                // Keyhole circle
                drawCircle(
                    color = CreamBackground,
                    radius = size.width * 0.09f,
                    center = androidx.compose.ui.geometry.Offset(cx, cy + size.height * 0.16f)
                )
                // Keyhole stem
                drawLine(
                    color = CreamBackground,
                    start = androidx.compose.ui.geometry.Offset(cx, cy + size.height * 0.22f),
                    end = androidx.compose.ui.geometry.Offset(cx, cy + size.height * 0.35f),
                    strokeWidth = size.width * 0.07f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Input fields ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                LabeledPasswordField(
                    label = "Password Lama",
                    value = oldPassword,
                    onValueChange = {
                        oldPassword = it
                        errorMsg = ""
                        successMsg = ""
                    }
                )

                Divider()

                LabeledPasswordField(
                    label = "Password Baru",
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        errorMsg = ""
                        successMsg = ""
                    }
                )

                Divider()

                LabeledPasswordField(
                    label = "Konfirmasi Password Baru",
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMsg = ""
                        successMsg = ""
                    }
                )
            }

            // ── Error / success message ──
            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMsg,
                    color = Color(0xFFCC2B2B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (successMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = successMsg,
                    color = Color(0xFF2E7D32),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Save button ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1A1A))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        when {
                            oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() ->
                                errorMsg = "Semua field harus diisi"
                            newPassword.length < 6 ->
                                errorMsg = "Password baru minimal 6 karakter"
                            newPassword != confirmPassword ->
                                errorMsg = "Konfirmasi password tidak cocok"
                            else -> {
                                successMsg = "Password berhasil diubah!"
                                oldPassword = ""
                                newPassword = ""
                                confirmPassword = ""
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SIMPAN PASSWORD",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = Gold
                )
            }
        }
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFEEEEEE))
    )
}

@Composable
private fun LabeledPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextGray,
            letterSpacing = 0.5.sp
        )
        DarkInputField(
            value = value,
            onValueChange = onValueChange,
            placeholder = "••••••••",
            isPassword = true
        )
    }
}
