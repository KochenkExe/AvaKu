package hj.dp.avaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hj.dp.avaku.ui.theme.AvaKuTheme
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvaKuTheme {
                AvaKuApp()
            }
        }
    }
}

enum class AppScreen {
    Registration,
    Login,
    Profile,
    Avatar
}

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val city: String,
    val hobby: String
)

@Composable
fun AvaKuApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Registration) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    when (currentScreen) {
        AppScreen.Registration -> {
            RegistrationScreen(
                onSave = { profile ->
                    userProfile = profile
                    currentScreen = AppScreen.Login
                },
                onNavigateToLogin = {
                    currentScreen = AppScreen.Login
                }
            )
        }

        AppScreen.Login -> {
            LoginScreen(
                registeredUser = userProfile,
                onLoginSuccess = {
                    currentScreen = AppScreen.Profile
                },
                onNavigateToRegistration = {
                    currentScreen = AppScreen.Registration
                }
            )
        }

        AppScreen.Profile -> {
            val profile = userProfile
            if (profile == null) {
                currentScreen = AppScreen.Registration
            } else {
                ProfileScreen(
                    profile = profile,
                    onLogout = {
                        currentScreen = AppScreen.Login
                    },
                    onShowAvatar = {
                        currentScreen = AppScreen.Avatar
                    }
                )
            }
        }

        AppScreen.Avatar -> {
            AvatarScreen(
                onBack = {
                    currentScreen = AppScreen.Profile
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onSave: (UserProfile) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var hobby by rememberSaveable { mutableStateOf("") }

    val isValid = listOf(
        firstName,
        lastName,
        username,
        email,
        password,
        phoneNumber,
        city,
        hobby
    ).all { it.isNotBlank() }

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Registrasi User") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Lengkapi data diri Anda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            RegistrationField(
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name"
            )
            RegistrationField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name"
            )
            RegistrationField(
                value = username,
                onValueChange = { username = it },
                label = "Username"
            )
            RegistrationField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            RegistrationField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true
            )
            RegistrationField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Nomor Telepon",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            RegistrationField(
                value = city,
                onValueChange = { city = it },
                label = "Kota Domisili"
            )
            RegistrationField(
                value = hobby,
                onValueChange = { hobby = it },
                label = "Hobi Favorit"
            )

            Button(
                onClick = {
                    onSave(
                        UserProfile(
                            firstName = firstName,
                            lastName = lastName,
                            username = username,
                            email = email,
                            password = password,
                            phoneNumber = phoneNumber,
                            city = city,
                            hobby = hobby
                        )
                    )
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Sudah punya akun? Login")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    registeredUser: UserProfile?,
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            snackbarHostState.showSnackbar(message)
            message = ""
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Login") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Masuk ke akun Anda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            RegistrationField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                modifier = Modifier.fillMaxWidth()
            )
            RegistrationField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (registeredUser == null) {
                        message = "Belum ada data registrasi. Mohon daftar terlebih dahulu."
                    } else if (username == registeredUser.username && password == registeredUser.password) {
                        onLoginSuccess()
                    } else {
                        message = "Username atau password salah"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotBlank() && password.isNotBlank()
            ) {
                Text(text = "Login")
            }
            TextButton(onClick = {
                message = "Fitur lupa password belum tersedia"
            }) {
                Text(text = "Lupa Password")
            }
            TextButton(onClick = onNavigateToRegistration) {
                Text(text = "Belum punya akun? Registrasi")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onLogout: () -> Unit,
    onShowAvatar: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profil User") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(text = "Logout", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Halo, ${profile.firstName} ${profile.lastName}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileInfoRow(title = "First Name", value = profile.firstName)
                    ProfileInfoRow(title = "Last Name", value = profile.lastName)
                    ProfileInfoRow(title = "Username", value = profile.username)
                    ProfileInfoRow(title = "Email", value = profile.email)
                    ProfileInfoRow(title = "Nomor Telepon", value = profile.phoneNumber)
                    ProfileInfoRow(title = "Kota Domisili", value = profile.city)
                    ProfileInfoRow(title = "Hobi Favorit", value = profile.hobby)
                }
            }

            Button(
                onClick = onShowAvatar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Lihat Avatar")
            }

            Text(
                text = "Terima kasih telah menggunakan aplikasi AvaKu!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarScreen(
    onBack: () -> Unit
) {
    var showEyes by rememberSaveable { mutableStateOf(true) }
    var showEyebrows by rememberSaveable { mutableStateOf(true) }
    var showNose by rememberSaveable { mutableStateOf(true) }
    var showMouth by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Avatar") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sesuaikan avatar Anda",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dasar),
                        contentDescription = "Dasar wajah",
                        modifier = Modifier.fillMaxSize()
                    )
                    if (showEyes) {
                        Image(
                            painter = painterResource(id = R.drawable.mata),
                            contentDescription = "Mata",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (showEyebrows) {
                        Image(
                            painter = painterResource(id = R.drawable.alis),
                            contentDescription = "Alis",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (showMouth) {
                        Image(
                            painter = painterResource(id = R.drawable.mulut),
                            contentDescription = "Mulut",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (showNose) {
                        Image(
                            painter = painterResource(id = R.drawable.hidung),
                            contentDescription = "Hidung",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AvatarFeatureToggle(
                    label = "Tampilkan Mata",
                    checked = showEyes,
                    onCheckedChange = { showEyes = it }
                )
                AvatarFeatureToggle(
                    label = "Tampilkan Alis",
                    checked = showEyebrows,
                    onCheckedChange = { showEyebrows = it }
                )
                AvatarFeatureToggle(
                    label = "Tampilkan Mulut",
                    checked = showMouth,
                    onCheckedChange = { showMouth = it }
                )
                AvatarFeatureToggle(
                    label = "Tampilkan Hidung",
                    checked = showNose,
                    onCheckedChange = { showNose = it }
                )
            }
        }
    }
}

@Composable
fun AvatarFeatureToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ProfileInfoRow(title: String, value: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun RegistrationField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun RegistrationPreview() {
    AvaKuTheme {
        RegistrationScreen(onSave = {}, onNavigateToLogin = {})
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun LoginPreview() {
    AvaKuTheme {
        LoginScreen(registeredUser = null, onLoginSuccess = {}, onNavigateToRegistration = {})
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProfilePreview() {
    AvaKuTheme {
        ProfileScreen(
            profile = UserProfile(
                firstName = "Ava",
                lastName = "Ku",
                username = "avaku",
                email = "ava@ku.com",
                password = "password",
                phoneNumber = "081234567890",
                city = "Bandung",
                hobby = "Membaca"
            ),
            onLogout = {},
            onShowAvatar = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun AvatarPreview() {
    AvaKuTheme {
        AvatarScreen(onBack = {})
    }
}