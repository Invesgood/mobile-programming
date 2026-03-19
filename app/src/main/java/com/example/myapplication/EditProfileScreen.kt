package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun EditProfileScreen(
    profile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var fullName by remember { mutableStateOf(profile.fullName) }
    var email by remember { mutableStateOf(profile.email) }
    var phone by remember { mutableStateOf(profile.phone) }
    var dateOfBirth by remember { mutableStateOf(profile.dateOfBirth) }

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
                text = "EDIT PROFILE",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ── Form ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(DarkButton),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fullName.take(1).uppercase().ifBlank { "?" },
                    color = Gold,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ubah Foto",
                color = Gold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(2.dp)
                    .background(Gold, RoundedCornerShape(2.dp))
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fields
            EditField("Full Name", fullName, { fullName = it }, "Masukkan nama lengkap")
            Spacer(modifier = Modifier.height(16.dp))
            EditField("Email", email, { email = it }, "Masukkan email", KeyboardType.Email)
            Spacer(modifier = Modifier.height(16.dp))
            EditField(
                label = "Nomor HP",
                value = phone,
                onValueChange = { newValue ->
                    val digits = newValue.filter { it.isDigit() }.take(13)
                    phone = digits.chunked(4).joinToString("-")
                },
                placeholder = "0812-3456-7890",
                keyboardType = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Tanggal Lahir", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Spacer(modifier = Modifier.height(8.dp))
                DateInputRow(value = dateOfBirth, onValueChange = { dateOfBirth = it })
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    onSave(UserProfile(
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        dateOfBirth = dateOfBirth
                    ))
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkButton)
            ) {
                Text(
                    text = "SIMPAN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = Gold
                )
            }
        }
    }
}

@Composable
private fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        Spacer(modifier = Modifier.height(8.dp))
        DarkInputField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardType = keyboardType
        )
    }
}
