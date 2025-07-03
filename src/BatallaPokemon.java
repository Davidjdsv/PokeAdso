import org.json.JSONArray;
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
import java.util.Random;


public class BatallaPokemon {
    private JPanel main;

    // Nombre del entrenador (label)
    private JLabel nombreEntrenadorLabel;

    // Pokémon Entrenador
    private JLabel nombrePokeEntrenador;
    private JLabel vidaPokeEntrenador;
    private JLabel ataquePokeEntrenador;
    private JLabel defensaPokeEntrenador;

    // Pokémon Salvaje
    private JLabel vidaPokeSalvaje;
    private JLabel ataquePokeSalvaje;
    private JLabel defensaPokeSalvaje;
    private JLabel nombrePokeSalvaje;

    // Imagenes random
    private JLabel imagenPokeEntrenador;
    private JLabel imagenPokeSalvaje;

    //Botones para el random
    private JButton randomizarEntrenadorButton;
    private JButton randomizarSalvajeButton;
    private JLabel contSalvajeLabel;
    private JLabel contEntrenadorLabel;
    private JButton lucharButton;
    private JLabel pokedexIdEntrenadorLabel;
    private JLabel pokedexIdSalvajeLabel;

    // TODO: Variables para almacenar los datos de los Pokémon
    private String nombrePokemonEntrenador;
    public int idPokeEntrenador; // Id Pokedex
    public int hpEntrenador;
    public int ataqueEntrenador;
    public int defensaEntrenador;
    private ImageIcon imagenEntrenador;

    // Contadores
    public int contadorEntrenador = 0;
    public int contadorSalvaje = 0;

    private String nombrePokemonSalvaje;
    public int idPokeSalvaje; // Id Pokedex
    public int hpSalvaje;
    public int ataqueSalvaje;
    public int defensaSalvaje;
    private ImageIcon imagenSalvaje;

    private Random random = new Random();
    public int idEntrenador;
    public int idSalvaje;

    //Instanciando la clase ReproducirMusica
    private ReproducirMusica reproductor;
    ReproducirMusica reproducirMusica = new ReproducirMusica();

    /**
     @author
     * En el constructor, agregamos los botones y dentro de cada botón, agregamos que se disparen las funciones requeridas.
     */
    BatallaPokemon() {
        reproductor = new ReproducirMusica();

        // Obtener el nombre del entrenador
        String nombre = JOptionPane.showInputDialog("¿Cómo te llamas, jóven maestro Pokémon? ");
        nombreEntrenadorLabel.setText(nombre); // En el label "nombreEntrenadorLabel", "seteamos"/agregamos el nombre que haya ingresado el usuario

        // Generar primeros Pokémon
        idEntrenador = generarIdAleatorio();
        idSalvaje = generarIdAleatorio();
        actualizarPokemones();

        // Configurar botones

        //TODO: Una vez generado un id aleatorio, lo guarda en la variable idEntrenador y se llama el método para actualizar la interfaz
        randomizarEntrenadorButton.addActionListener(e -> {
            idEntrenador = generarIdAleatorio();
            actualizarPokemonEntrenador();
        });

        randomizarSalvajeButton.addActionListener(e -> {
            idSalvaje = generarIdAleatorio();
            actualizarPokemonSalvaje();
        });


        /**
         * @author Davidjdsv
         * Se ejecuta el método para hacer combatir a los pokémon
         */
        lucharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                combatirPokemon();
            }
        });
    }


    private void actualizarPokemones() {
        obtenerPokemon(idEntrenador, true);
        obtenerPokemon(idSalvaje, false);
        actualizarInterfaz();
    }

    private void actualizarPokemonEntrenador() {
        obtenerPokemon(idEntrenador, true);
        actualizarInterfaz();
    }

    private void actualizarPokemonSalvaje() {
        obtenerPokemon(idSalvaje, false);
        actualizarInterfaz();
    }

    // TODO: Refresca la interfaz y vuelve a llamar los datos
    /**
    @author Davidjdsv
     En cada label "setea", pone/agrega texto y la variable que contiene la información.
     */
    private void actualizarInterfaz() {
        nombrePokeEntrenador.setText(nombrePokemonEntrenador);
        vidaPokeEntrenador.setText("HP: " + hpEntrenador);
        pokedexIdEntrenadorLabel.setText("Id Pokédex: " + idPokeEntrenador);
        ataquePokeEntrenador.setText("Ataque: " + ataqueEntrenador);
        defensaPokeEntrenador.setText("Defensa: " + defensaEntrenador);
        imagenPokeEntrenador.setIcon(imagenEntrenador);

        nombrePokeSalvaje.setText(nombrePokemonSalvaje);
        vidaPokeSalvaje.setText("HP: " + hpSalvaje);
        pokedexIdSalvajeLabel.setText("Id Pokédex: " + idPokeSalvaje);
        ataquePokeSalvaje.setText("Ataque: " + ataqueSalvaje);
        defensaPokeSalvaje.setText("Defensa: " + defensaSalvaje);
        imagenPokeSalvaje.setIcon(imagenSalvaje);
    }

    // Inicializar reproductor


    /**
    @author Davidjdsv
     Randomiza el Pokémon de entre los más de 1000 existentes
     */
    public int generarIdAleatorio() {
        //max - min + 1 = 649 - 494 + 1 = 155 + 1 = 156
        return random.nextInt(156) + 494; // La pokédex de Teselia
    }


    /**
     * @author Davidjdsv
     * TODO: El método obtenerPokemon() consulta la pokeAPI y requiere de 2 párametros:
     * @param pokemonSeleccionado (int) que es un número random ya elegido
     * @param esEntrenador (boolean) que verifica si es entrenador o es la CPU
     *
     * Consultamos la pokeAPI mediante la siguiente estructura
     */
    public void obtenerPokemon(int pokemonSeleccionado, boolean esEntrenador) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonSeleccionado))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());

            if (response.statusCode() == 200) {
                String nombre = json.getString("name");
                int idPoke = json.getInt("id");
                pokedexIdEntrenadorLabel.setText("Id Pokédex: " + idPokeEntrenador);

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
                String imageTrainer = "back_default";
                String imageWild = "front_default";
                String imageURL;

                if (esEntrenador){
                    imageURL = json.getJSONObject("sprites").getString(imageTrainer);
                } else {
                    imageURL = json.getJSONObject("sprites").getString(imageWild);
                }
                ImageIcon icon = new ImageIcon(new URL(imageURL));
                Image scaledImage = icon.getImage().getScaledInstance(150, 250, Image.SCALE_SMOOTH);
                ImageIcon imagen = new ImageIcon(scaledImage);

                // Almacenar los datos según corresponda
                if (esEntrenador) {
                    nombrePokemonEntrenador = nombre;
                    idPokeEntrenador = idPoke;
                    hpEntrenador = hp;
                    ataqueEntrenador = attack;
                    defensaEntrenador = defense;
                    imagenEntrenador = imagen;
                } else {
                    nombrePokemonSalvaje = nombre;
                    idPokeSalvaje = idPoke;
                    hpSalvaje = hp;
                    ataqueSalvaje = attack;
                    defensaSalvaje = defense;
                    imagenSalvaje = imagen;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(main, "Error al cargar Pokémon", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void combatirPokemon(){
        int sumaEntrenador = hpEntrenador + ataqueEntrenador + defensaEntrenador;
        int sumaSalvaje = hpSalvaje + ataqueSalvaje + defensaSalvaje;

        if(sumaEntrenador > sumaSalvaje){
            JOptionPane.showMessageDialog(null, "Gana el entrenador!\nSuma total de los stats de: " + nombrePokemonEntrenador + " es de: " + sumaEntrenador);
            contadorEntrenador += 1;
            contEntrenadorLabel.setText(String.valueOf(contadorEntrenador));
            idSalvaje = generarIdAleatorio();
            actualizarPokemonSalvaje();
        } else {
            JOptionPane.showMessageDialog(null, "Gana el salvaje!\nSuma total de los stats de: " + nombrePokemonSalvaje + " es de: " + sumaSalvaje);
            contadorSalvaje += 1;
            contSalvajeLabel.setText(String.valueOf(contadorSalvaje));
            idEntrenador = generarIdAleatorio();
            actualizarPokemonEntrenador();
        }

        if(contadorEntrenador == 6 || contadorSalvaje == 6){
            String ganador = sumaEntrenador > sumaSalvaje ? "Gana el entrenador con un número de victorias de: " + contadorEntrenador : "Gana el pokémon salvaje con un número de victorias de: " + contadorSalvaje;
            JOptionPane.showMessageDialog(null, ganador);
            JOptionPane.showMessageDialog(null, "Se empieza otra batalla pokémon!");

            // Se reinicia el contador para empezar de nuevo a luchar
            contSalvajeLabel.setText(String.valueOf(0));
            contadorSalvaje = 0;
            contEntrenadorLabel.setText(String.valueOf(0));
            contadorEntrenador = 0;
        }


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Batalla Pokémon");
            frame.setContentPane(new BatallaPokemon().main);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(1006, 750);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}