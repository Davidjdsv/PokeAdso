import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// La clase se ha modificado para que devuelva la descripción como un String.
public class DescPokemon {

    /**
     * Obtiene la descripción en español de un Pokémon de la PokeAPI.
     * @param poke El nombre del Pokémon a buscar.
     * @return La descripción en español del Pokémon o un mensaje de error si no se encuentra.
     */
    public String obtenerDescripcionPokemon(String poke) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon-species/" + poke))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());

                JSONArray descripciones_poke = json.getJSONArray("flavor_text_entries");
                for (int i = 0; i < descripciones_poke.length(); i++) {
                    JSONObject desc = descripciones_poke.getJSONObject(i);
                    JSONObject idioma = desc.getJSONObject("language");

                    if (idioma.getString("name").equals("es")) {
                        String descripcionEs = desc.getString("flavor_text");
                        // Eliminamos saltos de línea para que se vea bien en el JLabel
                        return descripcionEs.replace("\n", " ").replace("\f", " ");
                    }
                }
                return "Descripción en español no encontrada."; // Si no hay descripción en español
            } else {
                return "Error al buscar la descripción: " + response.statusCode();
            }
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al obtener la descripción del Pokémon: " + e.getMessage());
            return "Error al obtener la descripción.";
        }
    }
}
