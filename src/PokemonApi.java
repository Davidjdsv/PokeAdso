// Librerías necesarias para trabajar con JSON, HTTP y la interfaz
import org.json.JSONArray;              // Importa la clase JSONArray para manejar listas de objetos JSON.
import org.json.JSONObject;             // Importa la clase JSONObject para manejar objetos JSON individuales.

import java.net.URI;                    // Necesario para construir la URL de la API.
import java.net.http.HttpClient;        // Cliente HTTP para enviar solicitudes.
import java.net.http.HttpRequest;       // Objeto para construir la solicitud HTTP.
import java.net.http.HttpResponse;      // Objeto para recibir la respuesta HTTP.
import java.util.Random;                // Utilizado para generar IDs de Pokémon aleatorios.

/*
 * Clase principal para obtener datos de Pokémon desde la PokeAPI.
 */
class ObtenerPokemonApiExplicacion {
    Random random = new Random(); // Instancia de Random para generar números aleatorios.

    /**
     * Genera un ID de Pokémon aleatorio entre 1 y 150 (primera generación).
     * @return Un entero que representa el ID aleatorio del Pokémon.
     */
    public int generarIdAleatorio() {
        return random.nextInt(150) + 1; // nextInt(150) genera números de 0 a 149.
        // Sumando 1, obtenemos de 1 a 150.
    }

    // Se generan dos IDs de Pokémon aleatorios para los "entrenadores".
    int entrenador1 = generarIdAleatorio();
    int entrenador2 = generarIdAleatorio();

    /**
     * Obtiene y muestra información de un Pokémon específico desde la PokeAPI.
     *
     * @param pokemonSeleccionado El ID del Pokémon que se desea obtener.
     * @return El ID del Pokémon que fue seleccionado.
     */
    public int obtenerPokemon(int pokemonSeleccionado) {
        try {
            // 1. Configuración del cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // 2. Construcción de la solicitud HTTP
            // Endpoint Clave de la PokeAPI:
            // La URL base para obtener información de un Pokémon individual por ID o nombre es:
            // "https://pokeapi.co/api/v2/pokemon/{id_o_nombre}"
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado)) // Aquí se construye la URL del endpoint.
                    // Por ejemplo, si pokemonSeleccionado es 25, la URL será:
                    // https://pokeapi.co/api/v2/pokemon/25 (para Pikachu).
                    .GET() // Especifica que es una solicitud GET.
                    .build(); // Construye la solicitud.

            // 3. Envío de la solicitud y recepción de la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Parseo de la respuesta JSON
            // El cuerpo de la respuesta (response.body()) es un String que contiene datos JSON.
            // Se convierte este String a un objeto JSONObject para poder manipularlo fácilmente.
            JSONObject json = new JSONObject(response.body());


            // 5. Verificación del código de estado HTTP
            // Si el código de estado es 200 (OK), significa que la solicitud fue exitosa.
            if (response.statusCode() == 200) {

                System.out.println("Tu Pokémon escogido al azar es: ");

                // Puntos Clave de Acceso a Datos JSON:

                // Acceder a un valor STRING directamente de la raíz del objeto JSON:
                // json.getInt("id") -> Obtiene el valor asociado a la clave "id" como un entero.
                System.out.println("Número de Pokédex: " + json.getInt("id"));
                // json.getString("name") -> Obtiene el valor asociado a la clave "name" como un String.
                System.out.println("Nombre: " + json.getString("name"));


                System.out.println("Habilidades");
                // Acceder a un ARRAY de objetos JSON:
                // json.getJSONArray("abilities") -> Obtiene el valor asociado a la clave "abilities"
                //                                  como un JSONArray. "abilities" es una lista (array)
                //                                  de objetos que representan las habilidades.
                json.getJSONArray("abilities").forEach(ability -> {
                    // Cada elemento dentro del JSONArray "abilities" es a su vez un JSONObject.
                    // Se realiza un casting para tratar 'ability' como un JSONObject.
                    JSONObject abilityObj = (JSONObject) ability;
                    // Dentro de cada 'abilityObj', hay otro JSONObject anidado bajo la clave "ability".
                    JSONObject abilityInfo = abilityObj.getJSONObject("ability");
                    // Desde 'abilityInfo', se obtiene el nombre de la habilidad.
                    System.out.println("- " + abilityInfo.getString("name"));
                });

                System.out.println("Estadísticas base: " + json.getString("name") + ":");
                // Acceder a otro ARRAY de objetos JSON:
                // json.getJSONArray("stats") -> Obtiene el valor asociado a la clave "stats" como un JSONArray.
                //                               "stats" es una lista (array) de objetos que representan las estadísticas.
                JSONArray stats = json.getJSONArray("stats"); // Se consulta y recorre stats

                // Variables para almacenar los stats que nos interesan
                int hp = 0;
                int attack = 0;
                int defense = 0;

                // Iterar sobre el JSONArray "stats"
                for (int i = 0; i < stats.length(); i++) {
                    // Cada elemento del JSONArray "stats" es un JSONObject individual (una estadística).
                    JSONObject stat = stats.getJSONObject(i);
                    // Dentro de cada objeto 'stat', hay un objeto anidado bajo la clave "stat".
                    // De este objeto anidado, obtenemos el nombre de la estadística.
                    String statName = stat.getJSONObject("stat").getString("name");
                    // Obtenemos el valor numérico de la estadística base.
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

                // Acceder a un objeto JSON ANIDADO y luego a un valor STRING:
                // json.getJSONObject("sprites") -> Obtiene el valor asociado a la clave "sprites" como un JSONObject.
                //                                 "sprites" es un objeto que contiene varias URLs de imágenes.
                // .getString("front_default") -> Dentro del objeto "sprites", obtiene el valor asociado a la clave
                //                                "front_default" como un String (que es la URL de la imagen frontal).
                System.out.println("Imagen: " + json.getJSONObject("sprites").getString("front_default"));
            } else {
                // Manejo de errores si la solicitud no fue exitosa.
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            // Captura cualquier excepción que ocurra durante el proceso (ej. problemas de red, JSON malformado).
            e.printStackTrace(); // Imprime la traza de la pila de la excepción para depuración.
        }
        return pokemonSeleccionado;
    }

    /**
     * Método principal para ejecutar el programa.
     * Crea una instancia de ObtenerPokemonApi y obtiene información para dos Pokémon aleatorios.
     */
    public static void main(String[] args) {
        ObtenerPokemonApiExplicacion obtenerPokemonApi = new ObtenerPokemonApiExplicacion();

        // Se obtienen y muestran los datos del primer Pokémon generado aleatoriamente.
        obtenerPokemonApi.obtenerPokemon(obtenerPokemonApi.entrenador1);
        System.out.println("\n-----------------------------------\n"); // Separador para claridad.
        // Se obtienen y muestran los datos del segundo Pokémon generado aleatoriamente.
        obtenerPokemonApi.obtenerPokemon(obtenerPokemonApi.entrenador2);
    }
}
