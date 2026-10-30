package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TimbaktuRepository
import com.example.ui.screens.CreateShortDialog
import com.example.ui.screens.CreatorHubScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.NotificationsDialog
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuDarkBg
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPurple

class MainActivity : ComponentActivity() {

    private val repository = TimbaktuRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                TrendGramApp(repository = repository)
            }
        }
    }
}

@Composable
fun TrendGramApp(repository: TimbaktuRepository) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val videos by repository.videos.collectAsStateWithLifecycle()
    val creators by repository.creators.collectAsStateWithLifecycle()
    val commentsMap by repository.comments.collectAsStateWithLifecycle()
    val creatorStats by repository.creatorStats.collectAsStateWithLifecycle()
    val watchedVideoIds by repository.watchedVideoIds.collectAsStateWithLifecycle()
    val notifications by repository.notifications.collectAsStateWithLifecycle()

    BackHandler(enabled = selectedTab != 0) {
        selectedTab = 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TimbaktuDarkBg)
    ) {
        // Main Screen Content
        when (selectedTab) {
            0 -> FeedScreen(
                videos = videos,
                commentsMap = commentsMap,
                walletBalance = creatorStats.walletBalanceInr,
                watchedVideoIds = watchedVideoIds,
                onLike = { repository.toggleLike(it) },
                onFollow = { repository.toggleFollow(it) },
                onSendBoost = { vidId, coins -> repository.sendBoost(vidId, coins) },
                onAddComment = { vidId, text -> repository.addComment(vidId, text) },
                onToggleCommentLike = { vidId, commId -> repository.toggleCommentLike(vidId, commId) },
                onClaimWatchReward = { vidId -> repository.claimWatchReward(vidId) },
                onOpenSearch = { selectedTab = 1 },
                onOpenNotifications = { showNotificationsDialog = true },
                onOpenWallet = { selectedTab = 2 },
                unreadNotificationsCount = notifications.size
            )
            1 -> ExploreScreen(
                videos = videos,
                creators = creators,
                onSelectVideo = { _ ->
                    selectedTab = 0
                },
                onFollowCreator = { handle -> repository.toggleFollow(handle) }
            )
            2 -> CreatorHubScreen(
                stats = creatorStats,
                boostPackages = repository.boostPackages,
                userVideos = videos,
                onWithdraw = { amount, upi -> repository.withdrawEarnings(amount, upi) },
                onBoostVideo = { vidId, pkg -> repository.boostMyVideo(vidId, pkg) }
            )
            3 -> ProfileScreen(
                videos = videos,
                onSelectVideo = { _ ->
                    selectedTab = 0
                }
            )
        }

        // Clean & Easy TrendGram Bottom Navigation Bar
        TrendGramBottomBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onCreateClick = { showCreateDialog = true },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Upload / Create Dialog
        if (showCreateDialog) {
            CreateShortDialog(
                onDismiss = { showCreateDialog = false },
                onPublish = { title, desc, comm, tags, music, videoUri, rewardInr ->
                    repository.uploadShort(title, desc, comm, tags, music, videoUri, rewardInr)
                    selectedTab = 0
                }
            )
        }

        // Notifications Dialog
        if (showNotificationsDialog) {
            NotificationsDialog(
                notifications = notifications,
                onDismiss = { showNotificationsDialog = false }
            )
        }
    }
}

@Composable
fun TrendGramBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color(0xF50D0D14), // clean dark glass
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 0: Watch & Earn
            BottomNavItem(
                icon = if (selectedTab == 0) Icons.Filled.Whatshot else Icons.Outlined.Whatshot,
                label = "Watch & Earn",
                badgeText = "₹",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                testTag = "nav_shorts"
            )

            // Tab 1: Explore & Communities
            BottomNavItem(
                icon = if (selectedTab == 1) Icons.Filled.Explore else Icons.Outlined.Explore,
                label = "Explore",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                testTag = "nav_explore"
            )

            // Center: Elevated Create Short Button (+)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(TimbaktuPurple, TimbaktuFlame, TimbaktuAmber)
                        )
                    )
                    .clickable { onCreateClick() }
                    .shadow(10.dp, RoundedCornerShape(16.dp))
                    .testTag("nav_create_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Short",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Tab 2: Wallet & Cashout (Creator Studio)
            BottomNavItem(
                icon = if (selectedTab == 2) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                label = "Wallet ₹",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                testTag = "nav_studio"
            )

            // Tab 3: My Profile
            BottomNavItem(
                icon = if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                label = "Profile",
                isSelected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    badgeText: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) TimbaktuAmber else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            if (badgeText != null) {
                Surface(
                    color = TimbaktuAmber,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(start = 14.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.Black,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
