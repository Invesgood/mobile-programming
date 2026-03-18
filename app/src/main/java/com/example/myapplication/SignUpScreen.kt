package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SignUpScreen(
    db: DatabaseHelper,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Photo area (top ~26%) ──
        Image(
            painter = painterResource(id = R.drawable.loginsignup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.26f)
        )

        // ── Cream card ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 28.dp, bottom = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // NEW ACCOUNT title
            Text(
                text = "NEW ACCOUNT",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                color = TextDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Gold underline (left-aligned like Figma)
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(2.5.dp)
                    .background(Gold, RoundedCornerShape(2.dp))
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name
            FormField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "example@exmaple.com"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            FormField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••••",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            FormField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "example@exmaple.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mobile Number
            FormField(
                label = "Mobile Number",
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                placeholder = "+62",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth
            FormField(
                label = "Date of birth",
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                placeholder = "DD / MM / YY",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = androidx.compose.ui.graphics.Color(0xFFCC2B2B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SIGN UP button
            Button(
                onClick = {
                    when {
                        fullName.isBlank() -> errorMsg = "Nama lengkap tidak boleh kosong"
                        email.isBlank() -> errorMsg = "Email tidak boleh kosong"
                        mobileNumber.isBlank() -> errorMsg = "Nomor HP tidak boleh kosong"
                        password.length < 6 -> errorMsg = "Password minimal 6 karakter"
                        else -> {
                            when {
                                db.emailExists(email.trim()) ->
                                    errorMsg = "Email sudah terdaftar"
                                db.phoneExists(mobileNumber.trim()) ->
                                    errorMsg = "Nomor HP sudah terdaftar"
                                else -> {
                                    db.registerUser(
                                        UserData(
                                            fullName = fullName.trim(),
                                            email = email.trim(),
                                            phone = mobileNumber.trim(),
                                            password = password,
                                            dateOfBirth = dateOfBirth.trim()
                                        )
                                    )
                                    onSignUpSuccess()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkButton)
            ) {
                Text(
                    text = "SIGN UP",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = Gold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Already have account
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 13.sp,
                    color = TextGray
                )
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        DarkInputField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            isPassword = isPassword,
            passwordVisible = passwordVisible,
            onTogglePassword = onTogglePassword,
            keyboardType = keyboardType
        )
    }
}
