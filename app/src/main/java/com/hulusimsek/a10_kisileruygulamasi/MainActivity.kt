package com.hulusimsek.a10_kisileruygulamasi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hulusimsek.a10_kisileruygulamasi.entity.Kisiler
import com.hulusimsek.a10_kisileruygulamasi.ui.theme._10_KisilerUygulamasiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _10_KisilerUygulamasiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SayfaGecisleri(
                    )
                }
            }
        }
    }
}

@Composable
fun SayfaGecisleri() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "anasayfa") {
        composable("anasayfa") {
            AnaSayfa(navController = navController)
        }
        composable("kisi_kayit_sayfa") {
            KisiKayitSayfa()
        }
        composable(
            "kisi_detay_sayfa/{kisi}",
            arguments = listOf(navArgument("kisi") { type = NavType.StringType })) {
            val json = it.arguments?.getString("kisi")
            val nesne = Gson().fromJson(json,Kisiler::class.java)
            KisiDetaySayfa(nesne)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnaSayfa(modifier: Modifier = Modifier, navController: NavController) {
    val aramaYapiliyorMu = remember {
        mutableStateOf(false)
    }
    val tf = remember {
        mutableStateOf("")
    }
    val kisilerListesi = remember {
        mutableStateListOf<Kisiler>()
    }

    LaunchedEffect(key1 = true) {
        val k1 = Kisiler(1, "Ahmet", "1111")
        val k2 = Kisiler(2, "Zeynep", "2222")
        kisilerListesi.add(k1)
        kisilerListesi.add(k2)

    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (aramaYapiliyorMu.value) {
                        TextField(
                            value = tf.value,
                            onValueChange = { tf.value = it },
                            label = { Text(text = "Ara") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedLabelColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                unfocusedIndicatorColor = Color.White
                            )
                        )

                    } else {
                        Text(text = "Kişiler Uygulaması", color = Color.White)
                    }

                },
                actions = {
                    if (aramaYapiliyorMu.value) {
                        IconButton(onClick = {
                            aramaYapiliyorMu.value = false
                            tf.value = ""
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    } else {
                        IconButton(onClick = { aramaYapiliyorMu.value = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.teal_200))
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(
                    count = kisilerListesi.count(),
                    itemContent = {
                        val kisi = kisilerListesi[it]
                        Card(
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.clickable {
                                val kisiJson = Gson().toJson(kisi)
                                navController.navigate("kisi_detay_sayfa/${kisiJson}")
                            }) {
                                Row(
                                    modifier = Modifier
                                        .padding(all = 10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${kisi.kisi_ad} - ${kisi.kisi_tel}",
                                        color = Color.Black
                                    )
                                    IconButton(onClick = { Log.e("kişi sil","${kisi.kisi_ad} silindi") }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("kisi_kayit_sayfa") },
                containerColor = colorResource(id = R.color.teal_200),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            )
        }
    )
}