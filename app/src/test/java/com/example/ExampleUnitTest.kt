package com.example

import com.example.data.TimbaktuRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testTimbaktuFeedAndLikes() {
        val repository = TimbaktuRepository()
        val initialVideos = repository.videos.value
        assertTrue("Initial videos should not be empty", initialVideos.isNotEmpty())

        val firstVideo = initialVideos.first()
        val initialLikes = firstVideo.likesCount
        val initialIsLiked = firstVideo.isLiked

        // Toggle Like
        repository.toggleLike(firstVideo.id)
        val updatedVideo = repository.videos.value.first { it.id == firstVideo.id }
        assertEquals(!initialIsLiked, updatedVideo.isLiked)
        if (updatedVideo.isLiked) {
            assertEquals(initialLikes + 1, updatedVideo.likesCount)
        } else {
            assertEquals(initialLikes - 1, updatedVideo.likesCount)
        }
    }

    @Test
    fun testWatchRewardBetweenOneFiftyAndTwoRupees() {
        val repository = TimbaktuRepository()
        val firstVideo = repository.videos.value.first()

        // Check video reward rate is between 1.50 and 2.00 INR
        assertTrue("Reward should be >= 1.50", firstVideo.watchRewardInr >= 1.50)
        assertTrue("Reward should be <= 2.00", firstVideo.watchRewardInr <= 2.00)
        assertEquals(60, firstVideo.requiredWatchSeconds) // 1 minute watch
    }

    @Test
    fun testOnePersonCountedOnlyOnceRule() {
        val repository = TimbaktuRepository()
        val video = repository.videos.value.first()
        val initialWallet = repository.creatorStats.value.walletBalanceInr
        val initialViews = video.viewsCount

        // 1st Watch Completion: Should succeed and credit wallet
        val (firstSuccess, firstReward) = repository.claimWatchReward(video.id)
        assertTrue(firstSuccess)
        assertTrue(firstReward >= 1.50 && firstReward <= 2.00)

        val updatedVideo = repository.videos.value.first { it.id == video.id }
        assertEquals(initialViews + 1, updatedVideo.viewsCount)
        assertEquals(initialWallet + firstReward, repository.creatorStats.value.walletBalanceInr, 0.01)
        assertTrue(repository.isVideoWatched(video.id))

        // 2nd Watch Attempt by same person: Must be REJECTED ("1 person ek bar hi count hoga")
        val (secondSuccess, secondReward) = repository.claimWatchReward(video.id)
        assertFalse("Same person should NOT be counted or rewarded again for same video", secondSuccess)
        assertEquals(0.0, secondReward, 0.001)

        // View count and wallet balance must remain unchanged on repeat
        val repeatVideo = repository.videos.value.first { it.id == video.id }
        assertEquals(initialViews + 1, repeatVideo.viewsCount)
        assertEquals(initialWallet + firstReward, repository.creatorStats.value.walletBalanceInr, 0.01)
    }

    @Test
    fun testSendBoostIncreasesCoins() {
        val repository = TimbaktuRepository()
        val firstVideo = repository.videos.value.first()
        val initialBoosts = firstVideo.boostCount

        repository.sendBoost(firstVideo.id, 50)
        val updatedVideo = repository.videos.value.first { it.id == firstVideo.id }
        assertEquals(initialBoosts + 50, updatedVideo.boostCount)
        assertTrue(updatedVideo.isBoosted)
    }

    @Test
    fun testAddComment() {
        val repository = TimbaktuRepository()
        val videoId = repository.videos.value.first().id
        val initialCommentsCount = repository.comments.value[videoId]?.size ?: 0

        repository.addComment(videoId, "Awesome short! 🔥")
        val updatedComments = repository.comments.value[videoId] ?: emptyList()
        assertEquals(initialCommentsCount + 1, updatedComments.size)
        assertEquals("Awesome short! 🔥", updatedComments.first().content)
    }

    @Test
    fun testWithdrawEarningsSecurity() {
        val repository = TimbaktuRepository()
        val initialBalance = repository.creatorStats.value.walletBalanceInr

        // Valid withdrawal
        val success = repository.withdrawEarnings(500.0, "creator@okhdfcbank")
        assertTrue(success)
        assertEquals(initialBalance - 500.0, repository.creatorStats.value.walletBalanceInr, 0.01)

        // Invalid amount (overdraft)
        val failOverdraft = repository.withdrawEarnings(9999999.0, "creator@upi")
        assertFalse(failOverdraft)

        // Invalid amount (negative or zero)
        val failNegative = repository.withdrawEarnings(-50.0, "creator@upi")
        assertFalse(failNegative)
        val failZero = repository.withdrawEarnings(0.0, "creator@upi")
        assertFalse(failZero)

        // Invalid UPI ID (anti-injection)
        val failBadUpi = repository.withdrawEarnings(100.0, "invalid_upi_no_domain")
        assertFalse(failBadUpi)
    }
}
