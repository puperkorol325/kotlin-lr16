package com.example.lr7kotlin

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.presentation.tasks.TasksUiState
import com.example.lr7kotlin.presentation.tasks.TasksViewModel
import com.example.lr7kotlin.ui.theme.Lr7kotlinTheme
import com.example.lr7kotlin.ui.theme.ThemeMode
import com.example.lr7kotlin.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var themeMode by remember { mutableStateOf(ThemeMode.System) }
            val viewModel: TasksViewModel = hiltViewModel()

            Lr7kotlinTheme(themeMode = themeMode) {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val windowSizeClass = calculateWindowSizeClass(this)

                var showDialog by remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = Routes.NOTES_LIST
                ) {
                    composable(Routes.NOTES_LIST) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    themeMode = themeMode,
                                    onThemeModeChange = { themeMode = it },
                                    windowSizeClass = windowSizeClass
                                )
                            },
                            modifier = Modifier.fillMaxSize(),
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { showDialog = true },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ) {
                                    Icon(
                                        Icons.Default.AddCircle,
                                        contentDescription = stringResource(R.string.fab_add)
                                    )
                                }
                            }
                        ) { innerPadding ->
                            TasksScreen(
                                modifier = Modifier.padding(innerPadding),
                                uiState = uiState,
                                windowSizeClass = windowSizeClass,
                                navController = navController
                            )

                            if (showDialog) {
                                AddTaskDialog(
                                    onDismiss = { showDialog = false },
                                    onConfirm = { title, description ->
                                        viewModel.addTask(title, description)
                                        showDialog = false
                                    }
                                )
                            }
                        }
                    }

                    composable(
                        route = "note/{noteId}",
                        arguments = listOf(
                            navArgument("noteId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = { detailTopAppBar(onBack = { navController.popBackStack() }) }
                        ) { innerPadding ->
                            NoteDetailScreen(
                                noteId = noteId,
                                uiState = uiState,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.fab_add)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.note_title)) },
                    placeholder = { Text(stringResource(R.string.note_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), description.trim())
                    }
                }
            ) {
                Text(stringResource(R.string.note_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.note_cancel))
            }
        }
    )
}

object Routes {
    const val NOTES_LIST: String = "notes";
    const val NOTE_DETAILS: String = "note/{id}";

    fun noteDetail(noteId: String) = "note/$noteId";
}

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    windowSizeClass: WindowSizeClass,
    navController: NavController
) {

    val horizontalPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 16.dp
        WindowWidthSizeClass.Expanded -> 24.dp
        else -> 8.dp
    }

    val cardPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 12.dp
        WindowWidthSizeClass.Expanded -> 16.dp
        else -> 8.dp
    }

    val contentPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.dp
        WindowWidthSizeClass.Medium -> 20.dp
        WindowWidthSizeClass.Expanded -> 24.dp
        else -> 16.dp
    }

    when {
        uiState.isLoading -> CircularProgressIndicator(Modifier.padding(16.dp))
        uiState.error != null -> Text("Ошибка: ${uiState.error}", color = MaterialTheme.colorScheme.error)
        else ->
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Expanded -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = modifier,
                        contentPadding = PaddingValues(horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(cardPadding),
                        verticalArrangement = Arrangement.spacedBy(cardPadding)
                    ) {
                        items(uiState.tasks) { item ->
                            TaskCard(
                                item = item,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = contentPadding,
                                navController = navController
                            )
                        }
                    }
                }

                WindowWidthSizeClass.Medium -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = modifier,
                        contentPadding = PaddingValues(horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(cardPadding),
                        verticalArrangement = Arrangement.spacedBy(cardPadding)
                    ) {
                        items(uiState.tasks) { item ->
                            TaskCard(
                                item = item,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = contentPadding,
                                navController = navController
                            )
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = modifier,
                        contentPadding = PaddingValues(horizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(cardPadding),
                        verticalArrangement = Arrangement.spacedBy(cardPadding)
                    ) {
                        items(uiState.tasks) { item ->
                            TaskCard(
                                item = item,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = contentPadding,
                                navController = navController
                            )
                        }
                    }
                }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Lr7kotlinTheme {
//        Greeting("Android", modifier = Modifier, )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(item: Task, modifier: Modifier = Modifier, contentPadding: Dp, navController: NavController) {
    Card (modifier = modifier, onClick = { navController.navigate("note/${item.id}") }) {
        Column (modifier = Modifier.padding(contentPadding)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp).fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(themeMode: ThemeMode, onThemeModeChange: (ThemeMode) -> Unit, windowSizeClass: WindowSizeClass) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.notes_title),
                style = when {
                    isLandscape && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ->
                        MaterialTheme.typography.headlineLarge
                    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ->
                        MaterialTheme.typography.headlineMedium
                    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium ->
                        MaterialTheme.typography.titleLarge
                    else -> MaterialTheme.typography.titleMedium
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
            IconButton(onClick = {
                val newMode = when (themeMode) {
                    ThemeMode.System -> if (ThemeMode.System == ThemeMode.Dark) ThemeMode.Light else ThemeMode.Dark
                    ThemeMode.Light -> ThemeMode.Dark
                    ThemeMode.Dark -> ThemeMode.System
                }
                onThemeModeChange(newMode)
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Переключить тему"
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun detailTopAppBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null);
            }
        },
        modifier = Modifier.background(Color.Cyan)
    )
}

@Composable
fun NoteDetailScreen(noteId: Int?,uiState: TasksUiState, modifier: Modifier) {
    val task = uiState.tasks.find { it.id == noteId }
    var taskCompletedText: String = "Ожидает выполнения";
    if (task?.completed == true) {
        taskCompletedText = "Выполнено";
    }
    Column (
        modifier = modifier.fillMaxWidth().padding(10.dp),
    ) {
        task?.title?.let { Text(text = it, style = Typography.titleMedium) }
        Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))
        task?.completed?.let { Text(text = taskCompletedText, style = Typography.bodyMedium) }
    }
}