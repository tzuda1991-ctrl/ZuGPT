package com.vixenzugpt.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF09070F)
private val SurfaceDark = Color(0xFF15111F)
private val Purple = Color(0xFF9B5CFF)
private val Lavender = Color(0xFFD4B8FF)
private val Muted = Color(0xFFAAA2B8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ZuGptApp() }
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZuGptApp() {
    var prompt by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Chat") }
    var drawerOpen by remember { mutableStateOf(false) }
    val messages = remember { mutableStateListOf<ChatMessage>() }

    BackHandler(enabled = drawerOpen) { drawerOpen = false }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            secondary = Lavender,
            background = Background,
            surface = SurfaceDark,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Row(Modifier.fillMaxSize().background(Background)) {
            if (drawerOpen) {
                Column(
                    Modifier.width(260.dp).fillMaxHeight()
                        .background(SurfaceDark).padding(18.dp)
                ) {
                    Text("ZuGPT", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Lavender)
                    Text("YOUR PERSONAL AI", fontSize = 10.sp, color = Muted, letterSpacing = 2.sp)
                    Spacer(Modifier.height(26.dp))
                    DrawerItem("Chat", Icons.Default.AutoAwesome, selectedTab) { selectedTab = it; drawerOpen = false }
                    DrawerItem("My Library", Icons.Default.Book, selectedTab) { selectedTab = it; drawerOpen = false }
                    DrawerItem("Coding Studio", Icons.Default.Code, selectedTab) { selectedTab = it; drawerOpen = false }
                    DrawerItem("Deep Research", Icons.Default.Explore, selectedTab) { selectedTab = it; drawerOpen = false }
                    DrawerItem("History", Icons.Default.History, selectedTab) { selectedTab = it; drawerOpen = false }
                    Spacer(Modifier.weight(1f))
                    DrawerItem("Settings", Icons.Default.Settings, selectedTab) { selectedTab = it; drawerOpen = false }
                }
            }

            Column(Modifier.fillMaxSize().weight(1f)) {
                TopAppBar(
                    title = {
                        Column {
                            Text("ZuGPT", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp)
                            Text(
                                when (selectedTab) {
                                    "My Library" -> "YOUR KNOWLEDGE BASE"
                                    "Coding Studio" -> "BUILD SOMETHING GREAT"
                                    "Deep Research" -> "EXPLORE THE WEB"
                                    "History" -> "PAST CONVERSATIONS"
                                    "Settings" -> "MAKE IT YOURS"
                                    else -> "YOUR PERSONAL AI"
                                },
                                color = Muted, fontSize = 9.sp, letterSpacing = 1.5.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { drawerOpen = !drawerOpen }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Lavender)
                        }
                    },
                    actions = {
                        IconButton(onClick = { messages.clear(); selectedTab = "Chat" }) {
                            Icon(Icons.Default.Add, contentDescription = "New chat", tint = Lavender)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
                )

                when (selectedTab) {
                    "My Library" -> LibraryScreen()
                    "Coding Studio" -> FeatureScreen(
                        "Coding Studio",
                        "Describe what you want to build.",
                        listOf("Generate code", "Explain an error", "Build a website", "Create a project ZIP")
                    )
                    "Deep Research" -> FeatureScreen(
                        "Deep Research",
                        "Research tools will connect here.",
                        listOf("Ask a research question", "Compare sources", "Summarize a topic")
                    )
                    "History" -> FeatureScreen("Chat History", "Your saved conversations will appear here.", listOf("History storage is not connected yet"))
                    "Settings" -> FeatureScreen("Settings", "Personalize your ZuGPT experience.", listOf("AI provider: not connected", "Theme: Black + Purple", "App version: 1.0"))
                    else -> ChatScreen(
                        messages = messages,
                        prompt = prompt,
                        onPromptChange = { prompt = it },
                        onSend = {
                            val question = prompt.trim()
                            if (question.isNotEmpty()) {
                                messages.add(ChatMessage(question, true))
                                messages.add(ChatMessage(
                                    "ZuGPT ka AI backend abhi connect nahi hua hai. Filhaal yeh app ka starter interface hai. Agle step mein hum AI API, book search aur web research connect karenge.",
                                    false
                                ))
                                prompt = ""
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: String, onClick: (String) -> Unit) {
    val active = selected == label
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp)
            .background(if (active) Purple.copy(alpha = 0.18f) else Color.Transparent, RoundedCornerShape(14.dp))
            .clickable { onClick(label) }.padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if (active) Lavender else Muted)
        Spacer(Modifier.width(12.dp))
        Text(label, color = if (active) Color.White else Muted, fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    prompt: String,
    onPromptChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        if (messages.isEmpty()) {
            Spacer(Modifier.weight(0.5f))
            Box(
                Modifier.size(76.dp).align(Alignment.CenterHorizontally)
                    .background(Brush.linearGradient(listOf(Purple, Color(0xFF4A1E83))), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(22.dp))
            Text("Think bigger with", modifier = Modifier.align(Alignment.CenterHorizontally), color = Muted, fontSize = 16.sp)
            Text("ZuGPT", modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
            Text("Your books. Your ideas. One AI.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp), color = Muted, fontSize = 13.sp)
            Spacer(Modifier.height(26.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                SuggestionCard("📚", "Ask my books", Modifier.weight(1f))
                SuggestionCard("💻", "Write code", Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                SuggestionCard("🌐", "Research", Modifier.weight(1f))
                SuggestionCard("✨", "Brainstorm", Modifier.weight(1f))
            }
            Spacer(Modifier.weight(0.5f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start) {
                        Surface(
                            color = if (msg.isUser) Color(0xFF38215B) else SurfaceDark,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.widthIn(max = 310.dp)
                        ) {
                            Text(msg.text, modifier = Modifier.padding(14.dp), color = Color.White, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp)
                .background(SurfaceDark, RoundedCornerShape(24.dp))
                .padding(start = 14.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = onPromptChange,
                placeholder = { Text("Message ZuGPT...", color = Muted) },
                modifier = Modifier.weight(1f),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Lavender
                )
            )
            IconButton(onClick = onSend, modifier = Modifier.background(Purple, CircleShape)) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
        Text("Starter build • AI connection coming next", color = Color(0xFF70677F), fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
    }
}

@Composable
fun SuggestionCard(emoji: String, title: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(72.dp),
        color = SurfaceDark,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF30243F))
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.Center) {
            Text(emoji, fontSize = 17.sp)
            Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun LibraryScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("My Library", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Build your personal knowledge base.", color = Muted, modifier = Modifier.padding(top = 6.dp))
        Spacer(Modifier.height(22.dp))
        Surface(
            color = SurfaceDark,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Purple.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Book, contentDescription = null, tint = Lavender, modifier = Modifier.size(38.dp))
                Spacer(Modifier.height(12.dp))
                Text("Your library starts here", color = Color.White, fontWeight = FontWeight.Bold)
                Text("PDF upload and indexing will be added in the next build.", color = Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("Supported formats planned", color = Lavender, fontWeight = FontWeight.SemiBold)
        Text("PDF • TXT • DOCX • Markdown • Code files", color = Muted, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun FeatureScreen(title: String, subtitle: String, items: List<String>) {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Text(title, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp, bottom = 20.dp))
        items.forEach { item ->
            Surface(
                color = SurfaceDark,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            ) {
                Text(item, color = Color.White, modifier = Modifier.padding(16.dp), fontSize = 14.sp)
            }
        }
    }
}
