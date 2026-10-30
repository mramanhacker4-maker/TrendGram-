package com.example.model

data class ShortVideo(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatarUrl: String,
    val community: String,
    val likesCount: Long,
    val commentsCount: Long,
    val shareCount: Long,
    val boostCount: Long,
    val musicTitle: String,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowing: Boolean = false,
    val tags: List<String> = emptyList(),
    val viewsCount: Long = 10000,
    val isBoosted: Boolean = false,
    val primaryColorHex: Long = 0xFF7C3AED,
    val secondaryColorHex: Long = 0xFFEC4899,
    val watchRewardInr: Double = 1.80, // 1.50₹ - 2.00₹ per 1 minute watch
    val requiredWatchSeconds: Int = 60
)

data class WatchRewardHistory(
    val id: String,
    val videoId: String,
    val videoTitle: String,
    val amountInr: Double,
    val time: String
)

data class Comment(
    val id: String,
    val authorName: String,
    val authorHandle: String,
    val content: String,
    val timeAgo: String,
    val likes: Int = 0,
    val isLiked: Boolean = false
)

data class Creator(
    val id: String,
    val name: String,
    val handle: String,
    val avatarUrl: String,
    val category: String,
    val followers: String,
    val isVerified: Boolean = true,
    val isFollowing: Boolean = false,
    val totalEarnings: String = "₹28,450"
)

data class BoostPackage(
    val id: String,
    val title: String,
    val coins: Int,
    val reachEstimate: String,
    val costRupees: Int,
    val isPopular: Boolean = false
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String // "boost", "like", "follow", "reward", "watch_earn"
)

data class CreatorStats(
    val walletBalanceInr: Double = 4850.00,
    val watchEarningsInr: Double = 48.60,
    val totalVideosWatched: Int = 27,
    val boostCoins: Long = 12450,
    val totalViews: String = "2.8M",
    val totalLikes: String = "340K",
    val boostsReceived: Long = 1420,
    val tierLevel: String = "Platinum Creator",
    val revenueSharePercent: Int = 80
)
