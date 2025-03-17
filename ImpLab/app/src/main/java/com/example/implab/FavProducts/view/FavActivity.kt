package com.example.implab.FavProducts.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.implab.AllProducts.view.AllProductsUI
import com.example.implab.AllProducts.view.LoadingIndicator
import com.example.implab.AllProducts.view.ProductRow
import com.example.implab.FavProducts.viewmodel.FavProductsFactory
import com.example.implab.FavProducts.viewmodel.FavProductsViewModel
import com.example.implab.data.local.ProductsDataBase
import com.example.implab.data.local.ProductsLocalDataSource
import com.example.implab.data.models.Product
import com.example.implab.data.remote.ProductsRemoteDataSource
import com.example.implab.data.remote.Response
import com.example.implab.data.remote.RetrofitHelper
import com.example.implab.data.repo.RepoImpl
import com.example.implab.ui.theme.ImpLabTheme
import kotlinx.coroutines.launch

class FavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpLabTheme {
                AllFavProductsUIState(
                    viewModel(
                        factory = FavProductsFactory(
                            RepoImpl(
                                ProductsLocalDataSource(
                                    ProductsDataBase.getDatabase(this).productDao()
                                ),
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
fun AllFavProductsUIState(viewModel: FavProductsViewModel) {
    val uiState by viewModel.productsList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when(uiState){
        is Response.Loading->{
            LoadingIndicator()
        }
        is Response.Success->{
            val products = (uiState as Response.Success).data
            AllFavProductsUI(viewModel,products)
        }
        is Response.Error->{
            val error = (uiState as Response.Error).error
//val messageState = viewModel.message
            Log.i("error",error.message.toString())
            //  Toast.makeText(LocalContext.current,error.message,Toast.LENGTH_LONG).show()
        }}

}

@Composable
fun AllFavProductsUI(viewModel: FavProductsViewModel, productsState: List<Product>?) {

   // val messageState = viewModel.message.observeAsState()
    viewModel.getFavProducts()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold (
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ){ innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(productsState?.size ?: 0) {
                    ProductRow(productsState?.get(it),"Delete",
                        {
                            viewModel.deleteProduct(productsState?.get(it))
                            scope.launch {
//                                if(messageState.value!=null){
//                                    snackBarHostState.showSnackbar(messageState.value.toString(),duration = SnackbarDuration.Short)
//
//                                }
                            }
                        }


                    )
                }

            }
        }
    }


}

