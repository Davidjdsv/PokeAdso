import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

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
    private JButton buscarPokémonButton;

    Pokedex(){

        buscarPokémonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String consulta = buscarPokemon();
                if(consulta != null || consulta.trim().isEmpty()){
                    obtenerPokemonAPI(consulta);
                } else {
                    JOptionPane.showMessageDialog(main, "Por favor, ingrese un nombre o número de Pokémon.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public String buscarPokemon(){
        return JOptionPane.showInputDialog("Ingrese el nombre o el número del Pokémon: ");
    }

    //String consulta = buscarPokemon();

    public String obtenerPokemonAPI(String pokemonSeleccionado){
        try{
            // TODO: Obteniendo la PokeAPI por su URL
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon-species/" + pokemonSeleccionado ))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            //TODO: Obteniendo los datos Json de la pokeAPI
            if(response.statusCode() == 200){
                JSONObject json = new JSONObject(response.body());
                String nombre = json.getString("name");



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
