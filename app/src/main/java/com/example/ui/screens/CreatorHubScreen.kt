package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.BoostPackage
import com.example.model.CreatorStats
import com.example.model.ShortVideo
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuDarkBg
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPink
import com.example.ui.theme.TimbaktuPurple
import com.example.ui.theme.TimbaktuPurpleLight
import com.example.ui.theme.TimbaktuSurface
import com.example.ui.theme.TimbaktuSurfaceVariant

@Composable
fun CreatorHubScreen(
    stats: CreatorStats,
    boostPackages: List<BoostPackage>,
    userVideos: List<ShortVideo>,
    onWithdraw: (Double, String) -> Boolean,
    onBoostVideo: (String, BoostPackage) -> Unit,
    modifier: Modifier = Modifier
) {
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var selectedPackage by remember { mutableStateOf<BoostPackage?>(boostPackages.getOrNull(1)) }
    var selectedVideoId by remember { mutableStateOf(userVideos.firstOrNull()?.id ?: "") }
    var boostCampaignSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TimbaktuDarkBg)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Studio Header
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Creator Studio ⚡",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Monetization, Boosts & Audience Analytics",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                    }

                    Surface(
                        color = TimbaktuAmber.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, TimbaktuAmber)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = TimbaktuAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stats.tierLevel,
                                color = TimbaktuAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Wallet Balance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = TimbaktuSurface),
                border = BorderStroke(1.dp, TimbaktuPurple.copy(alpha = 0.6f))
            ) {
                Box {
                    // Ambient gradient background
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        TimbaktuPurple.copy(alpha = 0.25f),
                                        TimbaktuFlame.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Earnings & Balance",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Image(
                                painter = painterResource(id = R.drawable.img_timbaktu_coin_1793158689625),
                                contentDescription = "Coin",
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "₹${String.format("%,.2f", stats.walletBalanceInr)}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${stats.boostCoins} TrendGram Coins",
                                color = TimbaktuAmber,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "•  ${stats.revenueSharePercent}% Creator Share Active",
                                color = AccentGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { showWithdrawDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("withdraw_earnings_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TimbaktuFlame)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Withdraw UPI", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 4 Analytics Key Performance Indicators
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Views",
                    value = stats.totalViews,
                    subtitle = "+24% this week",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Boosts Earned",
                    value = "${stats.boostsReceived} 🚀",
                    subtitle = "From community",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Likes",
                    value = stats.totalLikes,
                    subtitle = "9.4% Avg Eng.",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Club Rank",
                    value = "Top 1%",
                    subtitle = "Verified Member",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Timbaktu Algorithmic Boost Campaign Builder
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TimbaktuSurface),
                border = BorderStroke(1.dp, TimbaktuAmber.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(TimbaktuFlame.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = TimbaktuFlame,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Boost Your Video Algorithm 🚀",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Push your short directly to Explore & Community Feeds",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "1. Choose Boost Package:",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(boostPackages) { pkg ->
                            val isSelected = selectedPackage?.id == pkg.id
                            Box(
                                modifier = Modifier
                                    .width(135.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) TimbaktuPurple.copy(alpha = 0.35f)
                                        else TimbaktuSurfaceVariant
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isSelected) TimbaktuPurple else Color.Transparent,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedPackage = pkg }
                                    .padding(12.dp)
                            ) {
                                Column {
                                    if (pkg.isPopular) {
                                        Surface(
                                            color = TimbaktuAmber,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        ) {
                                            Text(
                                                text = "BEST VALUE",
                                                color = Color.Black,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = pkg.title,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = pkg.reachEstimate,
                                        color = TimbaktuPurpleLight,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${pkg.coins} 🪙 (₹${pkg.costRupees})",
                                        color = TimbaktuAmber,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "2. Select Short to Boost:",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    userVideos.take(3).forEach { vid ->
                        val isSelected = selectedVideoId == vid.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) TimbaktuPurple.copy(alpha = 0.2f)
                                    else TimbaktuSurfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) TimbaktuPurple else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedVideoId = vid.id }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(vid.primaryColorHex), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎬",
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vid.title,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${vid.viewsCount} views  •  ${vid.boostCount} boosts",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = TimbaktuPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            selectedPackage?.let { pkg ->
                                onBoostVideo(selectedVideoId, pkg)
                                boostCampaignSuccess = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("launch_boost_campaign_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TimbaktuAmber)
                    ) {
                        Text(
                            text = "Launch Boost Campaign 🚀",
                            color = Color.Black,
                            fontWeight = FontWeight.Black
                        )
                    }

                    if (boostCampaignSuccess) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🎉 Boost campaign successfully queued in TrendGram Algorithm!",
                            color = AccentGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Creator Quests
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Daily Creator Quests 🎯",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            QuestItem(title = "Upload 1 Short in Tech Hub", reward = "+100 Coins", isCompleted = true)
            QuestItem(title = "Receive 50 Likes today", reward = "+150 Coins", isCompleted = true)
            QuestItem(title = "Tip or Boost a fellow creator", reward = "+50 Coins", isCompleted = false)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Fast UPI Withdrawal Dialog
    if (showWithdrawDialog) {
        WithdrawalDialog(
            currentBalance = stats.walletBalanceInr,
            onDismiss = { showWithdrawDialog = false },
            onConfirm = { amount, upi ->
                onWithdraw(amount, upi)
                showWithdrawDialog = false
            }
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TimbaktuSurface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = AccentGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun QuestItem(
    title: String,
    reward: String,
    isCompleted: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = TimbaktuSurface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = reward,
                    color = TimbaktuAmber,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                color = if (isCompleted) AccentGreen.copy(alpha = 0.2f) else TimbaktuPurple.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isCompleted) "Claimed ✓" else "In Progress",
                    color = if (isCompleted) AccentGreen else TimbaktuPurpleLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun WithdrawalDialog(
    currentBalance: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit
) {
    val context = LocalContext.current
    var amountText by remember { mutableStateOf("1000") }
    var upiId by remember { mutableStateOf("creator@upi") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = TimbaktuDarkBg,
            border = BorderStroke(1.5.dp, TimbaktuFlame.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Instant Creator Payout 💸",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Withdraw your TrendGram video earnings to your bank account via UPI.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Withdrawal Amount (₹):",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TimbaktuSurface,
                        unfocusedContainerColor = TimbaktuSurface,
                        focusedBorderColor = TimbaktuFlame,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "UPI ID (e.g. mobile@paytm, name@okhdfcbank):",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TimbaktuSurface,
                        unfocusedContainerColor = TimbaktuSurface,
                        focusedBorderColor = TimbaktuFlame,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TimbaktuSurfaceVariant)
                    ) {
                        Text("Cancel", color = Color.White)
                    }

                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull() ?: 0.0
                            if (amount <= 0) {
                                Toast.makeText(context, "Please enter a valid amount!", Toast.LENGTH_SHORT).show()
                            } else if (amount > currentBalance) {
                                Toast.makeText(context, "Amount exceeds current balance of ₹${String.format("%.2f", currentBalance)}", Toast.LENGTH_SHORT).show()
                            } else if (upiId.isBlank() || !upiId.contains("@")) {
                                Toast.makeText(context, "Please enter a valid UPI ID (e.g. name@upi)!", Toast.LENGTH_SHORT).show()
                            } else {
                                onConfirm(amount, upiId)
                                Toast.makeText(context, "₹$amount transferred to $upiId successfully via UPI FastPay! ✓", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TimbaktuFlame)
                    ) {
                        Text("Transfer Now", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
