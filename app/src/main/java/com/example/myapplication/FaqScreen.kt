package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

private data class FaqItem(val question: String, val answer: String)

private val faqList = listOf(
    FaqItem(
        "Apa itu Noblesse Parfum?",
        "Noblesse Parfum adalah brand parfum premium yang menghadirkan koleksi wewangian eksklusif dengan bahan-bahan pilihan terbaik dari seluruh dunia, terinspirasi dari tradisi parfumeri Eropa sejak 1928."
    ),
    FaqItem(
        "Berapa lama ketahanan parfum?",
        "Ketahanan parfum kami berkisar antara 6–12 jam tergantung jenis aroma. Kategori Warm & Oriental (seperti Oud) memiliki ketahanan terlama, sementara Fresh & Floral sedikit lebih ringan."
    ),
    FaqItem(
        "Bagaimana cara menyimpan parfum yang benar?",
        "Simpan parfum di tempat yang sejuk, kering, dan terhindar dari paparan sinar matahari langsung. Hindari menyimpan di kamar mandi karena uap air dapat merusak kualitas aroma."
    ),
    FaqItem(
        "Apakah produk ini original?",
        "Ya, semua produk yang tersedia di Noblesse Parfum adalah 100% original dan bersertifikat. Kami bekerja sama langsung dengan distributor resmi untuk menjamin keaslian setiap produk."
    ),
    FaqItem(
        "Apakah ada garansi produk?",
        "Kami menyediakan garansi keaslian produk. Jika produk yang diterima tidak sesuai, silakan hubungi kami dalam 7 hari setelah penerimaan untuk proses pengembalian."
    ),
    FaqItem(
        "Bagaimana cara pemesanan?",
        "Pilih parfum yang kamu inginkan, kemudian hubungi admin melalui kontak yang tersedia. Pesanan akan dikonfirmasi dalam 1x24 jam dan dikirim dalam 2–5 hari kerja."
    ),
    FaqItem(
        "Apakah tersedia ukuran sample/tester?",
        "Ya, beberapa produk tersedia dalam ukuran sample 5ml dan 10ml untuk kamu coba sebelum membeli ukuran penuh. Tanyakan ketersediaan langsung kepada admin."
    ),
    FaqItem(
        "Metode pembayaran apa saja yang diterima?",
        "Kami menerima transfer bank (BCA, Mandiri, BNI, BRI), dompet digital (GoPay, OVO, DANA), dan COD untuk area tertentu."
    )
)

@Composable
fun FaqScreen(onBack: () -> Unit) {
    var expandedIndex by remember { mutableStateOf(-1) }

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
                text = "FAQ",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ── Header ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavBar)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bell icon (Canvas)
            androidx.compose.foundation.Canvas(modifier = Modifier.size(48.dp)) {
                val cx = size.width / 2f
                val gold = Gold

                // Bell body
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx, size.height * 0.08f)
                    cubicTo(
                        cx - size.width * 0.35f, size.height * 0.08f,
                        cx - size.width * 0.42f, size.height * 0.35f,
                        cx - size.width * 0.42f, size.height * 0.62f
                    )
                    lineTo(cx - size.width * 0.48f, size.height * 0.75f)
                    lineTo(cx + size.width * 0.48f, size.height * 0.75f)
                    lineTo(cx + size.width * 0.42f, size.height * 0.62f)
                    cubicTo(
                        cx + size.width * 0.42f, size.height * 0.35f,
                        cx + size.width * 0.35f, size.height * 0.08f,
                        cx, size.height * 0.08f
                    )
                    close()
                }
                drawPath(path, gold)

                // Clapper
                drawCircle(
                    color = gold,
                    radius = size.width * 0.09f,
                    center = androidx.compose.ui.geometry.Offset(cx, size.height * 0.85f)
                )

                // Stem
                drawLine(
                    color = gold,
                    start = androidx.compose.ui.geometry.Offset(cx - size.width * 0.07f, size.height * 0.08f),
                    end = androidx.compose.ui.geometry.Offset(cx + size.width * 0.07f, size.height * 0.08f),
                    strokeWidth = size.width * 0.06f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Pertanyaan Umum",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Temukan jawaban untuk pertanyaan yang sering ditanyakan",
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 12.sp
            )
        }

        // ── FAQ list ──
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CreamBackground),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(faqList) { index, item ->
                FaqCard(
                    item = item,
                    isExpanded = expandedIndex == index,
                    onClick = {
                        expandedIndex = if (expandedIndex == index) -1 else index
                    }
                )
            }
        }
    }
}

@Composable
private fun FaqCard(
    item: FaqItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isExpanded) 4.dp else 2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        // Question row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.question,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isExpanded) Color(0xFF1A1A1A) else Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "−" else "+",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpanded) Gold else TextGray
                )
            }
        }

        // Answer
        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically { -it / 2 } + fadeIn(),
            exit = slideOutVertically { -it / 2 } + fadeOut()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFF0F0F0))
                )
                // Gold accent bar
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 14.dp)
                        .width(28.dp)
                        .height(2.dp)
                        .background(Gold, RoundedCornerShape(2.dp))
                )
                Text(
                    text = item.answer,
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 21.sp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 16.dp)
                )
            }
        }
    }
}
