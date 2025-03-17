package com.example.implab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.implab.AllProducts.view.AllProductsActivity
import com.example.implab.FavProducts.view.FavActivity
import com.example.implab.searchBar.searchActivity
import com.example.implab.ui.theme.ImpLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val intent = Intent(context, AllProductsActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "All products",
                modifier = modifier
            )
        }
        Button(onClick = {
            val intent = Intent(context, FavActivity::class.java)
            context.startActivity(intent)

        }) {
            Text(
                text = "fav products",
                modifier = modifier
            )
        }
        Button(onClick = {
            val intent = Intent(context, searchActivity::class.java)
            context.startActivity(intent)

        }) {
            Text(
                text = "click to search",
                modifier = modifier
            )
        }
    }
}

