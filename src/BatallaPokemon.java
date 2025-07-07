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
    private JLabel imagenPokeEntrenadorLabel;
    private JLabel imagenPokeSalvajeLabel;

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

    // Variables con las rutas de cada soundtrack
    String sountrack1 = "Media/Pokemon Black & White 2 OST Legendary Battle Music.mp3";
    String sountrack2 = "Media/Pokemon Black & White 2 OST Rare Wild Battle Music.mp3";
    String sountrack3 = "Media/Pokemon Black & White 2 OST Reshiram_Zekrom Battle Music.mp3";
    String sountrack4 = "Media/Pokemon Black & White 2 OST Trainer Battle Music.mp3";
    String sountrack5 = "Media/Pokemon Black & White 2 OST Wild Battle Music.mp3";

    // Array que contiene las rutas de los soundtracks
    public String[] soundTracks = {sountrack1, sountrack2, sountrack3, sountrack4, sountrack5};


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

        String soundTrackElegido = randomSoundTrack(soundTracks);
        reproductor.reproducir(soundTrackElegido);

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
        imagenPokeEntrenadorLabel.setIcon(imagenEntrenador);

        nombrePokeSalvaje.setText(nombrePokemonSalvaje);
        vidaPokeSalvaje.setText("HP: " + hpSalvaje);
        pokedexIdSalvajeLabel.setText("Id Pokédex: " + idPokeSalvaje);
        ataquePokeSalvaje.setText("Ataque: " + ataqueSalvaje);
        defensaPokeSalvaje.setText("Defensa: " + defensaSalvaje);
        imagenPokeSalvajeLabel.setIcon(imagenSalvaje);
    }

    /**
     *
    // TODO: Metódo para tomar el array que contiene los soundtracks y retornar uno aleatorio del índice
     * @param sountrack
     * @return
     */
    public String randomSoundTrack(String[] sountrack){
        if (soundTracks.length == 0) return "El array está vacío. Por favor añada música";
        int randomIndice = random.nextInt(sountrack.length);
        return  soundTracks[randomIndice];
    }

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

                // TODO: Declarando variables
                String spriteURL = null, spritePoke = null;

                // TODO: Decidir si es el entrenador o el rival
                if(esEntrenador){
                    spritePoke = "back_default";
                } else {
                    spritePoke = "front_default";
                }

                //TODO: Intentar tomar la URL del sprite animado de la gen-5
                try {
                    spriteURL = json.getJSONObject("sprites")
                            .getJSONObject("versions")
                            .getJSONObject("generation-v")
                            .getJSONObject("black-white")
                            .getJSONObject("animated").getString(spritePoke);
                    //TODO: Captruando el mensaje de error si no encuentra la url del pokémon de la gen-V
                } catch (JSONException e) {
                    System.err.println("Advertencia: No se encontré el sprite animado de la generación V (" + spritePoke + "). " + e.getMessage());
                    try{
                        spriteURL = json.getJSONObject("sprites").getString(spritePoke);
                        System.out.println("Se usará entonces el sprite estático por defecto: (" + spritePoke + ")");
                    } catch (JSONException eFallBack) {
                        System.err.println("Error: No se pudo encontrar un sprite para el Pokémon: " + spritePoke + ")" + eFallBack.getMessage());
                    }
                }

                // TODO: Crear la imágen una sola vez
                ImageIcon pokemonGif = null;
                JLabel pokemonSpriteLabel = null;

                try {
                    if(spriteURL != null && !spritePoke.isEmpty()){
                        pokemonGif = new ImageIcon(new URL(spritePoke));
                        pokemonSpriteLabel = new JLabel(pokemonGif);
                        pokemonSpriteLabel.setPreferredSize(new Dimension(300, 300));
                    } else {
                        pokemonSpriteLabel = new JLabel("Error al cargar la imágen");
                        pokemonSpriteLabel.setPreferredSize(new Dimension(300, 300));
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar la imágen: (" + spritePoke + "). " + e.getMessage());
                    e.printStackTrace();
                    pokemonSpriteLabel = new JLabel("Error al cargar la imágen de excepción");
                    pokemonSpriteLabel.setPreferredSize(new Dimension(300, 300));
                }

                // TODO: Asignando las estadísticas a cada Pokémon
                if(esEntrenador){
                    nombrePokemonEntrenador = nombre;
                    idPokeEntrenador = idPoke;
                    hpEntrenador = hp;
                    ataqueEntrenador = attack;
                    defensaEntrenador = defense;
                    imagenEntrenador = pokemonGif;
                } else {
                    nombrePokemonSalvaje = nombre;
                    idSalvaje = idPoke;
                    hpSalvaje = hp;
                    ataqueSalvaje = attack;
                    defensaSalvaje = defense;
                    imagenSalvaje = pokemonGif;
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