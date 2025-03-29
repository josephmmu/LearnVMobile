package com.example.learnvmobile.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnvmobile.presentation.MainViewModel

@Composable
fun LessonListScreen(mainViewModel: MainViewModel, userId: Int, courseId: String) {
    LaunchedEffect(courseId) {
        mainViewModel.loadLessons(courseId)
        mainViewModel.loadUserProgress(userId, courseId)
    }

    LazyColumn(

    ) {
        items(mainViewModel.lessons) { lesson ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        mainViewModel.markLessonCompleted(userId, courseId, lesson.lessonId)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = mainViewModel.userProgress[lesson.lessonId] ?: false,
                    onCheckedChange = null
                )
                Text(text = lesson.title, fontSize = 18.sp)
            }

        }
    }

}