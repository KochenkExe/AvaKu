package hj.dp.avaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hj.dp.avaku.ui.theme.AvaKuTheme

private enum class AppScreen {
    Search,
    RecipeDetail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvaKuTheme {
                Surface(color = Color(0xFFF6F6F6)) {
                    AvaKuApp()
                }
            }
        }
    }
}

@Composable
private fun AvaKuApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Search) }
    var showAllergyDialog by rememberSaveable { mutableStateOf(false) }

    when (currentScreen) {
        AppScreen.Search -> SearchScreen(onOpenRecipe = { currentScreen = AppScreen.RecipeDetail })
        AppScreen.RecipeDetail -> RecipeDetailScreen(
            onBack = { currentScreen = AppScreen.Search },
            onEggClicked = { showAllergyDialog = true }
        )
    }

    if (showAllergyDialog) {
        AlertDialog(
            onDismissRequest = { showAllergyDialog = false },
            confirmButton = {
                TextButton(onClick = { showAllergyDialog = false }) {
                    Text(text = "Mengerti")
                }
            },
            title = { Text(text = "Perhatian Alergi") },
            text = { Text(text = "Bahan ini mengandung telur. Hindari jika Anda memiliki alergi terhadap telur.") }
        )
    }
}

@Composable
private fun SearchScreen(onOpenRecipe: () -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(onClick = { /* No-op back on root screen */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFF1D6FF2)
                )
            }

            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text(text = "Cari resep masakan") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFB3B3B3))
            )

            Button(
                onClick = onOpenRecipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.buttonColors(containerColor = Color(0xFF3B95FA))
            ) {
                Text(
                    text = "Nasi Goreng Spesial",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun RecipeDetailScreen(onBack: () -> Unit, onEggClicked: () -> Unit) {
    val scrollState = rememberScrollState()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFF1D6FF2)
                )
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFFFE1B7), Color(0xFFFFF4E2))
                                ),
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Foto Nasi Goreng",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFAA6C1D)
                        )
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Nasi Goreng Spesial",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1B1B)
                        )
                        Text(
                            text = "Resep nasi goreng sederhana dan cepat. Cocok untuk anak-anak dan keluarga.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF565656)
                        )
                    }

                    IngredientCard(onEggClicked = onEggClicked)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PreparationSteps()
        }
    }
}

@Composable
private fun IngredientCard(onEggClicked: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Bahan-bahan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            IngredientRow("500 gram", "Nasi")
            IngredientRow("40 gram", "Bumbu halus")
            IngredientRow(
                amount = "2 butir",
                name = "Telur",
                highlighted = true,
                onClick = onEggClicked
            )
            IngredientRow("30 gr", "Saus tiram")
            IngredientRow("20 gr", "Kecap asin")
            IngredientRow("10 gr", "Kecap manis")
            IngredientRow("2 gr", "Lada")
            IngredientRow("5 gr", "Garam")
            IngredientRow("10 gr", "Minyak")
        }
    }
}

@Composable
private fun IngredientRow(
    amount: String,
    name: String,
    highlighted: Boolean = false,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (highlighted) Color(0xFFFCEFD6) else Color.White
    val borderColor = if (highlighted) Color(0xFFF29F5C) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = highlighted) { onClick() }
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color = Color(0xFF565656), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Normal,
                color = if (highlighted) Color(0xFFB55A17) else Color(0xFF2F2F2F)
            )
        }
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF2F2F2F)
        )
    }
}

@Composable
private fun PreparationSteps() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Langkah memasak",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val steps = listOf(
                "Panaskan wajan dengan api sedang dan tambahkan minyak goreng.",
                "Masukkan bumbu halus dan tumis hingga harum.",
                "Pecahkan 2 butir telur dan masak hingga matang.",
                "Masukkan nasi dan masak sejenak.",
                "Matikan api dan masukkan kecap asin, kecap manis, saus tiram, lada, serta kaldu.",
                "Campurkan hingga merata dan nyalakan api besar.",
                "Masak nasi dengan api besar hingga teksturnya sesuai dengan hasil yang diinginkan."
            )

            steps.forEachIndexed { index, step ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StepCircle(number = index + 1)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2F2F2F),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepCircle(number: Int) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(color = Color(0xFF3B95FA), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
