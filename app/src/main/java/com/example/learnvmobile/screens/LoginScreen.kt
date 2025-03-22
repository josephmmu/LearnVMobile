package com.example.learnvmobile.screens

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnvmobile.R
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.google.GoogleAuthClient
import com.example.learnvmobile.presentation.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    mainViewModel: MainViewModel,
    onLogin:(id : Int) -> Unit,
    toRegister:(id : Int) -> Unit,
    onSignInSuccess: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as? Activity

    var email by remember { mutableStateOf("") }
    val isEmailValid = email.isNotEmpty() && email.contains('@')
    var password by remember { mutableStateOf("") }
    val isPasswordValid = password.isNotEmpty() && password.length >= 8

    var wasEmailTouched by remember { mutableStateOf(false) }
    var wasPasswordTouched by remember { mutableStateOf(false) }

    val user = User(0, "", email, password, "", "")

    val loginState by mainViewModel.loginState

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)
        .imePadding(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth(0.25f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login",
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )

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
            // JUST USE NORMAL TEXT Fields
            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {password = it ; wasPasswordTouched = true},
                placeholder =  {Text(text = "Go0dp@ssword")},
                label = {Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.size(40.dp))

            Button (
                onClick = {
                    if (isEmailValid && isPasswordValid) {
                        mainViewModel.checkUserExists(
                            email = email,
                            onUserExists = { user ->
                                if (user.password == password) { // Add password check
                                    onLogin(user.id)
                                } else {
                                    Toast.makeText(context, "Incorrect password!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onUserNotFound = {
                                Toast.makeText(context, "User not found! Please register.", Toast.LENGTH_LONG).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Invalid email or password!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login",
                    fontSize = 17.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            //Spacer(modifier = Modifier.size(50.dp))

            TextButton(
               modifier = Modifier
                   .align(alignment = Alignment.CenterHorizontally),
                onClick = {toRegister(user.id)},
                ) {
                Text(text= "No account? Register now pls")
            }

            Button(
                //onClick = { launcher.launch(signInClient.signInIntent) },
//                onClick = {launchGoogleSignIn(activity)},
                onClick = {
                    activity?.let {
                        mainViewModel.signIn(it) { success ->
                            if (success) {
                                println("Sign-in yay")
                            } else {
                                println(" Google Sign-In Failed")
                            }
                        }
                    }
                          },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Sign in with Google", color = Color.White)
            }

            // DELETE ALL USERS
            Button (onClick = {mainViewModel.deleteAllUsers()},modifier = Modifier.fillMaxWidth()) { Text(text = "Delete alllllll the users", fontSize = 17.sp, modifier = Modifier.padding(vertical = 8.dp))}




        }// Baby Column
    } // Parent Column

}