package com.example.ui.components

import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.model.ShortVideo
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuDarkBg
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPink
import com.example.ui.theme.TimbaktuPurple

@Composable
fun VideoPlayerView(
    video: ShortVideo,
    isActive: Boolean,
    isMuted: Boolean,
    onDoubleTapLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isBuffering by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }

    var showPlayPauseAnim by remember { mutableStateOf(false) }
    var showHeartAnim by remember { mutableStateOf(false) }

    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }
    var mediaPlayerRef by remember { mutableStateOf<MediaPlayer?>(null) }

    // Floating heart burst animation on double tap
    val heartScale by animateFloatAsState(
        targetValue = if (showHeartAnim) 1.5f else 0f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        finishedListener = {
            if (showHeartAnim) showHeartAnim = false
        },
        label = "heartScale"
    )

    // Dynamic color theme for short video
    val primaryColor = Color(video.primaryColorHex)
    val secondaryColor = Color(video.secondaryColorHex)

    // Animated ambient lighting
    val infiniteTransition = rememberInfiniteTransition(label = "ambient")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseProgress"
    )

    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    // Safe playback state transition
    LaunchedEffect(isActive, isPrepared, hasError) {
        if (!hasError && isPrepared) {
            try {
                videoViewRef?.let { vv ->
                    if (!isActive) {
                        if (vv.isPlaying) vv.pause()
                        isPlaying = false
                    } else {
                        if (!vv.isPlaying) vv.start()
                        isPlaying = true
                    }
                }
            } catch (t: Throwable) {
                // Safely catch native player state exceptions
                hasError = true
            }
        }
    }

    // Audio mute sync
    LaunchedEffect(isMuted, isPrepared, hasError) {
        if (!hasError && isPrepared) {
            try {
                mediaPlayerRef?.let { mp ->
                    if (isMuted) mp.setVolume(0f, 0f) else mp.setVolume(1f, 1f)
                }
            } catch (_: Throwable) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TimbaktuDarkBg)
            .pointerInput(video.id) {
                detectTapGestures(
                    onTap = {
                        if (isPrepared && !hasError) {
                            try {
                                videoViewRef?.let { vv ->
                                    if (vv.isPlaying) {
                                        vv.pause()
                                        isPlaying = false
                                    } else {
                                        vv.start()
                                        isPlaying = true
                                    }
                                }
                            } catch (_: Throwable) {
                                isPlaying = !isPlaying
                            }
                        } else {
                            isPlaying = !isPlaying
                        }
                        showPlayPauseAnim = true
                    },
                    onDoubleTap = {
                        showHeartAnim = true
                        onDoubleTapLike()
                    }
                )
            }
    ) {
        // Video View Component (Safely isolated)
        if (!hasError) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        try {
                            setVideoURI(Uri.parse(video.videoUrl))
                            setOnPreparedListener { mp ->
                                try {
                                    mediaPlayerRef = mp
                                    isPrepared = true
                                    isBuffering = false
                                    mp.isLooping = true
                                    if (isMuted) {
                                        mp.setVolume(0f, 0f)
                                    } else {
                                        mp.setVolume(1f, 1f)
                                    }
                                    if (isActive) {
                                        start()
                                        isPlaying = true
                                    }
                                } catch (e: Throwable) {
                                    hasError = true
                                    isBuffering = false
                                }
                            }
                            setOnErrorListener { _, what, extra ->
                                // Prevent native MediaPlayer error crash and fallback gracefully
                                isPrepared = false
                                hasError = true
                                isBuffering = false
                                true // Error handled
                            }
                        } catch (e: Throwable) {
                            hasError = true
                            isBuffering = false
                        }
                        videoViewRef = this
                    }
                },
                update = { vv ->
                    videoViewRef = vv
                    if (isPrepared && !hasError) {
                        try {
                            if (isActive && isPlaying && !vv.isPlaying) {
                                vv.start()
                            } else if ((!isActive || !isPlaying) && vv.isPlaying) {
                                vv.pause()
                            }
                        } catch (_: Throwable) {
                            hasError = true
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Clean-up on disposal: ONLY stop if prepared to avoid "stop called in state 4"
        DisposableEffect(video.id) {
            onDispose {
                try {
                    if (isPrepared && !hasError) {
                        videoViewRef?.stopPlayback()
                    }
                } catch (_: Throwable) {
                    // Safe cleanup
                }
                isPrepared = false
                videoViewRef = null
                mediaPlayerRef = null
            }
        }

        // Cinematic Rich Backdrop & Visualizer (Displays if buffering, streaming error, or ambient background)
        if (hasError || isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.55f * pulseProgress.coerceIn(0.6f, 1.2f)),
                                secondaryColor.copy(alpha = 0.35f),
                                TimbaktuDarkBg
                            )
                        )
                    )
            ) {
                // Background artistic visualizer
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Glowing floating sound orbs
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor.copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(width * 0.3f, height * 0.4f),
                            radius = width * 0.5f * pulseProgress
                        ),
                        center = Offset(width * 0.3f, height * 0.4f),
                        radius = width * 0.5f * pulseProgress
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(secondaryColor.copy(alpha = 0.35f), Color.Transparent),
                            center = Offset(width * 0.7f, height * 0.6f),
                            radius = width * 0.6f
                        ),
                        center = Offset(width * 0.7f, height * 0.6f),
                        radius = width * 0.6f
                    )

                    // Live Audio Equalizer Waves at center
                    val barCount = 18
                    val barWidth = width * 0.025f
                    val spacing = width * 0.02f
                    val totalWidth = barCount * (barWidth + spacing)
                    val startX = (width - totalWidth) / 2f
                    val baseY = height * 0.45f

                    for (i in 0 until barCount) {
                        val factor = kotlin.math.sin((i.toFloat() / barCount * 3.14159f) + (wavePhase * 6.28f))
                        val barHeight = (40f + (factor * 50f).coerceAtLeast(10f)) * pulseProgress

                        drawRoundRect(
                            color = if (i % 2 == 0) primaryColor.copy(alpha = 0.85f) else TimbaktuAmber.copy(alpha = 0.85f),
                            topLeft = Offset(startX + i * (barWidth + spacing), baseY - barHeight / 2f),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                        )
                    }
                }

                // Center Creator Hero Portrait
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(TimbaktuPurple.copy(alpha = 0.2f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = video.creatorAvatarUrl,
                            contentDescription = video.creatorName,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = TimbaktuAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Timbaktu Live Short Feed",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (isBuffering && !hasError) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(46.dp),
                        color = TimbaktuPurple,
                        strokeWidth = 3.dp
                    )
                }
            }
        }

        // Tap Play/Pause Indicator
        AnimatedVisibility(
            visible = showPlayPauseAnim && !isPlaying,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        // Double-Tap Floating Heart Animation
        if (showHeartAnim || heartScale > 0.05f) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(heartScale)
                    .alpha(1f - (heartScale - 1f).coerceIn(0f, 0.5f) * 2),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Liked",
                    tint = Color(0xFFFF2A6D),
                    modifier = Modifier.size(96.dp)
                )
            }
        }
    }
}
