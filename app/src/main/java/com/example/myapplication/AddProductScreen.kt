package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.*

private val categoryGradients = listOf(
    Color(0xFFD4C4A8) to Color(0xFF6B5030),
    Color(0xFFD4DCC8) to Color(0xFF808A68),
    Color(0xFFD0E8D8) to Color(0xFF608070),
    Color(0xFFE8C8C0) to Color(0xFFC08878),
    Color(0xFF3A2A1A) to Color(0xFF1A1008)
)

@Composable
fun AddProductScreen(
    onAdd: (PerfumeItem) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(-1) }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var errorMsg by remember { mutableStateOf("") }

    val context = LocalContext.current

    fun persistUri(uri: Uri): Uri {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: Exception) {}
        return uri
    }

    // Fallback picker (works on all API levels including emulator without Play)
    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) selectedImageUri = persistUri(uri) }

    // Modern photo picker (Android 13+ or Play Services)
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) selectedImageUri = persistUri(uri) }

    fun launchPicker() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)) {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            getContent.launch("image/*")
        }
    }

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
                text = "TAMBAH PRODUK",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ── Scrollable form ──
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFF7F7F7))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Nama Produk
            FormField(label = "Nama Parfum") {
                PlainInputField(
                    value = name,
                    onValueChange = { name = it; errorMsg = "" },
                    placeholder = "cth. Vanilla Sky, Oud Intense"
                )
            }

            // Harga
            FormField(label = "Harga Produk") {
                PlainInputField(
                    value = price,
                    onValueChange = { price = it; errorMsg = "" },
                    placeholder = "cth. 450.000"
                )
            }

            // Kategori — selector
            FormField(label = "Kategori Produk") {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEEEEEE))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { showCategoryPicker = !showCategoryPicker }
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = if (selectedCategory >= 0) scentCategories[selectedCategory].label else "Pilih Kategori",
                            fontSize = 14.sp,
                            color = if (selectedCategory >= 0) TextDark else Color(0xFFAAAAAA),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Text(
                            text = if (showCategoryPicker) "∧" else "›",
                            fontSize = 18.sp,
                            color = TextGray,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                    if (showCategoryPicker) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                        ) {
                            scentCategories.forEachIndexed { index, cat ->
                                val isSelected = index == selectedCategory
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            selectedCategory = index
                                            showCategoryPicker = false
                                            errorMsg = ""
                                        }
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cat.label,
                                        fontSize = 14.sp,
                                        color = if (isSelected) Color(0xFF1A1A1A) else TextGray,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isSelected) {
                                        Text(text = "✓", color = Gold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (index < scentCategories.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Color(0xFFF0F0F0))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Brand
            FormField(label = "Brand") {
                PlainInputField(
                    value = brand,
                    onValueChange = { brand = it; errorMsg = "" },
                    placeholder = "cth. Noblesse Private"
                )
            }

            // Deskripsi
            FormField(label = "Deskripsi Produk") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEEEEEE))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        textStyle = TextStyle(fontSize = 14.sp, color = TextDark),
                        cursorBrush = SolidColor(Gold),
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { inner ->
                            if (description.isEmpty()) {
                                Text(
                                    "cth. Aroma hangat vanilla dibalut sentuhan kayu cedar...",
                                    fontSize = 14.sp,
                                    color = Color(0xFFAAAAAA)
                                )
                            }
                            inner()
                        }
                    )
                }
            }

            // Add Photo
            FormField(label = "TAMBAH FOTO") {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEEEEEE))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            launchPicker()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Foto produk",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // overlay edit hint
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ganti", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("+", fontSize = 28.sp, color = Color(0xFFAAAAAA), fontWeight = FontWeight.Light)
                            Text("Tambah Foto", fontSize = 12.sp, color = Color(0xFFAAAAAA))
                        }
                    }
                }
            }

            // Error
            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = Color(0xFFCC2B2B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // ── Simpan button — dashed outline ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F7F7))
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .dashedBorder(color = Gold, cornerRadius = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        when {
                            name.isBlank() -> errorMsg = "Nama produk tidak boleh kosong"
                            price.isBlank() -> errorMsg = "Harga tidak boleh kosong"
                            brand.isBlank() -> errorMsg = "Brand tidak boleh kosong"
                            selectedCategory < 0 -> errorMsg = "Pilih kategori terlebih dahulu"
                            else -> {
                                val (top, bottom) = categoryGradients[selectedCategory]
                                onAdd(
                                    PerfumeItem(
                                        name = name.trim(),
                                        brand = brand.trim(),
                                        price = "Rp ${price.trim()}",
                                        category = selectedCategory,
                                        photoTop = top,
                                        photoBottom = bottom,
                                        photoUri = selectedImageUri?.toString()
                                    )
                                )
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✦", color = Gold, fontSize = 14.sp)
                    Text(
                        text = "Simpan",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }
    }
}

// ── Helper: dashed border modifier ──
fun Modifier.dashedBorder(color: Color, cornerRadius: Dp): Modifier =
    this.drawWithContent {
        drawContent()
        drawRoundRect(
            color = color,
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
            )
        )
    }

@Composable
private fun FormField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        content()
    }
}

@Composable
private fun PlainInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontSize = 14.sp, color = TextDark),
        cursorBrush = SolidColor(Gold),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEEEEE))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, fontSize = 14.sp, color = Color(0xFFAAAAAA))
                }
                inner()
            }
        }
    )
}
