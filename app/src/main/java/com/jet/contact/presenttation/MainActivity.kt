package com.jet.contact.presenttation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil3.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jet.contact.R
import com.jet.contact.domain.MainViewModel
import com.jet.contact.model.Contact
import com.jet.contact.presenttation.ui.theme.JetContactAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1);
        }

        enableEdgeToEdge()
        setContent {
            val viewModel = MainViewModel(context = LocalContext.current)
            JetContactAppTheme {
                val contacts by viewModel.contacts.collectAsState()
                var l by remember { mutableStateOf("") }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    if (contacts.isNullOrEmpty()) {
                        NoContacts(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            contacts.forEach {
                                val f = it.name.substring(0, 1)
                                if (f != l) {
                                    l = f
                                    item {
                                        Text(
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp),
                                            text = "${f}"
                                        )
                                    }
                                }
                                item {
                                    ContactItem(
                                        contact = it
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

@Composable
fun NoContacts(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("empty_animation.json")
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(200.dp),
            composition = composition,
            progress = { progress }
        )
        Text(
            text = "No contacts"
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
) {

}

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    contact: Contact,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {

        Image(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            painter = if (contact.photoUri.isNullOrEmpty()) {
                painterResource(R.drawable.blank_user)
            } else {
                rememberAsyncImagePainter(contact.photoUri)
            },
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = contact.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = contact.number,
                fontSize = 12.sp,
                color = Color(0, 0, 0, 128)
            )
            if (!contact.accounts.isNullOrEmpty()) {
                Box {
                    contact.accounts.forEachIndexed { index, it ->
                        Image(
                            modifier = Modifier
                                .padding(start = (22 * index).dp)
                                .size(28.dp)
                                .border(1.dp, color = Color.White, shape = CircleShape)
                                .clip(CircleShape),
                            painter = painterResource(it.ICON_RES),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
) {
}


//@Preview(showBackground = true)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
}