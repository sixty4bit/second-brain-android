package com.sweepsatlas.secondbrain.ui.screens

import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.sweepsatlas.secondbrain.ui.viewmodel.DocumentDetailViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    onBackClick: () -> Unit,
    viewModel: DocumentDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.document?.title ?: "Loading...",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️ ${state.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = viewModel::refresh) {
                            Text("Retry")
                        }
                    }
                }
                state.document != null -> {
                    DocumentContent(document = state.document!!)
                }
            }
        }
    }
}

@Composable
private fun DocumentContent(
    document: com.sweepsatlas.secondbrain.data.model.Document
) {
    val context = LocalContext.current
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .build()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header with metadata
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = document.displayTitle,
                    style = MaterialTheme.typography.headlineSmall
                )
                
                document.created?.let { created ->
                    Text(
                        text = "Created: $created",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (document.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        document.tags.forEach { tag ->
                            AssistChip(
                                onClick = { },
                                label = { Text(tag) }
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content
        document.content?.let { content ->
            // Use AndroidView to render markdown with Markwon
            AndroidView(
                factory = { ctx ->
                    TextView(ctx).apply {
                        textSize = 16f
                        setTextColor(ctx.getColor(android.R.color.black))
                        setLineSpacing(0f, 1.3f)
                    }
                },
                update = { textView ->
                    markwon.setMarkdown(textView, content)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
