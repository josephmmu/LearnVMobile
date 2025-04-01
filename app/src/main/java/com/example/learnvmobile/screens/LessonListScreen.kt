package com.example.learnvmobile.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnvmobile.domain.model.Lesson
import com.example.learnvmobile.presentation.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LessonListScreen(
    mainViewModel: MainViewModel,
    userId: Int,
    courseId: String,
    onBack:() -> Unit) {
    LaunchedEffect(courseId) {
        mainViewModel.loadLessons(courseId)
        mainViewModel.loadUserProgress(userId, courseId)
        mainViewModel.getUserById(userId)
    }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded } // Disable half-expanded
    )

    val scope = rememberCoroutineScope()
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                selectedLesson?.let { lesson ->
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = lesson.content,
                        style = MaterialTheme.typography.body1
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {scope.launch { bottomSheetState.hide() }}) {
                    Text("Close")
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Lesson")},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "back")
                        }
                    }
                )
            }
        ) {
            paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(mainViewModel.lessons) { lesson ->
                    LessonItem(
                        lesson = lesson,
                        isCompleted = mainViewModel.userProgress[lesson.lessonId] ?: false,
                        onClick = {
                            selectedLesson = lesson
                            scope.launch { bottomSheetState.show() }
                        },
                        onCheckboxClick = {
                            mainViewModel.markLessonCompleted(userId, courseId, lesson.lessonId)
                        }
                    )
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
//                            .clickable {
//                                mainViewModel.markLessonCompleted(userId, courseId, lesson.lessonId)
//                            },
//                        verticalAlignment = Alignment.CenterVertically,
//                    ) {
//                        Checkbox(
//                            checked = mainViewModel.userProgress[lesson.lessonId] ?: false,
//                            onCheckedChange = null
//                        )
//                        Text(text = lesson.title, fontSize = 18.sp)
//                    }
                }
            }
        }
    } // end of ModalBottomSheetLayout

}

@Composable
fun LessonItem(
    lesson: Lesson,
    isCompleted: Boolean,
    onClick: () -> Unit,
    onCheckboxClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f) // 90% width for better centering
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Checkbox to mark completion
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { onCheckboxClick() },
            modifier = Modifier.padding(end = 16.dp)
        )

        // Lesson title and details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Lesson ${lesson.order}",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        // Chevron icon (optional)
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "View lesson",
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}