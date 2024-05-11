package fr.isen.pasqualini.theodroidburger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.pasqualini.theodroidburger.ui.theme.TheoDroidBurgerTheme
import fr.isen.pasqualini.theodroidburger.ui.theme.secondary
import androidx.compose.material3.DropdownMenuItem


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheoDroidBurgerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CommandeBurger{}
                }
            }
        }
    }
}




@Composable
fun CommandeBurger(onValidateClicked: () -> Unit) {
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
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        // Titre de la commande
        Text(
            text = "Commande de Burger",
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
            colors = textFieldColors(),
        )

        OutlinedTextField(
            value = adresse,
            onValueChange = { adresse = it },
            label = { Text("Adresse") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors(),
        )

        OutlinedTextField(
            value = numeroTelephone,
            onValueChange = { numeroTelephone = it },
            label = { Text("Numéro de téléphone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = textFieldColors()
        )



        OutlinedTextField(
            value = heureLivraison,
            onValueChange = { heureLivraison = it },
            label = { Text("Heure de livraison (HH:MM)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = textFieldColors()
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Text("Sélectionnez votre burger:")
            // Utilisation de DropdownMenu pour afficher la liste des burgers à sélectionner
            DropdownMenu(
                expanded = false, // Initialisé à false pour ne pas afficher la liste par défaut
                onDismissRequest = { /* No action */ },
                modifier = Modifier.fillMaxWidth(), // Modificateur pour définir la largeur du menu déroulant
            ) {
                val burgerList = stringArrayResource(id = R.array.burger_list)
                burgerList.forEach { burger ->
                    DropdownMenuItem(
                        onClick = {
                            selectedBurger = burger // Met à jour le burger sélectionné
                        },
                        modifier = Modifier.fillMaxWidth(), // Modificateur pour définir la largeur de chaque élément du menu
                    ) {

                    }
                }
            }
        }
        Button(
            onClick = onValidateClicked, // Appel de la fonction de validation lorsque le bouton est cliqué
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Valider")
        }



    }

}

fun DropdownMenuItem(onClick: () -> Unit, modifier: Modifier, interactionSource: () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun PreviewCommandeBurger() {
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
