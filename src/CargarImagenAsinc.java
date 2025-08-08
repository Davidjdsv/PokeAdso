import javax.swing.*;
import java.net.URL;

public class CargarImagenAsinc {

    public void cargar(JLabel label, String spriteUrl) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                if (spriteUrl != null && !spriteUrl.isEmpty()) {
                    return new ImageIcon(new URL(spriteUrl));
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon imagen = get();
                    if (imagen != null) {
                        label.setIcon(imagen);
                        label.setText("");
                    } else {
                        label.setIcon(null);
                        label.setText("Imagen no disponible");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    label.setText("Error al cargar imagen");
                }
            }
        };

        worker.execute();
    }
}
