package fr.isen.pasqualini.theodroidburger

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.pasqualini.theodroidburger.ui.theme.TheoDroidBurgerTheme
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Head()
            ReviewActivityContent()
        }
    }
}

@Composable
fun Head() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo de la société
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la société",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ReviewActivityContent() {
    var pastOrders by remember { mutableStateOf(emptyList<Order>()) }
    var confirmationDialogShown by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Récupérer les commandes passées depuis le webservice
        getListOfOrders("1", 357,
            onSuccess = { orders ->
                pastOrders = orders
            },
            onFailure = { error ->
                // Gérer l'échec de la récupération des commandes passées
                Log.e("ReviewActivity", "Failed to fetch orders: $error")
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Afficher les commandes passées
        PastOrdersList(pastOrders)

        // Afficher la boîte de dialogue de confirmation si confirmationDialogShown est vrai
        if (confirmationDialogShown) {
            ConfirmationDialog(onDismiss = { confirmationDialogShown = false })
        }
    }
}


@Composable
fun ConfirmationDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmation de commande") },
        text = { Text(text = "Votre commande a été prise en compte avec succès.") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        }
    )
}

@Composable
fun PastOrdersList(orders: List<Order>) {
    LazyColumn {
        items(orders) { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Nom: ${order.firstName} ${order.lastName}")
                    Text(text = "Adresse: ${order.address}")
                    Text(text = "Téléphone: ${order.phone}")
                    Text(text = "Burger: ${order.burger}")
                    Text(text = "Heure de livraison: ${order.deliveryTime}")
                }
            }
        }
    }
}

fun getListOfOrders(idShop: String, idUser: Int, onSuccess: (List<Order>) -> Unit, onFailure: (String) -> Unit) {
    val url = "http://test.api.catering.bluecodegames.com/listorders"

    val jsonObject = JSONObject().apply {
        put("id_shop", idShop)
        put("id_user", idUser)
    }

    val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e.message ?: "Unknown error")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                // Parse the JSON response and convert it to a list of orders
                val orders = parseOrdersFromJson(responseBody)
                onSuccess(orders)
            } else {
                onFailure("Failed to fetch orders. Response: $responseBody")
            }
        }
    })
}

//fun parseOrdersFromJson(jsonString: String?): List<Order> {
//    val orders = mutableListOf<Order>()
//    try {
//        val jsonObject = JSONObject(jsonString)
//        val msg = jsonObject.getString("msg")
//        val msgObject = JSONObject(msg)
//        val firstName = msgObject.getString("firstname")
//        val lastName = msgObject.getString("lastname")
//        val address = msgObject.getString("address")
//        val phone = msgObject.getString("phone")
//        val burger = msgObject.getString("burger")
//        val deliveryTime = msgObject.getString("delivery_time")
//        val order = Order(firstName, lastName, address, phone, burger, deliveryTime)
//        orders.add(order)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return orders
//}

fun parseOrdersFromJson(jsonString: String?): List<Order> {
    // Afficher le JSON retourné dans les logs
    Log.d("JSON", "JSON reçu du service web : $jsonString")

    val orders = mutableListOf<Order>()
    try {
        val jsonObject = JSONObject(jsonString)
        // Traitement du JSON pour extraire les détails de la commande...
        // (Votre code de traitement JSON continue ici)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return orders
}



// Supposons que Order soit une classe simple avec une méthode toString() pour l'affichage.
data class Order(
    val firstName: String,
    val lastName: String,
    val address: String,
    val phone: String,
    val burger: String,
    val deliveryTime: String
)



