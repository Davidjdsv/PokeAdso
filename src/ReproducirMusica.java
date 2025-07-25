import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException; // Importar IOException

public class ReproducirMusica {
    private Player player;
    private Thread reproductorThread;
    private volatile boolean enReproduccion = false; // Usar volatile para visibilidad entre hilos
    private String archivoActual;

    //loop de los soundtracks
    public void reproducir(String archivo) {
        // Detener la reproducción anterior de forma limpia
        if (reproductorThread != null && reproductorThread.isAlive()) {
            detener(); // Asegurarse de que el hilo anterior finalice o sea interrumpido
            try {
                reproductorThread.join(100); // Esperar un corto tiempo para que el hilo termine
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            }
        }

        try {
            archivoActual = archivo;
            // Asegurarse de que el recurso no es nulo antes de continuar
            InputStream is = getClass().getResourceAsStream(archivo);
            if (is == null) {
                System.err.println("Error: Archivo de música no encontrado en el classpath: " + archivo);
                return; // Salir si el archivo no se encuentra
            }
            BufferedInputStream bis = new BufferedInputStream(is);

            player = new Player(bis); // Nuevo Player con nuevo InputStream
            enReproduccion = true; // Establecer a true antes de iniciar el hilo

            reproductorThread = new Thread(() -> {
                try {
                    player.play(); // Esto puede lanzar JavaLayerException o IOException
                } catch (JavaLayerException e) {
                    System.err.println("Error de JLayer al reproducir: " + e.getMessage());
                    // Si el stream se cierra inesperadamente, podría ser capturado aquí
                } catch (Exception e) { // Capturar otras excepciones, como IOException
                    if (!e.getMessage().equals("Stream closed")) { // Ignorar el error si es "Stream closed" esperado
                        System.err.println("Error inesperado en el hilo de reproducción: " + e.getMessage());
                        e.printStackTrace();
                    }
                } finally {
                    // Cerrar el player y limpiar al finalizar la reproducción o por error
                    if (player != null) {
                        player.close();
                    }
                    enReproduccion = false; // Restablecer al finalizar
                    player = null; // Limpiar la referencia
                    // No limpiar reproductorThread aquí, se gestiona en detener() o al iniciar nuevo
                }
            });

            reproductorThread.setDaemon(true); // Opcional: Hacerlo un hilo "daemon" para que no impida la salida de la JVM
            reproductorThread.start();
        } catch (Exception e) {
            System.err.println("Error al iniciar la reproducción de música: " + e.getMessage());
            e.printStackTrace();
            enReproduccion = false; // Asegurarse de que el estado sea consistente en caso de error
        }
    }

    // Para efectos de sonido (una sola vez)
    public void reproducirEfecto(String archivo) {
        // Para efectos de sonido, no necesitamos detener nada, solo reproducir una vez
        new Thread(() -> {
            try {
                InputStream is = getClass().getResourceAsStream(archivo);
                if (is != null) {
                    Player efectoPlayer = new Player(new BufferedInputStream(is));
                    efectoPlayer.play();
                    efectoPlayer.close(); // Cerrar el player una vez que el efecto termina
                } else {
                    System.err.println("Error: Archivo de efecto de sonido no encontrado: " + archivo);
                }
            } catch (Exception e) {
                System.err.println("Error al reproducir efecto de sonido: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public void detener() {
        if (player != null) {
            player.close(); // Cierra el JLayer Player, que también cierra el InputStream.
            // Esto también debería causar que player.play() en el otro hilo lance una excepción
            // y el hilo termine naturalmente.
            player = null; // Quita la referencia para evitar usarlo de nuevo
        }
        if (reproductorThread != null) {
            // No es estrictamente necesario interrumpir si player.close() ya mata el play()
            // Pero ayuda a asegurar que el hilo termine si se cuelga por alguna razón.
            reproductorThread.interrupt();
            try {
                reproductorThread.join(500); // Esperar un máximo de 500ms a que el hilo termine
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Hilo de reproducción interrumpido mientras esperaba su finalización.");
            }
            reproductorThread = null; // Quita la referencia al hilo
        }
        enReproduccion = false; // Asegura que el estado es "no reproduciendo"
    }

    public boolean estaReproduciendo() {
        return enReproduccion;
    }

    public String getArchivoActual() {
        return archivoActual;
    }
}