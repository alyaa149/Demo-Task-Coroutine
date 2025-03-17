package com.example.implab.AllProducts.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.implab.AllProducts.viewmodel.AllProductsFactory
import com.example.implab.AllProducts.viewmodel.AllProductsViewModel
import com.example.implab.data.local.ProductsDataBase
import com.example.implab.data.local.ProductsLocalDataSource
import com.example.implab.data.models.Product
import com.example.implab.data.remote.ProductsRemoteDataSource
import com.example.implab.data.remote.Response
import com.example.implab.data.remote.RetrofitHelper
import com.example.implab.data.repo.RepoImpl
import com.example.implab.ui.theme.ImpLabTheme
import kotlinx.coroutines.launch

class AllProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpLabTheme {
                AllProductsState(
                    viewModel(
                        factory = AllProductsFactory(
                            RepoImpl(
                                ProductsLocalDataSource(ProductsDataBase.getDatabase(this).productDao()),
                                ProductsRemoteDataSource(RetrofitHelper.service)
                            )
                    )
                    )
                    )
            }
        }
    }
}
@Composable
fun AllProductsState(viewModel:AllProductsViewModel){
    val uiState by viewModel.productsList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when(uiState){
        is Response.Loading->{
            LoadingIndicator()
        }
        is Response.Success->{
            val products = (uiState as Response.Success).data
            AllProductsUI(viewModel,products)

        }
        is Response.Error->{
            val error = (uiState as Response.Error).error
//val messageState = viewModel.message
            Log.i("error",error.message.toString())
          //  Toast.makeText(LocalContext.current,error.message,Toast.LENGTH_LONG).show()
        }}

}

@Composable
fun AllProductsUI(viewModel:AllProductsViewModel,products : List<Product>?) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope =rememberCoroutineScope()
    Scaffold (
        snackbarHost = {SnackbarHost(snackBarHostState)}
    ){ innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(products?.size ?: 0) {
ProductRow(products?.get(it),"Fav",
    {
            viewModel.addToFav(
                products?.get(it)
            )

    }
    ) } } }
    }
}

@Composable
fun LoadingIndicator() {
    Box (
        modifier = Modifier.fillMaxSize().wrapContentSize()
    ){
        CircularProgressIndicator()
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(product: Product?, actionName: String, action: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
        ) {
            GlideImage(
                modifier = Modifier.size(height = 100.dp, width = 50.dp),
                model = product?.thumbnail,
                contentDescription = "Product Image",
            )
            Column(
                modifier = Modifier
                    .padding(start = 3.dp)
            ) {
                Text(text = product?.title.orEmpty())
                Text(text = "Price: ${product?.price ?: "N/A"}")
                Text(text = "Rating: ${product?.rating ?: "N/A"} ⭐")
                Text(
                    text = product?.description.orEmpty(),
                    maxLines = 1,
                )
                Button(
                    onClick = action,
                ) {
                    Text(actionName)
                }
            }
        }
    }
}
