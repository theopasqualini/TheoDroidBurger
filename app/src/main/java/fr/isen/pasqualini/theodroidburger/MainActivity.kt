package fr.isen.pasqualini.theodroidburger

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.pasqualini.theodroidburger.ui.theme.TheoDroidBurgerTheme
import fr.isen.pasqualini.theodroidburger.ui.theme.secondary
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            TheoDroidBurgerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var jsonContent by remember { mutableStateOf("") }
                    Column {
                        CommandeBurger {}
                        JsonDisplay(jsonContent)
                    }


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandeBurger(onValidateClicked: () -> Unit) {
    var envoiReussi by remember { mutableStateOf(false) }
    var alertShown by remember { mutableStateOf(false) }
    var jsonContent by remember { mutableStateOf("") }


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

        // Titre de la commande
        Text(
            text = "Commande de Burger",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Champs de saisie
        var nom by remember { mutableStateOf("") }
        var prenom by remember { mutableStateOf("") }
        var adresse by remember { mutableStateOf("") }
        var numeroTelephone by remember { mutableStateOf("") }
        var heureLivraison by remember { mutableStateOf("") }
        var selectedBurger by remember { mutableStateOf("") }

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = prenom,
            onValueChange = { prenom = it },
            label = { Text("Prénom") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = adresse,
            onValueChange = { adresse = it },
            label = { Text("Adresse") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors()
        )

        OutlinedTextField(
            value = numeroTelephone,
            onValueChange = { numeroTelephone = it },
            label = { Text("Numéro de téléphone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Limite le clavier aux chiffres
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors()
        )

        val context = LocalContext.current
        val burgerlist = arrayOf("Burger du chef", "Cheese Burger", "Burger Montagnard", "Burger Italien", "Burgr Végétarien")
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(burgerlist[0]) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    burgerlist.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Fetching local context
        val mContext = LocalContext.current

        // Declaring and initializing a calendar
        val mCalendar = Calendar.getInstance()
        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]

        // Value for storing time as a string
        val mTime = remember { mutableStateOf("") }

        // Creating a TimePicker dialog
        val mTimePickerDialog = TimePickerDialog(
            mContext,
            { _, mHour : Int, mMinute: Int ->
                mTime.value = "$mHour:$mMinute"
            }, mHour, mMinute, false
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            // Display selected time
            OutlinedTextField(
                value = mTime.value,
                onValueChange = { /* Nothing */ },
                readOnly = true,
                label = { Text("Heure sélectionnée") },
                modifier = Modifier.weight(1f),
                colors = textFieldColors()

            )

            // Add a spacer
            Spacer(modifier = Modifier.width(10.dp))

            // Button for displaying the TimePicker
            Button(
                onClick = { mTimePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(contentColor  = Color(0XFF0F9D58)),
                modifier = Modifier.size(60.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.orloge), // Remplacez "votre_image" par le nom de votre image dans le dossier res/drawable
                    contentDescription = "Choisir une heure de livraison",
                    modifier = Modifier.size(512.dp) // Taille de l'image
                )
            }
            Spacer(modifier = Modifier.width(8.dp))    }

        Button(
            onClick = {
                val infosCommande = Commande(
                    nom,
                    prenom,
                    adresse,
                    numeroTelephone,
                    selectedBurger,
                    heureLivraison
                )
                val idUtilisateur = 357 // Remplacer par l'id de l'utilisateur reçu par email

                val jsonCommande = infosCommande.toJson() // Appel de la fonction toJson() pour obtenir le JSON de la commande

                envoyerCommandeAuServeur(infosCommande, idUtilisateur,
                    onSuccess = { success ->
                        // Traitement en cas de succès
                        if (success) {
                            // Le JSON a été envoyé avec succès
                            // Ajoutez ici le code que vous souhaitez exécuter en cas de succès
                            println("Le JSON a été envoyé avec succès.")
                        } else {
                            // Gestion des erreurs si nécessaire
                            println("Une erreur s'est produite lors de l'envoi du JSON.")
                        }
                    },
                    onFailure = { errorMessage ->
                        // Traitement en cas d'échec
                        // Ajoutez ici le code que vous souhaitez exécuter en cas d'échec
                        println("Erreur lors de l'envoi du JSON : $errorMessage")
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Valider")
        }


//        if (alertShown) {
//            val title = if (envoiReussi) "Succès" else "Erreur"
//            val message = if (envoiReussi) "Le JSON a été envoyé avec succès." else "Une erreur s'est produite lors de l'envoi du JSON."
//            val buttonColor = if (envoiReussi) Color.Green else Color.Red
//
//            AlertDialog(
//                onDismissRequest = { alertShown = false },
//                title = { Text(title) },
//                text = { Text(message) },
//                confirmButton = {
//                    Button(
//                        onClick = { alertShown = false },
//                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
//                    ) {
//                        Text("OK")
//                    }
//                }
//            )
//        }
    }
}








@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CommandeBurger(onValidateClicked = {})
}

@Composable
fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = secondary,
        unfocusedBorderColor = secondary,
        focusedLabelColor = secondary,
        unfocusedLabelColor = secondary,
    )
}

data class Commande(
    val nom: String,
    val prenom: String,
    val adresse: String,
    val numeroTelephone: String,
    val burger: String,
    val heureLivraison: String
) {
    fun toJson(): String {
        val msgJson = """
        {
            'firstname': '$nom',
            'lastname': '$prenom',
            'address': '$adresse',
            'phone': '$numeroTelephone',
            'burger': '$burger',
            'delivery_time': '$heureLivraison'
        }
    """.trimIndent()

        val jsonObject = JSONObject().apply {
            put("id_shop", "1")
            put("id_user", 357)
            put("msg", msgJson.replace("\"", "'"))
        }

        return jsonObject.toString()
    }


}

fun envoyerCommandeAuServeur(
    infosCommande: Commande,
    idUtilisateur: Int,
    onSuccess: (Boolean) -> Unit,
    onFailure: (String) -> Unit
) {
    val url = "http://test.api.catering.bluecodegames.com/user/order"
    val client = OkHttpClient()

    // Génération du JSON de la commande avec la fonction toJson()
    val msgJson = infosCommande.toJson()

    // Création de la requête
    val request = Request.Builder()
        .url(url)
        .post(msgJson.toRequestBody("application/json; charset=utf-8".toMediaType())) // Utilisation directe du JSON généré
        .build()

    // Envoi de la requête asynchrone
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e.message ?: "Unknown error")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                onSuccess(true)
            } else {
                onFailure("Failed to send JSON. Response: $responseBody")
            }
        }
    })
}




@Composable
fun JsonDisplay(jsonContent: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(50.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "JSON Content",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = jsonContent,
                fontSize = 16.sp,
                modifier = Modifier.verticalScroll(rememberScrollState())
            )
        }
    }
}