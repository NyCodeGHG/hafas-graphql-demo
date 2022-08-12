package dev.nycode.hafas_graphql_demo

import androidx.compose.runtime.*
import com.apollographql.apollo3.ApolloClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.time.Duration.Companion.milliseconds

fun main() {
    val apolloClient = ApolloClient.Builder()
        .serverUrl("https://hafas-graphql-nightly.nycode.dev/")
        .build()
    renderComposable(rootElementId = "root") {
        val flow = remember { MutableStateFlow("") }
        val text by flow.collectAsState()
        val scope = rememberCoroutineScope()
        val places by flow
            .debounce(300.milliseconds)
            .map { it.ifBlank { null } }
            .map {
                it?.let {
                    apolloClient.query(SearchPlaceQuery(it)).execute().data?.searchPlace
                }
            }
            .collectAsState(null)
        Input(InputType.Text) {
            value(text)
            onInput {
                scope.launch {
                    flow.emit(it.value)
                }
            }
        }
        if (places == null) {
            P {
                Text("No places found.")
            }
        } else {
            Ul {
                places?.forEach { place ->
                    Li {
                        Text(place.name)
                    }
                }
            }
        }
    }
}
