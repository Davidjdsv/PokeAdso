import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PracticaPokemonAPI {

    // Método para obtener datos de Pokémon de la API
    public void obtenerPokemonAPI(){
        try {
            // Configuración del cliente HTTP para enviar y recibir peticiones
            HttpClient httpClient = HttpClient.newHttpClient();

            boolean pokemonEncontrado = false;
            String consultaUsuario;
            HttpResponse<String> response = null;

            do {
                // Solicita al usuario el nombre o número de Pokédex del Pokémon
                consultaUsuario = JOptionPane.showInputDialog("Ingrese el nombre o número de la Pokédex del Pokémon: ").toLowerCase();

                if (consultaUsuario == null) {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                    return; // Termina la ejecución del método
                }

                // Construcción de la solicitud HTTP con la entrada del usuario
                // El endpoint de la PokeAPI funciona tanto con ID numérico como con nombre
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + consultaUsuario))
                        .GET() // Especifica que es una solicitud GET
                        .build();

                // El cliente envía la solicitud y recibe la respuesta completa
                response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                // Verifica el código de estado HTTP de la respuesta
                if (response.statusCode() == 200) {
                    // Si el código es 200 (OK), el Pokémon fue encontrado y sale del bucle
                    pokemonEncontrado = true;
                } else if (response.statusCode() == 404) {
                    // Si el código es 404 (Not Found), el Pokémon no existe o está mal escrito
                    JOptionPane.showMessageDialog(null, "El Pokémon '" + consultaUsuario + "' no existe o está mal escrito.\nPor favor, intente de nuevo.");
                } else {
                    // Si es otro código de estado, indica un error de conexión o del servidor
                    JOptionPane.showMessageDialog(null, "Conexión fallida con la PokeAPI. Código de error: " + response.statusCode() + "\nPor favor, intente de nuevo.");
                }

            } while (!pokemonEncontrado);

            JSONObject json = new JSONObject(response.body());

            System.out.println("-- National Pokédex --");
            System.out.println("Pokedex Number: " + json.getInt("id"));
            System.out.println("Pokemon: " + json.getString("name"));

            // Recorriendo el array "abilities"
            System.out.println("-- Abilities --");
            json.getJSONArray("abilities").forEach(ability ->{
                JSONObject jsonObject = (JSONObject) ability;
                JSONObject abilityInfo = jsonObject.getJSONObject("ability");
                System.out.println("Ability: " + abilityInfo.getString("name"));
            });

            int hp = 0;
            int attack = 0;
            int defense = 0;
            int sp_attack = 0;
            int sp_defense = 0;
            int speed = 0;

            //Recorriendo el array "stats"
            System.out.println("-- Pokémon Stats --");
            JSONArray stats = json.getJSONArray("stats"); // Se recorre el array de stats
            for (int i = 0; i < stats.length(); i++) {
                JSONObject stat = stats.getJSONObject(i); // por cada stat dentro de stat en la posición i de stats que obtiene un JSONObject
                String statName = stat.getJSONObject("stat").getString("name");
                int baseStat = stat.getInt("base_stat");


                switch (statName){
                    case "hp":
                        hp = baseStat;
                        break;
                    case "attack":
                        attack = baseStat;
                        break;
                    case "defense":
                        defense = baseStat;
                        break;
                    case "special-attack":
                        sp_attack = baseStat;
                        break;
                    case "special-defense":
                        sp_defense = baseStat;
                        break;
                    case "speed":
                        speed = baseStat;
                }

            }


            System.out.println("Hp: " + hp);
            System.out.println("Attack: " + attack);
            System.out.println("Defense: " + defense);
            System.out.println("Special Attack: " + sp_attack);
            System.out.println("Special Defense: " + sp_defense);
            System.out.println("Speed: " + speed);

            // Tipo del Pokémon
            System.out.println(" -- Types --");
            json.getJSONArray("types").forEach(type ->{ //Entra al array de tipos y recorre cada tipo de objeto
                JSONObject jsonObject = (JSONObject) type; // Castea el type a ser un objeto implícitamente
                JSONObject typeInfo = jsonObject.getJSONObject("type");
                System.out.println("Type: " + typeInfo.getString("name"));
            });


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error has ocured with the API connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PracticaPokemonAPI practicaPokemonAPI = new PracticaPokemonAPI();
        practicaPokemonAPI.obtenerPokemonAPI();
    }
}
