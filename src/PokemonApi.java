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
class ObtenerPokemonApi {
    Random random = new Random();

    public int generarIdAleatorio() {
        return random.nextInt(150) + 1;
    }

    int entrenador1 = generarIdAleatorio();
    int entrenador2 = generarIdAleatorio();

    public int obtenerPokemon(int pokemonSeleccionado) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());


            if (response.statusCode() == 200) {

                System.out.println("Tu Pokémon escogido al azar es: ");
                System.out.println("Número de Pokédex: " + json.getInt("id"));
                System.out.println("Nombre: " + json.getString("name"));


                System.out.println("Habilidades");
                json.getJSONArray("abilities").forEach(ability -> {
                    JSONObject abilityObj = (JSONObject) ability;
                    JSONObject abilityInfo = abilityObj.getJSONObject("ability");
                    System.out.println("- " + abilityInfo.getString("name"));
                });

                System.out.println("Estadísticas base: " + json.getString("name") + ":");
                JSONArray stats = json.getJSONArray("stats"); // Se consulta y recorre stats

                // Variables para almacenar los stats que nos interesan
                int hp = 0;
                int attack = 0;
                int defense = 0;

                for (int i = 0; i < stats.length(); i++) {
                    JSONObject stat = stats.getJSONObject(i);
                    String statName = stat.getJSONObject("stat").getString("name");
                    int baseStat = stat.getInt("base_stat");

                    // Filtramos solo los stats que nos interesan
                    switch (statName) {
                        case "hp":
                            hp = baseStat;
                            break;
                        case "attack":
                            attack = baseStat;
                            break;
                        case "defense":
                            defense = baseStat;
                            break;
                    }
                }

                System.out.println("- HP: " + hp);
                System.out.println("- Ataque: " + attack);
                System.out.println("- Defensa: " + defense);

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
        ObtenerPokemonApi obtenerPokemonApi = new ObtenerPokemonApi();

        obtenerPokemonApi.obtenerPokemon(obtenerPokemonApi.entrenador1);
        obtenerPokemonApi.obtenerPokemon(obtenerPokemonApi.entrenador2);
    }
}
