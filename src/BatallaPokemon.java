import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

public class BatallaPokemon {
    private JPanel main;
    private JLabel vidaPokeEntrenador;
    private JLabel ataquePokeEntrenador;
    private JLabel defensaPokeEntrenador;  // Nuevo JLabel para defensa
    private JLabel vidaPokeSalvaje;
    private JLabel ataquePokeSalvaje;
    private JLabel defensaPokeSalvaje;  // Nuevo JLabel para defensa
    private JLabel nombrePokeSalvaje;
    private JLabel nombrePokeEntrenador;
    private JLabel imagenPokeEntrenador;    // Nuevo JLabel para imagen
    private JLabel imagenPokeSalvaje;   // Nuevo JLabel para imagen

    // Variables para almacenar los datos de los Pokémon
    private JLabel nombreEntrenador;
    private int hpEntrenador;
    private int ataqueEntrenador;
    private int defensaEntrenador;
    private ImageIcon imagenEntrenador;

    private String nombreSalvaje;
    private int hpSalvaje;
    private int ataqueSalvaje;
    private int defensaSalvaje;
    private ImageIcon imagenSalvaje;

    BatallaPokemon() {
        // Obtener datos de los Pokémon

        String nombre = JOptionPane.showInputDialog("¿Cómo te llamas? ");

        obtenerPokemon(entrenador, true);  // Pokémon para Entrenador
        obtenerPokemon(pokeSalvaje, false); // Pokémon para Salvaje

        // Configurar GUI con los datos obtenidos
        nombrePokeEntrenador.setText(String.valueOf(nombreEntrenador));
        vidaPokeEntrenador.setText("HP: " + hpEntrenador);
        ataquePokeEntrenador.setText("Ataque: " + ataqueEntrenador);
        defensaPokeEntrenador.setText("Defensa: " + defensaEntrenador);
        imagenPokeEntrenador.setIcon(imagenEntrenador);

        nombrePokeSalvaje.setText(nombreSalvaje);
        vidaPokeSalvaje.setText("HP: " + hpSalvaje);
        ataquePokeSalvaje.setText("Ataque: " + ataqueSalvaje);
        defensaPokeSalvaje.setText("Defensa: " + defensaSalvaje);
        imagenPokeSalvaje.setIcon(imagenSalvaje);
    }

    Random random = new Random();

    public int generarIdAleatorio() {
        return random.nextInt(150) + 1;
    }

    int entrenador = generarIdAleatorio();
    int pokeSalvaje = generarIdAleatorio();

    public void obtenerPokemon(int pokemonSeleccionado, boolean esEntrenador) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            if (response.statusCode() == 200) {
                String nombre = json.getString("name");
                int hp = 0;
                int attack = 0;
                int defense = 0;

                JSONArray stats = json.getJSONArray("stats");
                for (int i = 0; i < stats.length(); i++) {
                    JSONObject stat = stats.getJSONObject(i);
                    String statName = stat.getJSONObject("stat").getString("name");
                    int baseStat = stat.getInt("base_stat");

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

                // Obtener la imagen
                String imageURL = json.getJSONObject("sprites").getString("front_default");
                ImageIcon icon = new ImageIcon(new URL(imageURL));
                Image scaledImage = icon.getImage().getScaledInstance(150, 250, Image.SCALE_SMOOTH);
                ImageIcon imagen = new ImageIcon(scaledImage);

                // Almacenar los datos según corresponda
                if (esEntrenador) {
                    nombreEntrenador.getText(nombre);
                    hpEntrenador = hp;
                    ataqueEntrenador = attack;
                    defensaEntrenador = defense;
                    imagenEntrenador = imagen;
                } else {
                    nombreSalvaje = nombre;
                    hpSalvaje = hp;
                    ataqueSalvaje = attack;
                    defensaSalvaje = defense;
                    imagenSalvaje = imagen;
                }

            } else {
                System.out.println("Error: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Batalla Pokémon");
        frame.setContentPane(new BatallaPokemon().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1006, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
}