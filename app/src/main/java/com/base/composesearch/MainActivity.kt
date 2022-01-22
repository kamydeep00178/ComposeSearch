package com.base.composesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.base.composesearch.ui.theme.ComposeSearchTheme
import com.base.composesearch.ui.theme.Shapes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSearchTheme {
                val dogViewModel = hiltViewModel<DogViewModel>()
                val searchText by dogViewModel.searchText.collectAsState(initial = dogViewModel.searchText.value)
                val list by dogViewModel.dogListData.collectAsState(initial = emptyList())
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LaunchedEffect(key1 = Unit)
                    {
                        dogViewModel.getData()
                    }
                    Column {
                        SearchView(searchText, dogViewModel::onSearchTextChange)
                        ListDataView(searchText = searchText, dogList = list)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchView(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "")
        },
        label = { Text(text = "Search here ...") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ListDataView(
    searchText: String,
    dogList: List<DogModel>
) {
    LazyColumn {
        itemsIndexed(dogList) { _, item ->
            DogItemCard(searchText = searchText, itemName = item)
        }
    }
}

@Composable
fun DogItemCard(searchText: String, itemName: DogModel) {
    val subStrings = getSubStrings(searchText.lowercase(), itemName.name.lowercase())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = Shapes.small
    ) {
        Text(
            text = buildAnnotatedString {
                append(itemName.name)
                addStyle(
                    style = SpanStyle(
                        color = if (searchText.isNotEmpty()) Color.LightGray else Color.DarkGray
                    ),
                    start = 0,
                    end = itemName.name.length
                )
                if (searchText.isNotEmpty()) {
                    for (subStringRange in subStrings) {
                        addStyle(
                            style = SpanStyle(Color.Black),
                            start = subStringRange.first,
                            end = subStringRange.last + 1
                        )
                    }
                }
            },
            modifier = Modifier.padding(6.dp)
        )
    }
}

private fun getSubStrings(subString: String, text: String): List<IntRange> {
    return subString.toRegex()
        .findAll(text)
        .map { it.range }
        .toList()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSearchTheme {
    }
}