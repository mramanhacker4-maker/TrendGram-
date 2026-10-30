package com.example.data

import com.example.model.BoostPackage
import com.example.model.Comment
import com.example.model.Creator
import com.example.model.CreatorStats
import com.example.model.NotificationItem
import com.example.model.ShortVideo
import com.example.model.WatchRewardHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimbaktuRepository {

    private val sampleVideos = listOf(
        ShortVideo(
            id = "vid_1",
            title = "Zero to 1 Crore Startup Journey 🚀",
            description = "How we scaled our AI productivity app to 100k downloads in just 30 days without spending on ads! Watch 1 minute to earn cash reward #Entrepreneurship #Startup #Tech #TrendGram",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            creatorName = "Aman Sharma",
            creatorHandle = "aman_builds",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200",
            community = "Entrepreneurship",
            likesCount = 142800,
            commentsCount = 3840,
            shareCount = 12900,
            boostCount = 4210,
            musicTitle = "TrendGram Hustle Beats - High Energy",
            isLiked = false,
            isFollowing = true,
            tags = listOf("Startup", "Business", "Growth", "India"),
            viewsCount = 890400,
            isBoosted = true,
            primaryColorHex = 0xFF7C3AED,
            secondaryColorHex = 0xFF3B82F6,
            watchRewardInr = 1.80,
            requiredWatchSeconds = 60
        ),
        ShortVideo(
            id = "vid_2",
            title = "Epic Mountain Cliff Jump in Manali! 🏔️",
            description = "Hidden waterfalls and breathtaking valleys you won't find on Google Maps. Watch 1 min to earn ₹1.95! #Travel #IncredibleIndia #Wanderlust #Himachal",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            creatorName = "Riya Wanderer",
            creatorHandle = "riya_vlogs",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
            community = "Travel & Vlogs",
            likesCount = 289500,
            commentsCount = 7620,
            shareCount = 34500,
            boostCount = 9850,
            musicTitle = "Acoustic Sunset Chill - Lo-Fi Vibes",
            isLiked = true,
            isFollowing = false,
            tags = listOf("Travel", "Manali", "Nature", "Adventure"),
            viewsCount = 1450200,
            isBoosted = true,
            primaryColorHex = 0xFF059669,
            secondaryColorHex = 0xFF10B981,
            watchRewardInr = 1.95,
            requiredWatchSeconds = 60
        ),
        ShortVideo(
            id = "vid_3",
            title = "Desi Office Humor: Work from Home reality 😂",
            description = "When boss calls an emergency meeting right when lunch is served... tag that one colleague who is always on mute! #DesiHumor #Comedy #CorporateLife #WFH",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            creatorName = "Kabir Desi Roaster",
            creatorHandle = "kabir_laughs",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200",
            community = "Comedy & Memes",
            likesCount = 512000,
            commentsCount = 18400,
            shareCount = 98100,
            boostCount = 15200,
            musicTitle = "Funny Desi Dholak Beats - Comedy",
            isLiked = false,
            isFollowing = false,
            tags = listOf("Comedy", "Meme", "DesiHumor", "Office"),
            viewsCount = 2890000,
            isBoosted = false,
            primaryColorHex = 0xFFF59E0B,
            secondaryColorHex = 0xFFEF4444,
            watchRewardInr = 1.60,
            requiredWatchSeconds = 60
        ),
        ShortVideo(
            id = "vid_4",
            title = "Insane AI Video Generation Tech 2026 🤖",
            description = "This new open neural architecture renders 8K cinematic 3D worlds in real time on mobile GPUs! Earn ₹2.00 for 1 minute watch #Tech #AI #FutureTech #Innovations",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            creatorName = "Tech Guru Vikram",
            creatorHandle = "vikram_ai",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            community = "Tech & AI",
            likesCount = 98700,
            commentsCount = 2150,
            shareCount = 8900,
            boostCount = 3120,
            musicTitle = "Cyberpunk Synthwave 2099",
            isLiked = false,
            isFollowing = true,
            tags = listOf("Tech", "AI", "Mobile", "Gadgets"),
            viewsCount = 640000,
            isBoosted = true,
            primaryColorHex = 0xFF6366F1,
            secondaryColorHex = 0xFF8B5CF6,
            watchRewardInr = 2.00,
            requiredWatchSeconds = 60
        ),
        ShortVideo(
            id = "vid_5",
            title = "15-Minute Calisthenics Core Blast 💪🔥",
            description = "No gym equipment needed! Follow this morning routine for shredded abs and unbreakable core stability. Earn ₹1.70 by watching #Fitness #Calisthenics #Workout #Motivation",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            creatorName = "Arjun Athlete",
            creatorHandle = "arjun_fit",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            community = "Fitness & Health",
            likesCount = 186300,
            commentsCount = 4120,
            shareCount = 14200,
            boostCount = 5600,
            musicTitle = "Gym Beast Trap Remix 140BPM",
            isLiked = false,
            isFollowing = false,
            tags = listOf("Fitness", "Core", "HealthyLife", "Abs"),
            viewsCount = 980000,
            isBoosted = false,
            primaryColorHex = 0xFFDC2626,
            secondaryColorHex = 0xFFF97316,
            watchRewardInr = 1.70,
            requiredWatchSeconds = 60
        ),
        ShortVideo(
            id = "vid_6",
            title = "Old Delhi's 100-Year-Old Secret Jalebi Rabri 🤤",
            description = "Crispy, pure desi ghee spirals dipped in saffron syrup topped with thick creamy rabri! Earn ₹1.85 cash reward #StreetFood #DelhiEats #Foodie",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            creatorName = "Pooja Food Diaries",
            creatorHandle = "pooja_foodie",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
            community = "Food & Recipes",
            likesCount = 345000,
            commentsCount = 9840,
            shareCount = 42000,
            boostCount = 11200,
            musicTitle = "Desi Street Flute & Tabla Groove",
            isLiked = true,
            isFollowing = false,
            tags = listOf("Food", "Delhi", "Dessert", "IndianFood"),
            viewsCount = 1790000,
            isBoosted = true,
            primaryColorHex = 0xFFEA580C,
            secondaryColorHex = 0xFFFACC15,
            watchRewardInr = 1.85,
            requiredWatchSeconds = 60
        )
    )

    private val sampleCreators = listOf(
        Creator("cr_1", "Aman Sharma", "aman_builds", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200", "Entrepreneurship", "380K", true, true, "₹1,42,800"),
        Creator("cr_2", "Riya Wanderer", "riya_vlogs", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200", "Travel & Vlogs", "950K", true, false, "₹4,12,000"),
        Creator("cr_3", "Kabir Desi Roaster", "kabir_laughs", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200", "Comedy & Memes", "1.8M", true, false, "₹7,80,000"),
        Creator("cr_4", "Tech Guru Vikram", "vikram_ai", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200", "Tech & AI", "420K", true, true, "₹2,10,000"),
        Creator("cr_5", "Arjun Athlete", "arjun_fit", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200", "Fitness & Health", "620K", true, false, "₹3,40,000"),
        Creator("cr_6", "Pooja Food Diaries", "pooja_foodie", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200", "Food & Recipes", "810K", true, false, "₹4,50,000")
    )

    private val initialComments = mapOf(
        "vid_1" to listOf(
            Comment("c1", "Rohit Verma", "rohit_v", "This breakdown was so clear bro! Implementing the SEO hook right away.", "2h ago", 142, false),
            Comment("c2", "Sneha Kapoor", "sneha_k", "Bro boost this video, more founders need to see this! 🔥🚀", "5h ago", 89, true),
            Comment("c3", "Tech Guy", "tech_dude", "What tech stack did you use for the backend?", "8h ago", 34, false)
        ),
        "vid_2" to listOf(
            Comment("c4", "Ananya Singh", "ananya_s", "Adding this valley to my Himachal wishlist right now! 🏔️✨", "1h ago", 240, true),
            Comment("c5", "Karan Malhotra", "karan_m", "Which camera did you shoot this with? The grading is fire!", "4h ago", 65, false)
        ),
        "vid_3" to listOf(
            Comment("c6", "Priya Mehra", "priya_m", "LMAO relatable at 1000%! My manager literally did this yesterday 😂", "30m ago", 890, true),
            Comment("c7", "Naveen", "naveen_99", "Best creator on TrendGram hands down!!", "3h ago", 312, false)
        )
    )

    val boostPackages = listOf(
        BoostPackage("b_1", "Mini Spark", 100, "+2,500 Targeted Reach", 99, false),
        BoostPackage("b_2", "Viral Comet 🚀", 500, "+15,000 High-Engagement Views", 399, true),
        BoostPackage("b_3", "Supernova Blast 🔥", 1500, "+50,000 Community Spotlights", 999, false),
        BoostPackage("b_4", "Galaxy Trend Setter 👑", 5000, "+200,000 Top Feed Pinning", 2999, false)
    )

    private val _videos = MutableStateFlow(sampleVideos)
    val videos: StateFlow<List<ShortVideo>> = _videos.asStateFlow()

    private val _creators = MutableStateFlow(sampleCreators)
    val creators: StateFlow<List<Creator>> = _creators.asStateFlow()

    private val _comments = MutableStateFlow(initialComments)
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    private val _creatorStats = MutableStateFlow(CreatorStats())
    val creatorStats: StateFlow<CreatorStats> = _creatorStats.asStateFlow()

    // Rule: "1 person ek bar hi count hoga views or time"
    // Set of video IDs that the user has watched and earned reward from
    private val _watchedVideoIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedVideoIds: StateFlow<Set<String>> = _watchedVideoIds.asStateFlow()

    private val _watchHistory = MutableStateFlow<List<WatchRewardHistory>>(
        listOf(
            WatchRewardHistory("wh_1", "vid_demo_1", "Tech Innovation 2026", 1.80, "10 mins ago"),
            WatchRewardHistory("wh_2", "vid_demo_2", "Top 5 AI Startup Tools", 1.90, "25 mins ago")
        )
    )
    val watchHistory: StateFlow<List<WatchRewardHistory>> = _watchHistory.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem("n1", "Watch & Earn Active! 💰", "Watch any video for 1 minute to earn ₹1.50 - ₹2.00 instantly!", "Just now", "watch_earn"),
            NotificationItem("n2", "Boost Received! 🚀", "Sneha sent 100 Boost Coins to your short video!", "5m ago", "boost"),
            NotificationItem("n3", "Viral Milestone 🎉", "Your video reached 500,000 views in the Entrepreneurship Hub!", "1h ago", "reward"),
            NotificationItem("n4", "Weekly Payout Ready 💰", "Earnings available for instant UPI bank transfer", "1d ago", "reward")
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    /**
     * Completes a 1-minute watch session.
     * Enforces the strict rule: "1 person ek bar hi count hoga views or time"
     * Returns Pair(isSuccess, earnedAmount)
     */
    fun claimWatchReward(videoId: String): Pair<Boolean, Double> {
        // If this user already watched and claimed on this video, reject duplicate counting
        if (_watchedVideoIds.value.contains(videoId)) {
            return Pair(false, 0.0)
        }

        val video = _videos.value.firstOrNull { it.id == videoId } ?: return Pair(false, 0.0)
        val rewardAmount = video.watchRewardInr

        // 1. Mark video as watched by this person
        _watchedVideoIds.update { it + videoId }

        // 2. Increment video view count by exactly 1 (unique person count)
        _videos.update { list ->
            list.map { v ->
                if (v.id == videoId) {
                    v.copy(viewsCount = v.viewsCount + 1)
                } else v
            }
        }

        // 3. Credit wallet balance
        _creatorStats.update { stats ->
            stats.copy(
                walletBalanceInr = stats.walletBalanceInr + rewardAmount,
                watchEarningsInr = stats.watchEarningsInr + rewardAmount,
                totalVideosWatched = stats.totalVideosWatched + 1
            )
        }

        // 4. Log reward history
        _watchHistory.update { history ->
            listOf(
                WatchRewardHistory(
                    id = "wh_${System.currentTimeMillis()}",
                    videoId = videoId,
                    videoTitle = video.title,
                    amountInr = rewardAmount,
                    time = "Just now"
                )
            ) + history
        }

        // 5. Add notification
        _notifications.update { current ->
            listOf(
                NotificationItem(
                    id = "watch_${System.currentTimeMillis()}",
                    title = "Watch Reward Claimed! 💰",
                    message = "Earned ₹${String.format("%.2f", rewardAmount)} for watching 1 minute of '${video.title}'!",
                    timeAgo = "Just now",
                    type = "watch_earn"
                )
            ) + current
        }

        return Pair(true, rewardAmount)
    }

    fun isVideoWatched(videoId: String): Boolean {
        return _watchedVideoIds.value.contains(videoId)
    }

    fun toggleLike(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    val newIsLiked = !video.isLiked
                    val newLikes = if (newIsLiked) video.likesCount + 1 else video.likesCount - 1
                    video.copy(isLiked = newIsLiked, likesCount = newLikes)
                } else video
            }
        }
    }

    fun toggleSave(videoId: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(isSaved = !video.isSaved)
                } else video
            }
        }
    }

    fun toggleFollow(creatorHandle: String) {
        _videos.update { list ->
            list.map { video ->
                if (video.creatorHandle == creatorHandle) {
                    video.copy(isFollowing = !video.isFollowing)
                } else video
            }
        }
        _creators.update { list ->
            list.map { creator ->
                if (creator.handle == creatorHandle) {
                    creator.copy(isFollowing = !creator.isFollowing)
                } else creator
            }
        }
    }

    fun sendBoost(videoId: String, coins: Int) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(
                        boostCount = video.boostCount + coins,
                        isBoosted = true
                    )
                } else video
            }
        }
        _creatorStats.update { stats ->
            stats.copy(
                boostsReceived = stats.boostsReceived + coins,
                walletBalanceInr = stats.walletBalanceInr + (coins * 0.40)
            )
        }
        _notifications.update { current ->
            listOf(
                NotificationItem(
                    id = "n_${System.currentTimeMillis()}",
                    title = "Boost Sent Successfully! 🚀",
                    message = "You supported this creator with $coins TrendGram Boost Coins!",
                    timeAgo = "Just now",
                    type = "boost"
                )
            ) + current
        }
    }

    fun addComment(videoId: String, text: String, authorName: String = "You", authorHandle: String = "my_trendgram") {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "comm_${System.currentTimeMillis()}",
            authorName = authorName,
            authorHandle = authorHandle,
            content = text.trim(),
            timeAgo = "Just now",
            likes = 0,
            isLiked = false
        )
        _comments.update { map ->
            val existing = map[videoId] ?: emptyList()
            map + (videoId to (listOf(newComment) + existing))
        }
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(commentsCount = video.commentsCount + 1)
                } else video
            }
        }
    }

    fun toggleCommentLike(videoId: String, commentId: String) {
        _comments.update { map ->
            val list = map[videoId] ?: return@update map
            val updated = list.map { comment ->
                if (comment.id == commentId) {
                    val newIsLiked = !comment.isLiked
                    val newLikes = if (newIsLiked) comment.likes + 1 else comment.likes - 1
                    comment.copy(isLiked = newIsLiked, likes = newLikes)
                } else comment
            }
            map + (videoId to updated)
        }
    }

    private val upiRegex = Regex("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$")

    fun withdrawEarnings(amount: Double, upiId: String): Boolean {
        val sanitizedUpi = upiId.trim()
        // Strict boundary check (positive amount and reasonable single transaction limit)
        if (amount <= 0 || amount > 100000.0) return false
        // Anti-injection strict UPI format validation
        if (!upiRegex.matches(sanitizedUpi)) return false
        // Prevent overdraft / negative balance
        if (_creatorStats.value.walletBalanceInr < amount) return false

        _creatorStats.update { it.copy(walletBalanceInr = (it.walletBalanceInr - amount).coerceAtLeast(0.0)) }
        _notifications.update { current ->
            listOf(
                NotificationItem(
                    id = "w_${System.currentTimeMillis()}",
                    title = "Withdrawal Processed! 💸",
                    message = "₹${String.format("%.2f", amount)} transferred to $sanitizedUpi securely via UPI FastPay.",
                    timeAgo = "Just now",
                    type = "reward"
                )
            ) + current
        }
        return true
    }

    fun boostMyVideo(videoId: String, pkg: BoostPackage) {
        _videos.update { list ->
            list.map { video ->
                if (video.id == videoId) {
                    video.copy(isBoosted = true, viewsCount = video.viewsCount + pkg.coins * 30L)
                } else video
            }
        }
        _notifications.update { current ->
            listOf(
                NotificationItem(
                    id = "boost_${System.currentTimeMillis()}",
                    title = "Campaign Activated! 🔥",
                    message = "${pkg.title} applied to your video. Expected reach: ${pkg.reachEstimate}",
                    timeAgo = "Just now",
                    type = "boost"
                )
            ) + current
        }
    }

    fun uploadShort(
        title: String,
        description: String,
        community: String,
        tags: List<String>,
        musicTitle: String,
        videoUri: String? = null,
        watchRewardInr: Double = 1.80
    ) {
        val safeTitle = title.trim().take(120).replace("<", "").replace(">", "").ifBlank { "Viral Short #TrendGram" }
        val safeDesc = description.trim().take(300).replace("<", "").replace(">", "").ifBlank { "#TrendGram #Shorts #Viral" }
        val safeReward = watchRewardInr.coerceIn(1.50, 2.00)
        val finalUrl = if (!videoUri.isNullOrBlank()) videoUri else "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        val newVideo = ShortVideo(
            id = "vid_${System.currentTimeMillis()}",
            title = safeTitle,
            description = safeDesc,
            videoUrl = finalUrl,
            creatorName = "Creator Aman",
            creatorHandle = "creator_aman",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200",
            community = community,
            likesCount = 1,
            commentsCount = 0,
            shareCount = 0,
            boostCount = 50,
            musicTitle = musicTitle.ifBlank { "Original Sound - Creator Aman" },
            isLiked = true,
            isFollowing = true,
            tags = tags,
            viewsCount = 1,
            isBoosted = true,
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFFF59E0B,
            watchRewardInr = safeReward,
            requiredWatchSeconds = 60
        )
        _videos.update { listOf(newVideo) + it }
        _notifications.update { current ->
            listOf(
                NotificationItem(
                    id = "up_${System.currentTimeMillis()}",
                    title = "Short Published! 🎬",
                    message = "'$title' is now live across the TrendGram $community community hub!",
                    timeAgo = "Just now",
                    type = "reward"
                )
            ) + current
        }
    }
}
