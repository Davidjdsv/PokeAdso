import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class ReproducirMusica {
    // Creando las variables
    private Player player;
    private Thread reproductorThread;
    private boolean enReproduccion = false;
    private String archivoActual; // Variable que guarda la ruta del archivo

    //loop de los soundtracks
    public void reproducir(String archivo) {
        detener();  // Detener cualquier reproducción anterior

        try {
            archivoActual = archivo;
            InputStream is = getClass().getResourceAsStream(archivo);
            BufferedInputStream bis = new BufferedInputStream(is);

            player = new Player(bis);
            reproductorThread = new Thread(() -> {
                try {
                    enReproduccion = true;
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                } finally {
                    enReproduccion = false;
                }
            });

            reproductorThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Para efectos de sonido (una sola vez)
    public void reproducirEfecto(String archivo) {
        new Thread(() -> {
            try (InputStream is = getClass().getResourceAsStream(archivo)) {
                if (is != null) {
                    Player efecto = new Player(new BufferedInputStream(is));
                    efecto.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void detener() {
        if (player != null) {
            player.close();
            player = null;
        }
        if (reproductorThread != null) {
            reproductorThread.interrupt();
            reproductorThread = null;
        }
        enReproduccion = false;
    }

    public boolean estaReproduciendo() {
        return enReproduccion;
    }

    public String getArchivoActual() {
        return archivoActual;
    }
}
