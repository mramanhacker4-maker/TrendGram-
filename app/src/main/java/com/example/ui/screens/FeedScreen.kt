package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Comment
import com.example.model.ShortVideo
import com.example.ui.components.BoostDialog
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.ShortsOverlay
import com.example.ui.components.VideoPlayerView
import com.example.ui.components.WatchAndEarnWidget
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuDarkBg
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    videos: List<ShortVideo>,
    commentsMap: Map<String, List<Comment>>,
    walletBalance: Double,
    watchedVideoIds: Set<String>,
    onLike: (String) -> Unit,
    onFollow: (String) -> Unit,
    onSendBoost: (String, Int) -> Unit,
    onAddComment: (String, String) -> Unit,
    onToggleCommentLike: (String, String) -> Unit,
    onClaimWatchReward: (String) -> Pair<Boolean, Double>,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenWallet: () -> Unit,
    unreadNotificationsCount: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedFeedTab by remember { mutableIntStateOf(1) } // 0: Following, 1: For You
    var isMuted by remember { mutableStateOf(false) }

    // Dialog & Sheet states
    var activeCommentVideoId by remember { mutableStateOf<String?>(null) }
    var activeBoostVideo by remember { mutableStateOf<ShortVideo?>(null) }
    var lastRewardMessage by remember { mutableStateOf<String?>(null) }
    val commentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val displayedVideos = remember(videos, selectedFeedTab) {
        if (selectedFeedTab == 0) {
            val followingList = videos.filter { it.isFollowing }
            if (followingList.isEmpty()) videos else followingList
        } else {
            videos
        }
    }

    val pagerState = rememberPagerState(pageCount = { displayedVideos.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TimbaktuDarkBg)
    ) {
        if (displayedVideos.isNotEmpty()) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("shorts_vertical_pager")
            ) { page ->
                val video = displayedVideos[page]
                val isCurrentPage = pagerState.currentPage == page
                val isWatchedAlready = watchedVideoIds.contains(video.id)

                Box(modifier = Modifier.fillMaxSize()) {
                    VideoPlayerView(
                        video = video,
                        isActive = isCurrentPage,
                        isMuted = isMuted,
                        onDoubleTapLike = { onLike(video.id) }
                    )

                    ShortsOverlay(
                        video = video,
                        isMuted = isMuted,
                        onToggleMute = { isMuted = !isMuted },
                        onLike = { onLike(video.id) },
                        onCommentClick = { activeCommentVideoId = video.id },
                        onBoostClick = { activeBoostVideo = video },
                        onShareClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' and earn cash on TrendGram: https://trendgram.app/v/${video.id}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Short"))
                        },
                        onFollowClick = { onFollow(video.creatorHandle) }
                    )

                    // Floating 1-Minute Watch & Earn Timer Widget
                    WatchAndEarnWidget(
                        video = video,
                        isActive = isCurrentPage,
                        isAlreadyClaimed = isWatchedAlready,
                        onClaimReward = { vidId ->
                            val result = onClaimWatchReward(vidId)
                            if (result.first) {
                                lastRewardMessage = "+₹${String.format("%.2f", result.second)} Wallet Mein Add Ho Gaya! 🎉"
                                scope.launch {
                                    delay(4000L)
                                    lastRewardMessage = null
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .statusBarsPadding()
                            .padding(top = 56.dp, start = 16.dp)
                    )
                }
            }
        }

        // Top Floating Nav Bar with Logo + Feed Switcher + Live Wallet Cashout Pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Name & Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trendgram_app_icon_1793212043396),
                    contentDescription = "TrendGram Logo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "TrendGram",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Feed Switcher: Following | For You
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Following",
                    color = if (selectedFeedTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = if (selectedFeedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { selectedFeedTab = 0 }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )

                Text(
                    text = "|",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 13.sp
                )

                Text(
                    text = "For You",
                    color = if (selectedFeedTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontWeight = if (selectedFeedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .clickable { selectedFeedTab = 1 }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }

            // Live Watch Earnings Wallet Button (1-Tap Cashout)
            Surface(
                color = TimbaktuAmber.copy(alpha = 0.25f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, TimbaktuAmber),
                modifier = Modifier
                    .clickable { onOpenWallet() }
                    .testTag("feed_wallet_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${String.format("%.2f", walletBalance)}",
                        color = TimbaktuAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Wallet",
                        tint = TimbaktuAmber,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Action Icons (Notifications)
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier.size(34.dp)
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationsCount > 0) {
                            Badge(containerColor = TimbaktuFlame) {
                                Text("$unreadNotificationsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Reward Celebration Pop-up Banner
        AnimatedVisibility(
            visible = lastRewardMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 110.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = AccentGreen),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.5.dp, Color.White),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = lastRewardMessage ?: "",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Comments Bottom Sheet
        activeCommentVideoId?.let { videoId ->
            val comments = commentsMap[videoId] ?: emptyList()
            CommentsBottomSheet(
                comments = comments,
                sheetState = commentSheetState,
                onDismiss = { activeCommentVideoId = null },
                onAddComment = { text ->
                    onAddComment(videoId, text)
                },
                onToggleCommentLike = { commentId ->
                    onToggleCommentLike(videoId, commentId)
                }
            )
        }

        // Boost Dialog
        activeBoostVideo?.let { video ->
            BoostDialog(
                video = video,
                onDismiss = { activeBoostVideo = null },
                onSendBoost = { coins ->
                    onSendBoost(video.id, coins)
                }
            )
        }
    }
}
