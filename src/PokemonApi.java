import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

/*
*Importando los datos de la api para cosumirla
*
*
*/
public class PokemonApi {
    Random random = new Random();

    int primerPokemon = random.nextInt(0, 150);
    int segundoPokemon = random.nextInt(0, 150);

    public int obtenerApi(int pokemonSeleccionado) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());


            if (response.statusCode() == 200) {

                System.out.println("Tu Pokémon escogido al azar es: ");
                System.out.println("Número de Pokédex: " + json.getInt("id"));
                System.out.println("Nombre: " + json.getString("name"));
                System.out.println("Peso: " + json.getInt("weight") + "kg");
                System.out.println("Altura: " + json.getInt("height") + "mts");


                System.out.println("Habilidades");
                json.getJSONArray("abilities").forEach(ability -> {
                    JSONObject abilityObj = (JSONObject) ability;
                    JSONObject abilityInfo = abilityObj.getJSONObject("ability");
                    System.out.println("- " + abilityInfo.getString("name"));
                });

                System.out.println("Estadísticas base: " + json.getString("name") + ":");
                JSONArray stats = json.getJSONArray("stats");
                for (int i = 0; i < stats.length(); i++) {
                    JSONObject stat = stats.getJSONObject(i);
                    int base = stat.getInt("base_stat");
                    String name = stat.getJSONObject("stat").getString("name");
                    System.out.println("- " + name + " " + base);
                }
                    System.out.println("Imagen: " + json.getJSONObject("sprites").getString("front_default"));
            } else {
                System.out.println("Error" + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonSeleccionado;
    }

    public static void main(String[] args) {
        PokemonApi pokemonApi = new PokemonApi();
        pokemonApi.obtenerApi(pokemonApi.primerPokemon);
        pokemonApi.obtenerApi(pokemonApi.segundoPokemon);

    }
}