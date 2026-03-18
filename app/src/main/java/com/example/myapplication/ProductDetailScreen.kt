package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

@Composable
fun ProductDetailScreen(
    perfume: PerfumeItem,
    onDelete: () -> Unit = {},
    onBack: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

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
                text = "‹",
                color = Gold,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onBack() }
            )
            Text(
                text = perfume.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = if (isFavorite) "♥" else "♡",
                color = if (isFavorite) Color(0xFFCC4444) else Gold,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isFavorite = !isFavorite }
            )
        }

        // ── Scrollable content ──
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 24.dp)
        ) {

            // ── Photo card ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(listOf(perfume.photoTop, perfume.photoBottom))
                    )
            ) {
                if (perfume.photoUri != null) {
                    AsyncImage(
                        model = android.net.Uri.parse(perfume.photoUri),
                        contentDescription = perfume.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Brand + category badge in bottom-left
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Black.copy(alpha = 0.45f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = scentCategories.getOrNull(perfume.category)?.label ?: "",
                            color = Gold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = perfume.brand,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }

                // Price badge top-right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = perfume.price,
                        color = Gold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Description ──
            Text(
                text = "Tentang Parfum",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Gold accent line
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(2.dp)
                    .background(Gold, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = getDescription(perfume.name),
                fontSize = 14.sp,
                color = TextGray,
                lineHeight = 22.sp,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Scent Notes card ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Scent Notes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                ScentNoteRow("Top Notes", getTopNotes(perfume.category))
                ScentNoteRow("Middle Notes", getMiddleNotes(perfume.category))
                ScentNoteRow("Base Notes", getBaseNotes(perfume.category))
            }
        }

        // ── Delete button (pinned to bottom) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CreamBackground)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            var showConfirm by remember { mutableStateOf(false) }

            if (showConfirm) {
                // Confirm dialog inline
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Hapus produk ini?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEEEEEE))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { showConfirm = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Batal", fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.SemiBold)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFCC2B2B))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onDelete() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Hapus", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFCC2B2B))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showConfirm = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HAPUS PRODUK",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ScentNoteRow(label: String, notes: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        Text(notes, fontSize = 13.sp, color = TextGray)
    }
}

private fun getDescription(name: String) = when (name) {
    "Vanilla Sky"  -> "Aroma hangat vanilla yang memikat, dibalut sentuhan kayu cedar yang lembut dan tahan lama di kulit."
    "Casnia"       -> "Perpaduan bunga kasnia yang elegan dengan nuansa musk putih, cocok untuk wanita modern yang percaya diri."
    "White Forest" -> "Terinspirasi dari hutan birch Skandinavia — segar, bersih, dengan sentuhan earthy yang unik."
    "September"    -> "Aroma musim gugur yang penuh nostalgia; buah beri segar bertemu kayu hangat."
    "Flower Muse"  -> "Bunga-bunga Denmark yang mekar di pagi hari, ringan dan feminin dengan daya tahan sepanjang hari."
    "Fjällsjö"     -> "Kesegaran danau pegunungan Swedia yang murni, berpadu mint dan angin sejuk."
    "Pink Pepper"  -> "Berani dan feminin — lada merah muda yang memercik di atas buah-buahan tropis yang ranum."
    "Botanist"     -> "Racikan botanical alami: herba, buah, dan rempah dalam harmoni yang menyenangkan."
    "Oud Intense"  -> "Oud terbaik dari Timur Tengah, kaya dan mewah, meninggalkan kesan yang tak terlupakan."
    "Bleu Noir"    -> "Maskulin dan misterius — fougère modern dengan sentuhan laut dan kayu gelap."
    else           -> "Parfum eksklusif dengan formula premium dari bahan-bahan pilihan terbaik dunia."
}

private fun getTopNotes(category: Int) = when (category) {
    0 -> "Cedar, Sandalwood"
    1 -> "Rose, Jasmine"
    2 -> "Bergamot, Mint"
    3 -> "Peach, Raspberry"
    4 -> "Oud, Saffron"
    else -> "Citrus, Bergamot"
}

private fun getMiddleNotes(category: Int) = when (category) {
    0 -> "Vetiver, Patchouli"
    1 -> "Peony, Lily"
    2 -> "Green Tea, Violet"
    3 -> "Mango, Pineapple"
    4 -> "Amber, Rose"
    else -> "Floral, Spice"
}

private fun getBaseNotes(category: Int) = when (category) {
    0 -> "Musk, Amber"
    1 -> "White Musk, Vanilla"
    2 -> "Cedarwood, Musk"
    3 -> "Vanilla, Musk"
    4 -> "Musk, Sandalwood"
    else -> "Musk, Vanilla"
}
