package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VideoCameraBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.TimbaktuAmber
import com.example.ui.theme.TimbaktuDarkBg
import com.example.ui.theme.TimbaktuFlame
import com.example.ui.theme.TimbaktuPurple
import com.example.ui.theme.TimbaktuSurface
import com.example.ui.theme.TimbaktuSurfaceVariant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateShortDialog(
    onDismiss: () -> Unit,
    onPublish: (title: String, desc: String, community: String, tags: List<String>, music: String, videoUri: String?, rewardInr: Double) -> Unit
) {
    val scope = rememberCoroutineScope()
    // Smart auto-filled defaults so user can publish in 2 taps without typing!
    var title by remember { mutableStateOf("New Viral Short 🔥") }
    var description by remember { mutableStateOf("#TrendGram #Shorts #Viral") }
    var selectedCommunity by remember { mutableStateOf("Entertainment") }
    var selectedMusic by remember { mutableStateOf("TrendGram Viral Beats 2026") }
    var selectedRewardInr by remember { mutableDoubleStateOf(1.80) }
    var autoBoost by remember { mutableStateOf(true) }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }
    var showMoreOptions by remember { mutableStateOf(false) }

    // Instant Zero-Permission Android Video Picker
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
        }
    }

    val communities = listOf("Entertainment", "Comedy", "Tech & AI", "Travel", "Fitness", "Food")
    val rewardOptions = listOf(1.50, 1.80, 2.00)

    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = { if (!isUploading) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = TimbaktuDarkBg,
            border = BorderStroke(1.5.dp, TimbaktuPurple.copy(alpha = 0.8f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(listOf(TimbaktuPurple, TimbaktuFlame)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Easy Video Upload ⚡",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "1-Tap choose & post",
                                color = TimbaktuAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!isUploading) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // STEP 1: BIG 1-TAP VIDEO SELECTOR BUTTON
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable(enabled = !isUploading) {
                            videoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                            )
                        }
                        .testTag("pick_video_button"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedVideoUri != null) AccentGreen.copy(alpha = 0.2f) else TimbaktuSurface
                    ),
                    border = BorderStroke(
                        2.dp,
                        if (selectedVideoUri != null) AccentGreen else TimbaktuPurple
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    if (selectedVideoUri != null) AccentGreen else TimbaktuPurple,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (selectedVideoUri != null) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (selectedVideoUri != null) "✓ Video Selected!" else "Tap to Choose Video from Phone",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (selectedVideoUri != null) "Tap here to change video" else "Select any video from gallery or camera",
                            color = if (selectedVideoUri != null) AccentGreen else Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // STEP 2: VIDEO TITLE (Pre-filled with cool default, one-tap editable)
                Text(
                    text = "Video Title:",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Enter video title...", color = Color.White.copy(alpha = 0.4f), fontSize = 13.sp) },
                    singleLine = true,
                    enabled = !isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upload_title_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TimbaktuSurface,
                        unfocusedContainerColor = TimbaktuSurface,
                        focusedBorderColor = TimbaktuPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // QUICK WATCH REWARD CHIPS (1-Tap)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Viewer Watch Reward:",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "1 Min Watch",
                        color = TimbaktuAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rewardOptions.forEach { reward ->
                        val isSelected = selectedRewardInr == reward
                        Surface(
                            color = if (isSelected) TimbaktuAmber else TimbaktuSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable(enabled = !isUploading) { selectedRewardInr = reward }
                        ) {
                            Text(
                                text = "₹${String.format("%.2f", reward)}",
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(vertical = 7.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // OPTIONAL EXPANDABLE SETTINGS (Topic & Hashtags)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showMoreOptions = !showMoreOptions }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = TimbaktuPurple, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showMoreOptions) "Hide Extra Options" else "Add Channel & Hashtags (Optional)",
                            color = TimbaktuPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = if (showMoreOptions) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TimbaktuPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = showMoreOptions) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Select Channel:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(communities) { comm ->
                                val isSelected = selectedCommunity == comm
                                Surface(
                                    color = if (isSelected) TimbaktuPurple else TimbaktuSurfaceVariant,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { selectedCommunity = comm }
                                ) {
                                    Text(
                                        text = comm,
                                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Hashtags:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = TimbaktuSurface,
                                unfocusedContainerColor = TimbaktuSurface,
                                focusedBorderColor = TimbaktuPurple,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Uploading State Bar
                if (isUploading) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Uploading Video... ${(uploadProgress * 100).toInt()}%",
                            color = TimbaktuAmber,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { uploadProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = TimbaktuFlame,
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    // STEP 3: BIG 1-TAP PUBLISH BUTTON
                    Button(
                        onClick = {
                            isUploading = true
                            scope.launch {
                                // Smooth instant progress animation
                                for (i in 1..10) {
                                    delay(80L)
                                    uploadProgress = i / 10f
                                }
                                val finalTitle = title.ifBlank { "Viral Short #TrendGram" }
                                val finalDesc = description.ifBlank { "#TrendGram #Shorts #Viral" }
                                val tags = listOf("TrendGram", selectedCommunity)
                                onPublish(
                                    finalTitle,
                                    finalDesc,
                                    selectedCommunity,
                                    tags,
                                    selectedMusic,
                                    selectedVideoUri?.toString(),
                                    selectedRewardInr
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("publish_short_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedVideoUri != null) AccentGreen else TimbaktuPurple
                        )
                    ) {
                        Icon(
                            imageVector = if (selectedVideoUri != null) Icons.Default.RocketLaunch else Icons.Default.Movie,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedVideoUri != null) "Publish Selected Video Now 🚀" else "Post Instant Short 🚀",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
