package com.example.learnvmobile.utils

import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun googleSignInOptions() : GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("AIzaSyAAjkKZCzhw0As1HuUBCy7MtB0OxklsHds")
        .requestEmail()
        .build()
}