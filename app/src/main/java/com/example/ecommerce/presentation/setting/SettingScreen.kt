package com.example.ecommerce.presentation.setting

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.auth.AuthViewModel
import com.example.ecommerce.presentation.cart.CartViewModel
import com.example.ecommerce.presentation.setting.components.SectionHeader
import com.example.ecommerce.presentation.setting.components.SettingsTextField
import com.example.ecommerce.presentation.wishlist.WishListViewModel
import com.example.ecommerce.ui.theme.Montserrat
import kotlinx.coroutines.delay
import javax.annotation.meta.When

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    wishListViewModel: WishListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    LaunchedEffect(Unit) {
        settingsViewModel.getUserProfile()
    }


    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    val userProfileState by settingsViewModel.userProfile.collectAsState()

    val wishlistState by wishListViewModel.state.collectAsState()
    val wishlistCount = wishlistState.allProducts.size

    val cartState by cartViewModel.state.collectAsState()
    val cartCount = cartState.cartItems.sumOf { it.quantity }

    var isLogoutVisible by remember { mutableStateOf(false) }

    var countdown by remember { mutableIntStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }


    LaunchedEffect(isCountingDown, isSuccess) {
        if (isCountingDown && isSuccess) {
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            isCountingDown = false
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is Result.Success -> {
                Toast.makeText(
                    context,
                    (authState as Result.Success<String>).data,
                    Toast.LENGTH_SHORT
                ).show()

                countdown = 60
                isCountingDown = true
                isSuccess = true

                authViewModel.resetAuthState()
            }

            is Result.Error -> {
                val error = (authState as Result.Error).message.toString()
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                isCountingDown = false
            }

            else -> {

            }
        }
    }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("**********") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }

    LaunchedEffect(userProfileState) {
        when (userProfileState) {
            is Result.Success -> {
                val userProfile = (userProfileState as Result.Success<UserProfile?>).data

                if (fullName.isEmpty()) {
                    fullName =
                        userProfile?.displayName.takeIf { !it.isNullOrEmpty() } ?: "Anonymous"
                }
                email = userProfile?.emailAddress ?: ""
                address = userProfile?.address ?: ""
                city = userProfile?.city ?: ""
                country = userProfile?.country ?: ""
                profilePicture = userProfile?.profilePicture ?: ""

                isLoading = false
            }

            is Result.Loading -> {
                isLoading = true
            }

            else -> {
                isLoading = false
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFF83758),
                    strokeWidth = 4.dp
                )
            }
        } else {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                            .statusBarsPadding()
                            .fillMaxWidth()
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier.size(80.dp)
                        ) {

                            val displayImage: Any? = when {
                                selectedImageUri != null -> selectedImageUri
                                profilePicture.isNotBlank() -> {
                                    try {
                                        val decodedBytes =
                                            Base64.decode(profilePicture, Base64.DEFAULT)
                                        BitmapFactory.decodeByteArray(
                                            decodedBytes,
                                            0,
                                            decodedBytes.size
                                        )
                                    } catch (e: IllegalArgumentException) {
                                        null
                                    }
                                }

                                else -> null
                            }

                            if (displayImage != null) {
                                AsyncImage(
                                    model = displayImage,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.profile),
                                    contentDescription = "Default Profile",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF83758))
                                    .clickable { galleryLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile Picture",
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.White
                                )
                            }
                        }


                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = fullName,
                                fontFamily = Montserrat,
                                fontSize = 25.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        navHostController.navigate(Routes.WishlistScreen)
                                    }, contentPadding = PaddingValues(
                                        start = 0.dp, end = 10.dp, top = 0.dp
                                    ), colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.Gray
                                    )
                                ) {
                                    Text(
                                        text = "$wishlistCount WishList",
                                        fontFamily = Montserrat,
                                        fontSize = 18.sp,
                                    )

                                }
                                Text(
                                    text = "•",
                                    fontFamily = Montserrat,
                                    fontSize = 25.sp,
                                    color = Color.Gray,
                                )

                                TextButton(
                                    onClick = {
                                        navHostController.navigate(Routes.CartScreen)
                                    }, contentPadding = PaddingValues(
                                        start = 10.dp, end = 10.dp, top = 0.dp
                                    ), colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.Gray
                                    )

                                ) {
                                    Text(
                                        text = "$cartCount Cart",
                                        fontFamily = Montserrat,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        }
                    }
                }, bottomBar = {
                    if (!isLogoutVisible) {
                        BottomAppBar(
                            containerColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        isLogoutVisible = !isLogoutVisible
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(68.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.LightGray.copy(alpha = .5f)
                                    )
                                ) {
                                    Text(
                                        text = "Log Out",
                                        fontSize = 18.sp,
                                        color = Color(0xFFF83758),
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }, containerColor = Color(0xFFF9F9F9)
            ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp, top = 16.dp)
                ) {

                    SectionHeader("Personal Details")

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "Full Name",
                        value = fullName,
                        onValueChange = { fullName = it },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "Email Address",
                        value = email,
                        onValueChange = { }, // Read-only, auto-populated from Firebase Auth
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "Password",
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true,
                        trailingText = if (countdown == 0) "Reset Password" else "Resend in ${countdown}s",
                        enabled = false,
                        onTrailingClick = {
                            if (userProfileState !is Result.Loading) {
                                if (countdown == 0) {
                                    Log.d("userEmail", "SettingsScreen: $email")
                                    authViewModel.resetPassword(email)
                                }
                            }
                        })

                    Spacer(modifier = Modifier.height(24.dp))

                    // Business Address Details Section
                    SectionHeader("Business Address Details")

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "Address", value = address, onValueChange = { address = it })

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "City", value = city, onValueChange = { city = it })

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingsTextField(
                        label = "Country", value = country, onValueChange = { country = it })

                    Spacer(modifier = Modifier.height(32.dp))

                    if (userProfileState is Result.Error) {
                        Text(
                            text = "Error: ${(userProfileState as Result.Error).message}",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    // Save Button
                    Button(
                        onClick = {
                            val updatedUserProfile = UserProfile(
                                displayName = fullName,
                                emailAddress = email,
                                address = address,
                                city = city,
                                country = country,
                                profilePicture = profilePicture
                            )
                            settingsViewModel.saveUserProfile(
                                userProfile = updatedUserProfile,
                                selectedImageUri = selectedImageUri
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF83758)
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (isLogoutVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = .5f))
                        .clickable {
                            isLogoutVisible = false
                        })
            }

            AnimatedVisibility(
                visible = isLogoutVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                modifier = Modifier
                    .clickable(enabled = false) {})
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(color = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Are you sure you want to log out ?",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.Black
                                    ),
                                    border = BorderStroke(1.dp, Color.Black.copy(alpha = .4f)),
                                    onClick = {
                                        isLogoutVisible = false
                                    }) {
                                    Text(
                                        text = "No",
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp,
                                    )
                                }

                                Button(
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF83758),
                                        contentColor = Color.White
                                    ),
                                    onClick = {
                                        authViewModel.logout()
                                        navHostController.navigate(Routes.LoginScreen) {
                                            popUpTo(0) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                        authViewModel.resetAuthState()
                                        isLogoutVisible = false
                                    }) {
                                    Text(
                                        text = "Yes",
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

