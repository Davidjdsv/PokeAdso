import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Pokedex {
    private JPanel main;
    private JLabel nombrePokemonLabel;
    private JLabel numero_pokedex_label;
    private JLabel descripcion_poke_label;
    private JLabel hp_pokemon_label;
    private JLabel attack_pokemon_label;
    private JLabel defense_pokemon_label;
    private JLabel special_attack_pokemon_label;
    private JLabel special_defense_pokemon_label;
    private JLabel speed_pokemon_label;
    private JLabel img_pokemon_label;
    private JButton buscarPokemonButton;

    Pokedex() {

        buscarPokemonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String consulta = buscarPokemon();
                if (consulta != null || consulta.trim().isEmpty()) {
                    obtenerPokemonAPI(consulta);
                    insertarDatos();
                } else {
                    JOptionPane.showMessageDialog(main, "Por favor, ingrese un nombre o número de Pokémon.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    //TODO: Variables, Estadísticas y datos del pokemon
    int id_pokedex = 0;
    String nombre = null;
    int hp = 0;
    int ataque = 0;
    int ataque_especial = 0;
    int defensa = 0;
    int defensa_especial = 0;
    int velocidad = 0;
    String descripcion = null;
    String sprite = null;


    public String buscarPokemon() {
        return JOptionPane.showInputDialog("Ingrese el nombre o el número del Pokémon: ");
    }

    public void insertarDatos(){
        nombrePokemonLabel.setText(nombre);
        numero_pokedex_label.setText(String.valueOf(id_pokedex));
        hp_pokemon_label.setText(String.valueOf(hp));
        attack_pokemon_label.setText(String.valueOf(ataque));
        defense_pokemon_label.setText(String.valueOf(defensa));
        special_attack_pokemon_label.setText(String.valueOf(ataque_especial));
        special_defense_pokemon_label.setText(String.valueOf(defensa_especial));
        speed_pokemon_label.setText(String.valueOf(velocidad));
    }

    //String consulta = buscarPokemon();

    public String obtenerPokemonAPI(String pokemonSeleccionado) {
        try {
            // TODO: Obteniendo la PokeAPI por su URL
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            //TODO: Obteniendo los datos Json de la pokeAPI
            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                nombre = json.getString("name");
                id_pokedex = json.getInt("id");

                JSONArray stats = json.getJSONArray("stats");
                for (int i = 0; i < stats.length(); i++) {
                    JSONObject stat = stats.getJSONObject(i);//Recorre los stats en la posición i que es el objeto de 'stat'
                    String statName = stat.getJSONObject("stat").getString("name");//nombre del stat
                    int baseStat = stat.getInt("base_stat");//estadísticas del stat

                    switch (statName) {
                        case "hp":
                            hp = baseStat;
                            break;
                        case "attack":
                            ataque = baseStat;
                            break;
                        case "defense":
                            defensa = baseStat;
                            break;
                        case "special-attack":
                            ataque_especial = baseStat;
                            break;
                        case "special-defense":
                            defensa_especial = baseStat;
                            break;
                        case "speed":
                            velocidad = baseStat;
                            break;
                    }
                }

                //TODO: Obteniendo la imagen o gif del pokémon
                try {
                    sprite = json.getJSONObject("sprites")
                            .getJSONObject("versions")
                            .getJSONObject("generation-v")
                            .getJSONObject("black-white")
                            .getJSONObject("animated").getString("front_default");
                    //TODO: Captruando el mensaje de error si no encuentra la url del pokémon de la gen-V
                } catch (JSONException e) {
                    System.err.println("Advertencia: No se encontré el sprite animado de la generación V. " + e.getMessage());
                    try {
                        sprite = json.getJSONObject("sprites").getString("front_default");
                        System.out.println("Se usará entonces el sprite estático por defecto");
                    } catch (JSONException eFallBack) {
                        System.err.println("Error: No se pudo encontrar un sprite para el Pokémon: " + eFallBack.getMessage());
                    }
                }

                // TODO: Crear la imágen una sola vez

                try {
                    if (sprite != null && !sprite.isEmpty()) {
                        ImageIcon pokemonGif = new ImageIcon(new URL(sprite));

                        img_pokemon_label.setIcon(pokemonGif);
                        img_pokemon_label.setText("");
                    } else {
                        img_pokemon_label.setIcon(null);
                        img_pokemon_label.setText("No se encontró el Pokémon");
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar la imágen" + e.getMessage());
                    e.printStackTrace();
                    img_pokemon_label = new JLabel("Error al cargar la imágen de excepción");
                    img_pokemon_label.setPreferredSize(new Dimension(250, 250)); // CORREGIDO: Cambié de 300 a 250
                }

            } else if (response.statusCode() == 404) {
                JOptionPane.showMessageDialog(main, "Oops!\nEl Pokémon '" + pokemonSeleccionado + "' no existe. Por favor, intente de nuevo.", "Pokémon No Encontrado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(main, "Error en la API: " + response.statusCode(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            }


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(main, "Error al cargar Pokémon", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return pokemonSeleccionado;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pokédex Nacional");
            frame.setContentPane(new Pokedex().main);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(1000, 750);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
