package com.example.learnvmobile.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.learnvmobile.R
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.presentation.common.SubjectCard
import com.example.learnvmobile.presentation.common.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    userId: Int,
    onCourseSelected: (String) -> Unit,
    onBack:() -> Unit // Make it logout
) {

    LaunchedEffect(Unit) {mainViewModel.loadCourses()}

    // for Navigation drawer
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    // For top Bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    LaunchedEffect(userId) {
        Log.d("HomeScreen", "Received userId: $userId")
        mainViewModel.getUserById(userId)
    }

    LaunchedEffect(
        key1 = true,
        block = { mainViewModel.getUserById(id = userId) }
    )

    Scaffold { paddingValues ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet() {
                    DrawerContent(mainViewModel = mainViewModel, userId = userId)
                }
            }
        ) {
            Scaffold (
                topBar = {
                    TopBar(
                        onOpenDrawer = {
                            scope.launch{
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        navController = navController
                    )
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { paddingValues ->
                ScreenContent(paddingValues, mainViewModel, onCourseSelected)
            }
        }// end of ModalNavigationDrawer
    }
}

// Only Temporary but these should be the subjects
@Composable
fun ScreenContent(paddingValues: PaddingValues, mainViewModel: MainViewModel, onCourseSelected: (String) -> Unit) {
    LazyColumn (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp
        )
    ){
        items(mainViewModel.courses) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        onCourseSelected(course.courseId)
                    }
            ) {
                Text (
                    text = course.title,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

//        item {
//            // Put the subject cards here
//            SubjectCard(painter = painterResource(id = R.drawable.mathsmol), subjectName = "Math")
//            Spacer(modifier = Modifier.height(16.dp))
//        }


    }
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier, mainViewModel: MainViewModel, userId: Int) {


    val user by produceState<User?>(initialValue = null) {
        value = mainViewModel.getUserById2(userId)
    }

    if (user == null) {
        Text("Loading user...")
        return
    }

    Text(text = "Welcome ${user!!.fullName ?: "Guest"}",
        fontSize = 17.sp,
        modifier = Modifier.padding(16.dp))

    Text(text = "User ID ${user!!.id}",
        fontSize = 17.sp,
        modifier = Modifier.padding(16.dp))

    HorizontalDivider()

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Book,
                contentDescription = "Subject 1"
            )
        },
        label = {
            Text(text = "Subject 1",
                fontSize = 17.sp,
                modifier = Modifier.padding(16.dp))
        },
        selected = false,
        onClick = {  }
    )

    Spacer(modifier = Modifier.height( 8.dp ))

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Book,
                contentDescription = "Subject 2"
            )
        },
        label = {
            Text(text = "Subject 2",
                fontSize = 17.sp,
                modifier = Modifier.padding(16.dp))
        },
        selected = false,
        onClick = {  }
    )

    Spacer(modifier = Modifier.height( 8.dp ))

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Book,
                contentDescription = "Subject 3"
            )
        },
        label = {
            Text(text = "Subject 3",
                fontSize = 17.sp,
                modifier = Modifier.padding(16.dp))
        },
        selected = false,
        onClick = {  }
    )
}