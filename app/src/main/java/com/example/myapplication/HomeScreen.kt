package com.example.myapplication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.PI

// ── Data ──────────────────────────────────────────

data class ScentCategory(
    val iconType: Int,   // 0=tree 1=flower 2=smiley 3=pineapple 4=reddot
    val label: String
)

data class PerfumeItem(
    val name: String,
    val brand: String,
    val price: String,
    val category: Int,
    val photoTop: Color,
    val photoBottom: Color,
    val photoUri: String? = null,
    val id: Int = 0
)

val scentCategories = listOf(
    ScentCategory(0, "Woody"),
    ScentCategory(1, "Floral"),
    ScentCategory(2, "Fresh"),
    ScentCategory(3, "Fruity"),
    ScentCategory(4, "Warm")
)

val perfumeList = listOf(
    // Woody
    PerfumeItem("Vanilla Sky", "Tukan", "Rp 450.000", 0, Color(0xFFD4C4A8), Color(0xFF6B5030)),
    PerfumeItem("White Forest", "Björk & Berries", "Rp 620.000", 0, Color(0xFFDED8CC), Color(0xFF706050)),
    // Floral
    PerfumeItem("Casnia", "Atelier de Fleurs Chloé", "Rp 580.000", 1, Color(0xFFC8B8A0), Color(0xFF7A6040)),
    PerfumeItem("Flower Muse", "Flora Danica", "Rp 680.000", 1, Color(0xFFD4DCC8), Color(0xFF808A68)),
    // Fresh
    PerfumeItem("September", "Björk & Berries", "Rp 520.000", 2, Color(0xFFF0E8D8), Color(0xFF988060)),
    PerfumeItem("Fjällsjö", "Björk & Berries", "Rp 490.000", 2, Color(0xFFD0E8D8), Color(0xFF608070)),
    // Fruity
    PerfumeItem("Pink Pepper", "Chloé", "Rp 720.000", 3, Color(0xFFE8C8C0), Color(0xFFC08878)),
    PerfumeItem("Botanist", "Björk & Berries", "Rp 550.000", 3, Color(0xFFE0D0C0), Color(0xFFA08060)),
    // Warm/Oriental
    PerfumeItem("Oud Intense", "Noblesse Private", "Rp 850.000", 4, Color(0xFF3A2A1A), Color(0xFF1A1008)),
    PerfumeItem("Bleu Noir", "Chanel", "Rp 1.200.000", 4, Color(0xFF2A2A3A), Color(0xFF0A0A1A))
)

// ── Screen ────────────────────────────────────────

@Composable
fun HomeScreen(
    username: String = "",
    products: List<PerfumeItem> = perfumeList,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProduct: (PerfumeItem) -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(0) }
    var prevCategory by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Dark top bar ──────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavBar)
                .statusBarsPadding()
        ) {
            // Title + icons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username.uppercase().ifBlank { "SELAMAT DATANG" },
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.5.dp, Gold, CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToAddProduct() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", color = Gold, fontSize = 22.sp, textAlign = TextAlign.Center)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.5.dp, Gold, CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToFaq() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔔", color = Gold, fontSize = 15.sp, textAlign = TextAlign.Center)
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF555555))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = username.take(1).uppercase().ifBlank { "👤" },
                            color = Gold,
                            fontSize = if (username.isNotBlank()) 16.sp else 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Category filter row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 0.dp)
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                scentCategories.forEachIndexed { index, cat ->
                    CategoryChip(
                        category = cat,
                        isSelected = index == selectedCategory,
                        onClick = {
                            prevCategory = selectedCategory
                            selectedCategory = index
                        }
                    )
                }
            }
        }

        // ── Perfume list with slide animation ─────
        AnimatedContent(
            targetState = selectedCategory,
            transitionSpec = {
                if (targetState > prevCategory)
                    (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                else
                    (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
            },
            label = "categoryContent",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CreamBackground)
        ) { category ->
            val displayed = products.filter { it.category == category }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (displayed.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Belum ada parfum di kategori ini",
                                color = TextGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(displayed) { perfume ->
                        PerfumeCard(perfume, onClick = { onNavigateToProduct(perfume) })
                    }
                }
            }
        }
    }
}

// ── Components ────────────────────────────────────

@Composable
private fun GoldCircleIcon(label: String, textSize: Int) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .border(1.5.dp, Gold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Gold,
            fontSize = textSize.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CategoryChip(
    category: ScentCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val offsetY by animateDpAsState(
        targetValue = if (isSelected) (-6).dp else 0.dp,
        label = "chipOffset"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .offset(y = offsetY)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(if (isSelected) 52.dp else 48.dp)
                .shadow(if (isSelected) 10.dp else 2.dp, CircleShape)
                .clip(CircleShape)
                .background(if (isSelected) Gold else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(28.dp)) {
                val tint = if (isSelected) Color(0xFF1A1A1A) else Color(0xFF222222)
                when (category.iconType) {
                    0 -> drawTreeIcon(this, tint)
                    1 -> drawFlowerIcon(this, tint)
                    2 -> drawSmileyIcon(this, tint)
                    3 -> drawPineappleIcon(this, tint)
                    4 -> drawSunIcon(this, tint)
                }
            }
        }
        Text(
            text = category.label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Gold else TextGray,
            textAlign = TextAlign.Center
        )
    }
}

private fun drawTreeIcon(scope: DrawScope, iconColor: Color = Color(0xFF222222)) {
    with(scope) {
        val cx = size.width / 2f
        val sw = size.width * 0.07f

        // Trunk
        drawLine(
            color = iconColor,
            start = Offset(cx, size.height * 0.58f),
            end = Offset(cx, size.height * 0.95f),
            strokeWidth = sw * 1.4f,
            cap = StrokeCap.Round
        )

        // Main branches (left & right)
        listOf(-0.28f to 0.72f, 0.28f to 0.72f).forEach { (dx, dy) ->
            drawLine(iconColor, Offset(cx, size.height * 0.58f),
                Offset(cx + dx * size.width, size.height * dy), sw, StrokeCap.Round)
        }

        // Mid branches
        listOf(-0.22f to 0.45f, 0.22f to 0.45f, -0.35f to 0.5f, 0.35f to 0.5f).forEach { (dx, dy) ->
            drawLine(iconColor, Offset(cx, size.height * 0.38f),
                Offset(cx + dx * size.width, size.height * dy), sw * 0.85f, StrokeCap.Round)
        }

        // Upper branches
        listOf(-0.18f to 0.25f, 0.18f to 0.25f, -0.28f to 0.32f, 0.28f to 0.32f).forEach { (dx, dy) ->
            drawLine(iconColor, Offset(cx, size.height * 0.18f),
                Offset(cx + dx * size.width, size.height * dy), sw * 0.7f, StrokeCap.Round)
        }

        // Spine
        drawLine(iconColor, Offset(cx, size.height * 0.95f), Offset(cx, size.height * 0.05f),
            sw, StrokeCap.Round)
    }
}

private fun drawFlowerIcon(scope: DrawScope, iconColor: Color = Color(0xFF222222)) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val petalR = size.width * 0.28f
        val petalDist = size.width * 0.22f

        repeat(8) { i ->
            val angle = (i * PI / 4).toFloat()
            val px = cx + petalDist * cos(angle)
            val py = cy + petalDist * sin(angle)
            drawCircle(iconColor, radius = petalR, center = Offset(px, py),
                style = Stroke(size.width * 0.065f))
        }
        drawCircle(iconColor, radius = size.width * 0.15f, center = Offset(cx, cy))
    }
}

private fun drawSmileyIcon(scope: DrawScope, iconColor: Color = Color(0xFF222222)) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width * 0.44f
        val sw = size.width * 0.08f

        // Face circle
        drawCircle(iconColor, radius = r, center = Offset(cx, cy), style = Stroke(sw))

        // Eyes
        drawCircle(iconColor, radius = size.width * 0.065f, center = Offset(cx - r * 0.35f, cy - r * 0.2f))
        drawCircle(iconColor, radius = size.width * 0.065f, center = Offset(cx + r * 0.35f, cy - r * 0.2f))

        // Smile arc
        val smilePath = Path().apply {
            moveTo(cx - r * 0.42f, cy + r * 0.1f)
            cubicTo(cx - r * 0.42f, cy + r * 0.55f, cx + r * 0.42f, cy + r * 0.55f, cx + r * 0.42f, cy + r * 0.1f)
        }
        drawPath(smilePath, iconColor, style = Stroke(sw, cap = StrokeCap.Round))
    }
}

private fun drawPineappleIcon(scope: DrawScope, iconColor: Color = Color(0xFF222222)) {
    with(scope) {
        val cx = size.width / 2f
        val sw = size.width * 0.07f

        // Body (oval)
        val bodyTop = size.height * 0.38f
        val bodyBottom = size.height * 0.95f
        val bodyW = size.width * 0.38f
        val bodyH = (bodyBottom - bodyTop) / 2f
        val bodyCy = bodyTop + bodyH
        drawOval(iconColor, topLeft = Offset(cx - bodyW, bodyTop),
            size = androidx.compose.ui.geometry.Size(bodyW * 2, bodyH * 2),
            style = Stroke(sw))

        // Diamond crosshatch lines (horizontal)
        for (i in 1..3) {
            val y = bodyTop + bodyH * 2 * i / 4f
            val ratio = ((y - bodyCy) / bodyH).let { it * it }
            val halfW = bodyW * sqrt(maxOf(0f, 1f - ratio))
            drawLine(iconColor.copy(alpha = 0.5f), Offset(cx - halfW + sw, y), Offset(cx + halfW - sw, y), sw * 0.6f)
        }
        // Diagonal lines
        for (i in -2..2) {
            val x = cx + i * bodyW * 0.45f
            drawLine(iconColor.copy(alpha = 0.4f), Offset(x, bodyTop + sw), Offset(x + bodyW * 0.45f, bodyBottom - sw), sw * 0.5f)
        }

        // Crown leaves
        val leafData = listOf(-0.35f to 0.05f, 0f to 0f, 0.35f to 0.05f)
        leafData.forEach { (dx, dy) ->
            val path = Path().apply {
                moveTo(cx, bodyTop)
                cubicTo(cx + dx * size.width, (dy - 0.18f) * size.height,
                    cx + dx * size.width * 1.1f, (dy - 0.05f) * size.height,
                    cx + dx * size.width * 0.6f, bodyTop - size.height * 0.15f)
            }
            drawPath(path, iconColor, style = Stroke(sw, cap = StrokeCap.Round))
        }
    }
}

private fun drawSunIcon(scope: DrawScope, iconColor: Color = Color(0xFF222222)) {
    with(scope) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val sw = size.width * 0.07f
        val rayLen = size.width * 0.18f
        val rayStart = size.width * 0.28f

        // Rays (8 arah)
        repeat(8) { i ->
            val angle = (i * PI / 4).toFloat()
            drawLine(
                color = iconColor,
                start = Offset(cx + rayStart * cos(angle), cy + rayStart * sin(angle)),
                end = Offset(cx + (rayStart + rayLen) * cos(angle), cy + (rayStart + rayLen) * sin(angle)),
                strokeWidth = sw,
                cap = StrokeCap.Round
            )
        }

        // Lingkaran tengah
        drawCircle(
            color = iconColor,
            radius = size.width * 0.22f,
            center = Offset(cx, cy)
        )

        // Lingkaran dalam untuk kesan outline
        drawCircle(
            color = if (iconColor == Color.White) Color(0xFF1A1A1A) else Color.White,
            radius = size.width * 0.13f,
            center = Offset(cx, cy)
        )
    }
}

@Composable
private fun PerfumeCard(perfume: PerfumeItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        Column {
            // Photo area
            if (perfume.photoUri != null) {
                AsyncImage(
                    model = android.net.Uri.parse(perfume.photoUri),
                    contentDescription = perfume.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(perfume.photoTop, perfume.photoBottom)
                            )
                        )
                )
            }

            // Details strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = perfume.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = perfume.brand,
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                Text(
                    text = perfume.price,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Gold
                )
            }
        }
    }
}
