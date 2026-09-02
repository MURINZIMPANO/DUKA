package com.duka.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.duka.app.data.local.entity.Product
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.ProductRepository
import com.duka.app.domain.VoiceEntryParser
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import kotlinx.coroutines.launch

// Voice UI state machine
sealed class VoiceUiState {
    data object Idle : VoiceUiState()
    data object RequestingPermission : VoiceUiState()
    data object PermissionDenied : VoiceUiState()
    data object Listening : VoiceUiState()
    data object Processing : VoiceUiState()
    data class Confirm(
        val name: String,
        val detail: String?,
        val price: String
    ) : VoiceUiState()
    data class Error(val message: String) : VoiceUiState()
    data object Saved : VoiceUiState()
}

// Diagnosis findings for Voice Add Product fix:
// Check A: Permission flow was broken — isListening set to true before permission granted.
//   Fix: Check permission state, only start listening after grant.
// Check B: SpeechRecognizer created correctly via remember with LocalContext.current (Activity context). ✅
// Check C: Intent missing EXTRA_PREFER_OFFLINE — fails on offline devices with ERROR_NETWORK.
//   Fix: Added EXTRA_PREFER_OFFLINE = true.
// Check D: onError callback silently returned empty string — no user feedback.
//   Fix: Added proper error messages for each error code.

@Composable
fun VoiceAddScreen(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    voiceEntryParser: VoiceEntryParser,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)

    var uiState by remember { mutableStateOf<VoiceUiState>(VoiceUiState.Idle) }
    var transcript by remember { mutableStateOf("") }

    // Editable fields from parsed result
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableStateOf("1") }
    var parsedSizeDetail by remember { mutableStateOf<String?>(null) }

    // SpeechRecognizer must be created on main thread with Activity context
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    // Permission launcher — only starts listening AFTER permission is granted
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            uiState = VoiceUiState.Listening
            startListening(context, speechRecognizer,
                onReady = { uiState = VoiceUiState.Listening },
                onResult = { text ->
                    if (text.isBlank()) {
                        uiState = VoiceUiState.Error("Didn't catch that — try again.")
                        return@startListening
                    }
                    transcript = text
                    uiState = VoiceUiState.Processing
                    val parsed = voiceEntryParser.parse(text)
                    val name = parsed.productName ?: ""
                    val priceStr = parsed.price?.toString() ?: ""
                    if (name.isBlank() && priceStr.isBlank()) {
                        uiState = VoiceUiState.Error("Couldn't parse a product name or price from: \"$text\". Try again.")
                    } else {
                        productName = name
                        price = priceStr
                        stockQuantity = parsed.quantity?.toString() ?: "1"
                        parsedSizeDetail = parsed.sizeDetail
                        uiState = VoiceUiState.Confirm(
                            name = name,
                            detail = parsed.sizeDetail,
                            price = priceStr
                        )
                    }
                },
                onError = { errorCode ->
                    val message = when (errorCode) {
                        SpeechRecognizer.ERROR_NETWORK ->
                            "Voice recognition requires a network connection. Check your connection and try again."
                        SpeechRecognizer.ERROR_NO_MATCH ->
                            "Didn't catch that — try again."
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                            "Recognizer busy — wait a moment and try again."
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                            "Microphone permission is required."
                        SpeechRecognizer.ERROR_AUDIO ->
                            "Audio error — check your microphone."
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                            "No speech detected — try again."
                        else ->
                            "Voice error ($errorCode) — try again."
                    }
                    uiState = VoiceUiState.Error(message)
                }
            )
        } else {
            uiState = VoiceUiState.PermissionDenied
        }
    }

    DisposableEffect(Unit) {
        onDispose { speechRecognizer.destroy() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.VOICE_ADD,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.voice_add_title), color = White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (val state = uiState) {
                    is VoiceUiState.Idle -> {
                        Text(
                            text = stringResource(R.string.voice_tap_mic),
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        MicButton(onClick = {
                            // Check permission before starting
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                uiState = VoiceUiState.Listening
                                startListening(context, speechRecognizer,
                                    onReady = { uiState = VoiceUiState.Listening },
                                    onResult = { text ->
                                        if (text.isBlank()) {
                                            uiState = VoiceUiState.Error("Didn't catch that — try again.")
                                            return@startListening
                                        }
                                        transcript = text
                                        uiState = VoiceUiState.Processing
                                        val parsed = voiceEntryParser.parse(text)
                                        val name = parsed.productName ?: ""
                                        val priceStr = parsed.price?.toString() ?: ""
                                        if (name.isBlank() && priceStr.isBlank()) {
                                            uiState = VoiceUiState.Error("Couldn't parse a product name or price from: \"$text\". Try again.")
                                        } else {
                                            productName = name
                                            price = priceStr
                                            stockQuantity = parsed.quantity?.toString() ?: "1"
                                            parsedSizeDetail = parsed.sizeDetail
                                            uiState = VoiceUiState.Confirm(
                                                name = name,
                                                detail = parsed.sizeDetail,
                                                price = priceStr
                                            )
                                        }
                                    },
                                    onError = { errorCode ->
                                        val message = when (errorCode) {
                                            SpeechRecognizer.ERROR_NETWORK ->
                                                "Voice recognition requires a network connection. Check your connection and try again."
                                            SpeechRecognizer.ERROR_NO_MATCH ->
                                                "Didn't catch that — try again."
                                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                                                "Recognizer busy — wait a moment and try again."
                                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                                                "Microphone permission is required."
                                            SpeechRecognizer.ERROR_AUDIO ->
                                                "Audio error — check your microphone."
                                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                                                "No speech detected — try again."
                                            else ->
                                                "Voice error ($errorCode) — try again."
                                        }
                                        uiState = VoiceUiState.Error(message)
                                    }
                                )
                            } else {
                                uiState = VoiceUiState.RequestingPermission
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        })
                    }

                    is VoiceUiState.RequestingPermission -> {
                        Text(
                            text = "Requesting microphone permission…",
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        MicButton(onClick = { /* no-op while requesting */ }, enabled = false)
                    }

                    is VoiceUiState.PermissionDenied -> {
                        Icon(
                            Icons.Default.MicOff,
                            contentDescription = "Microphone permission denied",
                            modifier = Modifier.size(48.dp),
                            tint = Clay
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Microphone permission is needed to add products by voice.",
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                uiState = VoiceUiState.RequestingPermission
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Grant permission")
                        }
                    }

                    is VoiceUiState.Listening -> {
                        Text(
                            text = stringResource(R.string.voice_listening),
                            style = MaterialTheme.typography.headlineSmall.copy(color = Forest)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        MicButton(onClick = {
                            speechRecognizer.stopListening()
                            uiState = VoiceUiState.Idle
                        }, isActive = true)
                    }

                    is VoiceUiState.Processing -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Forest
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Processing voice input…",
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant)
                        )
                    }

                    is VoiceUiState.Error -> {
                        Icon(
                            Icons.Default.MicOff,
                            contentDescription = "Error",
                            modifier = Modifier.size(48.dp),
                            tint = Clay
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { uiState = VoiceUiState.Idle },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Try again")
                        }
                    }

                    is VoiceUiState.Confirm -> {
                        // Confirm step — editable fields before saving
                        Text(
                            text = "Confirm product details",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        )

                        Text(
                            text = "Transcript: \"$transcript\"",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = productName,
                            onValueChange = { productName = it },
                            label = { Text(stringResource(R.string.field_product_name)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Forest,
                                unfocusedBorderColor = Mist,
                                focusedContainerColor = White,
                                unfocusedContainerColor = White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text(stringResource(R.string.field_price)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Forest,
                                unfocusedBorderColor = Mist,
                                focusedContainerColor = White,
                                unfocusedContainerColor = White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = stockQuantity,
                            onValueChange = { stockQuantity = it.filter { c -> c.isDigit() } },
                            label = { Text(stringResource(R.string.field_quantity)) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Forest,
                                unfocusedBorderColor = Mist,
                                focusedContainerColor = White,
                                unfocusedContainerColor = White
                            )
                        )

                        if (parsedSizeDetail != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Size/detail: $parsedSizeDetail",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val priceValue = price.toDoubleOrNull() ?: return@Button
                                val stockValue = stockQuantity.toIntOrNull() ?: return@Button
                                val businessId = business?.id ?: return@Button

                                scope.launch {
                                    productRepository.addProduct(
                                        Product(
                                            businessId = businessId,
                                            name = productName.trim(),
                                            price = priceValue,
                                            stockQuantity = stockValue,
                                            category = "Other"
                                        )
                                    )
                                    uiState = VoiceUiState.Saved
                                }
                            },
                            enabled = productName.isNotBlank() && price.isNotBlank() && stockQuantity.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Amber,
                                contentColor = Ink,
                                disabledContainerColor = Mist,
                                disabledContentColor = OnSurfaceVariant
                            )
                        ) {
                            Text(stringResource(R.string.voice_confirm_button), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                uiState = VoiceUiState.Idle
                                transcript = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.voice_try_again))
                        }
                    }

                    is VoiceUiState.Saved -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Forest.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "✓ Product saved successfully",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                            )
                        }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(1500)
                            onBack()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MicButton(
    onClick: () -> Unit,
    isActive: Boolean = false,
    enabled: Boolean = true
) {
    val micColor by animateColorAsState(
        if (isActive) Clay else Forest, label = "micColor"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp)
            .scale(pulseScale),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) micColor else Mist
        ),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled
    ) {
        Icon(
            imageVector = if (isActive) Icons.Filled.Stop else Icons.Filled.Mic,
            contentDescription = if (isActive) "Stop listening" else "Start voice input",
            modifier = Modifier.size(32.dp)
        )
    }
}

private fun startListening(
    context: Context,
    speechRecognizer: SpeechRecognizer,
    onReady: () -> Unit,
    onResult: (String) -> Unit,
    onError: (Int) -> Unit
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "rw-RW") // Kinyarwanda, fallback to system
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true) // Work offline when possible
    }

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) { onReady() }
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            onError(error)
        }
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val text = matches?.firstOrNull() ?: ""
            onResult(text)
        }
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    speechRecognizer.startListening(intent)
}
