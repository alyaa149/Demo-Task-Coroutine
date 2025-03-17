package com.example.implab.searchBar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.implab.searchBar.ui.theme.ImpLabTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce

class searchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    SearchScreen()
        }
    }
}

class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableSharedFlow<String>()
   // val searchQuery: SharedFlow<String> = _searchQuery

    private val _filteredNames = MutableSharedFlow<List<String>>()
    val filteredNames: SharedFlow<List<String>> = _filteredNames

    private val allNames = listOf(
        "Aliaa", "Ali","Omar"
    )

    init {
        viewModelScope.launch {
            _searchQuery
                .collectLatest { query ->
                    val filtered = if (query.isBlank()) {
                        allNames
                    } else {
                        allNames.filter { name ->
                            name.startsWith(query, ignoreCase = true)
                        }
                    }
                    _filteredNames.emit(filtered)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }
}

@Composable
fun SearchScreen(viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredNames by viewModel.filteredNames.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(top =50.dp)) {
        BasicTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.onSearchQueryChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text("Search names...")
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredNames) { name ->
                Text(
                    text = name,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
