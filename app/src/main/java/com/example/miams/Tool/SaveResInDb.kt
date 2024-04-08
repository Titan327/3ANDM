import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class addDb{
    companion object {
        @Composable
        fun addToDb(id: Int, title: String, url: String, ingredients: List<String>) {
            val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
            val RecipesDAO = database.RecipesDAO()
            val DbRecipes = remember { mutableStateOf(listOf<Recipes>()) }


            fun urlToByteArray(url: String): ByteArray {
                val inputStream = URL(url).openStream()
                val bytes = inputStream.readBytes()
                inputStream.close()
                return bytes
            }
            suspend fun onAddRecipes() {
                withContext(Dispatchers.IO) {
                    val ingredientsString = Gson().toJson(ingredients)
                    RecipesDAO.upsertRecipes(
                        Recipes(
                            id = id,
                            title = title,
                            image = urlToByteArray(url),
                            ingredients = ingredientsString
                        )
                    )
                }
            }

            LaunchedEffect(true) {
                onAddRecipes()
            }
        }
    }
}

