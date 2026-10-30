package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ShortVideo
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPurple
import com.example.ui.theme.TimbaktuSurface
import kotlinx.coroutines.delay

@Composable
fun WatchAndEarnWidget(
    video: ShortVideo,
    isActive: Boolean,
    isAlreadyClaimed: Boolean,
    onClaimReward: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var secondsWatched by remember(video.id) { mutableIntStateOf(0) }
    var showRewardCelebration by remember(video.id) { mutableStateOf(false) }

    val requiredSeconds = video.requiredWatchSeconds // 60 seconds (1 minute)
    val progress = (secondsWatched.toFloat() / requiredSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "watchProgress"
    )

    // Active watch timer loop
    LaunchedEffect(isActive, isAlreadyClaimed, video.id) {
        if (isActive && !isAlreadyClaimed) {
            while (secondsWatched < requiredSeconds) {
                delay(1000L)
                secondsWatched++
                if (secondsWatched >= requiredSeconds) {
                    onClaimReward(video.id)
                    showRewardCelebration = true
                    break
                }
            }
        }
    }

    Box(modifier = modifier) {
        if (isAlreadyClaimed || showRewardCelebration) {
            // Already counted & claimed for this person
            Surface(
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, AccentGreen.copy(alpha = 0.8f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Earned",
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "₹${String.format("%.2f", video.watchRewardInr)} Earned ✓",
                            color = AccentGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "1 time counted • Swipe next",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        } else {
            // Live 1-Minute Watch & Earn Progress
            Surface(
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.2.dp, TimbaktuAmber.copy(alpha = 0.7f)),
                modifier = Modifier.testTag("watch_earn_timer_widget")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Progress with seconds countdown
                    Box(
                        modifier = Modifier.size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.size(32.dp),
                            color = TimbaktuAmber,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            strokeWidth = 3.dp
                        )
                        Text(
                            text = "${requiredSeconds - secondsWatched}s",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Watch 1 Min:",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+₹${String.format("%.2f", video.watchRewardInr)}",
                                color = TimbaktuAmber,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Text(
                            text = "Watching... ${secondsWatched}s / 60s",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}
