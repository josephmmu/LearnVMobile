package com.example.learnvmobile.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.presentation.MainViewModel
import com.example.learnvmobile.presentation.common.CourseDropDown


@Composable
fun SetupScreen(
    mainViewModel: MainViewModel,
    onRegister:(id :Int) -> Unit,
    onBack:() -> Unit) {

    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var courseID by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    val isEmailValid = email.isNotEmpty() && email.contains('@')
    var wasEmailTouched by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var passwordTest by remember { mutableStateOf("") }

    val isPasswordValid = password.isNotEmpty() && password.length >= 8
    val isPasswordTestValid = passwordTest.isNotEmpty() && passwordTest.length >= 8

    var wasPasswordTouched by remember { mutableStateOf(false) }
    var wasPasswordTestTouched by remember { mutableStateOf(false) }

    val user = User(0, fullName, email, password, courseID, "")

    // We might not need this, maybe for the Home screen
//    LaunchedEffect(
//        key1 = true,
//        block = { mainViewModel.getUserById(id = userId) }
//    )


    Column (
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Full name and Course
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = fullName,
                onValueChange = {fullName = it},
                label = {Text(text = "Full Name")},
                singleLine = true,
                colors = TextFieldDefaults.colors(),
                placeholder = { Text(text = "Fullname") }
            )

            Spacer(modifier = Modifier.size(20.dp))

            courseID = CourseDropDown()
        }

        Spacer(modifier = Modifier.size(50.dp))

        // Email
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {email = it ; wasEmailTouched = true},
                placeholder = {Text(text = "generic@email.com")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                label = {Text(text = "Email")},
                singleLine = true,
                isError = wasEmailTouched && !isEmailValid,
                supportingText = {
                    if (wasEmailTouched && !isEmailValid) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Not a real email bro",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.size(50.dp))

        // Password and Validation
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {password = it ; wasPasswordTouched = true},
                placeholder =  {Text(text = "Go0dp@ssword")},
                label = {Text(text = "Password")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = wasPasswordTouched && !isPasswordValid,
                colors = TextFieldDefaults.colors(),
                supportingText = {
                    if (wasPasswordTouched && !isPasswordValid) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Make it at least 8 characters, guy",
                            color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordTest,
                onValueChange = {passwordTest = it ; wasPasswordTestTouched = true},
                placeholder =  {Text(text = "Go0dp@ssword")},
                label = {Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = wasPasswordTestTouched && !isPasswordTestValid,
                colors = TextFieldDefaults.colors(),
                supportingText = {
                    if (wasPasswordTestTouched && !isPasswordTestValid) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Make it at least 8 characters, guy",
                            color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Spacer(modifier = Modifier.size(30.dp))

        Button(onClick = {

            if (isEmailValid && isPasswordValid) {
                mainViewModel.insertUserIfNotExists(
                    user = user,
                    onUserExists = {
                        Toast.makeText(context, "User already exists!", Toast.LENGTH_SHORT).show()
                        // go to main screen
                    },
                    onUserInserted = {insertedId ->
                        println("User inserted successfully with ID: $insertedId")
                        onRegister(insertedId.toInt()) // Pass the correct ID

                    }
                )
            } else{
                println(isEmailValid)
                println(isPasswordValid)
                Toast.makeText(context,"Are you even trying?", Toast.LENGTH_LONG).show()
            }


        }
        ) {

            Text(text = "Finish Registering")

        }

        Spacer(modifier = Modifier.size(30.dp))



    }

}
