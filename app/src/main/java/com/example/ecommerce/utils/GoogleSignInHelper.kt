//package com.example.ecommerce.utils
//
//import android.content.Context
//import android.util.Log
//import com.example.ecommerce.R
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//
//object GoogleSignInHelper {
//
//    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
//
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(R.string.web_client_id))
//            .requestEmail()
//            .requestProfile()
//            .build()
//
//        Log.d("GoogleSignInHelper", "GoogleSignInOptions configured successfully")
//        return GoogleSignIn.getClient(context, gso)
//    }
//}
