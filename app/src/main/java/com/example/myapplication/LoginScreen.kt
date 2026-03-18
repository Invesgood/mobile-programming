package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.myapplication.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoginScreen(
    db: DatabaseHelper,
    onLoginSuccess: (UserData) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Photo area (top ~40%) ──
        Image(
            painter = painterResource(id = R.drawable.loginsignup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
        )

        // ── Cream card ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(CreamBackground)
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 32.dp)
                .navigationBarsPadding()
        ) {
            // Welcome
            Text(
                text = "Welcome",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Masuk dan temukan aroma yang mencerminkan dirimu.",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gold underline
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(2.5.dp)
                    .background(Gold, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Email field
            Text(
                text = "Email atau No Hp",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            DarkInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "example@exmaple.com"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password field
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            DarkInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••••",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            // Forgot password
            Text(
                text = "Forgot password?",
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = Gold,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 10.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Error message
            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMsg,
                    color = androidx.compose.ui.graphics.Color(0xFFCC2B2B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // LOGIN button
            Button(
                onClick = {
                    when {
                        email.isBlank() -> errorMsg = "Email atau No HP tidak boleh kosong"
                        password.isBlank() -> errorMsg = "Password tidak boleh kosong"
                        else -> {
                            val user = db.findUser(email.trim())
                            when {
                                user == null -> errorMsg = "Akun tidak ditemukan, silakan daftar dulu"
                                user.password != password -> errorMsg = "Password salah"
                                else -> onLoginSuccess(user)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
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

            Spacer(modifier = Modifier.height(20.dp))

            // Sign up link
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 13.sp,
                    color = TextGray
                )
                Text(
                    text = "Sign Up",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigateToSignUp() }
                )
            }
        }
    }
}

@Composable
fun DarkInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val visualTransformation = if (isPassword && !passwordVisible)
        PasswordVisualTransformation() else VisualTransformation.None

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(DarkButton),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 15.sp
            ),
            cursorBrush = SolidColor(Gold),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(end = if (isPassword) 40.dp else 0.dp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xFF888888),
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        )

        // Eye icon for password
        if (isPassword && onTogglePassword != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onTogglePassword() },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(24.dp)) {
                    if (passwordVisible) drawEyeIcon(this) else drawEyeSlashIcon(this)
                }
            }
        }
    }
}

private fun drawEyeIcon(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val color = Gold

        // Eye outline
        val eyePath = Path().apply {
            moveTo(cx - size.width * 0.45f, cy)
            cubicTo(cx - size.width * 0.2f, cy - size.height * 0.35f, cx + size.width * 0.2f, cy - size.height * 0.35f, cx + size.width * 0.45f, cy)
            cubicTo(cx + size.width * 0.2f, cy + size.height * 0.35f, cx - size.width * 0.2f, cy + size.height * 0.35f, cx - size.width * 0.45f, cy)
            close()
        }
        drawPath(eyePath, color, style = Stroke(width = 2.5f, cap = StrokeCap.Round))
        drawCircle(color, radius = size.width * 0.14f, center = Offset(cx, cy), style = Stroke(2.5f))
        drawCircle(color, radius = size.width * 0.06f, center = Offset(cx, cy))
    }
}

private fun drawEyeSlashIcon(scope: DrawScope) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val color = Gold

        // Eye outline (same as above)
        val eyePath = Path().apply {
            moveTo(cx - size.width * 0.45f, cy)
            cubicTo(cx - size.width * 0.2f, cy - size.height * 0.35f, cx + size.width * 0.2f, cy - size.height * 0.35f, cx + size.width * 0.45f, cy)
            cubicTo(cx + size.width * 0.2f, cy + size.height * 0.35f, cx - size.width * 0.2f, cy + size.height * 0.35f, cx - size.width * 0.45f, cy)
            close()
        }
        drawPath(eyePath, color.copy(alpha = 0.5f), style = Stroke(width = 2.5f, cap = StrokeCap.Round))
        drawCircle(color.copy(0.5f), radius = size.width * 0.13f, center = Offset(cx, cy), style = Stroke(2f))

        // Slash line
        drawLine(
            color,
            start = Offset(cx - size.width * 0.35f, cy + size.height * 0.32f),
            end = Offset(cx + size.width * 0.35f, cy - size.height * 0.32f),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
    }
}
