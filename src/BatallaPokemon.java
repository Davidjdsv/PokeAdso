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
    private JLabel vidaPokeAsh;
    private JLabel ataquePokeAsh;
    private JLabel defensaPokeAsh;  // Nuevo JLabel para defensa
    private JLabel vidaPokeRival;
    private JLabel ataquePokeRival;
    private JLabel defensaPokeRival;  // Nuevo JLabel para defensa
    private JLabel nombrePokeRival;
    private JLabel nombrePokeAsh;
    private JLabel imagenPokeAsh;    // Nuevo JLabel para imagen
    private JLabel imagenPokeRival;   // Nuevo JLabel para imagen

    // Variables para almacenar los datos de los Pokémon
    private String nombreAsh;
    private int hpAsh;
    private int ataqueAsh;
    private int defensaAsh;
    private ImageIcon imagenAsh;

    private String nombreRival;
    private int hpRival;
    private int ataqueRival;
    private int defensaRival;
    private ImageIcon imagenRival;

    BatallaPokemon() {
        // Obtener datos de los Pokémon
        obtenerPokemon(entrenador1, true);  // Pokémon para Ash
        obtenerPokemon(entrenador2, false); // Pokémon para Rival

        // Configurar GUI con los datos obtenidos
        nombrePokeAsh.setText(nombreAsh);
        vidaPokeAsh.setText("HP: " + hpAsh);
        ataquePokeAsh.setText("Ataque: " + ataqueAsh);
        defensaPokeAsh.setText("Defensa: " + defensaAsh);
        imagenPokeAsh.setIcon(imagenAsh);

        nombrePokeRival.setText(nombreRival);
        vidaPokeRival.setText("HP: " + hpRival);
        ataquePokeRival.setText("Ataque: " + ataqueRival);
        defensaPokeRival.setText("Defensa: " + defensaRival);
        imagenPokeRival.setIcon(imagenRival);
    }

    Random random = new Random();

    public int generarIdAleatorio() {
        return random.nextInt(150) + 1;
    }

    int entrenador1 = generarIdAleatorio();
    int entrenador2 = generarIdAleatorio();

    public void obtenerPokemon(int pokemonSeleccionado, boolean esAsh) {
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
                if (esAsh) {
                    nombreAsh = nombre;
                    hpAsh = hp;
                    ataqueAsh = attack;
                    defensaAsh = defense;
                    imagenAsh = imagen;
                } else {
                    nombreRival = nombre;
                    hpRival = hp;
                    ataqueRival = attack;
                    defensaRival = defense;
                    imagenRival = imagen;
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